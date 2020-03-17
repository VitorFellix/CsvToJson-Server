package projeto;

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

import com.google.gson.Gson;

public class Main {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String filePathCSV = "resources//brasil.csv";
		String filePathJSON = "resources//File.json";

		Scanner Input = new Scanner(System.in);
		System.out.println("Escreva o diretório do arquivo: \n (Utilize duas barras ex: //)");
		filePathCSV = Input.next();
		// createJsonFile(filePathJSON, createLancamentoList(filePathCSV));

		// Le o arquivo CSV
		// List<String> fileContent = readFile(filePathCSV);
		// for (String string : fileContent) {System.out.println(string);}

		// Cria lista de pessoas
		List<People> peoples = createPeopleList(filePathCSV);
		System.out.println("Lista de pessoas foi criada!");

		// Cria o arquivo JSON
		filePathJSON = createJsonFile(filePathJSON, peoples, Input);
		System.out.println("O arquivo foi convertido!");
		System.out.println("O arquivo se encontra no diretório: " + Paths.get(filePathJSON));
	}

	private static List<String> readFile(String filePath) {
		try {
			Instant start = Instant.now();
			Path path = Paths.get(filePath);
			List<String> fileContent = Files.readAllLines(path, StandardCharsets.UTF_8);
			System.out.println(
					"Tempo para ler o Arquivo: " + Duration.between(start, Instant.now()).toMillis() + " milisegundos");
			System.out.println("O arquivo foi lido!");
			return fileContent;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	private static <T> String createJsonFile(String filePathJSON, List<T> fileContent, Scanner Input) {
		try {
			Instant start = Instant.now();
			// Creates Gson object
			Gson gson = new Gson();
			// Reads and Writes to JSON from FileContent
			String Json = gson.toJson(fileContent);
			// Write to File with BufferedWriter
			filePathJSON = writeJSON(filePathJSON, Input, Json);
			System.out.println("Tempo para escrever o Arquivo Json: "
					+ Duration.between(start, Instant.now()).toMillis() + " milisegundos");

			// Output of Json
			// System.out.println(Json);

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return filePathJSON;
	}

	private static String writeJSON(String filePathJSON, Scanner Input, String Json) throws IOException {
		while(Files.exists(Paths.get(filePathJSON))) {
			filePathJSON += ".new";
			/*
			 * System.out.
			 * println("O arquivo já existe neste diretório, quer sobre escrever? (y/n)");
			 * String next = Input.next(); if(next == "y" || next == "Y" ) { writer =
			 * Files.newBufferedWriter(Paths.get(filePathJSON),StandardOpenOption.WRITE); }
			 * else if(next == "n" || next == "N" ){
			 * System.out.println("O arquivo terá outro nome: " + filePathJSON + ".new");
			 * writer =
			 * Files.newBufferedWriter(Paths.get(filePathJSON+".new"),StandardOpenOption.
			 * CREATE_NEW); }else { System.out.println("Não foi possível entender: " + next
			 * + "\nO arquivo terá outro nome: " + filePathJSON + ".new"); writer =
			 * Files.newBufferedWriter(Paths.get(filePathJSON+".new"),StandardOpenOption.
			 * CREATE_NEW); }
			 */
		}
		System.out.println("O arquivo será criado em: " + filePathJSON);
		BufferedWriter writer = Files.newBufferedWriter(Paths.get(filePathJSON), StandardOpenOption.CREATE_NEW);
		writer.write(Json);
		writer.close();
		return filePathJSON;
	}

	private static List<People> createPeopleList(String filePathCSV) {

		Instant start = Instant.now();
		// Get Content of the file
		List<String> listFileContent = readFile(filePathCSV);

		// Puts it in a Array
		String[] arrayFileContent = listFileContent.toArray(new String[listFileContent.size()]);
		// Create the list of Objects
		List<People> listOfPeoples = new ArrayList<People>();
		;
		// For every string in the array
		for (int i = 1; i < arrayFileContent.length; i++) {
			// Separates the Array in 3 parts
			String[] tempString = removeUnwantedChars(arrayFileContent[i]);
			// Creates the object
			People people = new People(tempString);
			listOfPeoples.add(people);

			// Prints it
			// System.out.println(lancamento.toString());
		}
		System.out.println("Tempo para criar a lista de Pessoas a partir do CSV: "
				+ Duration.between(start, Instant.now()).toMillis() + " milisegundos");
		return listOfPeoples;
	}

	private static String[] removeUnwantedChars(String notFormated) {
		// Separates the Array in 3 parts
		String[] formated = notFormated.split(",");
		// Removes unwanted chars
		formated[6] = formated[6].replace("\"", "");
		return formated;
	}

}
