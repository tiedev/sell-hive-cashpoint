package de.tiedev.sellhive.cashpoint.controllers;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import de.tiedev.sellhive.cashpoint.architecture.propertyconverter.MoneyStringConverter;
import de.tiedev.sellhive.cashpoint.model.Seller;
import de.tiedev.sellhive.cashpoint.model.SellerState;
import de.tiedev.sellhive.cashpoint.services.GameService;
import de.tiedev.sellhive.cashpoint.services.SellerService;
import de.tiedev.sellhive.cashpoint.services.SettlementService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;

@Component
public class SettlementTabController {
	@FXML
	TextField sellerNumberTxt;

	@FXML
	Button singleSettlementBtn;
	
	@FXML
	CheckBox finalAllSettlementChkBox;

	@FXML
	CheckBox finalSingleSettlementChkBox;
	
	@FXML
	TextField feeTotalTxt;
	
	@FXML
	TextField incomeOfGamesSoldTxt;
	
	@FXML
	TextField changeTxt;
	
	@FXML
	TextField cashTotalTxt;
	
	@FXML
	Button calculateCashBtn;

	@Autowired
	SellerService sellerService;

	@Autowired
	GameService gameService;

	@Autowired
	SettlementService settlementService;
	
	@Autowired
	MoneyStringConverter moneyStringConverter;

	@FXML
	public void handleSingleSettlementBtnOnAction(ActionEvent event) {
		String errorMsg = "";
		Long externalSellerId;
		Seller seller = null;
		if (!StringUtils.isEmpty(sellerNumberTxt.getText())) {
			externalSellerId = Long.valueOf(sellerNumberTxt.getText());
			seller = sellerService.findByExternalId(externalSellerId);
		}
		if (StringUtils.isEmpty(sellerNumberTxt.getText())) {
			errorMsg = "Keine Vekäufernummer eingegeben!";
		} else if (seller == null) {
			errorMsg = "Verkäufernummer nicht gefunden!";
		} else if (SellerState.INACTIVE.equals(seller.getSellerState())) {
			errorMsg = "Verkäufer war nicht eingecheckt!";
		} else if (SellerState.CHECKEDOUT.equals(seller.getSellerState())) {
			errorMsg = "Verkäufer hat schon ausgecheckt!";
		}

		if (StringUtils.isEmpty(errorMsg)) {
			Boolean finalSettlementConfirmed = Boolean.FALSE;
			if (finalSingleSettlementChkBox.isSelected()) {
				Alert alert = new Alert(AlertType.CONFIRMATION, "ACHTUNG der Verkäufer " + seller.getName() + "("
						+ seller.getExternalId()
						+ ") wird abgerechnet.\n Nach dieser Aktion kann keines seiner Spiele mehr verkauft werden!",
						ButtonType.YES, ButtonType.CANCEL);
				alert.showAndWait();
				finalSettlementConfirmed = (alert.getResult() == ButtonType.YES);
			}
			if (!finalSingleSettlementChkBox.isSelected() || finalSettlementConfirmed) {
				settlementService.createSettlementForSingleSeller(seller, finalSingleSettlementChkBox.isSelected());
				resetTab();
			}
		} else {
			Alert alert = new Alert(AlertType.ERROR, errorMsg, ButtonType.OK);
			alert.showAndWait();
		}
	}

	@FXML
	public void handleAllSettlementBtnOnAction(ActionEvent event) {
		Boolean finalSettlementConfirmed = Boolean.FALSE;
		if (finalAllSettlementChkBox.isSelected()) {
			Alert alert = new Alert(AlertType.CONFIRMATION,
					"ACHTUNG es werden alle Verkäufer abgerechnet. Nach dieser Aktion kann kein Spiel mehr verkauft werden!",
					ButtonType.YES, ButtonType.CANCEL);
			alert.showAndWait();
			finalSettlementConfirmed = (alert.getResult() == ButtonType.YES);
		}
		if (!finalAllSettlementChkBox.isSelected() || finalSettlementConfirmed) {
			settlementService.createSettlementForAllSellers(finalAllSettlementChkBox.isSelected());
		}
		resetTab();
	}

	@FXML
	public void handleCalculateCashBtnOnAction(ActionEvent event) {
		BigDecimal feeTotal = StringUtils.isEmpty(feeTotalTxt.getText()) ? BigDecimal.ZERO : moneyStringConverter.fromString(feeTotalTxt.getText());
		BigDecimal incomeOfSoldGames = StringUtils.isEmpty(incomeOfGamesSoldTxt.getText()) ? BigDecimal.ZERO : moneyStringConverter.fromString(incomeOfGamesSoldTxt.getText());
		BigDecimal change = StringUtils.isEmpty(changeTxt.getText()) ? BigDecimal.ZERO : moneyStringConverter.fromString(changeTxt.getText());
		cashTotalTxt.setText(moneyStringConverter.toString(feeTotal.add(incomeOfSoldGames).add(change)));
	}


	private void resetTab() {
		sellerNumberTxt.clear();
		finalAllSettlementChkBox.setSelected(false);
		finalSingleSettlementChkBox.setSelected(false);
	}

	public void handleTabButtonBar() {
		BigDecimal feeTotal = settlementService.getFeeTotal();
		feeTotalTxt.setText(moneyStringConverter.toString(feeTotal));
		BigDecimal incomeOfSoldGames = settlementService.getIncomeOfSoldGames();
		incomeOfGamesSoldTxt.setText(moneyStringConverter.toString(incomeOfSoldGames));
		BigDecimal change = moneyStringConverter.fromString(changeTxt.getText());
		cashTotalTxt.setText(moneyStringConverter.toString(feeTotal.add(incomeOfSoldGames).add(change)));		
	}
}
