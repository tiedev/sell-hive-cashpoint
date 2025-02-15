package de.tiedev.sellhive.cashpoint.services;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import de.tiedev.sellhive.cashpoint.model.Seller;
import de.tiedev.sellhive.cashpoint.model.SellerState;
import de.tiedev.sellhive.cashpoint.repositories.SellerRepository;

@Service
public class SellerService {
	
	@Autowired
	SellerRepository sellerRepository;
	
	@Autowired
	GameService gameService;
	
	public long countAll() {
		return sellerRepository.count();
	}


	public Seller save(Seller seller) {
		return sellerRepository.save(seller);
	}

	public List<Seller> save(List<Seller> sellers) {
		return (List<Seller>) sellerRepository.saveAll(sellers);
	}

	public Seller findByExternalId(Long externalId) {
		Seller seller = sellerRepository.findByExternalId(externalId);
		return seller;
	}

	public List<Seller> getAll() {
		return (List<Seller>) sellerRepository.findAll();
	}

	public void deleteAll() {
		sellerRepository.deleteAll();
		
	}

	@Transactional
	public Seller checkedIn(Seller seller, BigDecimal feePaid) {
		seller.setSellerState(SellerState.CHECKEDIN);
		seller.setFeePaid(feePaid);
		seller = save(seller);
		gameService.feePaid(seller.getGames());
		return seller;
	}
	
	@Transactional
	public Seller checkOut(Seller seller) {
		seller.setSellerState(SellerState.CHECKEDOUT);
		gameService.checkedOut(seller.getGames());
		return save(seller);
	}


	public List<Seller> findBySellerState(SellerState sellerState) {
		return sellerRepository.findBySellerState(sellerState);
	}


	public void checkOut(List<Seller> sellers) {
		for(Seller seller : sellers) {
			checkOut(seller);
		}	
	}
}
