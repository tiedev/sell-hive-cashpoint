package de.tiedev.sellhive.cashpoint.controllers;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import de.tiedev.sellhive.cashpoint.architecture.propertyconverter.MoneyStringConverter;
import de.tiedev.sellhive.cashpoint.model.Game;
import de.tiedev.sellhive.cashpoint.model.GameState;
import de.tiedev.sellhive.cashpoint.model.InvoiceLineItem;
import de.tiedev.sellhive.cashpoint.model.SellerState;
import de.tiedev.sellhive.cashpoint.services.GameService;
import de.tiedev.sellhive.cashpoint.services.InvoiceService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

@Component
public class CashPointTabController {

	@FXML
	TextField newItemTxt;

	@FXML
	Button addLineItemBtn;

	@FXML
	TableView gamesTblView;

	@FXML
	TextField numberOfLinesTxt;

	@FXML
	TextField sumTxt;

	@FXML
	Button sellBtn;

	@FXML
	Button restartBtn;

	@FXML
	TextField textfieldIdOfSavedInvoice;

	@Autowired
	InvoiceService invoiceService;

	@Autowired
	GameService gameService;

	@Autowired
	MoneyStringConverter moneyStringConverter;

	public void initialize() {
		initInvoiceCol();
	}

	@FXML
	public void handleNewItemTxtKeyReleased(KeyEvent ke) {
		if (ke.getCode().equals(KeyCode.ENTER)) {
			addLineItem();
		}
	}

	@FXML
	public void handleAddLineItemBtn(ActionEvent event) {
		addLineItem();
	}

	private void addLineItem() {
		if (!StringUtils.isEmpty(newItemTxt.getText())) {
			String barcode = newItemTxt.getText();
			Game game = gameService.findByBarcode(barcode);
			String errorMsg = "";
			if (game == null) {
				errorMsg = "Der Barcode " + newItemTxt.getText()
						+ "wurde nicht gefunden. \n Bitte neuen Code einscannen.";
			} else if (game.isSold()) {
				errorMsg = "Das Spiel wurde schon verkauft. \n Bitte neuen Code einscannen.";
				newItemTxt.setText("");
			} else if (gamesTblView.getItems().contains(game)) {
				errorMsg = "Code " + newItemTxt.getText() + " wurde bereits gescannt. Bitte nächsten Code scannen";
			} else if (game.getGameState() == null || GameState.LABELD.equals(game.getGameState())) {
				errorMsg = "Gebühr für das Spiel wurde nicht bezahlt!";
			} else if (GameState.CHECKEDOUT.equals(game.getGameState())) {
				errorMsg = "Verkäufer hat bereits ausgecheckt!";
			}

			// If game exists, was not sold and not already scanned
			if (StringUtils.isEmpty(errorMsg)) {
				ObservableList<Game> games = gamesTblView.getItems();
				games.add(game);
				newItemTxt.clear();
				numberOfLinesTxt.setText(Integer.toString(gamesTblView.getItems().size()));

				BigDecimal sum = moneyStringConverter.fromString(sumTxt.getText());
				sum = sum.add(game.getPrice());
				sumTxt.setText(moneyStringConverter.toString(sum));

			} else {
				Alert alert = new Alert(AlertType.ERROR, errorMsg, ButtonType.OK);
				alert.showAndWait();
				newItemTxt.clear();
				newItemTxt.requestFocus();

			}
		}
	}

	private void initInvoiceCol() {
		int i = 0;
		for (TableColumn column : (ObservableList<TableColumn>) gamesTblView.getColumns()) {
			if (i == 0) {
				column.setCellValueFactory(new PropertyValueFactory<InvoiceLineItem, String>("barcode"));
			} else if (i == 1) {
				column.setCellValueFactory(new PropertyValueFactory<InvoiceLineItem, String>("name"));
			} else if (i == 2) {
				column.setCellValueFactory(new PropertyValueFactory<InvoiceLineItem, String>("price"));
				column.setStyle("-fx-alignment: CENTER-RIGHT;");
			}
			i++;
		}
	}

	@FXML
	public void handleSellBtn(ActionEvent event) {
		sellGames();
	}

	@FXML
	public void handleRestartBtn(ActionEvent event) {
		resetTab();
	}

	@FXML
	public void handleCashPointTabKeyReleased(KeyEvent ke) {
		if (ke.getCode().equals(KeyCode.SPACE)) {
			sellGames();
		}
	}

	private void sellGames() {
		Alert alert = new Alert(AlertType.CONFIRMATION, "Gesamtsumme: " + sumTxt.getText() 
		+ "\n Anzahl Spiele: " + numberOfLinesTxt.getText()
		+ "\n Verkauf abschließen?", ButtonType.YES, ButtonType.CANCEL);
		alert.showAndWait();

		if (alert.getResult() == ButtonType.YES && gamesTblView.getItems().size() > 0) {
			invoiceService.saveInvoice(gamesTblView.getItems());
			resetTab();
		}
		
	}
	/**
	 * clears all fields
	 */
	private void resetTab() {
		// clear all fields
		sumTxt.clear();
		numberOfLinesTxt.clear();
		ObservableList<Game> games = FXCollections.observableArrayList();
		gamesTblView.setItems(games);
		newItemTxt.clear();
		newItemTxt.requestFocus();
	}

	public void init() {
		resetTab();
	}

}
