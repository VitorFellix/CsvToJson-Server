package com.felix.principal;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;
import java.net.URL;
import java.net.UnknownHostException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ResourceBundle;
import java.util.Scanner;

import com.felix.conversor.Converter;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;

public class Controller implements Initializable {

	double perc = 0f;
	@FXML
	private Button Button_Csv;
	@FXML
	private TextField TextField_Csv;
	@FXML
	private Button Button_Json;
	@FXML
	private TextField TextField_Json;
	@FXML
	private ProgressBar ProgressBarLer;
	@FXML
	private ProgressBar ProgressBarConverter;
	@FXML
	private ProgressBar ProgressBarSalvar;
	@FXML
	private TextArea TextArea1;

	private File fileCSV;
	private File fileJson;

	public static boolean RodandoIniciar = false;
	
	
	boolean csv = false;
	boolean json = false;

	String SocketIP = "127.0.0.1";
	int Port = 12341;
	String serverRequests;
	String csvArqSelecionado = "Arquivo Csv foi selecionado";
	String pastaSelecionada = "Pasta foi selecionada";
	String conectando = "Conectando ao servidor";
	String enviando = "Enviando paths...";
	String convertendo = "Convertendo Arquivos...";
	String esperando = "Esperando por resultados do servidor...";

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stub
	}

	@FXML
	public void ProcurarCsv(ActionEvent event) throws UnknownHostException, IOException {
		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("Selecione o arquivo CSV para conversão");

		// seta um filtro para arquivos csv
		FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("CSV files (*.csv)", "*.csv");
		fileChooser.getExtensionFilters().add(extFilter);

		// seta o diretório inicial para o desktop
		File defaultDirectory = new File(System.getProperty("user.home") + File.separator + "Desktop");
		if (defaultDirectory != null)
			fileChooser.setInitialDirectory(defaultDirectory);

		// Abre a janela de seleção
		fileCSV = fileChooser.showOpenDialog((Stage) ((Node) event.getSource()).getScene().getWindow());

		// Verifica se um arquivo foi selecionado
		if (fileCSV != null) {
			TextField_Csv.appendText(fileCSV.getAbsolutePath().toString());
			Button_Csv.setDisable(true);
			csv = true;

			// Outputs
			TextArea1.appendText("\n" + csvArqSelecionado);
			System.out.println(fileCSV.getAbsolutePath() + " :: File Csv selected");
		} else {
			Button_Csv.setDisable(false);
			csv = false;
		}
		if (csv && json)
			ClientSide();
	}

	@FXML
	public void ProcurarJson(ActionEvent event) throws UnknownHostException, IOException {
		DirectoryChooser dirChooser = new DirectoryChooser();
		dirChooser.setTitle("Selecione uma pasta onde o File.Json e o times.txt será salvo");

		// seta o diretório inicial para o desktop
		File defaultDirectory = new File(System.getProperty("user.home") + File.separator + "Desktop");
		if (defaultDirectory != null)
			dirChooser.setInitialDirectory(defaultDirectory);

		// Abre a janela de seleção
		fileJson = dirChooser.showDialog((Stage) ((Node) event.getSource()).getScene().getWindow());

		// Verifica se um arquivo foi selecionado
		if (fileJson != null) {
			TextField_Json.appendText(fileJson.getAbsolutePath().toString());
			Button_Json.setDisable(true);
			json = true;

			// Outputs
			TextArea1.appendText("\n" + pastaSelecionada);
			System.out.println(fileJson.getAbsolutePath() + " :: Folder selected");
		} else {
			Button_Json.setDisable(false);
			json = false;
		}
		if (csv && json)
			ClientSide();
	}

	private void ClientSide(){
		System.out.println("Cliente");
		// Outputs
		TextArea1.clear();
		TextArea1.appendText("\n" + conectando);
		System.out.println(conectando + " :: IP = " + SocketIP + " :: PORT = " + Port);

		// Solicita uma conexÃ£o
		Socket cliente = null;
		try {
			cliente = new Socket(SocketIP, Port);
		} catch (java.net.ConnectException e) {
			e.printStackTrace();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		// Cria um canal de envio e recebimento
		PrintStream OutClient = null;
		Scanner InServer = null;
		try {
			OutClient = new PrintStream(cliente.getOutputStream());
			InServer = new Scanner(cliente.getInputStream());
		} catch (IOException e) {
			e.printStackTrace();
		}

		// Recebe do Servidor informações
		serverRequests = InServer.nextLine();
		TextArea1.appendText("\n" + serverRequests);
		System.out.println(serverRequests);
		serverRequests = InServer.nextLine();
		TextArea1.appendText("\n" + serverRequests);
		System.out.println(serverRequests);
		// Recebe do Servidor informações

		// Outputs
		TextArea1.appendText("\n" + enviando);
		System.out.println(enviando);

		// Envia os dados
		OutClient.println(fileCSV.getAbsolutePath());
		OutClient.println(fileJson.getAbsolutePath());

		// Outputs
		TextArea1.appendText("\n" + convertendo);
		TextArea1.appendText("\n" + esperando);
		System.out.println(convertendo);
		System.out.println(esperando);
		
		serverRequests = InServer.nextLine();
		if(serverRequests.contentEquals("Sucesso")) {
			try {
				TextArea1.appendText("\n" + serverRequests);
				System.out.println(serverRequests);
				Button_Csv.setDisable(false);
				Button_Json.setDisable(false);
				json = false;
				csv = false;
				cliente.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}else {
			try {
				TextArea1.appendText("\nNot Sucesso");
				System.out.println("Not Sucesso");
				cliente.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	@FXML
	public void converter() {
		task.messageProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> obs, String oldMessage, String newMessage) {
				TextArea1.appendText(task.getMessage());
			}
		});
		//ProgressBarLer.progressProperty().bind(task.progressProperty());
		//new Thread(task).start();

	}

	Task task = new Task<Void>() {
		@Override
		public Void call() {
			final int max = 100000000;
			int centena = 0;
			for (int i = 1; i <= max; i++) {
				if (isCancelled()) {
					break;
				}
				updateProgress(i, max);

				if (i % 100 == 0) {
					updateMessage("Processados: " + ++centena);
				}
			}
			return null;
		}

		@Override
		protected void succeeded() {
			super.succeeded();
			updateMessage("Done!");
		}

		@Override
		protected void cancelled() {
			super.cancelled();
			updateMessage("Cancelled!");
		}

		@Override
		protected void failed() {
			super.failed();
			updateMessage("Failed!");
		}
	};
}
