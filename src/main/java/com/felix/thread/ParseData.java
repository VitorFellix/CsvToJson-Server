package com.felix.thread;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import com.felix.classes.People;
import com.felix.classes.TimeRegistry;
import com.felix.controller.ControlQueue;

public class ParseData implements Runnable{
	// Implementa o Runnable para que possa ser rodada por uma thread
	
	private String threadName;
	private List<TimeRegistry> timeRegistry;
	
	public ParseData(String threadName) {
		this.threadName = threadName;
		this.timeRegistry = new ArrayList<TimeRegistry>();
	}

	public void run() {
		do {
			String task = ControlQueue.getNextTask();
			if(task == null) {
				//Sync this Thread
				synchronized (this) {
					try {
						//Pode sair do wait antes do tempo acabar
						this.wait(500);
						//System.out.println(this.threadName + " is waiting");
					} catch (Exception e) {
						// TODO: handle exception
					}
				}
			}else {
				Instant start = Instant.now();
				Parse(task);
				timeRegistry.add(new TimeRegistry(threadName,Duration.between(start, Instant.now()).toNanos()));
				//System.out.println(threadName  + " :: " + Parse(task).toString());
			}
		}while(ControlQueue.isFinished());
		calculateTimes();
		//System.out.println(threadName + " :: Has finished");

	}
	
	private People Parse(String task){
		try {
			// Separates the Array in 3 parts
			String[] formatedString = task.split(",");
			// Removes unwanted chars
			formatedString[6] = formatedString[6].replace("\"", "");
			People people = new People(formatedString);
			ControlQueue.addData(people);
			return people;
		} catch (Exception e) {
			e.getMessage();
			return null;
		}
	}
	private void calculateTimes(){
		TimeRegistry max = new TimeRegistry("null", (long) 0);
		TimeRegistry min = new TimeRegistry("null", Long.MAX_VALUE);
		Long Media = (long) 0;
		for (TimeRegistry registry : timeRegistry) {
			if(max.getDuration() < registry.getDuration()){
				max = registry;
			}
			if(min.getDuration() > registry.getDuration()){
				min = registry;
			}
			Media += registry.getDuration();
		}
		TimeRegistry media = new TimeRegistry("Med " + threadName,Media / timeRegistry.size());
		max.setName("Max " + threadName);
		min.setName("Min " + threadName);

		List<TimeRegistry> newTimeRegistry = new ArrayList<TimeRegistry>();

		newTimeRegistry.add(max);
		newTimeRegistry.add(min);
		newTimeRegistry.add(media);

		ControlQueue.addTimeRegistries(newTimeRegistry);
	}
}
