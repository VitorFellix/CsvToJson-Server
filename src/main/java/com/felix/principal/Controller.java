package com.felix.principal;

import java.io.File;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ResourceBundle;

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
	private ProgressBar ProgressBar1;
	@FXML
	private TextArea TextArea1;

	private File fileCSV;
	private File fileJson;
	
	boolean csv = false;
	boolean json = false;
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stub
	}

	@FXML
	public void ProcurarCsv(ActionEvent event) {
		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("Selecione o arquivo CSV para conversão");

		// seta um filtro para arquivos csv
		FileChooser.ExtensionFilter extFilter = 
                new FileChooser.ExtensionFilter("CSV files (*.csv)", "*.csv");
        fileChooser.getExtensionFilters().add(extFilter);

		// seta o diretório inicial para o desktop
		File defaultDirectory = new File(System.getProperty("user.home") + File.separator + "Desktop");
		if(defaultDirectory != null)
			fileChooser.setInitialDirectory(defaultDirectory);
        
		//Abre a janela de seleção
		fileCSV = fileChooser.showOpenDialog((Stage) ((Node) event.getSource()).getScene().getWindow());

		//Verifica se um arquivo foi selecionado
		if (fileCSV != null) {
			TextField_Csv.appendText(fileCSV.getAbsolutePath().toString());
			Button_Csv.setDisable(true);
			csv = true;
			System.out.println(fileCSV.getAbsolutePath() + " :: File Csv selected");
		}else {
			Button_Csv.setDisable(false);
			csv = false;
		}
		if(csv && json) 
			converter();
	}

	@FXML
	public void ProcurarJson(ActionEvent event) {
		DirectoryChooser dirChooser = new DirectoryChooser();
		dirChooser.setTitle("Selecione uma pasta onde o File.Json e o times.txt será salvo");
		
		// seta o diretório inicial para o desktop
		File defaultDirectory = new File(System.getProperty("user.home") + File.separator + "Desktop");
		if(defaultDirectory != null)
			dirChooser.setInitialDirectory(defaultDirectory);

		//Abre a janela de seleção
        fileJson = dirChooser.showDialog((Stage) ((Node) event.getSource()).getScene().getWindow());

		//Verifica se um arquivo foi selecionado
		if (fileJson != null) {
			TextField_Json.appendText(fileJson.getAbsolutePath().toString());
			Button_Json.setDisable(true);
			json = true;
			System.out.println(fileJson.getAbsolutePath() + " :: Folder selected");
		}else {
			Button_Json.setDisable(false);
			json = false;
		}
		if(csv && json) 
			converter();
	}

	@FXML
	public void converter() {
		task.messageProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> obs, String oldMessage, String newMessage) {
				TextArea1.appendText(task.getMessage());
			}
		});
	
		ProgressBar1.progressProperty().bind(task.progressProperty());
		new Thread(task).start();
			
		System.out.println("Convertendo Arquivos...");
			
		Converter converter = new Converter();
		converter.convert(fileCSV, fileJson);
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
