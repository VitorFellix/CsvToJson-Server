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

		// #region Variaveis
		Scanner Input = new Scanner(System.in);
		String filePathJSON = "resources//File.json";
		ArrayList<Long> tempos = new ArrayList<Long>();
		Long[] Calculedtempos = new Long[9];
		// #endregion

		// Começa o programa
		Run(args, filePathJSON, tempos, Input, Calculedtempos);
		// Informa a saída do programa
		System.out.println("Closing...");
	}

	private static void Run(String[] args, String filePathJSON, ArrayList<Long> tempos, Scanner Input,
			Long[] Calculedtempos) {
		// Dá a opção para sair ou continuar
		System.out.println("Write 'q' to exit or write 'y' to convert an file.");
		String InputKey = Input.nextLine();
		// Verifica o Input recebido
		if (InputKey.equals("q")) {
			// Finaliza o Programa
			return;
		} else if (InputKey.equals("y")) {
			// Continua a converter
			// Input do diretório
			String filePathCSV;
			System.out.println("Escreva o diretório do arquivo: \n (Exemplo: resources/brasil.csv)");
			filePathCSV = Input.next();
			filePathCSV.replace("/", "//");

			// Verifica se o diretório existe
			// Se não existe
			if (!Files.exists(Paths.get(filePathCSV))) {
				System.out.println("\nERRO!!!\nO arquivo não existe no diretório selecionado!");
				Run(args, filePathJSON, tempos, Input, Calculedtempos);
			} else {
				// Cria lista de pessoas
				List<People> peoples = criaListaPessoas(filePathCSV, tempos);
				System.out.println("Lista de pessoas foi criada!");

				// Cria o arquivo JSON
				filePathJSON = criaArquivoJSON(filePathJSON, peoples, Input, tempos);

				// Output
				System.out.println("O arquivo foi convertido!");
				System.out.println("E se encontra no diretório: " + Paths.get(filePathJSON));

				//Calcula os tempos med, min e max
				Calculedtempos = calcularTempos(tempos, Calculedtempos);

				// Registra os tempos em um arquivo
				escreverTempos(tempos, Calculedtempos, "resources//Times.txt");
				Run(args, filePathJSON, tempos, Input, Calculedtempos);
			}
		} else {
			System.out.println("Please write 'q' or 'y'");
			Run(args, filePathJSON, tempos, Input, Calculedtempos);
		}
	}

	private static List<People> criaListaPessoas(String filePathCSV, ArrayList<Long> tempos) {

		Instant start = Instant.now();
		// Lê o conteúdo do arquivo
		List<String> listaConteudo = lerArquivo(filePathCSV, tempos);
		// Converte para um Array
		String[] arrayConteudo = listaConteudo.toArray(new String[listaConteudo.size()]);
		// Cria uma lista de objetos
		List<People> listaPessoas = new ArrayList<People>();
		
		// For
		for (int i = 1; i < arrayConteudo.length; i++) {
			// Separa o Array em 3 partes e remove chars indesejaveis
			String[] tempString = removeChars(arrayConteudo[i]);
			// Cria um pessoa
			People people = new People(tempString);
			// Adiciona ela na lista
			listaPessoas.add(people);
		}
		// Registra o tempo que demorou
		tempos.add(Duration.between(start, Instant.now()).toMillis());
		return listaPessoas;
	}

	private static <T> String criaArquivoJSON(String filePathJSON, List<T> conteudoFile, Scanner input,
			ArrayList<Long> tempos) {
		try {
			Instant start = Instant.now();
			// Cria o objeto Gson
			Gson gson = new Gson();
			// Lê o conteudo do arquivo e tranforma em uma String no formato JSON
			String JSON = gson.toJson(conteudoFile);
			// Adiciona no ArrayList o Tempo gasto
			Long tempo = tempos.get(tempos.size() - 1);
			tempo += Duration.between(start, Instant.now()).toMillis();
			tempos.set(tempos.size() - 1, tempo);
			// Escreve no arquivo o JSON
			filePathJSON = escreverJSON(filePathJSON, input, JSON, tempos);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return filePathJSON;
	}

	private static String escreverJSON(String filePathJSON, Scanner input, String JSON, ArrayList<Long> tempos)
			throws IOException {
		Instant start = Instant.now();
				// Adiciona .new ao final do arquivo até que não exista nenhum arquivo com o nome igual
		while (Files.exists(Paths.get(filePathJSON))) {
			filePathJSON += ".new";
		}
		// Output do diretório em que será criado o arquivo
		System.out.println("O arquivo será criado em: " + filePathJSON);
		//Escreve o Arquivo
		BufferedWriter writer = Files.newBufferedWriter(Paths.get(filePathJSON), StandardOpenOption.CREATE_NEW);
		writer.write(JSON);
		writer.close();
		tempos.add(Duration.between(start, Instant.now()).toMillis());
		return filePathJSON;
	}
	
	private static List<String> lerArquivo(String filePath, ArrayList<Long> tempos) {
		try {
			Instant start = Instant.now();
			Path path = Paths.get(filePath);
			List<String> fileContent = Files.readAllLines(path, StandardCharsets.UTF_8);
			tempos.add(Duration.between(start, Instant.now()).toMillis());
			System.out.println("O arquivo foi lido!");
			return fileContent;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	private static String[] removeChars(String naoFormatado) {
		// Separates the Array in 3 parts
		String[] formatado = naoFormatado.split(",");
		// Removes unwanted chars
		formatado[6] = formatado[6].replace("\"", "");
		return formatado;
	}

	private static void escreverTempos(ArrayList<Long> tempos, Long[] temposCalculados, String filePath) {
		try {
			BufferedWriter writer;
			if (Files.exists(Paths.get(filePath))) {
				writer = Files.newBufferedWriter(Paths.get(filePath), StandardOpenOption.APPEND);
			} else {
				writer = Files.newBufferedWriter(Paths.get(filePath), StandardOpenOption.CREATE_NEW);
			}
			String strg = "=================\n";
			for (int i = 0; i < tempos.size(); i++) {
				if (i == 0 | i % 3 == 0) {
					strg += "Leitura:  " + tempos.get(i) + "\n";
				} else if (i % 2 == 0) {
					strg += "Gravação: " + tempos.get(i) + "\n";
				} else if (i % 1 == 0) {
					strg += "Parse:    " + tempos.get(i) + "\n";
				} else {
					strg += "ERROR :: i = " + i + "\n";
				}
			}
			strg += "=================\n";
			strg += "Min_Leitura:  " + temposCalculados[3] + "\n";
			strg += "Med_Leitura:  " + temposCalculados[6] + "\n";
			strg += "Max_Leitura:  " + temposCalculados[0] + "\n";

			strg += "Min_Parse:    " + temposCalculados[4] + "\n";
			strg += "Med_Parse:    " + temposCalculados[7] + "\n";
			strg += "Max_Parse:    " + temposCalculados[1] + "\n";

			strg += "Min_Gravacao: " + temposCalculados[5] + "\n";
			strg += "Med_Gravacao: " + temposCalculados[8] + "\n";
			strg += "Max_Gravacao: " + temposCalculados[2] + "\n";

			writer.write(strg);
			writer.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private static Long[] calcularTempos(ArrayList<Long> tempos, Long[] temposCalculados) {
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

		for (int i = 0; i < tempos.size(); i++) {
			if (i == 0 | i % 3 == 0) {
				if (tempos.get(i) > Max_Leitura)
					Max_Leitura = tempos.get(i);
				if (tempos.get(i) < Min_Leitura | Min_Leitura == 0)
					Min_Leitura = tempos.get(i);
				Med_Leitura += tempos.get(i);
				System.out.println("Leitura : " + tempos.get(i) + " milisegundos");
			} else if (i % 2 == 0) {
				if (tempos.get(i) > Max_Gravacao)
					Max_Gravacao = tempos.get(i);
				if (tempos.get(i) < Min_Gravacao | Min_Gravacao == 0)
					Min_Gravacao = tempos.get(i);
				Med_Gravacao += tempos.get(i);
				System.out.println("Gravação: " + tempos.get(i) + " milisegundos");
			} else if (i % 1 == 0) {
				if (tempos.get(i) > Max_Parse)
					Max_Parse = tempos.get(i);
				if (tempos.get(i) < Min_Parse | Min_Parse == 0)
					Min_Parse = tempos.get(i);
				Med_Parse += tempos.get(i);
				System.out.println("Parse   : " + tempos.get(i) + " milisegundos");
			}
		}

		Med_Leitura /= (tempos.size() / 3);
		Med_Parse /= (tempos.size() / 3);
		Med_Gravacao /= (tempos.size() / 3);

		temposCalculados[0] = Max_Leitura;
		temposCalculados[1] = Max_Parse;
		temposCalculados[2] = Max_Gravacao;
		temposCalculados[3] = Min_Leitura;
		temposCalculados[4] = Min_Parse;
		temposCalculados[5] = Min_Gravacao;
		temposCalculados[6] = Med_Leitura;
		temposCalculados[7] = Med_Parse;
		temposCalculados[8] = Med_Gravacao;
		return temposCalculados;
	}


}
