package de.tiedev.sellhive.cashpoint.model;

public enum SellerState {
	INACTIVE("sellerstate.inactive"),
	CHECKEDIN("sellerstate.checkedin"),
	CHECKEDOUT("sellerstate.checkedout");
		
	private final String status;
		
	SellerState(String status) {
		this.status = status;
	}

}
