package tz.co.nezatech.cusi.data.repository;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import tz.co.nezatech.cusi.data.model.Role;
import tz.co.nezatech.nezadb.model.NamedQueryParam;
import tz.co.nezatech.nezadb.repository.IDataRepository;

@Repository
public class RoleRepository implements IDataRepository<Long, Role> {
	@Autowired
	JdbcTemplate jdbcTemplate;

	public RowMapper<Role> getRowMapper() {
		return (rs, rowNum) -> {
			Role e = new Role(rs.getString("name"), rs.getString("description"));
			e.setId(rs.getLong("id"));
			return e;
		};
	}

	@Override
	public String querySql() {
		return "select r.* from tbl_role r where 1=1 ";
	}

	@Override
	public Role query(Long id) {
		return jdbcTemplate.queryForObject(querySql() + " and r.id = ?", new Long[] { id }, getRowMapper());
	}

	@Override
	public List<Role> query(List<NamedQueryParam> filters) {
		StringBuilder sb = new StringBuilder();
		List<Object> args = new LinkedList<>();
		filters.forEach(p -> {
			sb.append(String.format(Locale.ENGLISH, " AND %s %s ?", p.getName(), p.getOp()));
			args.add(p.getValue());
		});
		return jdbcTemplate.query(querySql() + sb.toString(), args.toArray(), getRowMapper());
	}

	@Override
	public Role create(final Role e) {
		KeyHolder keyHolder = new GeneratedKeyHolder();
		jdbcTemplate.update((con) -> {
			PreparedStatement ps = con.prepareStatement("insert into tbl_role(name, description) values (?, ?)",
					Statement.RETURN_GENERATED_KEYS);
			ps.setString(1, e.getName());
			ps.setString(2, e.getDescription());
			return ps;
		}, keyHolder);

		keyHolder.getKeyList().forEach(m -> {
			e.setId((Long) m.get("id"));
		});
		return e;
	}

	@Override
	public Role update(Role e) {
		String sql = "update tbl_role set name=?,  description=? where id=?";
		jdbcTemplate.update(sql, new Object[] { e.getName(), e.getDescription(), e.getId() });
		return e;
	}

	@Override
	public void delete(Long id) {
		jdbcTemplate.update(deleteSql() + " where u.id = ?", id);
	}

	@Override
	public List<Role> search(String lookup) {
		lookup = "%" + lookup + "%";
		return jdbcTemplate.query(
				String.format(Locale.ENGLISH, "% and name like ? or description like ? order by r.name", querySql()),
				new String[] { lookup, lookup }, getRowMapper());
	}

	public void manageMatrix(final Role e) {
		final List<Long> inserts = new ArrayList<Long>();
		final List<Long> deletes = new ArrayList<Long>();
		for (Iterator<String> iterator = e.getMtrxPermissionIds().iterator(); iterator.hasNext();) {
			String line = iterator.next();
			String tokens[] = line.split("-");
			Long permissionId = Long.parseLong(tokens[0]);
			boolean wasEnabled = Boolean.parseBoolean(tokens[1]);
			if (wasEnabled) {
				if (!e.getPermissionIds().contains(tokens[0])) {
					deletes.add(permissionId);
				}
			} else {
				if (e.getPermissionIds().contains(tokens[0])) {
					inserts.add(permissionId);
				}
			}
		}
		// Inserts
		matrixInserts(inserts, e);
		// Deletes
		matrixDeletes(deletes, e);
	}

	public void matrixInserts(final List<Long> inserts, final Role e) {
		String sql = "INSERT INTO tbl_role_permission (permission_id, role_id) VALUES (?, ?) ON CONFLICT (permission_id, role_id) DO NOTHING ";
		List<Object[]> args = new LinkedList<>();
		inserts.forEach(id -> {
			args.add(new Long[] { id, e.getId() });
		});
		jdbcTemplate.batchUpdate(sql, args);
	}

	public void matrixDeletes(final List<Long> deletes, final Role e) {
		String sql = "DELETE FROM tbl_role_permission where permission_id=? and role_id=? ";
		List<Object[]> args = new LinkedList<>();
		deletes.forEach(id -> {
			args.add(new Long[] { id, e.getId() });
		});
		jdbcTemplate.batchUpdate(sql, args);
	}
}
