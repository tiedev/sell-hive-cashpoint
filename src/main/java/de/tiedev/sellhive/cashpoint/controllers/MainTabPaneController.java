package de.tiedev.sellhive.cashpoint.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javafx.fxml.FXML;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.input.MouseEvent;

@Component
public class MainTabPaneController {
	
	final String CASHPOINTTAB_TITLE = "Kassieren";
	
	@FXML
	private TabPane mainTabPane;
	
	@Autowired
	private DataImportTabController importDataController;
	
	@Autowired
	private CashPointTabController cashPointTabController;
	
	@Autowired
	private GameCheckinTabController gameCheckinController;

	@FXML
	public void tabOnMouseRelease(MouseEvent event) {
		Tab tab = mainTabPane.getSelectionModel().getSelectedItem();
		if (CASHPOINTTAB_TITLE.equals(tab.getText())) {
			cashPointTabController.init();
		}
	}
}
