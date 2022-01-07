/**
 * Sample Skeleton for 'MainPage.fxml' Controller Class
 */

package controller;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.BorderPane;

public class MainPageController {

	@FXML
	private BorderPane bp;
	
    @FXML // ResourceBundle that was given to the FXMLLoader
    private ResourceBundle resources;

    @FXML // URL location of the FXML file that was given to the FXMLLoader
    private URL location;

    @FXML // fx:id="algorithm"
    private ToggleGroup algorithm; // Value injected by FXMLLoader

    @FXML // fx:id="clock"
    private RadioButton clock; // Value injected by FXMLLoader

    @FXML // fx:id="secondChanceFIFO"
    private RadioButton secondChanceFIFO; // Value injected by FXMLLoader

    @FXML // fx:id="start"
    private Button start; // Value injected by FXMLLoader

    @FXML // This method is called by the FXMLLoader when initialization is complete
    void initialize() {
        assert algorithm != null : "fx:id=\"algorithm\" was not injected: check your FXML file 'MainPage.fxml'.";
        assert clock != null : "fx:id=\"clock\" was not injected: check your FXML file 'MainPage.fxml'.";
        assert secondChanceFIFO != null : "fx:id=\"secondChanceFIFO\" was not injected: check your FXML file 'MainPage.fxml'.";
        assert start != null : "fx:id=\"start\" was not injected: check your FXML file 'MainPage.fxml'.";

    }
    
    /*
    @FXML
	private void OpenPayments(ActionEvent event) {
		loadPage("Payments");

	}
     */
	private void loadPage(String page) {
			Parent r = null;
			try {
				r = FXMLLoader.load(getClass().getResource("/view/"+page+".fxml"));
			} catch (IOException e) {
				//Logger.getLogger(WelcomeController.class.getName()).log(Level.SEVERE, null, e);
			}
			bp.setCenter(r);


	}
}
