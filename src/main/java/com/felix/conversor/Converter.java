package com.felix.conversor;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import com.felix.classes.People;
import com.felix.classes.TimeRegistry;
import com.felix.controller.ControlQueue;
import com.google.gson.Gson;

import javafx.scene.control.TextArea;

public class Converter {
	// #region Variaveis
	Scanner scanner;
	ArrayList<TimeRegistry> timeRegistry;
	ControlQueue controlQueue;
	// #endregion

	public Converter() throws IOException {
		this.timeRegistry = new ArrayList<TimeRegistry>();
	}

	public void convert(File Csv, File savePath, TextArea textArea) {
		
		// Get path
		Path path = Paths.get(Csv.getAbsolutePath());

		// Verifica se o diretório existe
		if (Csv.exists()) {

			// Lê o Arquivo
			List<String> fileContent = readFile(path, textArea);

			// Cria lista de pessoas
			List<People> peoples = parseToPeoples(fileContent, textArea);

			// Cria o arquivo JSON
			createJsonFile(peoples, savePath, textArea);

			// Registra os tempos em um arquivo
			escreverTempos(savePath, textArea);
		}
	}
	private List<String> readFile(Path path, TextArea textArea) {
		try {
			Instant start = Instant.now();
			textArea.appendText("Lendo Arquivo CSV\n");
			// Lê o todas as linhas do arquivo
			List<String> fileContent = Files.readAllLines(path, StandardCharsets.UTF_8);
			timeRegistry.add(new TimeRegistry("read ",Duration.between(start, Instant.now()).toNanos()));

			textArea.appendText("Arquivo CSV foi lido\n");
			return fileContent;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	private List<People> parseToPeoples(List<String> fileContent, TextArea textArea) {

		textArea.appendText("Convertendo para Objetos\n");
		Instant start = Instant.now();

		controlQueue = new ControlQueue(fileContent, 3);
		List<People> listOfPeople = controlQueue.getParsedData();
		
		try {
			//Espera as Threads terminaram para continuar a processar
			for(Thread thread : ControlQueue.ListaDeThreads) {
				thread.join();
			}
			
			//ControlQueue.th1.join();
			//ControlQueue.th2.join();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		// Registra o tempo que demorou
		timeRegistry.add(new TimeRegistry("parse",Duration.between(start, Instant.now()).toNanos()));

		textArea.appendText("Objetos foram convertidos\n");
		return listOfPeople;
	}


	private <T> void createJsonFile(List<T> objectList, File savePath, TextArea textArea) {

		textArea.appendText("Escrevendo JSON\n");
		Instant start = Instant.now();
		// Cria o objeto Gson
		Gson gson = new Gson();

		// Get path
		Path path = Paths.get(savePath.getAbsolutePath() + "//File.json");

		// Lê o conteudo do arquivo e tranforma em uma String no formato JSON
		String JSON = gson.toJson(objectList);

		// Escreve no arquivo o JSON
		// Adiciona .new ao do arquivo até que não exista nenhum arquivo com o
		// nome igual
		if(Files.exists(path)) {
			try {
				System.out.println(path + " :: File already exists");
				Files.delete(path);
				System.out.println(path + " :: File deleted");

			} catch (IOException e) {e.printStackTrace();}
		}
		
		try {
			BufferedWriter writer = Files.newBufferedWriter(path,
					StandardOpenOption.CREATE_NEW);
			System.out.println(path + " :: File created");
			System.out.println(path + " :: Writing File");
			writer.write(JSON);
			writer.close();
			System.out.println(path + " :: Done Writing");
			textArea.appendText("JSON foi escrito\n");

		} catch (Exception e) {
		}
		
		timeRegistry.add(new TimeRegistry("write",Duration.between(start, Instant.now()).toNanos()));
	}



	private void escreverTempos(File savePath, TextArea textArea) {
		try {
			textArea.appendText("Escrevendo tempos\n");
			Path path = Paths.get(savePath.getAbsolutePath() + "//Times.txt");
			BufferedWriter writer;
			if (Files.exists(path)) {
				System.out.println(path + " :: File already exists");
				Files.delete(path);
				System.out.println(path + " :: File deleted");
			}

			writer = Files.newBufferedWriter(path, StandardOpenOption.CREATE_NEW);
			
			System.out.println(path + " :: File created");

			String strg = "===================================================\n";
			List<TimeRegistry> temp = controlQueue.getTimeRegistries();
			for (TimeRegistry registry : temp) {
				timeRegistry.add(registry);
			}
			for (int i = 0; i < timeRegistry.size(); i++) {
					strg += timeRegistry.get(i).toString() + "\n";
			}
			strg += "===================================================";

			System.out.println(path + " :: Writing File");
			writer.write(strg);
			writer.close();
			System.out.println(path + " :: Done Writing");
			textArea.appendText("Tempos foram escritos\n");

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
