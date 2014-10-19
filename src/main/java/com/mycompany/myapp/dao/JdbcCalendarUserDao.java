package com.mycompany.myapp.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import com.mycompany.myapp.domain.CalendarUser;

@Repository
public class JdbcCalendarUserDao implements CalendarUserDao {
	private JdbcTemplate jdbcTemplate;

	private RowMapper<CalendarUser> rowMapper;
	
	// --- constructors ---
	public JdbcCalendarUserDao() {
		rowMapper = new RowMapper<CalendarUser>() {
			public CalendarUser mapRow(ResultSet rs, int rowNum) throws SQLException {
				CalendarUser calendarUser = new CalendarUser();
				calendarUser.setId(rs.getInt("id"));
				calendarUser.setEmail(rs.getString("email"));
				calendarUser.setPassword(rs.getString("password"));
				calendarUser.setName(rs.getString("name"));

				return calendarUser;
			}
		};
	}

	@Autowired
	public void setDataSource(DataSource dataSource){
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}

	// --- CalendarUserDao methods ---
	@Override
	public CalendarUser findUser(int id){
		String sql_query = "select * from calendar_users where id = ?";
		return this.jdbcTemplate.queryForObject(sql_query, new Object[] {id}, rowMapper);
	}

	@Override
	public CalendarUser findUserByEmail(String email) {
		String sql_query = "select * from calendar_users where email = ?";
		return this.jdbcTemplate.queryForObject(sql_query, new Object[] {email}, rowMapper);
	}

	@Override
	public List<CalendarUser> findUsersByEmail(String email) {
		String sql_query;
		if(email == null)
			sql_query = "select * from calendar_users";
		else
			sql_query = "select * from calendar_users where email like '%"+email+"%'";

		return this.jdbcTemplate.query(sql_query, rowMapper);
	}

	@Override
	public int createUser(final CalendarUser userToAdd){
		KeyHolder keyHolder = new GeneratedKeyHolder();

		jdbcTemplate.update(new PreparedStatementCreator() {
			@Override
			public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
				PreparedStatement ps = connection.prepareStatement("insert into calendar_users(email, password, name) values(?,?,?)", Statement.RETURN_GENERATED_KEYS);
				ps.setString(1, userToAdd.getEmail());
				ps.setString(2, userToAdd.getPassword());
				ps.setString(3, userToAdd.getName());

				return ps;
			}
		}, keyHolder);
		return keyHolder.getKey().intValue();
	}
	
	@Override
    public List<CalendarUser> findAllusers() {
		String sql_query = "select * from calendar_users";
		return this.jdbcTemplate.query(sql_query, rowMapper);
    }

	@Override
	public void deleteAll() {
		// Assignment 2
		String sql = "delete from calendar_users";
		this.jdbcTemplate.update(sql);
	}
}