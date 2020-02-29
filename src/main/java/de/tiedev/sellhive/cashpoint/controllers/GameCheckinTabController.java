package de.tiedev.sellhive.cashpoint.controllers;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import de.tiedev.sellhive.cashpoint.architecture.propertyconverter.MoneyStringConverter;
import de.tiedev.sellhive.cashpoint.model.Game;
import de.tiedev.sellhive.cashpoint.model.Seller;
import de.tiedev.sellhive.cashpoint.model.SellerState;
import de.tiedev.sellhive.cashpoint.services.GameService;
import de.tiedev.sellhive.cashpoint.services.SellerService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

@Component
public class GameCheckinTabController {

	@FXML
	TextField sellerNumberTxt;

	@FXML
	TextField sellerNameTxt;

	@FXML
	TextField numberOfGamesTxt;

	@FXML
	TextField feeTxt;

	@FXML
	Button searchSellerBtn;

	@FXML
	Button feePaidBtn;

	@FXML
	Button cancelBtn;

	@FXML
	TextArea messageTxt;
	
	@FXML
	TableView gamesTblView;

	@Autowired
	SellerService sellerService;

	@Autowired
	GameService gameService;

	@Autowired
	MoneyStringConverter moneyStringConverter;

	public void initialize() {
		initInvoiceCol();
	}

	private void initInvoiceCol() {
		int i = 0;
		for (TableColumn column : (ObservableList<TableColumn>) gamesTblView.getColumns()) {
			if (i == 0) {
				column.setCellValueFactory(new PropertyValueFactory<Game, String>("barcode"));
			} else if (i == 1) {
				column.setCellValueFactory(new PropertyValueFactory<Game, String>("name"));
			} else if (i == 2) {
				column.setCellValueFactory(new PropertyValueFactory<Game, String>("price"));
				column.setStyle("-fx-alignment: CENTER-RIGHT;");
			} else if (i == 3) {
				column.setCellValueFactory(new PropertyValueFactory<Game, String>("fee"));
				column.setStyle("-fx-alignment: CENTER-RIGHT;");
			}
			i++;
		}
	}

	@FXML
	public void handleSearchSellerBtn(ActionEvent event) {
		if (!StringUtils.isEmpty(sellerNumberTxt.getText())) {
			setSellerAndGames(sellerNumberTxt.getText());
			feePaidBtn.setDisable(false);
		}
	}

	@FXML
	public void handleSellerNumberTxtKeyReleased(KeyEvent ke) {
		if (ke.getCode().equals(KeyCode.ENTER) && !StringUtils.isEmpty(sellerNumberTxt.getText())) {
			setSellerAndGames(sellerNumberTxt.getText());
			feePaidBtn.setDisable(false);
		}
	}

	private void setSellerAndGames(String idOfSellerAsString) {
		resetTab();
		sellerNumberTxt.setText(idOfSellerAsString);
		Long idOfSeller = Long.valueOf(idOfSellerAsString);
		Seller seller = sellerService.findByExternalId(idOfSeller);
		if (seller != null) {
			sellerNameTxt.setText(seller.getName());
			List<Game> games = gameService.getGamesBySeller(seller);
			if (games != null) {
				numberOfGamesTxt.setText(Integer.toString(games.size()));

				BigDecimal totalFee = BigDecimal.ZERO;
				for (Game game : games) {
					//totalFee = totalFee.add(game.getFee());
					gamesTblView.getItems().add(game);
				}
				
				totalFee = gameService.calculateFee(games);
				feeTxt.setText(moneyStringConverter.toString(totalFee));
				String message = new String("");
				message = gameService.getFeeMessage(games);
				messageTxt.setText(message);
			}
		} else {
			// ERROR Seller not found
		}
	}

	@FXML
	public void handleFeePaidBtn(ActionEvent event) {
		String errorMsg = "";
		Long idOfSeller = Long.valueOf(sellerNumberTxt.getText());
		Seller seller = sellerService.findByExternalId(idOfSeller);
		if (seller == null) {
			errorMsg = "Verkäufernummer ist nicht gesetzt";
		} else if (SellerState.CHECKEDIN.equals(seller.getSellerState())) {
			errorMsg = "Verkäufer ist bereits eingecheckt!";
		} else if (SellerState.CHECKEDOUT.equals(seller.getSellerState())) {
			errorMsg = "Verkäufer hat schon ausgecheckt!";
		}

		if (StringUtils.isEmpty(errorMsg)) {
			Alert alert = new Alert(AlertType.CONFIRMATION, "Gebühren: " + feeTxt.getText() + "\n Anzahl Spiele: "
					+ numberOfGamesTxt.getText() + "\n Gebühr bezahlt?", ButtonType.YES, ButtonType.CANCEL);
			alert.showAndWait();

			if (alert.getResult() == ButtonType.YES && gamesTblView.getItems().size() > 0) {
				sellerService.checkedIn(seller);
				resetTab();
			}
		} else {
			Alert alert = new Alert(AlertType.ERROR, errorMsg, ButtonType.OK);
			alert.showAndWait();
		}
	}

	@FXML
	public void handleCancelBtnOnAction(ActionEvent event) {
		resetTab();
	}

	private void resetTab() {
		sellerNumberTxt.clear();
		sellerNameTxt.clear();
		numberOfGamesTxt.setText("");
		feeTxt.clear();
		ObservableList<Game> games = FXCollections.observableArrayList();
		gamesTblView.setItems(games);
		feePaidBtn.setDisable(true);
		sellerNumberTxt.requestFocus();
	}
}
