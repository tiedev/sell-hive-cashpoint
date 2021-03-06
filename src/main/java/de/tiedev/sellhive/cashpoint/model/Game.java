package de.tiedev.sellhive.cashpoint.model;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @Entity
 * @author albers
 */
@Entity
public class Game {
	
	
	@Id
	@SequenceGenerator(name="GAME_SEQ", sequenceName="game_id_seq")	
	@GeneratedValue(strategy=GenerationType.IDENTITY, generator="GAME_SEQ")
	Long id;


	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	
	Long externalId;

	public Long getExternalId() {
		return externalId;
	}

	public void setExternalId(Long id) {
		this.externalId = id;
	}

	String name;
	
	String publisher;
	
	Boolean boxedAsNew;
	
	String comment;
	
	Boolean labeled;
	
	Boolean transferred;
	
	Boolean sold;
	
	BigDecimal price;
	
	GameState gameState;
	
	@Column(unique=true)
	String barcode;
	
	Long externalSellerId;
	
	@Transient
	Long sellerId;
	
	
	@ManyToOne
	@JoinColumn(name = "Seller_id")
	Seller seller;

	@OneToOne(optional = true)
	@JoinColumn(name = "InvoiceLineItem_id")
	InvoiceLineItem lineItem;
	
	BigDecimal fee;

	public void setExternalSellerId(Long externaSellerlId) {
		this.externalSellerId = externalSellerId;
	}
	
	public Long getExternalSellerId() {
		return externalSellerId;
	}
	
	public void setSellerId(Long id) {
		this.sellerId = id;
	}
	
	public Long getSellerId() {
		return sellerId;
	}

	public Seller getSeller() {
		return seller;
	}
	
	public void setSeller(Seller seller) {
		this.seller = seller;
	}
	
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPublisher() {
		return publisher;
	}
	
	public void setPublisher(String publisher) {
		this.publisher = publisher;
	}

	public Boolean getBoxedAsNew() {
		return boxedAsNew;
	}

	public void setBoxedAsNew(Boolean boxedAsNew) {
		this.boxedAsNew = boxedAsNew;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public Boolean getLabeled() {
		return labeled;
	}

	public void setLabeled(Boolean labeled) {
		this.labeled = labeled;
	}

	public Boolean getTransferred() {
		return transferred;
	}

	public void setTransferred(Boolean transferred) {
		this.transferred = transferred;
	}

	public Boolean isSold() {
		return sold != null ? sold : Boolean.FALSE;
	}

	public void setSold(Boolean sold) {
		this.sold = sold;
	}

	@JsonProperty("price")
	public BigDecimal getPrice() {
		return price;
	}

	@JsonProperty("price")
	public void setPrice(BigDecimal price) {
		this.price = price;
	}

	public String getBarcode() {
		return barcode;
	}

	public void setBarcode(String barcode) {
		this.barcode = barcode;
	}

	
	public void setFee(BigDecimal fee) {
		this.fee = fee;
	}
	
	public BigDecimal getFee() {
		return fee != null ? fee : new BigDecimal(0.5);
	}
	
	public void setGameState(GameState gameState) {
		this.gameState = gameState;
	}
	
	public GameState getGameState() {
		return gameState;
	}
	public Game() {
		super();
	}
	
	public Game(String name, String barcode, BigDecimal price, Seller seller) {
		this.name = name;
		this.barcode = barcode;
		this.price = price;
		this.seller = seller;
	}
	
	@Override
	public boolean equals(Object anObject) {
		if (anObject != null && anObject instanceof Game) {
			Game game = (Game) anObject;
			return barcode.equals(game.getBarcode());
		}
		return false;
	}
}
