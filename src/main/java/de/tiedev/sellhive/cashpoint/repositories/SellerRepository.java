package de.tiedev.sellhive.cashpoint.repositories;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import de.tiedev.sellhive.cashpoint.model.Seller;
import de.tiedev.sellhive.cashpoint.model.SellerState;

public interface SellerRepository extends CrudRepository<Seller, Long> {
	
	public Seller findByExternalId(Long externalId);

	public List<Seller> findBySellerState(SellerState sellerState);	

}
