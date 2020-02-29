package de.tiedev.sellhive.cashpoint.services;

import java.math.BigDecimal;

import org.springframework.stereotype.Service;

@Service
public class ConfigurationService {
String importURLSeller = "https://sellhive.tealtoken.de/backend/cashpoint/export/sellers/PaewvtScieMpKDmeHvG82g3OX1U4YN1L";
String importURLGames = "https://sellhive.tealtoken.de/backend/cashpoint/export/items/PaewvtScieMpKDmeHvG82g3OX1U4YN1L";

BigDecimal fee1fee = new BigDecimal(0.5);

int fee2NumberOfGames = 30;
BigDecimal fee2fee = new BigDecimal(0.8);

public String getImportURLSeller() {
	return importURLSeller;
}
public void setImportURLSeller(String importURLSeller) {
	this.importURLSeller = importURLSeller;
}
public String getImportURLGames() {
	return importURLGames;
}
public void setImportURLGames(String importURLGames) {
	this.importURLGames = importURLGames;
}

public BigDecimal getFee1fee() {
	return fee1fee;
}
public void setFee1fee(BigDecimal fee1fee) {
	this.fee1fee = fee1fee;
}
public int getFee2NumberOfGames() {
	return fee2NumberOfGames;
}
public void setFee2NumberOfGames(int fee2NumberOfGames) {
	this.fee2NumberOfGames = fee2NumberOfGames;
}
public BigDecimal getFee2fee() {
	return fee2fee;
}

public void setFee2fee(BigDecimal fee2fee) {
	this.fee2fee = fee2fee;
}
public boolean hasFee1() {
	return (fee1fee != null) && fee1fee != BigDecimal.ZERO;
}

public boolean hasFee2() {
	return (fee2fee != null) && fee2fee != BigDecimal.ZERO;
}
}
