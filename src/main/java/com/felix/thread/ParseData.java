package com.felix.thread;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;

import com.felix.classes.People;
import com.felix.controller.ControlQueue;

public class ParseData implements Runnable{
	// Implementa o Runnable para que possa ser rodada por uma thread
	
	private String threadName;
	private ArrayList<Long> timeRegistry;
	
	public ParseData(String threadName) {
		this.threadName = threadName;
		this.timeRegistry = new ArrayList<Long>();
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
				timeRegistry.add(Duration.between(start, Instant.now()).toMillis());
				//System.out.println(threadName  + " :: " + Parse(task).toString());
			}
		}while(ControlQueue.isFinished());
		System.out.println(threadName + " has finished");
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
	
	private String getName() {
		return threadName;
	}
	private ArrayList<Long> getTimeRegistry() {
		return timeRegistry;
	}
}
