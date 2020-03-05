package de.tiedev.sellhive.cashpoint.controllers;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import de.tiedev.sellhive.cashpoint.architecture.propertyconverter.MoneyStringConverter;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;

@Component
public class SellingConfirmationDialogController {
	
	private Stage stage;

	@FXML
	TextField numberOfGamesTxt;
	
	@FXML
	TextField priceTotalTxt;
	
	@FXML
	TextField cashTxt;
	
	@FXML
	Label returnLbl;
	
	@Autowired
	MoneyStringConverter moneyStringConverter;
	
	public void setStage(Stage stage) {
		this.stage = stage;
	}
	
	public void init(int numberOfGames, BigDecimal priceTotal) {
		moneyStringConverter = new MoneyStringConverter();
		priceTotalTxt.setText(moneyStringConverter.toString(priceTotal));
		numberOfGamesTxt.setText(String.valueOf(numberOfGames));
	}
		
	public void handleSellingConfirmationDialogPaneKeyReleased(KeyEvent event) {
		if (event.getCode().equals(KeyCode.ENTER)) {
			checkOKOrEnter();
		} else if (event.getCode().equals(KeyCode.ESCAPE)) {
			stage.setUserData(Boolean.FALSE);
			stage.hide();			
		}
	}
	
	public void handleCashTxtKeyReleased(KeyEvent event) {
//		if (ke.getCode().equals(KeyCode.ENTER)) {
//			addLineItem();
//		}
	}
	
	public void handleOkBtnOnAction(ActionEvent event) {
		checkOKOrEnter();
	}
	
	private void checkOKOrEnter() {
		if (!StringUtils.isEmpty(cashTxt.getText())) {
			BigDecimal cash = moneyStringConverter.fromString(cashTxt.getText());
			BigDecimal priceTotal = moneyStringConverter.fromString(priceTotalTxt.getText());
			if (cash.compareTo(priceTotal) < 0) {
				Alert alert = new Alert(AlertType.ERROR, "Gegeben ist kleiner als Gesamtpreis!", ButtonType.OK);
				alert.showAndWait();
				returnLbl.setText("");
			} else {
				BigDecimal returnMoney = cash.subtract(priceTotal);
			
				//return not set or new return value
				if (StringUtils.isEmpty(returnLbl.getText()) || !returnMoney.equals(moneyStringConverter.fromString(returnLbl.getText()))) {
					returnLbl.setText(moneyStringConverter.toString(returnMoney));
				} else {
					stage.setUserData(Boolean.TRUE);
					stage.hide();
				}
			}
		} else {
			stage.setUserData(Boolean.TRUE);
			stage.hide();
		}		
	}
	
	public void handleCancelBtnOnAction(ActionEvent event) {
		stage.setUserData(Boolean.FALSE);
		stage.hide();		
	}
}
