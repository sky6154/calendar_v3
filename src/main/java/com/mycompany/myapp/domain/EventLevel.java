package com.mycompany.myapp.domain;

public enum EventLevel {
	HOT(2, null),
	NORMAL(1, HOT);
	
	private final int value;
	private final EventLevel next;
	
	EventLevel(int value, EventLevel next) {
		this.value = value;
		this.next = next;
	}
	
	public int intValue() {
		return value;
	}
	
	public EventLevel nextLevel() {
		return this.next;
	}
	
	public static EventLevel valueOf(int value) {
		switch(value) {
			case 1: return NORMAL;
			case 2: return HOT;
			default:  throw new AssertionError("Unknown value: " + value);
		}
	}
}
