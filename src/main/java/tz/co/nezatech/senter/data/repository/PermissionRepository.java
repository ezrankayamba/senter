package tz.co.nezatech.senter.data.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import tz.co.nezatech.nezadb.model.NamedQueryParam;
import tz.co.nezatech.nezadb.repository.IDataRepository;
import tz.co.nezatech.senter.data.model.Permission;

@Repository
public class PermissionRepository implements IDataRepository<Long, Permission> {
	@Autowired
	JdbcTemplate jdbcTemplate;

	public RowMapper<Permission> getRowMapper() {
		return new RowMapper<Permission>() {

			@Override
			public Permission mapRow(ResultSet rs, int rowNum) throws SQLException {
				Permission e = new Permission(rs.getString("name"), rs.getString("description"));
				e.setId(rs.getLong("id"));
				Long parentId = rs.getLong("parent");
				if (parentId != null && parentId > 0) {
					Permission parent = new Permission(rs.getString("parent_name"), rs.getString("parent_description"));
					parent.setId(rs.getLong("parent"));

					e.setParent(parent);
				}
				e.setEnabled(rs.getBoolean("enabled"));
				return e;
			}
		};
	}

	@Override
	public String querySql() {
		return "select p.*, prt.name as parent_name, prt.description as parent_description, 0 as enabled, concat(prt.name, ' ', p.name) as prtchld from tbl_permission p left join tbl_permission prt on p.parent=prt.id where 1=1 ";
	}

	@Override
	public Permission query(Long id) {
		return jdbcTemplate.queryForObject(querySql() + " and p.id = ?", new Long[] { id }, getRowMapper());
	}
	@Override
	public List<Permission> query(List<NamedQueryParam> filters) {
		StringBuilder sb = new StringBuilder();
		List<Object> args = new LinkedList<>();
		filters.forEach(p -> {
			sb.append(String.format(Locale.ENGLISH, " AND %s %s ?", p.getName(), p.getOp()));
			args.add(p.getValue());
		});
		return jdbcTemplate.query(querySql() + sb.toString(), args.toArray(), getRowMapper());
	}
	@Override
	public Permission create(final Permission e) {
		KeyHolder keyHolder = new GeneratedKeyHolder();
		jdbcTemplate.update(new PreparedStatementCreator() {

			@Override
			public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
				Long parentId = e.getParent() == null ? null : e.getParent().getId();
				PreparedStatement ps = con.prepareStatement("insert into tbl_permission(name, description, parent) values (?, ?, ?)",
						Statement.RETURN_GENERATED_KEYS);
				ps.setString(1, e.getName());
				ps.setString(2, e.getDescription());
				ps.setObject(3, parentId);
				return ps;
			}
		}, keyHolder);

		Long id = keyHolder.getKey().longValue();
		e.setId(id);
		return e;
	}

	@Override
	public Permission update(Permission e) {
		Long parentId = e.getParent() == null ? null : e.getParent().getId();
		String sql = "update tbl_permission set name=?, description=?, parent=? where id=?";
		jdbcTemplate.update(sql, new Object[] { e.getName(), e.getDescription(), parentId, e.getId() });
		return e;
	}

	@Override
	public void delete(Permission e) {
		jdbcTemplate.update(deleteSql() + " where p.id = ?", e.getId());
	}

	@Override
	public List<Permission> search(String lookup) {
		lookup = "%" + lookup + "%";
		return jdbcTemplate.query(String.format(Locale.ENGLISH,
				"% and p.name like ? or p.description like ? order by p.name", querySql()),
				new String[] { lookup, lookup }, getRowMapper());
	}

	private String matrixSql() {
		String sql = "select p.*, prt.name as parent_name, prt.description as parent_description, (CASE WHEN rp.role_id is null THEN 0 ELSE 1 END) as enabled, concat(prt.name, ' ', p.name) as prtchld from tbl_permission p left join tbl_permission prt on p.parent=prt.id left outer join tbl_role_permission rp on p.id=rp.permission_id and (rp.role_id is null or rp.role_id = ?) where 1=1";
		return sql;
	}

	public List<Permission> matrix(final Long roleId) {
		return jdbcTemplate.query(matrixSql(), new Long[] { roleId }, getRowMapper());
	}

	public List<Permission> matrixSearch(final Long roleId, final String value) {
		return jdbcTemplate.query(matrixSql() + " and (p.name like ? or p.description like ?) order by p.name",
				new Object[] { roleId, value, value }, getRowMapper());
	}
}
