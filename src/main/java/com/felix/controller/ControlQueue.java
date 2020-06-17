package com.felix.controller;

import java.util.List;
import java.util.Vector;

import com.felix.classes.People;
import com.felix.classes.TimeRegistry;
import com.felix.thread.ParseData;

public class ControlQueue {

	private static List<String> ListaDeTasks;//Lista de tarefas
	private static List<People> ListaDePessoas;//Lista dos dados convertidos para objetos
	private static List<TimeRegistry> ListaDeTempos;//Lista de registro de tempos
	private static boolean receivingData = true;
	public static List<Thread> ListaDeThreads;
	private String ThreadName = "Threadripper";
	//public static Thread th1;
	//public static Thread th2;


	public ControlQueue(List<String> ListaDeDados, int NumThreads) {
		//Utilizar Vector pois e Thread-Safe
		ListaDeTasks = new Vector<String>();
		ListaDePessoas = new Vector<People>();
		ListaDeTempos = new Vector<TimeRegistry>();
		ListaDeThreads = new Vector<Thread>();

		//Levanta as threads
		
		//th1 = new Thread(new ParseData("thread 1"));
		//th2 = new Thread(new ParseData("thread 2"));
		//th1.start();
		//th2.start();
		
		Thread th = new Thread(new ParseData(ThreadName));
		th.setName(ThreadName);
		System.out.println("Nova Thread :: " + th.getName() + " Starting");
		th.start();
		ListaDeThreads.add(th);
		
		this.receiveData(ListaDeDados);
	}
	
	public void receiveData (List<String> dados) {
		//Gerar um dado aleatorio
		receivingData = true;
		
		for (String string : dados) {
			addTask(string);
		}

		receivingData = false;
	}

	public static boolean isFinished() {
		return receivingData || ListaDeTasks.size() > 0;
	}
	
	//E necesserio synchronized para que nao exista duplicidade de acesso das threads
	//A thread que chega depois sempre espera a anterior
	//SEMPRE DEVE SER UM METODO BEM LEVE PARA NaO GERAR GARGALO
	
	public static synchronized String getNextTask() {
		//Remove o elemento com base no Index e retorna este elemento
		if(ListaDeTasks.size() > 0)
			// Remova apenas se tiver alguma coisa na lista
			return ListaDeTasks.remove(0);
		return null;
	}
	
	public void addTask(String Task) {
		//Adiciona na lista o par�metro
		ListaDeTasks.add(Task);
	}
	
	public static synchronized void addData(People data) {
		//Adiciona na lista o par�metro
		ListaDePessoas.add(data);
	}
	
	public List<People> getParsedData() {
		return ListaDePessoas;
	}

	public static void addTimeRegistries(List<TimeRegistry> timeRegistry) {
		for (TimeRegistry registry : timeRegistry) {
			ListaDeTempos.add(registry);
		}
	}
	public List<TimeRegistry> getTimeRegistries(){
		return ListaDeTempos;
	}
}
