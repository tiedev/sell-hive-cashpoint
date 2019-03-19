package de.tiedev.sellhive.cashpoint.services;

import java.util.Arrays;
import java.util.Collection;

import javax.print.Doc;
import javax.print.DocFlavor;
import javax.print.DocPrintJob;
import javax.print.PrintException;
import javax.print.PrintService;
import javax.print.PrintServiceLookup;
import javax.print.SimpleDoc;

import org.springframework.stereotype.Component;

@Component
public class ReceiptPrintService {

	private static final String PRINTER_NAME = "pos-58";

	private PrintService printService;

	public ReceiptPrintService() {
		printService = initPrinter(PRINTER_NAME);
	}

	public void print(String msg) {

		if (printService == null) {
			return;
		}

		DocFlavor flavor = DocFlavor.BYTE_ARRAY.AUTOSENSE;

		Doc doc = new SimpleDoc(msg.getBytes(), flavor, null);

		DocPrintJob job = printService.createPrintJob();

		try {
			job.print(doc, null);
		} catch (PrintException e) {
			e.printStackTrace();
		}
	}

	private static PrintService initPrinter(String name) {
		Collection<PrintService> printers = Arrays.asList(PrintServiceLookup.lookupPrintServices(null, null));

		for (PrintService printer : printers) {
			if (printer.getName().toLowerCase().equals(name)) {
				System.out.println("selected printer '" + name + "'");
				return printer;
			}
		}

		System.err.println("no printer found with name '" + name + "' available are:");
		for (PrintService printer : printers) {
			System.err.println("-> '" + printer.getName().toLowerCase() + "'");
		}
		
		return null;
	}

}
