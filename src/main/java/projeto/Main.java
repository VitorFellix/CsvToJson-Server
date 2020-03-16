package projeto;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.OpenOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;

public class Main {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String filePathCSV = "resources//File.csv";
		String filePathJSON = "resources//File.json";
		createJsonFile(filePathJSON, createLancamentoList(filePathCSV));
		
		//Le o arquivo CSV
		//List<String> fileContent = readFile(filePathCSV);
		
		//Cria lista de lancamentos
		//List<Lancamento> lancamentos = createLancamentoList(filePath);
		
		//Exemplo para rever quando necessário
		//for (String string : fileContent) {System.out.println(string);}
	}
	
	private static List<String> readFile(String filePath) {
		try {
			Path path = Paths.get(filePath);
			return Files.readAllLines(path, StandardCharsets.UTF_8);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	private static <T> void createJsonFile(String filePathJSON, List<T> fileContent) {
		try {
			//Creates Gson object
			Gson gson = new Gson();
			//Reads and Writes to JSON from FileContent
			String Json = gson.toJson(fileContent);
			//Write to File with BufferedWriter
			BufferedWriter writer = Files.newBufferedWriter(Paths.get(filePathJSON),StandardOpenOption.WRITE);
			writer.write(Json);
			writer.close();
			
			//Output of Json
			//System.out.println(Json);
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private static List<Lancamento> createLancamentoList(String filePathCSV) {
		//Get Content of the file
		List<String> listFileContent = readFile(filePathCSV);
		//Puts it in a Array
		String[] arrayFileContent = listFileContent.toArray(new String[listFileContent.size()]);
		//Create the list of Objects
		List<Lancamento> lancamentos =  new ArrayList<Lancamento>();;
		//For every string in the array
		for (int i = 0; i < arrayFileContent.length; i++) {
			//Separates the Array in 3 parts
			String[] tempString = removeUnwantedChars(arrayFileContent[i]);
			//Creates the object
			Lancamento lancamento = new Lancamento(tempString[0], tempString[1], Float.parseFloat(tempString[2]));
			lancamentos.add(lancamento);
			//Prints it
			//System.out.println(lancamento.toString());
		}
		return lancamentos;
	}

	private static String[] removeUnwantedChars(String notFormated) {
		//Separates the Array in 3 parts
		String[] formated = notFormated.split(";");
		//Removes unwanted chars
		formated[2] = formated[2].replace("R$", "");
		formated[2] = formated[2].replace(",", ".");
		return formated;
	}

}
