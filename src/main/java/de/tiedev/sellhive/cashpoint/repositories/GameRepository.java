package de.tiedev.sellhive.cashpoint.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import de.tiedev.sellhive.cashpoint.model.Game;
import de.tiedev.sellhive.cashpoint.model.Seller;

public interface GameRepository extends CrudRepository<Game, Long> {

	public Game findByBarcode(String barcode);
	
	public Optional<Game> findById(Long id);
	
	public Long countBySeller(Seller seller);

	public List<Game> findBySeller(Seller seller);
	
	public List<Game> findBySellerAndSold(Seller seller, Boolean sold);

	public List<Game> findBySold(Boolean sold);

//	public List<Game> findByNameByPublisher(String nameOfGame, String publisher);
}
