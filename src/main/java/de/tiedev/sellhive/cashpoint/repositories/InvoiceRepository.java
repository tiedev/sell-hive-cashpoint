package de.tiedev.sellhive.cashpoint.repositories;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import de.tiedev.sellhive.cashpoint.model.Invoice;
import de.tiedev.sellhive.cashpoint.model.Seller;

public interface InvoiceRepository extends CrudRepository<Invoice, Long> {

}
