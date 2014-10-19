package com.mycompany.myapp.domain;

import java.util.Calendar;

public class Event {
    private Integer id;
    private Calendar when;
    private String summary;
    private String description;
    private CalendarUser owner;
    private int numLikes;                     /* Updated by Assignment 3 */
    private EventLevel eventLevel;            /* Updated by Assignment 3 */
    
    public Event(){
    	eventLevel = EventLevel.NORMAL;
    }

    /* Updated by Assignment 3 - Start */
    public int getNumLikes() {
		return numLikes;
	}

	public void setNumLikes(int numLikes) {
		this.numLikes = numLikes;
	}
	
	public EventLevel getEventLevel() {
		return eventLevel;
	}

	public void setEventLevel(EventLevel eventLevel) {
		this.eventLevel = eventLevel;
	}	
    /* Updated by Assignment 3 - End */
    
	public Integer getId() {
        return id;
    }
    
    public Calendar getWhen() {
        return when;
    }

    public String getSummary() {
        return summary;
    }

    public String getDescription() {
        return description;
    }
    
    public CalendarUser getOwner() {
        return owner;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setWhen(Calendar when) {
        this.when = when;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setOwner(CalendarUser owner) {
        this.owner = owner;
    }
    
	public void upgradeLevel() {
		EventLevel nextLevel = this.eventLevel.nextLevel();
		if (nextLevel == null) {
			throw new IllegalStateException(this.eventLevel+ "is not able to be upgraded."); 
		} else {
			this.eventLevel = nextLevel;
		}
	}

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Event other = (Event) obj;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        return true;
    }
}