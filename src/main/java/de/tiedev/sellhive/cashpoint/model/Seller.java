package de.tiedev.sellhive.cashpoint.model;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Seller {

	@Id
	@SequenceGenerator(name="SELLER_SEQ", sequenceName="seller_id_seq")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SELLER_SEQ")
	Long id;

	Long externalId;
	
	String name;
	
	SellerState sellerState;
	
	BigDecimal feePaid;

	@OneToMany(mappedBy = "seller", cascade = CascadeType.ALL)
	List<Game> games;

	
	public Long getId() {
		return id;
	}
	
	public void setId(Long id) {
		this.id = id;
	}
	
	public Long getExternalId() {
		return externalId;
	}
	
	public void setExternalId(Long id) {
		this.externalId = id;
	}
	
	public void setSellerState(SellerState sellerState) {
		this.sellerState = sellerState;
	}
	
	public SellerState getSellerState() {
		return sellerState == null ? SellerState.INACTIVE : sellerState;
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
    public List<Game> getGames() {
        return games;
    }

    public void addGame(Game game) {
    	if(games != null) {
    		games.add(game);
    	} else {
    		games = new ArrayList<Game>();
    		games.add(game);
    	}
    }
	public Seller() {
		super();
	}
	
	public Seller(String name) {
		this.name = name;
	}
	
    @Override
    public String toString() {
        return "Quote{" +
                "type='" + id + '\'' +
                ", value=" + name +
                '}';
    }

	public void setFeePaid(BigDecimal feePaid) {
		this.feePaid = feePaid;
		
	}

	public BigDecimal getFeePaid() {
		return feePaid;
	}
	
}
