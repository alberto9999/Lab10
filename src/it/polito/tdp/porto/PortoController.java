package it.polito.tdp.porto;

import java.net.URL;
import java.util.ResourceBundle;

import it.polito.tdp.porto.model.Author;
import it.polito.tdp.porto.model.Model;
import it.polito.tdp.porto.model.Paper;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;

public class PortoController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private ComboBox<Author> boxPrimo;

    @FXML
    private ComboBox<Author> boxSecondo;

    @FXML
    private TextArea txtResult;

	private Model model;

    @FXML
    void handleCoautori(ActionEvent event) {
    	try{
    		txtResult.setText("");
    		Author a= boxPrimo.getValue();
    		for(Author aut :model.getCoauthors(a)){
    		txtResult.appendText(aut+"\n");
    		}
    		boxSecondo.getItems().clear();
    		boxSecondo.getItems().addAll(model.getNonCoauthors());
    		
    	}
    	catch (RuntimeException re) {
    		txtResult.appendText("FORMATO INPUT NON VALIDO ");
    	}

    }

    @FXML
    void handleSequenza(ActionEvent event) {
    	try{
    		txtResult.setText("");
    		Author a= boxPrimo.getValue();
    		Author b= boxSecondo.getValue();
    		for(Paper p : model.getSequenza(a,b)){
    		txtResult.appendText(p+"\n");
    	}
    	}
    	catch (RuntimeException re) {
    		txtResult.appendText("FORMATO INPUT NON VALIDO ");
    	}

    }

    @FXML
    void initialize() {
        assert boxPrimo != null : "fx:id=\"boxPrimo\" was not injected: check your FXML file 'Porto.fxml'.";
        assert boxSecondo != null : "fx:id=\"boxSecondo\" was not injected: check your FXML file 'Porto.fxml'.";
        assert txtResult != null : "fx:id=\"txtResult\" was not injected: check your FXML file 'Porto.fxml'.";

    }

	public void setModel(Model model) {
	this.model=model;
	boxPrimo.getItems().addAll(model.getAuthors());	
	}
}
