package de.tiedev.sellhive.cashpoint.model;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import de.tiedev.sellhive.cashpoint.architecture.propertyconverter.MoneyStringConverter;

public class SellerSettlement {
	Seller seller;
	List<Game> gamesSold = new ArrayList<Game>();
	List<Game> gamesNotSold = new ArrayList<Game>();
	BigDecimal sumTotal = BigDecimal.ZERO;
	
	public void setSeller(Seller seller) {
		this.seller = seller;
	}
	
	public void setGamesSold(List<Game> games) {
		if (games != null) {
			gamesSold = games;
			for (Game game : games) {
				sumTotal = sumTotal.add(game.getPrice());
			}
		}
	}
	
	public void setGamesNotSold(List<Game> games) {
		gamesNotSold = games;
	}
	
	public String getHeader() {
		return "Verkäufernummer: " + seller.getExternalId() + " : Verkäufername: " + seller.getName();
	}
	
	public List<String> getGamesSold() {
		List<String> output = new ArrayList<String>();
		MoneyStringConverter moneyStringConverter = new MoneyStringConverter();
		for (Game game: gamesSold) {
			output.add(game.getBarcode() + " : " + game.getName() + " : " + moneyStringConverter.toString(game.getPrice()) + " €");  
		}
		return output;
	}
	
	public List<String> getGamesNotSold() {
		List<String> output = new ArrayList<String>();
		MoneyStringConverter moneyStringConverter = new MoneyStringConverter();
		for (Game game: gamesNotSold) {
			output.add(game.getBarcode() + " : " + game.getName() + " : " + moneyStringConverter.toString(game.getPrice()) + " €");  
		}
		return output;
	}
	
	
	public List<String> getFooter() {
		List<String> footer = new ArrayList<String>();
		footer.add("Anzahl Spiele verkauft: " + gamesSold.size() + " / Anzahl Spiele zurückbekommen: " + gamesNotSold.size());
		footer.add("Gesamtsumme: " + sumTotal + " €");
		return footer;
	}
	
	public String getSellerName() {
		return seller.getName();
	}
	
	public Long getSellerExternalId() {
		return seller.getExternalId();
	}
}
