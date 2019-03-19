package de.tiedev.sellhive.cashpoint.services;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import de.tiedev.sellhive.cashpoint.model.Game;
import de.tiedev.sellhive.cashpoint.model.Seller;
import de.tiedev.sellhive.cashpoint.repositories.GameRepository;
import de.tiedev.sellhive.cashpoint.repositories.SellerRepository;

@Service
public class TestDataService {
	
	@Autowired
	private SellerRepository sellerRepository;
	
	@Autowired
	private GameRepository gameRepository;
	
	@Autowired
	private SellerService sellerService;
	
	@Autowired
	private GameService gameService;
	
	@Autowired
	private InvoiceService invoiceService;

	public void createTestData() {
		cleanUpDatabase();
		createSellerAndGames();
	}
	
	private void createSellerAndGames() {
		List<Seller> sellers = new ArrayList<Seller>();
		Seller seller = new Seller ("Marlene");
		seller.setExternalId(Long.valueOf("1"));
		sellers.add(seller);
//		sellers.add(new Seller("Elise"));
//		sellers.add(new Seller("Jan"));
//		sellers.add(new Seller("Lotte"));
		sellers = (List) sellerRepository.saveAll(sellers);
		createGames(sellers);
	}
	
	private void createGames(List<Seller> sellers) {
		List<Game> games = new ArrayList<Game>();
		for(Seller seller : sellers) {
//			for(int i = 1; i <= seller.getName().length(); i++) {
				String name = "Scanner";
				String barcode = "X000606AIN";
				BigDecimal price = new BigDecimal(1);
				games.add(new Game(name, barcode, price, seller ));
				name = "Chips";
				barcode = "4334011009179";
				price = new BigDecimal(2);
				games.add(new Game(name, barcode, price, seller ));
//			}
		}
		gameRepository.saveAll(games);
	}
	
	@Transactional
	public void cleanUpDatabase() {
		invoiceService.deleteAll();
		gameService.deleteAll();
		sellerService.deleteAll();
	}	
	
}
