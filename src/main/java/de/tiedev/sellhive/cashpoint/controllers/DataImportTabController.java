package de.tiedev.sellhive.cashpoint.controllers;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import de.tiedev.sellhive.cashpoint.model.Game;
import de.tiedev.sellhive.cashpoint.model.Seller;
import de.tiedev.sellhive.cashpoint.services.DataImportService;
import de.tiedev.sellhive.cashpoint.services.TestDataService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextArea;

@Component
public class DataImportTabController {

	
	@Autowired
	DataImportService dataImportService;
	
	@Autowired
	TestDataService testDataService;
	
	@FXML
	TextArea dataImportTxtArea;   
	
	public void initialize() {
		
	}
	
	
	@FXML
	public void dataImportBtnOnAction(ActionEvent event) {
		Alert alert = new Alert(AlertType.CONFIRMATION, "Beim Importieren werden alle Daten in der Anwendung gelöscht. Sollen neue Daten importiert werden?", ButtonType.YES, ButtonType.CANCEL);
		alert.showAndWait();

		if (alert.getResult() == ButtonType.YES) {
			dataImportService.cleanUpDatabase();
			Map<Long, Seller> sellers = dataImportService.importSeller();
			dataImportTxtArea.appendText(sellers.size() + " Verkäufer importiert \n");
			List<Game> games = dataImportService.importGames(sellers);
			dataImportTxtArea.appendText(games.size() + " Spiele importiert \n");
			System.out.println("importDataOnAction");
		}
	}
	

    @FXML
    public void handleCreateTestDataBtnAction(ActionEvent event) {
    	testDataService.createTestData();
    	
    }	
}
