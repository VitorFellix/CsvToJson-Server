package com.felix.principal;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextField;

public class Controller implements Initializable {
	
	@FXML
	private Button ProcurarButton;
	@FXML
	private TextField CaminhoTextField;
	@FXML
	private ProgressBar ProgressBar1;
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stub
	}
	
	public void AcaoButton() {
		String chars = CaminhoTextField.getText();
		System.out.println(chars);
	}

}
