package de.tiedev.sellhive.cashpoint.services;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.PDPageContentStream.AppendMode;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDType0Font;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import de.tiedev.sellhive.cashpoint.model.Label;

@Component
public class PDFPrintServiceLabelDocument {
	
	float conversionMM2Points = 0.29098f;
	float labelWidth = 680f * conversionMM2Points;
	float labelHeight= 365f * conversionMM2Points;
	float labelInitX = 0f * conversionMM2Points;
	float labelInitY = 2800f * conversionMM2Points;
	float upperLableMargin = 50f * conversionMM2Points;
	float leftLableMargin = 50f * conversionMM2Points;
	float lineHeight = 15f;
	
	@Autowired
	ConfigurationService configurationService;
	

	public String printLabelDocument(List<Label> labels) {
		
		if (configurationService != null) {
			labelInitX = (float) configurationService.getlabelPrintInitX() * conversionMM2Points;
			labelInitY = (float) configurationService.getlabelPrintInitY() * conversionMM2Points;
		}
		String filePrefix = "Label";
		String timeStamp = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss").format(new Date());

		String filename = "./" + filePrefix + timeStamp + ".pdf";
		try (PDDocument doc = new PDDocument()) {

			File ttfFile = new File("src/main/resources/ttf/LiberationSans-Regular.ttf");
			PDType0Font font = PDType0Font.load(doc, ttfFile);

			doc.getDocumentInformation().setTitle("Labels");
			printLabeles(doc, font, labels);
			doc.save(filename);
			doc.close();

		} catch (Exception e) {
			e.printStackTrace(System.out);
		}
		return filename;
	}
	
	float nextX(float x) {
		float nextX = x + labelWidth;
		if ((nextX) <= PDRectangle.A4.getWidth()) {
			return nextX;
		} else {
			return labelInitX;
		}		
	}
	
	float nextY(float y) {
		float nextY = y - labelHeight;
		if (nextY > 0) {
			return nextY;
		} else {
			return labelInitY;
		}
	}
	
	private void printLabeles(PDDocument doc, PDType0Font font, List<Label> labels) {
		// TODO Auto-generated method stub
		PDPageContentStream cont = null;
		List<PDPage> pagesForLabels = new ArrayList<PDPage>();
		PDPage page = null;
		
		float y = labelInitY;
		float x = labelInitX;
		float lableX;
		float lableY;
		try {
			page = createAndAddNewPage(doc);
			for(Label label : labels) {
				lableX = x + leftLableMargin;
				lableY = y;// - upperLableMargin;
				cont = createNewPageStream(doc, font, page, lableX, lableY);
				cont.showText(label.getFirstLine());
				cont.newLine();
				cont.showText(label.getSecondLine());
				cont.newLine();
				cont.showText(label.getThirdLine());
				cont.endText();
				cont.close();
				x = nextX(x);
				if (x == labelInitX) {
					//next line
					y = nextY(y);
					if (y == labelInitY) {
						//next page
						pagesForLabels.add(page);
						page = createAndAddNewPage(doc);
					}
				}
			}
			if (page != null) {
				pagesForLabels.add(page);
			}
		} catch (Exception e) {
			e.printStackTrace(System.out);
		} finally {
		}	

	}

	private PDPage createAndAddNewPage(PDDocument doc) {
		PDPage page = new PDPage(new PDRectangle(PDRectangle.A4.getWidth(), PDRectangle.A4.getHeight()));
		doc.addPage(page);
		return page;
	}
	
	private PDPageContentStream createNewPageStream(PDDocument doc,  PDType0Font font, PDPage page, float x, float y) {
		PDPageContentStream cont = null;
		try {
			cont = new PDPageContentStream(doc, page, AppendMode.APPEND, false);
			cont.beginText();
			cont.setFont(font, 10);
			cont.setLeading(lineHeight);
			cont.newLineAtOffset(x, y);
		} catch (IOException e) {
			e.printStackTrace(System.out);
		}
		return cont;
	}
}
