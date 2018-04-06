package tz.co.nezatech.senter.data.repository;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import tz.co.nezatech.nezadb.model.NamedQueryParam;
import tz.co.nezatech.nezadb.repository.IDataRepository;
import tz.co.nezatech.senter.data.model.Role;
import tz.co.nezatech.senter.data.model.User;

@Repository
public class UserRepository implements IDataRepository<Long, User> {
	@Autowired
	JdbcTemplate jdbcTemplate;
	private static final Logger logger = LoggerFactory.getLogger(UserRepository.class);
	@Autowired
	PermissionRepository permissionRepository;

	public RowMapper<User> getRowMapper() {
		return (rs, rowNum) -> {
			User e = new User(rs.getString("username"), rs.getString("password"), rs.getString("email"),
					rs.getString("full_name"));
			Role r = new Role(rs.getString("role_name"), rs.getString("role_description"));
			r.setId(rs.getLong("role_id"));
			r.setPermissions(permissionRepository.matrix(r.getId()));
			e.setRole(r);
			e.setId(rs.getLong("id"));
			e.setResetOn(rs.getBoolean("reset_on"));
			e.setEnabled(rs.getBoolean("enabled"));

			return e;
		};

	}

	@Override
	public String querySql() {
		return "select u.*, r.name as role_name, r.description as role_description from tbl_user u left join tbl_role r on u.role_id=r.id where 1=1 ";
	}

	@Override
	public User query(Long id) {
		return jdbcTemplate.queryForObject(querySql() + " and u.id = ?", new Long[] { id }, getRowMapper());
	}

	@Override
	public List<User> query(List<NamedQueryParam> filters) {
		StringBuilder sb = new StringBuilder();
		List<Object> args = new LinkedList<>();
		filters.forEach(p -> {
			sb.append(String.format(Locale.ENGLISH, " AND %s %s ?", p.getName(), p.getOp()));
			args.add(p.getValue());
		});
		String sql = querySql() + sb.toString();
		logger.debug(String.format(Locale.ENGLISH, "query:=>SQL{%s}: %s", args, sql));
		return jdbcTemplate.query(sql, args.toArray(), getRowMapper());
	}

	@Override
	public User create(final User e) {
		KeyHolder keyHolder = new GeneratedKeyHolder();
		jdbcTemplate.update((con) -> {
			PreparedStatement ps = con.prepareStatement(
					"insert into tbl_user(username, password, email, enabled, role_id, full_name) values (?, ?, ?, ?, ?, ?)",
					Statement.RETURN_GENERATED_KEYS);
			ps.setString(1, e.getUsername());
			ps.setString(2, e.getPassword());
			ps.setString(3, e.getEmail());
			ps.setBoolean(4, true);
			ps.setObject(5, e.getRole() != null ? e.getRole().getId() : null);
			ps.setString(6, e.getFullName());
			return ps;
		}, keyHolder);

		Long id = keyHolder.getKey().longValue();
		e.setId(id);
		return e;
	}

	@Override
	public User update(User e) {
		String sql = "update tbl_user set username=?,  password=?, email=?, enabled=?, role_id=?, full_name=? where id=?";
		jdbcTemplate.update(sql, new Object[] { e.getUsername(), e.getPassword(), e.getEmail(), e.getRole().getId(),
				e.getFullName(), e.getId() });
		return e;
	}

	@Override
	public void delete(User e) {
		jdbcTemplate.update(deleteSql() + " where u.id = ?", e.getId());
	}

	@Override
	public List<User> search(String lookup) {
		lookup = "%" + lookup + "%";
		return jdbcTemplate.query(String.format(Locale.ENGLISH,
				"% and u.username like ? or u.email like ? or u.full_name like ? order by u.username", querySql()),
				new String[] { lookup, lookup, lookup }, getRowMapper());
	}

	public void updateChangePwd(final String newPassword, final Long id) {
		String sql = "update tbl_user set reset_on = 0, password=? where id=? ";
		jdbcTemplate.update(sql, new Object[] { newPassword, id });
	}

	public void resetPwd(final User e) {
		String sql = "update tbl_user set reset_on = 1, password=? where id=? ";
		jdbcTemplate.update(sql, new Object[] { e.getPassword(), e.getId() });
	}
}
