package com.felix.principal;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.ResourceBundle;

import com.felix.conversor.Converter;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
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
	private TextArea TextArea1;

	private File fileCSV;
	private File fileJson;

	public static boolean RodandoIniciar = false;
	
	boolean csv = false;
	boolean json = false;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stub
	}

	@FXML
	public void ProcurarCsv(ActionEvent event) throws UnknownHostException, IOException {
		try {
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
				TextArea1.appendText("Arquivo Csv foi Selecionado\n");
				System.out.println(fileCSV.getAbsolutePath() + " :: File Csv selected");
			} else {
				Button_Csv.setDisable(false);
				csv = false;
			}
			if (csv && json)
				ClientSide();
		} catch (Exception e) {
			e.printStackTrace();
			TextArea1.appendText("Arquivo Csv não foi Selecionada, por favor selecione novamente\nERRO!");
			Button_Csv.setDisable(false);
			csv = false;
		}
	}

	@FXML
	public void ProcurarJson(ActionEvent event){
		try {
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
				TextArea1.appendText("Pasta foi Selecionada\n");
				System.out.println(fileJson.getAbsolutePath() + " :: Folder selected");
			} else {
				Button_Json.setDisable(false);
				json = false;
			}
			if (csv && json)
				ClientSide();
		} catch (Exception e) {
			e.printStackTrace();
			TextArea1.appendText("Pasta não foi Selecionada, por favor selecione novamente\nERRO!");
			Button_Json.setDisable(false);
			json = false;
		}
	}

	private void ClientSide(){
		// Outputs
		TextArea1.clear();
		TextArea1.appendText("Começando converção");
		
		try {
			// Cria um Objeto Converter
			// Envia os locais para ler o csv e para salvar o json
			Converter converter = new Converter();
			converter.convert(fileCSV, fileJson);

			// Reseta a interface e as flags
			Button_Csv.setDisable(false);
			Button_Json.setDisable(false);
			json = false;
			csv = false;
			
		} catch (IOException e) {
			e.printStackTrace();
		}

		// Outputs
		TextArea1.appendText("Converção Terminou");
	}
}
