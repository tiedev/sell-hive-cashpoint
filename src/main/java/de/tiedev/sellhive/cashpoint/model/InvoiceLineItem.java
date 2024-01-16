package de.tiedev.sellhive.cashpoint.model;

import java.math.BigDecimal;

import jakarta.persistence.*;

@Entity
public class InvoiceLineItem {

	@Id
	@SequenceGenerator(name="INVOICE_LINE_ITEM_SEQ", sequenceName="invoice__line_item_id_seq")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="INVOICE_LINE_ITEM_SEQ")
	Long id;
	
	String name;
	
	String barcode;
	
	BigDecimal price;
	
	@OneToOne
	@JoinColumn(name = "Game_id")
	Game game;

	@ManyToOne
	@JoinColumn(name = "Invoice_id")
	Invoice invoice;
	
	public InvoiceLineItem() {
		super();
	}
	
	public InvoiceLineItem(Game game) {
		super();
		this.game = game;
		barcode = game.getBarcode();
		name = game.getName();
		price = game.getPrice();
	}

	public Game getGame() {
		return game;
	}
	
	public void setGame(Game game) {
		this.game = game;
	}
	
	public void setInvoice(Invoice invoice) {
		this.invoice = invoice;
	}
	
	public Invoice getInvoice() {
		return invoice;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getName() {
		return name;
	}
	public void setPrice(BigDecimal price) {
		this.price = price;
	}
	
	public BigDecimal getPrice() {
		return price;
	}
	
	public void setBarcode(String barcode) {
		this.barcode = barcode;
	}
	
	public String getBarcode() {
		return barcode;
	}

	@Override
	public boolean equals(Object that) {
		if (this == that) return true;
		if(!(that instanceof InvoiceLineItem)) return false;
		InvoiceLineItem lineItem = (InvoiceLineItem) that;
		return this.getBarcode().equals(lineItem.getBarcode());
	}
	
}
