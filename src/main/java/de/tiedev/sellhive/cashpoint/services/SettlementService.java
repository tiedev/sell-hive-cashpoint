package de.tiedev.sellhive.cashpoint.services;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import de.tiedev.sellhive.cashpoint.architecture.propertyconverter.MoneyStringConverter;
import de.tiedev.sellhive.cashpoint.model.Game;
import de.tiedev.sellhive.cashpoint.model.Label;
import de.tiedev.sellhive.cashpoint.model.Seller;
import de.tiedev.sellhive.cashpoint.model.SellerSettlement;
import de.tiedev.sellhive.cashpoint.model.SellerState;
import javafx.application.Application;
import javafx.application.HostServices;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Alert.AlertType;

@Component
public class SettlementService {

	@Autowired
	PDFPrintService pdfPrintService;

	@Autowired
	PDFPrintServiceLabelDocument pdfPrintServiceLabelDocument;
	
	@Autowired
	MoneyStringConverter moneyStringConverter;
	
	@Autowired
	GameService gameService;

	@Autowired
	SellerService sellerService;

	@Autowired
	InvoiceService invoiceService;
	
	@Autowired
	Application application;	

	@Transactional
	public void createSettlementForSingleSeller(Seller seller, boolean finalSettlement) {
		seller = sellerService.findByExternalId(seller.getExternalId());
		SellerSettlement sellerSettlement;
		if (seller != null) {
			sellerSettlement = generateSettlementForSeller(seller);
			System.out.println(seller.getName() + ":" + seller.getExternalId());
			try {
				String filename = pdfPrintService.printSettlement(sellerSettlement, finalSettlement);
				File file = new File(filename);
				HostServices hostServices = application.getHostServices();
		        hostServices.showDocument(file.getAbsolutePath());
			} catch (Exception e) {
				System.out.println(e.getStackTrace().toString());
			}
			if (finalSettlement) {
				sellerService.checkOut(seller);
			}
		}
	}

	private SellerSettlement generateSettlementForSeller(Seller seller) {
		SellerSettlement sellerSettlement = new SellerSettlement();
		if (seller != null) {
			sellerSettlement.setSeller(seller);
			System.out.println(seller.getName() + ":" + seller.getExternalId());
			List<Game> gamesSold = new ArrayList<Game>();
			List<Game> gamesNotSold = new ArrayList<Game>();
			for (Game game : seller.getGames()) {
				Boolean trash = game.isSold() ? gamesSold.add(game) : gamesNotSold.add(game);
			}
			sellerSettlement.setGamesSold(
					gamesSold.stream().sorted((p1, p2) -> p1.getName().compareTo(p2.getName())).collect(Collectors.toList()));
			sellerSettlement.setGamesNotSold(
					gamesNotSold.stream().sorted((p1, p2) -> p1.getName().compareTo(p2.getName())).collect(Collectors.toList()));
		}
		return sellerSettlement;
	}

	@Transactional
	public void createSettlementForAllSellers(boolean finalSettlement) {
		List<Seller> sellers = sellerService.findBySellerState(SellerState.CHECKEDIN);
		List<SellerSettlement> sellerSettlements = new ArrayList<SellerSettlement>();
		SellerSettlement sellerSettlement;
		for (Seller seller : sellers) {
			sellerSettlement = generateSettlementForSeller(seller);
			sellerSettlements.add(sellerSettlement);
		}
		try {
			String filename = pdfPrintService.printSettlements(sellerSettlements, finalSettlement);
			File file = new File("./" + filename);
			HostServices hostServices = application.getHostServices();
	        hostServices.showDocument(file.getAbsolutePath());
		} catch (Exception e) {
			System.out.println(e.getStackTrace().toString());
		}
		try {
			List<Label> labels = new ArrayList<Label>();
			for (SellerSettlement settlement : sellerSettlements) {
				labels.add(new Label("ID: " + String.valueOf(settlement.getSellerExternalId()), settlement.getSellerName(), 
						"Summe: " + moneyStringConverter.toString(settlement.getSumTotal()) + " €"));
			}
			String filename = pdfPrintServiceLabelDocument.printLabelDocument(labels);
			File file = new File("./" + filename);
			HostServices hostServices = application.getHostServices();
	        hostServices.showDocument(file.getAbsolutePath());
		} catch (Exception e) {
			System.out.println(e.getStackTrace().toString());
		}
		if (finalSettlement) {
			sellerService.checkOut(sellers);
		}
	}
	
}
