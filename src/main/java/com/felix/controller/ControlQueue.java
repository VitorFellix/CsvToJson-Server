package com.felix.controller;

import java.util.List;
import java.util.Vector;

import com.felix.thread.ParseData;

public class ControlQueue {

	private static List<String> TaskQueue;//Lista de tarefas
	private static boolean receivingData = true;


	public ControlQueue() {
		//Utilizo Vector pois ele � Thread-Safe
		TaskQueue = new Vector<String>();
		
		//Levanta as threads antes
		for (int i = 0; i < 2; i++) {
			new Thread(new ParseData("thread " + i,"FILEPATH")).start();
		}
		
		//Recebe os dados
		//this.generateData();
	}

	// M�todo �nico que as threads v�o acessar
	/*
	public static boolean IsFinished(List<String> TaskQueue) {
		if(((ControlQueue) TaskQueue).GetNextTask() == null &&
				((ControlQueue) TaskQueue).isReceivingData() == false) {
			return true;
		}
		return false;
	}

	public boolean isReceivingData() {
		return receivingData;
	}
	*/
	
	public static boolean isFinished() {
		return receivingData || TaskQueue.size() > 0;
	}
	
	public void generateData () {
		//Gerar um dado aleat�rio
		receivingData = true;
		int quantidadeTotal = 1000000000;
		int numRegistro = 0;
		do {
			numRegistro++;
			String task = "task" + numRegistro;
			addTask(task);
			System.out.println(task + " :: created");
			/*
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			*/
		}while(numRegistro <= quantidadeTotal);
		receivingData = false;
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
}
