package de.tiedev.sellhive.cashpoint.services;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import de.tiedev.sellhive.cashpoint.Constants;


@Service
public class ConfigurationService {
String importURLSeller = "https://sellhive.tealtoken.de/backend/cashpoint/export/sellers/thD8lPGfvnkyFaRHKXmsAQ4YLTM6ZcjE";
String importURLGames = "https://sellhive.tealtoken.de/backend/cashpoint/export/items/thD8lPGfvnkyFaRHKXmsAQ4YLTM6ZcjE";
String exportURLSoldStatus = "https://sellhive.tealtoken.de/backend/cashpoint/confirm/sold/thD8lPGfvnkyFaRHKXmsAQ4YLTM6ZcjE";

BigDecimal feeBase = new BigDecimal(0.5);

int fee2NumberOfGames = 30;

BigDecimal fee2fee = new BigDecimal(0.8);

Boolean sellingConfirmationWithReturn;

int labelPrintInitX = 0;
int labelPrintInitY = 2800;

@Autowired
Environment environment;

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
	return feeBase;
}
public void setFee1fee(BigDecimal fee1fee) {
	this.feeBase = fee1fee;
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
	return (feeBase != null) && !BigDecimal.ZERO.equals(feeBase);
}

public boolean hasFee2() {
	return (fee2fee != null) && !BigDecimal.ZERO.equals(fee2fee);
}

public Boolean isSellingConfirmationWithReturn() {
	if (sellingConfirmationWithReturn == null) {
		sellingConfirmationWithReturn = environment.getProperty(Constants.AP_SELLING_CONFIRMATION_WITH_RETURN, Boolean.class);
	}
	return sellingConfirmationWithReturn;
}
public void setSellingConfirmationWithReturn(Boolean sellingConfirmationWithReturn) {
	this.sellingConfirmationWithReturn = sellingConfirmationWithReturn;
	
}

public int getlabelPrintInitX() {
	return labelPrintInitX;
}

public int getlabelPrintInitY() {
	return labelPrintInitY;
}
public void setlabelPrintInitX(int labelPrintInitX) {
	this.labelPrintInitX = labelPrintInitX;	
}

public void setlabelPrintInitY(int labelPrintInitY) {
	this.labelPrintInitY = labelPrintInitY;	
}

public String getBarcodePrefixForMultipleSoldArticles() {
	return environment.getProperty(Constants.AP_BARCODE_PREFIX_FOR_MULTIPLE_SOLD_ARTICLES, String.class);
}

public String getBarcodePriceSeparatorForMultipleSoldArticles() {
	return environment.getProperty(Constants.AP_BARCODE_PRICESEPARATOR_FOR_ULTIPLE_SOLD_ARTICLES, String.class);
}

public long getVhsSellerID() {
	return environment.getProperty(Constants.AP_VHS_SELLERID, long.class);
}
public String getExportURLSoldStatus() {
	return exportURLSoldStatus;
}
}
