package com.mycompany.myapp.service;

import java.util.Calendar;
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
import static org.hamcrest.CoreMatchers.isA;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations="../applicationContext.xml")

public class CalendarServiceTest {
	@Autowired
	private CalendarService calendarService;	

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
		
		this.calendarService.deleteAllUsers();
		this.calendarService.deleteAllEvents();
		this.calendarService.deleteAllEventAttendees();
		
		for (int i = 0; i < numInitialNumUsers; i++) {
			calendarUsers[i] = new CalendarUser();
			calendarUsers[i].setEmail("user" + i + "@example.com");
			calendarUsers[i].setPassword("user" + i);
			calendarUsers[i].setName("User" + i);
			calendarUsers[i].setId(calendarService.createUser(calendarUsers[i]));
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
			events[i].setId(calendarService.createEvent(events[i]));
		}
		
		for (int i = 0; i < numInitialNumEvents; i++) {
			eventAttentees[i] = new EventAttendee();
			eventAttentees[i].setEvent(events[i]);
			eventAttentees[i].setAttendee(calendarUsers[3 * i ]);
			eventAttentees[i].setAttendee(calendarUsers[3 * i + 1]);
			eventAttentees[i].setAttendee(calendarUsers[3 * i + 2]);
			eventAttentees[i].setId(calendarService.createEventAttendee(eventAttentees[i]));
		}
	}
	
	@Test
	public void CalendarServiceBeanTest() {
		assertThat(calendarService, notNullValue() );
	}
	
	
	@Test
	public void upgradeEventLevels() throws Exception{
		this.calendarService.upgradeEventLevels();
		
		checkEventLevelUpgraded(events[0], false);
		checkEventLevelUpgraded(events[1], false);
		checkEventLevelUpgraded(events[2], true);
		checkEventLevelUpgraded(events[3], true);
	}
	
	private void checkEventLevelUpgraded(Event event, boolean upgraded) {
		Event eventFromDB = calendarService.getEvent(event.getId());
		if (upgraded) {
			assertThat(eventFromDB.getEventLevel(), is(event.getEventLevel().nextLevel()));
		}
		else {
			assertThat(eventFromDB.getEventLevel(), is(event.getEventLevel()));
		}
	}
	
	@Test
	public void upgradeAllOrNothing() throws Exception{
		CalendarService testCalendarService = new TestCalendarService(events[3].getId());
		
		try {
			testCalendarService.upgradeEventLevels();
			fail("TestUserServiceException expected");
		}
		catch(TestCalenadarServiceException e) {
			assertThat(e, isA(TestCalenadarServiceException.class));
		}
		
		checkEventLevelUpgraded(events[2], false);
	}
	
	static class TestCalendarService extends DefaultCalendarService {
		private int faultId;
		
		public TestCalendarService(int faultId) {
			this.faultId = faultId;
		}
		
		public void upgradeEventLevel(Event event) {
			if ( event.getId().equals(this.faultId) ) throw new TestCalenadarServiceException();
			super.upgradeEventLevel(event);
		}
	}
	
	static class TestCalenadarServiceException extends RuntimeException {
	}
}