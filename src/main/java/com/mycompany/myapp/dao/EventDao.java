package com.mycompany.myapp.dao;

import java.util.List;

import com.mycompany.myapp.domain.Event;
import com.mycompany.myapp.domain.EventLevel;

public interface EventDao {

    public Event findEvent(int eventId);

    public int createEvent(Event event);
    
    // udpateEvent -> updateEvent로 수정
    public void updateEvent(Event event); 

    public List<Event> findForOwner(int ownerUserId);

    public List<Event> findAllEvents();
    
    public List<Event> findEventsByLevel(EventLevel eventLevel);
    
    public void deleteAll();
}