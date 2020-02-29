package de.tiedev.sellhive.cashpoint.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import de.tiedev.sellhive.cashpoint.architecture.propertyconverter.MoneyStringConverter;
import de.tiedev.sellhive.cashpoint.services.ConfigurationService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;

@Component
public class ConfigurationTabController {
	
	@Autowired
	ConfigurationService configurationService;
	
	@Autowired
	MoneyStringConverter moneyStringConverter;
	
	@FXML
	TextField importURLGamesTxt;
	
	@FXML
	TextField importURLSellerTxt;
	
	
	@FXML
	TextField fee2NumberOfGamesTxt;
	
	@FXML
	TextField fee1FeeTxt;
	
	@FXML
	TextField fee2FeeTxt;
	
	@FXML
	public void handleSaveBtn(ActionEvent event) {
		save();
	}

	@FXML
	public void handleResetBtn(ActionEvent event) {
		resetTab();
	}
		
	public void initialize() {
		importURLGamesTxt.setText(configurationService.getImportURLGames());
		importURLSellerTxt.setText(configurationService.getImportURLSeller());
		fee1FeeTxt.setText(moneyStringConverter.toString(configurationService.getFee1fee()));
		fee2NumberOfGamesTxt.setText(String.valueOf((configurationService.getFee2NumberOfGames())));
		fee2FeeTxt.setText(moneyStringConverter.toString(configurationService.getFee2fee()));
	}	

	private void save() {
		if (!StringUtils.isEmpty(importURLGamesTxt.getText())) {
			configurationService.setImportURLGames(importURLGamesTxt.getText());
		}
		if (!StringUtils.isEmpty(importURLSellerTxt.getText())) {
			configurationService.setImportURLGames(importURLSellerTxt.getText());
		}


		if (!StringUtils.isEmpty(moneyStringConverter.fromString(fee1FeeTxt.getText()))) {
			configurationService.setFee1fee(moneyStringConverter.fromString(fee1FeeTxt.getText()));
		}
		
		if (!StringUtils.isEmpty(fee2NumberOfGamesTxt.getText())) {
			configurationService.setFee2NumberOfGames(Integer.parseInt(fee2NumberOfGamesTxt.getText()));
		}

		if (!StringUtils.isEmpty(moneyStringConverter.fromString(fee2FeeTxt.getText()))) {
			configurationService.setFee2fee(moneyStringConverter.fromString(fee2FeeTxt.getText()));
		}
		
	}
	
	public void resetTab() {
		
	}
}
