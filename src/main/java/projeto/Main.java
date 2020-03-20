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
		//String filePathCSV = "resources//brasil.csv";
		
		String filePathJSON = "resources//File.json";
		Long[] tempos = new Long[3];

		inputVerification(args, filePathJSON, tempos);
	}

	private static void inputVerification(String[] args, String filePathJSON, Long[] tempos) {
		String filePathCSV;
		// Input do diretório
		Scanner Input = new Scanner(System.in);
		System.out.println("Escreva o diretório do arquivo: \n (Exemplo: resources/brasil.csv)");
		filePathCSV = Input.next();
		filePathCSV.replace("/", "//");

		// Verifica se o diretório existe
		if (!Files.exists(Paths.get(filePathCSV))) {
			System.out.println("\nERRO!!!\nO arquivo não existe no diretório selecionado!");
			main(args);
		} else {
			// Cria lista de pessoas
			List<People> peoples = createPeopleList(filePathCSV, tempos);
			System.out.println("Lista de pessoas foi criada!");

			// Cria o arquivo JSON
			filePathJSON = createJsonFile(filePathJSON, peoples, Input, tempos);

			System.out.println("O arquivo foi convertido!");
			System.out.println("Leitura : " + tempos[0] + " milisegundos");
			System.out.println("Parse   : " + tempos[1] + " milisegundos");
			System.out.println("Gravação: " + tempos[2] + " milisegundos");
			System.out.println("O arquivo se encontra no diretório: " + Paths.get(filePathJSON));

			// Registra os tempos em um arquivo
			writeTimes(tempos, "resources//Times.txt");
		}
	}

	private static List<String> readFile(String filePath, Long[] tempos) {
		try {
			Instant start = Instant.now();
			Path path = Paths.get(filePath);
			List<String> fileContent = Files.readAllLines(path, StandardCharsets.UTF_8);
			tempos[0] = Duration.between(start, Instant.now()).toMillis();
			System.out.println("O arquivo foi lido!");
			return fileContent;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	private static List<People> createPeopleList(String filePathCSV, Long[] tempos) {

		Instant start = Instant.now();
		// Get Content of the file
		List<String> listFileContent = readFile(filePathCSV, tempos);

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

		tempos[1] = Duration.between(start, Instant.now()).toMillis();
		return listOfPeoples;
	}

	private static String[] removeUnwantedChars(String notFormated) {
		// Separates the Array in 3 parts
		String[] formated = notFormated.split(",");
		// Removes unwanted chars
		formated[6] = formated[6].replace("\"", "");
		return formated;
	}

	private static <T> String createJsonFile(String filePathJSON, List<T> fileContent, Scanner Input, Long[] tempos) {
		try {
			Instant start = Instant.now();
			// Creates Gson object
			Gson gson = new Gson();
			// Reads and Writes to JSON from FileContent
			String Json = gson.toJson(fileContent);
			// Write to File with BufferedWriter
			filePathJSON = writeJSON(filePathJSON, Input, Json, tempos);
			tempos[2] = Duration.between(start, Instant.now()).toMillis();

			// Output of Json
			// System.out.println(Json);

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return filePathJSON;
	}

	private static String writeJSON(String filePathJSON, Scanner Input, String Json, Long[] tempos) throws IOException {
		while (Files.exists(Paths.get(filePathJSON))) {
			filePathJSON += ".new";
		}
		System.out.println("O arquivo será criado em: " + filePathJSON);
		BufferedWriter writer = Files.newBufferedWriter(Paths.get(filePathJSON), StandardOpenOption.CREATE_NEW);
		writer.write(Json);
		writer.close();
		return filePathJSON;
	}

	private static void writeTimes(Long[] tempos, String filePath) {
		try {
			BufferedWriter writer;
			if (Files.exists(Paths.get(filePath))) {
				writer = Files.newBufferedWriter(Paths.get(filePath), StandardOpenOption.APPEND);
			} else {
				writer = Files.newBufferedWriter(Paths.get(filePath), StandardOpenOption.CREATE_NEW);
			}
			String strg = "=================\n";
			for (int i = 0; i < tempos.length; i++) {
				switch (i) {
				case 0:
					strg += "Leitura: " + tempos[i] + "\n";
					break;
				case 1:
					strg += "Parse: " + tempos[i] + "\n";
					break;
				case 2:
					strg += "Gravação: " + tempos[i] + "\n";
					break;

				default:
					strg += "ERROR\n";
					break;
				}
			}
			writer.write(strg);
			writer.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
