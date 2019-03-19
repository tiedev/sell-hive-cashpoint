package de.tiedev.sellhive.cashpoint.controllers;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import de.tiedev.sellhive.cashpoint.architecture.propertyconverter.MoneyStringConverter;
import de.tiedev.sellhive.cashpoint.model.Game;
import de.tiedev.sellhive.cashpoint.services.GameService;
import de.tiedev.sellhive.cashpoint.services.SellerService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

@Component
public class SearchTabController {

	@FXML
	TextField gameTxt;

	@FXML
	TextField publisherTxt;

	@FXML
	Button searchBtn;

	@FXML
	Button cancelBtn;

	@FXML
	TableView searchTblView;

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
//		int i = 0;
//		for (TableColumn column : (ObservableList<TableColumn>) searchTblView.getColumns()) {
//			if (i == 0) {
//				column.setCellValueFactory(new PropertyValueFactory<Game, String>("name"));
//			} else if (i == 1) {
//				column.setCellValueFactory(new PropertyValueFactory<Game, String>("publisher"));
//			} else if (i == 2) {
//				column.setCellValueFactory(new PropertyValueFactory<Game, String>("barcode"));
//			} else if (i == 3) {
//				column.setCellValueFactory(new PropertyValueFactory<Game, String>("price"));
//				column.setStyle("-fx-alignment: CENTER-RIGHT;");
//			} else if (i == 4) {
//				column.setCellValueFactory(new PropertyValueFactory<Game, String>("publisher"));
//			}
//			i++;
//		}
	}

	@FXML
	@Transactional
	public void handleSearchBtnOnAction(ActionEvent event) {
		String nameOfGame = gameTxt.getText();
		String publisher = publisherTxt.getText();
		List<Game> games = new ArrayList<Game>();
		if (!StringUtils.isEmpty(nameOfGame) || !StringUtils.isEmpty(publisher)) {
			resetTab();
			gameTxt.setText(nameOfGame);
			publisherTxt.setText(publisher);
			games = gameService.getGamesByNameByPublisher(nameOfGame, publisher);
			if (games != null) {
				for (Game game : games) {
					searchTblView.getItems().add(game);
				}
			}
		}
	}


	@FXML
	public void handleCancelBtnOnAction(ActionEvent event) {
		resetTab();
	}

	private void resetTab() {
		gameTxt.clear();
		publisherTxt.clear();
		ObservableList<Game> games = FXCollections.observableArrayList();
		searchTblView.setItems(games);
		gameTxt.requestFocus();
	}
}
