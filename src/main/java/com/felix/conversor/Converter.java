package com.felix.conversor;

import java.io.BufferedWriter;
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
import com.google.gson.Gson;

public class Converter{

	// #region Variaveis
	Scanner scanner;
	String filePathCSV;
	String filePathJSON;
	ArrayList<Long> timeRegistry;
	Long[] performanceTimes;
	// #endregion

	// Começa o programa
	// Run(args, filePathJSON, tempos, Input, Calculedtempos);

	public Converter(String filePathCSV, String filePathJSON) {
		scanner = new Scanner(System.in);
		this.filePathCSV = filePathCSV;
		this.filePathJSON = filePathJSON;
		this.timeRegistry = new ArrayList<Long>();
		performanceTimes = new Long[9];
	}

	public void startMenu() {
		
		// Dá a opção para sair ou continuar
		System.out.println("Write 'q' to exit or write 'y' to convert an file.");
		String InputKey = scanner.nextLine();
		
		// Verifica o Input recebido
		if (InputKey.equals("q")) {
			// Finaliza o Programa
			return;
		} else if (InputKey.equals("y")) {
			// Continua a converter
			convert();
		} else {
			System.out.println("Please write 'q' or 'y'");
			startMenu();
		}

		// Informa a saída do programa
		System.out.println("Closing...");
	}

	private void convert() {
		// Input do diretório
		String filePathCSV;
		System.out.println("Escreva o diretório do arquivo: \n (Exemplo: resources/brasil.csv)");
		filePathCSV = scanner.next();
		filePathCSV.replace("/", "//");

		// Get path
		Path path = Paths.get(filePathCSV);

		// Verifica se o diretório existe
		if (Files.exists(path)) {
			
			// Lê o Arquivo
			String[] fileContent = readFile(path);

			// Cria lista de pessoas
			List<People> peoples = createListOfPeoples(fileContent);

			// Cria o arquivo JSON
			createJsonFile(peoples);

			// Calcula os tempos med, min e max
			performanceTimes = calcularTempos();

			// Registra os tempos em um arquivo
			escreverTempos("resources//Times.txt");
			startMenu();
		} 
		// Se não existe
		else {
			System.out.println("\nERRO!!!\nO arquivo não existe no diretório selecionado!");
			startMenu();
		}
	} 


	private List<People> createListOfPeoples(String[] fileContent) {

		Instant start = Instant.now();

		// Cria uma lista de objetos a ser populada
		List<People> listOfPeople = new ArrayList<People>();
		// Popula
		for (int i = 1; i < fileContent.length; i++) {
			// Separa o Array em 3 partes e remove chars indesejaveis
			String[] temp = formatString(fileContent[i]);
			// Cria uma pessoa e Adiciona ela na lista
			listOfPeople.add(new People(temp));
		}

		// Registra o tempo que demorou
		timeRegistry.add(Duration.between(start, Instant.now()).toMillis());

		return listOfPeople;
	}
	
	private String[] formatString(String string) {
		// Separates the Array in 3 parts
		String[] formatedString = string.split(",");
		// Removes unwanted chars
		formatedString[6] = formatedString[6].replace("\"", "");
		return formatedString;
	}


	private <T> void createJsonFile(List<T> objectList) {
		Instant start = Instant.now();
		// Cria o objeto Gson
		Gson gson = new Gson();

		// Lê o conteudo do arquivo e tranforma em uma String no formato JSON
		String JSON = gson.toJson(objectList);

		// Adiciona no ArrayList o Tempo gasto
		Long tempo = timeRegistry.get(timeRegistry.size() - 1);
		tempo += Duration.between(start, Instant.now()).toMillis();
		timeRegistry.set(timeRegistry.size() - 1, tempo);
		
		// Escreve no arquivo o JSON
		// Adiciona .new ao final do arquivo até que não exista nenhum arquivo com o nome igual
		while (Files.exists(Paths.get(filePathJSON))) {
			filePathJSON += ".new";
		}

		try {
			BufferedWriter writer = Files.newBufferedWriter(Paths.get(filePathJSON), StandardOpenOption.CREATE_NEW);
			writer.write(JSON);
			writer.close();
		} catch (Exception e) {}
		
		timeRegistry.add(Duration.between(start, Instant.now()).toMillis());
	}


