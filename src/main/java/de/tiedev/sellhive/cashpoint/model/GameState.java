package de.tiedev.sellhive.cashpoint.model;

public enum GameState {
	LABELD("gamestatus.labled"),
	FEE_PAID("gamestatus.fee_paid"),
	SOLD("gamestatus.sold"),
	CHECKEDOUT("gamestatus.checkedout");
	
	private final String status;
	
	GameState(String status) {
		this.status = status;
	}
}
