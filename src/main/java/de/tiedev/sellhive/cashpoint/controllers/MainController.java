package de.tiedev.sellhive.cashpoint.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class MainController {
	
	@Autowired
	private DataImportTabController importDataController;
	
	@Autowired
	private CashPointTabController cashPoinTabController;
	
	@Autowired
	private GameCheckinTabController gameCheckinController;

}
