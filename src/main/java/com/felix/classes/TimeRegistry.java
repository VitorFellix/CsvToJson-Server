package com.felix.classes;

public class TimeRegistry {
	@Override
	public String toString() {
		return "[" + name + " :: duration=" + duration + "]";
	}

	private String name;
	private Long duration;

	public TimeRegistry(String name, Long duration) {
		super();
		this.name = name;
		this.duration = duration;
	}
}
