package com.mycompany.myapp.dao;

import java.util.Random;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.junit.Before;
import org.junit.runner.RunWith;

import com.mycompany.myapp.domain.CalendarUser;
import com.mycompany.myapp.domain.Event;
import com.mycompany.myapp.domain.EventAttendee;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations="../applicationContext.xml")

public class DaoJUnitTest {
	@Autowired
	private CalendarUserDao calendarUserDao;	
	
	@Autowired
	private EventDao eventDao;
	
	@Autowired
	private EventAttendeeDao eventAttendeeDao;
	
	private CalendarUser[] calendarUsers = null;
	private Event[] events = null;
	private EventAttendee[] eventAttentees = null;
	
	private Random random = new Random(System.currentTimeMillis());

	private static final int numInitialNumUsers = 12;
	private static final int numInitialNumEvents = 4;
	
	@Before
	public void setUp() {
		calendarUsers = new CalendarUser[numInitialNumUsers];
		events = new Event[numInitialNumEvents];
		eventAttentees = new EventAttendee[numInitialNumEvents];
		
		this.calendarUserDao.deleteAll();
		this.eventDao.deleteAll();
		this.eventAttendeeDao.deleteAll();
		
		for (int i = 0; i < numInitialNumUsers; i++) {
			calendarUsers[i] = new CalendarUser();
			calendarUsers[i].setEmail("user" + i + "@example.com");
			calendarUsers[i].setPassword("user" + i);
			calendarUsers[i].setName("User" + i);
			calendarUsers[i].setId(calendarUserDao.createUser(calendarUsers[i]));
		}
		
		for (int i = 0; i < numInitialNumEvents; i++) {
			events[i] = new Event();
			events[i].setSummary("Event Summary - " + i);
			events[i].setDescription("Event Description - " + i);
			events[i].setOwner(calendarUsers[random.nextInt(numInitialNumUsers)]);
			switch (i) {				          /* Updated by Assignment 3 */
				case 0:
					events[i].setNumLikes(0);  							
					break;
				case 1:
					events[i].setNumLikes(9);
					break;
				case 2:
					events[i].setNumLikes(10);
					break;
				case 3:
					events[i].setNumLikes(10);
				break;
			}
			events[i].setId(eventDao.createEvent(events[i]));
		}

		for (int i = 0; i < numInitialNumEvents; i++) {
			eventAttentees[i] = new EventAttendee();
			eventAttentees[i].setEvent(events[i]);
			eventAttentees[i].setAttendee(calendarUsers[3 * i]);
			eventAttentees[i].setAttendee(calendarUsers[3 * i + 1]);
			eventAttentees[i].setAttendee(calendarUsers[3 * i + 2]);
			eventAttentees[i].setId(eventAttendeeDao.createEventAttendee(eventAttentees[i]));
		}
	}

	@Test
	public void getAllUsers() {
		// 등록된 모든 Users 개수가 numInitialNumUsers 인지 확인하는 테스트 코드  
		assertThat(this.calendarUserDao.findAllusers().size(), is(numInitialNumUsers));
	}
	
	@Test
	public void getAllEvents() {
		// 등록된 모든 Events 개수가 numInitialNumEvents 인지 확인하는 테스트 코드 
		assertThat(this.eventDao.findAllEvents().size(), is(numInitialNumEvents));
	}

	@Test
	public void getOneUserByEmail() {
		// email이 'user0@example.com'인 CalendarUser가 존재하는 것을 테스크 
		assertTrue(this.calendarUserDao.findUserByEmail("user0@example.com") != null);
	}
	
	@Test
	public void getTwoUserByEmail() {
		// partialEmail이 'user'인 CalendarUser가 numInitialNumUsers명임을 확인하는 테스크 코드 작성
		assertThat(this.calendarUserDao.findUsersByEmail("user").size(), is(numInitialNumUsers));
	}
	
	@Test
	public void getAllEventAttendees() {
		// TODO Assignment 3
		// 각 이벤트 별로 등록된 Attendee 개수가 3인지 확인하는 테스트 코드
		for (int i = 0; i < numInitialNumEvents; i++) {
			assertThat(
					this.eventAttendeeDao.findEventAttendeeByEventId(events[i].getId()).size(), is(3));
		}
	}
}