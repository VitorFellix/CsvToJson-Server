package com.felix.controller;

import java.util.List;
import java.util.Vector;

import com.felix.classes.People;
import com.felix.classes.TimeRegistry;
import com.felix.thread.ParseData;

public class ControlQueue {

	private static List<String> TaskQueue;//Lista de tarefas
	private static List<People> ParsedData;//Lista dos dados convertidos para objetos
	private static List<TimeRegistry> timeRegistries;//Lista de registro de tempos
	private static boolean receivingData = true;
	public static List<Thread> threads;
	//public static Thread th1;
	//public static Thread th2;


	public ControlQueue(List<String> dados, int NumThreads) {
		//Utilizar Vector pois e Thread-Safe
		TaskQueue = new Vector<String>();
		ParsedData = new Vector<People>();
		timeRegistries = new Vector<TimeRegistry>();
		threads = new Vector<Thread>();

		//Levanta as threads
		//for (int i = 0; i < NumThreads; i++) {new Thread(new ParseData("thread " + i)).start();}
		
		//th1 = new Thread(new ParseData("thread 1"));
		//th2 = new Thread(new ParseData("thread 2"));
		//th1.start();
		//th2.start();
		
		this.receiveData(dados);
	}
	
	public void receiveData (List<String> tasks) {
		//Gerar um dado aleatorio
		receivingData = true;
		
		int counter = 0;

		for (String string : tasks) {
			addTask(string);
			//System.out.println("1 counter = " + counter + " :: counter100 = " + counter100);
			if(counter % 100 == 0) {
				Thread th = new Thread(new ParseData("thread " + counter));
				System.out.println("Nova Thread :: " + th.getName() + " Starting");
				th.start();
				threads.add(th);
			}
			counter += 1;
		}

		receivingData = false;
	}

	public static boolean isFinished() {
		return receivingData || TaskQueue.size() > 0;
	}
	
	//� necess�rio synchronized para que n�o exista duplicidade de acesso das threads
	//A thread que chega depois sempre espera a anterior
	//SEMPRE DEVE SER UM M�TODO BEM LEVE PARA N�O GERAR GARGALO
	
	public static synchronized String getNextTask() {
		//Remove o elemento com base no Index e retorna este elemento
		if(TaskQueue.size() > 0)
			// Remova apenas se tiver alguma coisa na lista
			return TaskQueue.remove(0);
		return null;
	}
	
	public void addTask(String Task) {
		//Adiciona na lista o par�metro
		TaskQueue.add(Task);
	}
	
	public static synchronized void addData(People data) {
		//Adiciona na lista o par�metro
		ParsedData.add(data);
	}
	
	public List<People> getParsedData() {
		return ParsedData;
	}

	public static void addTimeRegistries(List<TimeRegistry> timeRegistry) {
		for (TimeRegistry registry : timeRegistry) {
			timeRegistries.add(registry);
		}
	}
	public List<TimeRegistry> getTimeRegistries(){
		return timeRegistries;
	}
}