	private String[] readFile(Path path) {
		try {
			Instant start = Instant.now();

			// Lê o todas as linhas do arquivo
			List<String> fileContent = Files.readAllLines(path, StandardCharsets.UTF_8);

			timeRegistry.add(Duration.between(start, Instant.now()).toMillis());

			// retorna convertido para um Array
			return fileContent.toArray(new String[fileContent.size()]);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	private void escreverTempos(String filePath) {
		try {
			BufferedWriter writer;
			if (Files.exists(Paths.get(filePath))) {
				writer = Files.newBufferedWriter(Paths.get(filePath), StandardOpenOption.APPEND);
			} else {
				writer = Files.newBufferedWriter(Paths.get(filePath), StandardOpenOption.CREATE_NEW);
			}
			String strg = "=================\n";
			for (int i = 0; i < timeRegistry.size(); i++) {
				if (i == 0 | i % 3 == 0) {
					strg += "Leitura:  " + timeRegistry.get(i) + "\n";
				} else if (i % 2 == 0) {
					strg += "Gravação: " + timeRegistry.get(i) + "\n";
				} else if (i % 1 == 0) {
					strg += "Parse:    " + timeRegistry.get(i) + "\n";
				} else {
					strg += "ERROR :: i = " + i + "\n";
				}
			}
			strg += "=================\n";
			strg += "Min_Leitura:  " + performanceTimes[3] + "\n";
			strg += "Med_Leitura:  " + performanceTimes[6] + "\n";
			strg += "Max_Leitura:  " + performanceTimes[0] + "\n";

			strg += "Min_Parse:    " + performanceTimes[4] + "\n";
			strg += "Med_Parse:    " + performanceTimes[7] + "\n";
			strg += "Max_Parse:    " + performanceTimes[1] + "\n";

			strg += "Min_Gravacao: " + performanceTimes[5] + "\n";
			strg += "Med_Gravacao: " + performanceTimes[8] + "\n";
			strg += "Max_Gravacao: " + performanceTimes[2] + "\n";

			writer.write(strg);
			writer.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private Long[] calcularTempos() {
		// #region vars
		Long Max_Leitura = new Long(0);
		Long Max_Parse = new Long(0);
		Long Max_Gravacao = new Long(0);

		Long Min_Leitura = new Long(0);
		Long Min_Parse = new Long(0);
		Long Min_Gravacao = new Long(0);

		Long Med_Leitura = new Long(0);
		Long Med_Parse = new Long(0);
		Long Med_Gravacao = new Long(0);
		// #endregion

		for (int i = 0; i < timeRegistry.size(); i++) {
			if (i == 0 | i % 3 == 0) {
				if (timeRegistry.get(i) > Max_Leitura)
					Max_Leitura = timeRegistry.get(i);
				if (timeRegistry.get(i) < Min_Leitura | Min_Leitura == 0)
					Min_Leitura = timeRegistry.get(i);
				Med_Leitura += timeRegistry.get(i);
				System.out.println("Leitura : " + timeRegistry.get(i) + " milisegundos");
			} else if (i % 2 == 0) {
				if (timeRegistry.get(i) > Max_Gravacao)
					Max_Gravacao = timeRegistry.get(i);
				if (timeRegistry.get(i) < Min_Gravacao | Min_Gravacao == 0)
					Min_Gravacao = timeRegistry.get(i);
				Med_Gravacao += timeRegistry.get(i);
				System.out.println("Gravação: " + timeRegistry.get(i) + " milisegundos");
			} else if (i % 1 == 0) {
				if (timeRegistry.get(i) > Max_Parse)
					Max_Parse = timeRegistry.get(i);
				if (timeRegistry.get(i) < Min_Parse | Min_Parse == 0)
					Min_Parse = timeRegistry.get(i);
				Med_Parse += timeRegistry.get(i);
				System.out.println("Parse   : " + timeRegistry.get(i) + " milisegundos");
			}
		}

		Med_Leitura /= (timeRegistry.size() / 3);
		Med_Parse /= (timeRegistry.size() / 3);
		Med_Gravacao /= (timeRegistry.size() / 3);

		performanceTimes[0] = Max_Leitura;
		performanceTimes[1] = Max_Parse;
		performanceTimes[2] = Max_Gravacao;
		performanceTimes[3] = Min_Leitura;
		performanceTimes[4] = Min_Parse;
		performanceTimes[5] = Min_Gravacao;
		performanceTimes[6] = Med_Leitura;
		performanceTimes[7] = Med_Parse;
		performanceTimes[8] = Med_Gravacao;
		return performanceTimes;
	}

}
