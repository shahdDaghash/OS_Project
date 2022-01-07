package controller;

import java.net.URL;
import java.util.ResourceBundle;
import java.io.*;

import application.Main;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import model.Simulator;

public class WelcomePageController implements Initializable{
	
	
	@FXML
    private Label File;
	
	@FXML
    private Label error;

    @FXML
    private Button choose_file_btn;

    @FXML
    private RadioButton choose_file_radio;

    @FXML
    private TextField choose_path_txt;

    @FXML
    private Button dir_btn;

    @FXML
    private TextField dir_txt;

    @FXML
    private Label file_result;

    @FXML
    private TextField filename_txt;

    @FXML
    private RadioButton generate_radio;

    @FXML
    private Button start_simulation_btn;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
    	
    }
    
    
    public void file_chooser(ActionEvent event) {
    	FileChooser fc = new FileChooser();
    	fc.setInitialDirectory(new File("C:\\"));
    	fc.getExtensionFilters().add(new ExtensionFilter("Text Files", "*.txt"));
    	File f = fc.showOpenDialog(null);
    	
    	if(f != null) {
    		choose_path_txt.setText(f.getAbsolutePath());
    		file_result.setText(f.getAbsolutePath());
    	}
    	
    }
    
    public void directory_chooser(ActionEvent event) {
    	DirectoryChooser dc = new DirectoryChooser();
    	dc.setInitialDirectory(new File("C:\\"));
    	dc.setTitle("Choose Directory");
       	File f = dc.showDialog(null);
    	
    	if(f != null) {
    		dir_txt.setText(f.getAbsolutePath());
    		file_result.setText(f.getAbsolutePath());
    	}
    	
    }
    
    
    public void startSimulateSelect(ActionEvent event) throws IOException {
    	if(noErrors()) {
    		Simulator s = new Simulator();
    		
    		Main m = new Main();
    		boolean cont = s.readFile(choose_path_txt.getText());
    		if(cont)
    			m.changeScene("/view/MainPage.fxml");
    	}
    }
    
    
    public boolean noErrors() {
    	if(!choose_file_radio.isSelected() && !generate_radio.isSelected()) {
    		error.setText("Please Select a method!");
    		return false;
    	}
    	if(choose_file_radio.isSelected()) {
    		if(choose_path_txt.getText() == null || choose_path_txt.getText() == "") {
    			error.setText("You have to specify a file path");
    			return false;
    		}
    		
    		File file = new File(choose_path_txt.getText());
    		if(!file.exists()) {
    			error.setText("File does not exist!");
    			return false;
    		}
    	}
    	else if(generate_radio.isSelected()) {
    		if(dir_txt.getText() == null || dir_txt.getText() == "" || filename_txt.getText() == null || filename_txt.getText() == "") {
    			error.setText("You have to specify file name and/or directory");
    			return false;
    		}
    		
    		if(dir_txt.getText() != null) {
    			File file = new File(dir_txt.getText());
    			if(!file.exists()) {
    				error.setText("Directory does not exist");
    				return false;
    			}
    		}
    		
    		if(filename_txt.getText() != null) {
    			File file = new File(dir_txt.getText() +"\\" + filename_txt.getText() + ".txt");
    			if(file.exists()) {
    				error.setText("File exists in directory! choose another name!");
    				return false;
    			}
    		}
    		
    	}
    	
    	return true;
    }
    
}






