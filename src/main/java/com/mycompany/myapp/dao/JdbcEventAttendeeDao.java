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
import com.mycompany.myapp.domain.EventAttendee;

@Repository
public class JdbcEventAttendeeDao implements EventAttendeeDao {
	private JdbcTemplate jdbcTemplate;

	private RowMapper<EventAttendee> rowMapper;

	// CalendarUserDao -> EventDao로 수정
	@Autowired
	private EventDao eventDao;

	@Autowired
	private CalendarUserDao calendarUserDao;

	// --- constructors ---
	public JdbcEventAttendeeDao() {
		rowMapper = new RowMapper<EventAttendee>() {
			public EventAttendee mapRow(ResultSet rs, int rowNum)
					throws SQLException {
				EventAttendee eventAttendeeList = new EventAttendee();

				/* TODO Assignment 3 */
				eventAttendeeList.setAttendee(calendarUserDao.findUser(rs
						.getInt("attendee")));
				eventAttendeeList.setEvent(eventDao.findEvent(rs
						.getInt("event_id")));
				eventAttendeeList.setId(rs.getInt("id"));

				return eventAttendeeList;
			}
		};
	}

	@Autowired
	public void setDataSource(DataSource dataSource) {
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}

	@Override
	public List<EventAttendee> findEventAttendeeByEventId(int eventId) {
		// TODO Assignment 3
		String sql_query = "select * from events_attendees where event_id = ?";
		return this.jdbcTemplate.query(sql_query, new Object[] { eventId },
				rowMapper);
	}

	@Override
	public List<EventAttendee> findEventAttendeeByAttendeeId(int attendeeId) {
		// TODO Assignment 3
		String sql_query = "select * from events_attendees where attendee = ?";
		return this.jdbcTemplate.query(sql_query, new Object[] { attendeeId },
				rowMapper);
	}

	@Override
	public int createEventAttendee(final EventAttendee eventAttendee) {
		// TODO Assignment 3
		KeyHolder keyHolder = new GeneratedKeyHolder();

		jdbcTemplate.update(new PreparedStatementCreator() {
			@Override
			public PreparedStatement createPreparedStatement(
					Connection connection) throws SQLException {
				PreparedStatement ps = connection
						.prepareStatement(
								"insert into events_attendees(event_id, attendee) values(?,?)",
								Statement.RETURN_GENERATED_KEYS);
				ps.setInt(1, eventAttendee.getEvent().getId());
				ps.setInt(2, eventAttendee.getAttendee().getId());
				return ps;
			}
		}, keyHolder);
		return keyHolder.getKey().intValue();
	}

	@Override
	public void deleteEventAttendee(int id) {
		// TODO Assignment 3
		String sql_query = "delete from events_attendees where id = ?";
		this.jdbcTemplate.update(sql_query, new Object[] { id });
	}

	@Override
	public void deleteAll() {
		// TODO Assignment 3
		String sql_query = "delete from events_attendees";
		this.jdbcTemplate.update(sql_query);
	}
}