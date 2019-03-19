package de.tiedev.sellhive.cashpoint.model;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Transient; 

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

/**
 * @Entity
 * @author albers
 *  id
 * 	seller	1
 *	name	"China"
 *	publisher	"AbacusSpiele"
 *	price	1000
 *	boxed_as_new	true
 *	comment	"" ignored
 *	labeled	true ignored
 *	transfered	false ignored
 *	sold	false ignored
 *	state	"labeled" ignored
 */

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
"id",
"seller",
"name",
"publisher",
"boxed_as_new"
})
@JsonIgnoreProperties(ignoreUnknown = true)
public class SellHiveGame {
	
	

	@JsonProperty("id")	
	Long id;


	@JsonProperty("id")	
	public Long getId() {
		return id;
	}

	@JsonProperty("id")	
	public void setId(Long id) {
		this.id = id;
	}

	@JsonProperty("name")
	String name;
	
	@JsonProperty("publisher")
	String publisher;
	
	@JsonProperty("boxed_as_new")
	Boolean boxedAsNew;
	
	String comment;
	
	Boolean labeled;
	
	Boolean transferred;
	
	Boolean sold;
	
	@JsonProperty("price")
	BigDecimal price;
	
	@JsonProperty("barcode_id")
	String barcode;
	
	@JsonProperty("seller")
	Long sellerId;
	
	@JsonProperty("seller")
	public void setSellerId(Long id) {
		this.sellerId = id;
	}
	
	@JsonProperty("seller")
	public Long getSellerId() {
		return sellerId;
	}
	
	@JsonProperty("name")
	public String getName() {
		return name;
	}

	@JsonProperty("name")
	public void setName(String name) {
		this.name = name;
	}

	@JsonProperty("publisher")
	public String getPublisher() {
		return publisher;
	}
	
	@JsonProperty("publisher")
	public void setPublisher(String publisher) {
		this.publisher = publisher;
	}

	@JsonProperty("boxed_as_new")
	public Boolean getBoxedAsNew() {
		return boxedAsNew;
	}

	@JsonProperty("boxed_as_new")
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
		return sold;
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

	public SellHiveGame() {
		super();
	}
	
	public SellHiveGame(String name, String barcode, BigDecimal price, Seller seller) {
		this.name = name;
		this.barcode = barcode;
		this.price = price;
	}
}
