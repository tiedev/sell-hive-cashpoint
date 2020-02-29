package de.tiedev.sellhive.cashpoint.services;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import de.tiedev.sellhive.cashpoint.model.Game;
import de.tiedev.sellhive.cashpoint.model.GameState;
import de.tiedev.sellhive.cashpoint.model.SellHiveGame;
import de.tiedev.sellhive.cashpoint.model.SellHiveSeller;
import de.tiedev.sellhive.cashpoint.model.Seller;

@Service
public class DataImportService {
	
	@Autowired
	GameService gameService;
	
	@Autowired
	SellerService sellerService;
	
	@Autowired
	InvoiceService invoiceService;
	
	@Autowired
	ConfigurationService configurationService;

	@Transactional
	public Map<Long, Seller> importSeller() {
        List<SellHiveSeller> sellersToImport = getApi(configurationService.getImportURLSeller(), HttpMethod.GET,
        		new ParameterizedTypeReference<List<SellHiveSeller>>(){});
        List<Seller> sellers = convertSellHiveSellerToSeller(sellersToImport);
        sellers = sellerService.save(sellers);
        return sellers.stream().collect(Collectors.toMap(Seller::getExternalId, Function.identity()));
	}
	
	
	private List<Seller> convertSellHiveSellerToSeller(List<SellHiveSeller> sellersToImport) {
		List<Seller> sellers = new ArrayList<Seller>();
		Seller seller;
		for(SellHiveSeller sellHiveSeller : sellersToImport) {
			seller = new Seller(sellHiveSeller.getName());
			seller.setExternalId(sellHiveSeller.getId());
			sellers.add(seller);
		}
		return sellers;
	}


	public List<Game> importGames(Map<Long, Seller> sellers) {
		List<SellHiveGame> gamesToImport = getApi(configurationService.getImportURLGames(), HttpMethod.GET,
				new ParameterizedTypeReference<List<SellHiveGame>>(){});
		List<Game> games = convertSellHiveGameToGame(gamesToImport, sellers);
		gameService.save(games);
		return games;
	}	
	
	private List<Game> convertSellHiveGameToGame(List<SellHiveGame> gamesToImport, Map<Long, Seller> sellers) {
		List<Game> games = new ArrayList<Game>();
		Game game;
		Seller seller;
		for(SellHiveGame sellHiveGame : gamesToImport) {
			game = new Game();
			game.setBarcode(sellHiveGame.getBarcode());
			game.setName(sellHiveGame.getName());
			game.setExternalId(sellHiveGame.getId());
			BigDecimal price = sellHiveGame.getPrice();
			price = price != null? price : BigDecimal.ZERO;
			//Price format of sell hive is without a separator
			price = price.divide(BigDecimal.TEN);
			price = price.divide(BigDecimal.TEN);
			game.setPrice(price);
			if (sellHiveGame.getLabeled()) {
				game.setGameState(GameState.LABELD);
			}
			game.setPublisher(sellHiveGame.getPublisher());
			game.setExternalSellerId(sellHiveGame.getSellerId());
			seller = sellers.get(sellHiveGame.getSellerId());
			game.setSeller(seller);
			seller.addGame(game);
			games.add(game);
			System.out.println(game.getId() + ":" + game.getName() + ": " + game.getSeller().getId() + ": " + game.getSeller().getName() + ": " + game.getSeller().getExternalId());
		}
		return games;
	}
	
	
	private <T> List<T> getApi(final String path, final HttpMethod method, ParameterizedTypeReference<List<T>> referenceList) {     
	    final RestTemplate restTemplate = new RestTemplate();
	    final ResponseEntity<List<T>> response = restTemplate.exchange(
	      path,
	      method,
	      null,
	      referenceList);
	      
	    List<T> list = response.getBody();
	    return list;
	}


	@Transactional
	public void cleanUpDatabase() {
		invoiceService.deleteAll();
		gameService.deleteAll();
		sellerService.deleteAll();
	}	
}
