package com.felix.thread;

import com.felix.controller.ControlQueue;

public class ParseData implements Runnable{
	// Implementa o Runnable para que possa ser rodada por uma thread
	
	private String threadName;
	private String filePath;
	
	public ParseData(String threadName, String filePath) {
		this.threadName = threadName;
		this.filePath = filePath;
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
				System.out.println(task + " :: " + threadName);
			}
		}while(ControlQueue.isFinished());
		System.out.println(threadName + " has finished");
	}
}
