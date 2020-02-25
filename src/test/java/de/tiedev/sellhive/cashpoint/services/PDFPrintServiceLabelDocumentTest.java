package de.tiedev.sellhive.cashpoint.services;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import de.tiedev.sellhive.cashpoint.model.Label;

@RunWith(SpringJUnit4ClassRunner.class)
public class PDFPrintServiceLabelDocumentTest {
	PDFPrintServiceLabelDocument pdfPrintServiceLabelDocument = new PDFPrintServiceLabelDocument();
	List<Label> labels;
	
	@Before
	public void setUp() {
		labels = new ArrayList<Label>();
		for (int i = 1; i<=40; i++) {
			labels.add(new Label("Label " + i));
		}
	}
	
	@Test
	public void testPrintLabelDocument() {
		pdfPrintServiceLabelDocument.printLabelDocument(labels);
	}
}
