package tz.co.nezatech.cusi.data.repository;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.Date;
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

import tz.co.nezatech.cusi.data.model.User;
import tz.co.nezatech.cusi.data.model.VerificationToken;
import tz.co.nezatech.nezadb.model.NamedQueryParam;
import tz.co.nezatech.nezadb.repository.IDataRepository;

@Repository
public class VerificationTokenRepository implements IDataRepository<Long, VerificationToken> {
	@Autowired
	JdbcTemplate jdbcTemplate;
	private static final Logger logger = LoggerFactory.getLogger(UserRepository.class);
	@Autowired
	PermissionRepository permissionRepository;

	public RowMapper<VerificationToken> getRowMapper() {
		return (rs, rowNum) -> {
			VerificationToken e = new VerificationToken(rs.getLong("id"), rs.getLong("user_id"), rs.getString("token"),
					fromSQLTimestamp(rs.getTimestamp("record_date")));
			return e;
		};
	}

	@Override
	public String querySql() {
		return "select vt.* from tbl_verification_token vt where 1=1 ";
	}

	@Override
	public VerificationToken query(Long id) {
		return jdbcTemplate.queryForObject(querySql() + " and vt.id = ?", new Long[] { id }, getRowMapper());
	}

	@Override
	public List<VerificationToken> query(List<NamedQueryParam> filters) {
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
	public VerificationToken create(final VerificationToken e) {
		KeyHolder keyHolder = new GeneratedKeyHolder();
		jdbcTemplate.update((con) -> {
			PreparedStatement ps = con.prepareStatement(
					"insert into tbl_verification_token(user_id, token, record_date) values (?, ?, ?)",
					Statement.RETURN_GENERATED_KEYS);
			ps.setLong(1, e.getUserId());
			ps.setString(2, e.getToken());
			ps.setTimestamp(3, toSQLTimestamp(e.getRecordDate()));
			return ps;
		}, keyHolder);

		keyHolder.getKeyList().forEach(m -> {
			e.setId((Long) m.get("id"));
		});
		return e;
	}

	@Override
	public VerificationToken update(VerificationToken e) {
		String sql = "update tbl_user set user_id=?,  token=?, record_date=? where id=?";
		jdbcTemplate.update(sql, new Object[] { e.getUserId(), e.getToken(), toSQLTimestamp(e.getRecordDate()) });
		return e;
	}

	@Override
	public void delete(Long id) {
		jdbcTemplate.update(deleteSql() + " where u.id = ?", id);
	}

	@Override
	public List<VerificationToken> search(String lookup) {
		lookup = "%" + lookup + "%";
		return jdbcTemplate.query(
				String.format(Locale.ENGLISH, "% and vt.token like ? order by vt.record_date desc", querySql()),
				new String[] { lookup, lookup, lookup }, getRowMapper());
	}

	public VerificationToken createVerificationToken(User e, String token) {
		return this.create(new VerificationToken(e.getId(), token, new Date()));
	}

}
