package de.tiedev.sellhive.cashpoint.services;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

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
	
	public List<Game> save(List<Game> games) {
		return (List<Game>) gameRepository.saveAll(games);
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
    	return games.stream().map(Game::getFee).reduce(BigDecimal.ZERO, BigDecimal::add);
	}
	//TODO: Wird später vom SearchTabController genutzt
	public List<Game> getGamesByNameByPublisher(String nameOfGame, String publisher) {
//		return gameRepository.findByNameByPublisher(nameOfGame, publisher);
		return null;
	}

	public String getFeeMessage(List<Game> games) {
		String feeMessage = new String("Gebührenberechnung \n"
			+ "0,50 € Gebühr: 0,50 - 20,00 €  \n"
			+ "1,00 € Gebühr: 20,01 - 50,00 €  \n"
			+ "2,00 € Gebühr: 50,01 - 80,00 €  \n"
			+ "5,00 € Gebühr: 80,01 - 100.00 €");
		return feeMessage;
	}

	private String createFeeLineItem(int numberOfGames, BigDecimal fee, BigDecimal feeTotal) {
		
		return numberOfGames  + " * " + moneyStringConverter.toString(fee) + " = " + moneyStringConverter.toString(feeTotal) + " €";
	}

	public List<Game> gamesSold() {
		return gameRepository.findBySold(Boolean.TRUE);
	}

	public void updateSalesStatus(List<Game> games) {
		Map<String, Game> gamesWithNewSalesStatus = games.stream().collect(Collectors.toMap(Game::getBarcode, Function.identity()));
		List<Game> gamesToUpdate = (List<Game>) gameRepository.findAll();
		Game gameWithNewSalesStatus;
		for(Game gameToUpdate : gamesToUpdate) {
			gameWithNewSalesStatus = gamesWithNewSalesStatus.get(gameToUpdate.getBarcode());
			if (!gameToUpdate.isSold() && gameWithNewSalesStatus != null) {
				if (gameWithNewSalesStatus.isSold()) {
					gameToUpdate.setSold(gameWithNewSalesStatus.isSold());
					gameToUpdate.setGameState(GameState.SOLD);
				}
			}
		}
		gameRepository.saveAll(gamesToUpdate);
	}
}
