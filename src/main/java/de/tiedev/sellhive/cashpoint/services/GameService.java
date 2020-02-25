package de.tiedev.sellhive.cashpoint.services;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
		if (configurationService.hasFee2() && games.size() > configurationService.getFee1NumberOfGames()) {
			 feeTotal.add(configurationService.getFee1fee().multiply(BigDecimal.valueOf(configurationService.getFee1NumberOfGames())));
			 feeTotal.add(configurationService.getFee2fee().multiply(BigDecimal.valueOf(games.size() - configurationService.getFee1NumberOfGames())));
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
}
