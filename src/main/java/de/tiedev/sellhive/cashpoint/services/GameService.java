package de.tiedev.sellhive.cashpoint.services;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import de.tiedev.sellhive.cashpoint.architecture.propertyconverter.MoneyStringConverter;
import de.tiedev.sellhive.cashpoint.model.Game;
import de.tiedev.sellhive.cashpoint.model.GameState;
import de.tiedev.sellhive.cashpoint.model.Seller;
import de.tiedev.sellhive.cashpoint.repositories.GameRepository;

@Service
public class GameService {

	@Autowired
	GameRepository gameRepository;

	@Autowired
	ConfigurationService configurationService;
	
	
	@Autowired
	MoneyStringConverter moneyStringConverter;
	
	public long countAll() {
		return gameRepository.count();
	}
	
	public Game findByBarcode(String barcode) {
		return gameRepository.findByBarcode(barcode);
	}
	
	public void save(List<Game> games) {
		gameRepository.saveAll(games);		
	}
	
	public void save(Game game) {
		gameRepository.save(game);
	}

	public List<Game> getGamesBySeller(Seller seller) {
		return gameRepository.findBySeller(seller);
	}

	public List<Game> getGamesBySellerBySold(Seller seller, Boolean sold) {
		return gameRepository.findBySellerAndSold(seller, sold);
	}
	
	public List<Game> findAll() {
		return (List<Game>) gameRepository.findAll();
	}

	public void deleteAll() {
		gameRepository.deleteAll();
	}
	
	public void feePaid(List<Game> games) {
		for(Game game : games) {
			game.setGameState(GameState.FEE_PAID);
		}
		save(games);
	}

	public void checkedOut(List<Game> games) {
		for(Game game : games) {
			game.setGameState(GameState.CHECKEDOUT);
		}
		save(games);
	}

	public BigDecimal calculateFee(List<Game> games) {
		BigDecimal feeTotal = BigDecimal.ZERO;
		if (configurationService.hasFee2() && games.size() > configurationService.getFee2NumberOfGames()) {
			feeTotal = feeTotal.add(configurationService.getFee1fee().multiply(BigDecimal.valueOf(configurationService.getFee2NumberOfGames())));
			int numberOfGamesWithHigherFee = games.size() > configurationService.getFee2NumberOfGames() ? games.size() - configurationService.getFee2NumberOfGames() : 0;
			feeTotal = feeTotal.add(configurationService.getFee2fee().multiply(BigDecimal.valueOf(numberOfGamesWithHigherFee)));
		} else {
			feeTotal = configurationService.getFee1fee().multiply(BigDecimal.valueOf(games.size()));
		}
		return feeTotal;
	}
	//TODO: Wird später vom SearchTabController genutzt
	public List<Game> getGamesByNameByPublisher(String nameOfGame, String publisher) {
//		return gameRepository.findByNameByPublisher(nameOfGame, publisher);
		return null;
	}

	public String getFeeMessage(List<Game> games) {
		String feeMessage = new String("Gebührenberechnung \n");
		BigDecimal basicFee = BigDecimal.ZERO;
		if (configurationService.hasFee2() && games.size() > configurationService.getFee2NumberOfGames()) {
			basicFee = basicFee.add(configurationService.getFee1fee().multiply(BigDecimal.valueOf(configurationService.getFee2NumberOfGames())));
			feeMessage = feeMessage + createFeeLineItem(configurationService.getFee2NumberOfGames(), configurationService.feeBase, basicFee) + "\n";			
			int numberOfGamesWithHigherFee = games.size() > configurationService.getFee2NumberOfGames() ? games.size() - configurationService.getFee2NumberOfGames() : 0;
			BigDecimal higherFee = configurationService.getFee2fee().multiply(BigDecimal.valueOf(numberOfGamesWithHigherFee));
			feeMessage = feeMessage + createFeeLineItem(numberOfGamesWithHigherFee, configurationService.fee2fee, higherFee) + "\n";			
		} else {
			basicFee = configurationService.getFee1fee().multiply(BigDecimal.valueOf(games.size()));
			feeMessage = feeMessage + createFeeLineItem(games.size(), configurationService.feeBase, basicFee);
		}
		return feeMessage;
	}

	private String createFeeLineItem(int numberOfGames, BigDecimal fee, BigDecimal feeTotal) {
		
		return numberOfGames  + " * " + moneyStringConverter.toString(fee) + " = " + moneyStringConverter.toString(feeTotal) + " €";
	}

	public List<Game> gamesSold() {
		return gameRepository.findBySold(Boolean.TRUE);
	}
	
}
