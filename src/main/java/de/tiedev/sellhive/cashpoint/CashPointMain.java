package de.tiedev.sellhive.cashpoint;

import javafx.application.Application;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@SpringBootApplication
public class CashPointMain {

//	private static final Logger log = LoggerFactory.getLogger(CashPointMain.class);

	public static void main(String[] args) {
		Application.launch(CashPointFxApplication.class, args);
/*		Wird das noch benötigt?
System.exit(0);*/
	}

}
