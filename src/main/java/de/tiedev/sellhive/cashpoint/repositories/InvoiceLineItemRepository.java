package de.tiedev.sellhive.cashpoint.repositories;

import org.springframework.data.repository.CrudRepository;

import de.tiedev.sellhive.cashpoint.model.Game;
import de.tiedev.sellhive.cashpoint.model.InvoiceLineItem;

public interface InvoiceLineItemRepository extends CrudRepository<InvoiceLineItem, Long> {

	public InvoiceLineItem findByGame(Game game);

}
