package de.tiedev.sellhive.cashpoint.services;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import de.tiedev.sellhive.cashpoint.model.Game;
import de.tiedev.sellhive.cashpoint.model.GameState;
import de.tiedev.sellhive.cashpoint.model.Invoice;
import de.tiedev.sellhive.cashpoint.model.InvoiceLineItem;
import de.tiedev.sellhive.cashpoint.repositories.InvoiceLineItemRepository;
import de.tiedev.sellhive.cashpoint.repositories.InvoiceRepository;

@Service
public class InvoiceService {

	@Autowired
	GameService gameService;

	@Autowired
	InvoiceLineItemRepository invoiceLineItemRepository;

	@Autowired
	InvoiceRepository invoiceRepository;

	@Autowired
	ReceiptPrintService receiptPrintService;

	public Invoice createInvoice() {
		return new Invoice();
	}

	public void addLineItemToInvoice(Invoice invoice, String barcode) {
		InvoiceLineItem lineItem = null;
		if (invoice != null && !StringUtils.isEmpty(barcode)) {
			Game game = gameService.findByBarcode(barcode);
			lineItem = invoiceLineItemRepository.findByGame(game);
			if (lineItem == null) {
				lineItem = new InvoiceLineItem(game);
				if (!invoice.containsLineItem(lineItem)) {
					invoice.addLineItem(lineItem);
				}
			}
		}
	}

	@Transactional
	public Invoice saveInvoice(Invoice invoice) {
		Invoice returnInvoice = invoiceRepository.save(invoice);
		invoiceLineItemRepository.saveAll(invoice.getLineItems());
		List<Game> games = new ArrayList<Game>();
		Game game;
		for (InvoiceLineItem invoiceLineItem : invoice.getLineItems()) {
			game = invoiceLineItem.getGame();
			game.setSold(Boolean.TRUE);
			game.setGameState(GameState.SOLD);
			games.add(game);
		}
		gameService.save(games);
		return returnInvoice;
	}

	public void saveInvoice(List<Game> games) {
		Invoice invoice = new Invoice();
		if (games != null) {
			InvoiceLineItem invoiceLineItem;
			for (Game game : games) {
				invoiceLineItem = new InvoiceLineItem(game);
				invoice.addLineItem(invoiceLineItem);
				receiptPrintService.print(game.getBarcode() + "\n");
			}
		}
		saveInvoice(invoice);
		receiptPrintService.print(LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME) + "\n\n");
	}

	public void deleteAll() {
		invoiceLineItemRepository.deleteAll();
		invoiceRepository.deleteAll();

	}

}
