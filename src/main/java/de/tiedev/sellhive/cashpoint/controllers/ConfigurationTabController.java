package de.tiedev.sellhive.cashpoint.controllers;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import de.tiedev.sellhive.cashpoint.architecture.propertyconverter.MoneyStringConverter;
import de.tiedev.sellhive.cashpoint.services.ConfigurationService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
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
	CheckBox sellingConfirmationWithReturnBox;
	
	@FXML
	TextField labelPrintInitXTxt;
	
	@FXML
	TextField labelPrintInitYTxt;
	
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
		sellingConfirmationWithReturnBox.setSelected(configurationService.isSellingConfirmationWithReturn());
		labelPrintInitYTxt.setText(String.valueOf(configurationService.getlabelPrintInitX()));
		if (StringUtils.isEmpty(labelPrintInitXTxt.getText())) {
			labelPrintInitXTxt.setText("0");
		}
		labelPrintInitYTxt.setText(String.valueOf(configurationService.getlabelPrintInitY()));
		if (StringUtils.isEmpty(labelPrintInitYTxt.getText())) {
			labelPrintInitYTxt.setText("0");
		}		

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
		} else {
			configurationService.setFee2NumberOfGames(0);
		}

		if (!StringUtils.isEmpty(moneyStringConverter.fromString(fee2FeeTxt.getText()))) {
			configurationService.setFee2fee(moneyStringConverter.fromString(fee2FeeTxt.getText()));
		} else {
			configurationService.setFee2fee(BigDecimal.ZERO);
		}
		
		configurationService.setSellingConfirmationWithReturn(sellingConfirmationWithReturnBox.selectedProperty().get());

		if (!StringUtils.isEmpty(labelPrintInitXTxt.getText())) {
			configurationService.setlabelPrintInitX(Integer.parseInt(labelPrintInitXTxt.getText()));
		} 
		
		if (!StringUtils.isEmpty(labelPrintInitYTxt.getText())) {
			configurationService.setlabelPrintInitY(Integer.parseInt(labelPrintInitYTxt.getText()));
		}
	}
	
	public void resetTab() {
		
	}
}
