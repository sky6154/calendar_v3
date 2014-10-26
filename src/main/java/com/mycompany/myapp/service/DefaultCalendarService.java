package com.mycompany.myapp.service;

import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.Driver;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.util.List;
import java.util.logging.Logger;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import com.mycompany.myapp.dao.CalendarUserDao;
import com.mycompany.myapp.dao.EventAttendeeDao;
import com.mycompany.myapp.dao.EventDao;
import com.mycompany.myapp.domain.CalendarUser;
import com.mycompany.myapp.domain.Event;
import com.mycompany.myapp.domain.EventAttendee;
import com.mycompany.myapp.domain.EventLevel;

@Service
public class DefaultCalendarService implements CalendarService {
	@Autowired
	private EventDao eventDao;

	@Autowired
	private CalendarUserDao userDao;

	// Test파일의 upgradeEventLevels에서 Autowired가 필요
	@Autowired
	private PlatformTransactionManager transactionManager;

	@Autowired
	private EventAttendeeDao eventAttendeeDao;

	private static final int numberOfUpgrade = 10;

	// 추가됨(Test파일의 upgradeAllOrNothing에서 필요)
	public void setTransactionManager(
			PlatformTransactionManager transactionManager) {
		this.transactionManager = transactionManager;
	}
	
	// 추가됨(Test파일의 upgradeAllOrNothing에서 필요)
	public void setEventDao(EventDao eventDao){
		this.eventDao = eventDao;
	}

	/* CalendarUser */
	// userDao의 메소드 이용
	@Override
	public CalendarUser getUser(int id) {
		// TODO Assignment 3
		return userDao.findUser(id);
	}

	@Override
	public CalendarUser getUserByEmail(String email) {
		// TODO Assignment 3
		return userDao.findUserByEmail(email);
	}

	@Override
	public List<CalendarUser> getUsersByEmail(String partialEmail) {
		// TODO Assignment 3
		return userDao.findUsersByEmail(partialEmail);
	}

	@Override
	public int createUser(CalendarUser user) {
		// TODO Assignment 3
		return userDao.createUser(user);
	}

	@Override
	public void deleteAllUsers() {
		// TODO Assignment 3
		userDao.deleteAll();
	}

	/* Event */
	// eventDao의 메소드 이용
	@Override
	public Event getEvent(int eventId) {
		// TODO Assignment 3
		return eventDao.findEvent(eventId);
	}

	@Override
	public List<Event> getEventForOwner(int ownerUserId) {
		// TODO Assignment 3
		return eventDao.findForOwner(ownerUserId);
	}

	@Override
	public List<Event> getAllEvents() {
		// TODO Assignment 3
		return eventDao.findAllEvents();
	}

	@Override
	public int createEvent(Event event) {
		// TODO Assignment 3

		if (event.getEventLevel() == null) {
			event.setEventLevel(EventLevel.NORMAL);
		}

		return eventDao.createEvent(event);
	}

	@Override
	public void deleteAllEvents() {
		// TODO Assignment 3
		eventDao.deleteAll();
	}

	/* EventAttendee */
	// eventAttendeeDao의 메소드 이용
	@Override
	public List<EventAttendee> getEventAttendeeByEventId(int eventId) {
		// TODO Assignment 3
		return eventAttendeeDao.findEventAttendeeByEventId(eventId);
	}

	@Override
	public List<EventAttendee> getEventAttendeeByAttendeeId(int attendeeId) {
		// TODO Assignment 3
		return eventAttendeeDao.findEventAttendeeByAttendeeId(attendeeId);
	}

	@Override
	public int createEventAttendee(EventAttendee eventAttendee) {
		// TODO Assignment 3
		return eventAttendeeDao.createEventAttendee(eventAttendee);
	}

	@Override
	public void deleteEventAttendee(int id) {
		// TODO Assignment 3
		eventAttendeeDao.deleteEventAttendee(id);
	}

	@Override
	public void deleteAllEventAttendees() {
		// TODO Assignment 3
		eventAttendeeDao.deleteAll();
	}

	/* upgradeEventLevels */
	@Override
	public void upgradeEventLevels() throws Exception {
		// TODO Assignment 3
		// 트랜잭션 관련 코딩 필요함
		
		// 트랜잭션 시작
		TransactionStatus status = this.transactionManager
				.getTransaction(new DefaultTransactionDefinition());

		try {
			List<Event> events = eventDao.findAllEvents();
			// Event마다 업데이트가 가능하면 업데이트
			for (Event event : events) {
				if (canUpgradeEventLevel(event))
					upgradeEventLevel(event);
			}
			// 성공 시 commit
			this.transactionManager.commit(status);
		} catch (RuntimeException e) {
			// 실패 시 rollback
			this.transactionManager.rollback(status);
			throw e;
		}

	}

	@Override
	public boolean canUpgradeEventLevel(Event event) {
		// TODO Assignment 3
		// 해당 Event의 좋아요 갯수가 numberOfUpgrade 이상이면 true 반환
		return event.getNumLikes() >= numberOfUpgrade;
	}

	@Override
	public void upgradeEventLevel(Event event) {
		event.upgradeLevel();
		// udpateEvent -> updateEvent로 수정
		eventDao.updateEvent(event);
	}
}