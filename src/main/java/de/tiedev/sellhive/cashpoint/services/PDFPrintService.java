package de.tiedev.sellhive.cashpoint.services;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.PDPageContentStream.AppendMode;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDType0Font;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import de.tiedev.sellhive.cashpoint.model.SellerSettlement;

@Component
public class PDFPrintService {

	private static String LINE = "--------------------------------------------------------------------------------------";
	private static String SIGNATURE_TEXT = "Datum/Unterschrift:";
	private static Integer MAX_LINES_PER_PAGE = 50;


	public String printSettlement(SellerSettlement sellerSettlement, boolean finalSettlement) throws IOException {

		String filePrefix = finalSettlement ? "Einzelabrechnung_" : "Einzelauswertung_";
		String timeStamp = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss").format(new Date());

		String sellernameForFileName = StringUtils.replace(sellerSettlement.getSellerName(), " ", "-");
		String filename = "./" + filePrefix + sellerSettlement.getSellerExternalId() + "_" + sellernameForFileName + "_"
				+ timeStamp + ".pdf";
		try (PDDocument doc = new PDDocument()) {

			InputStream ttfStream = getClass().getResourceAsStream("/ttf/LiberationSans-Regular.ttf");
//			File ttfFile = new File("ttf/LiberationSans-Regular.ttf");
			PDType0Font font = PDType0Font.load(doc, ttfStream);

			doc.getDocumentInformation().setTitle("Einzelabrechnung für " + sellerSettlement.getSellerName());
			printSingleSettlement(doc, font, sellerSettlement);
			addPageNumbers(doc, font);
			doc.save(filename);
			doc.close();

		} catch (Exception e) {
			e.printStackTrace(System.out);
		}
		return filename;
	}

	public String printSettlements(List<SellerSettlement> sellerSettlements, boolean finalSettlement) {
		String filename = finalSettlement ? "Sammelabrechnung_" : "Sammelauswertung_";
		String timeStamp = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss").format(new Date());
		filename = "./" + filename + timeStamp + ".pdf";
		try (PDDocument doc = new PDDocument()) {

			File ttfFile = new File("src/main/resources/ttf/LiberationSans-Regular.ttf");
			PDType0Font font = PDType0Font.load(doc, ttfFile);

			doc.getDocumentInformation().setTitle("Sammelabrechnung");

			for (SellerSettlement sellerSettlement : sellerSettlements) {
				printSingleSettlement(doc, font, sellerSettlement);
			}
			addPageNumbers(doc, font);
			doc.save(filename);
			doc.close();
		} catch (Exception e) {
			e.printStackTrace(System.out);
		}
		return filename;

	}

	private void printSingleSettlement(PDDocument doc, PDType0Font font, SellerSettlement sellerSettlement) {
		Integer lineCount = 0;
		PDPageContentStream cont = null;
		List<PDPage> pagesForSeller = new ArrayList<PDPage>();
		PDPage page;
		try {
			page = createAndAddNewPage(doc);
			cont = createNewPageStream(doc, font, page);
			pagesForSeller.add(page);
			String content = "";
			content = sellerSettlement.getHeader();
			cont.showText(content);
			cont.newLine();
			cont.showText(LINE);
			cont.newLine();
			cont.showText("VERKAUFTE SPIELE");
			cont.newLine();
			lineCount = 3;
			for (String s : sellerSettlement.getGamesSold()) {
				cont.showText(s);
				cont.newLine();
				lineCount++;
				if (lineCount == MAX_LINES_PER_PAGE) {
					cont.endText();
					cont.close();
					page = createAndAddNewPage(doc);
					cont = createNewPageStream(doc, font, page);
					pagesForSeller.add(page);
					lineCount = 0;
				}
			}

			cont.showText(LINE);
			cont.newLine();
			lineCount++;
			if (lineCount >= (MAX_LINES_PER_PAGE - 4)) {
				cont.endText();
				cont.close();
				page = createAndAddNewPage(doc);
				cont = createNewPageStream(doc, font, page);
				pagesForSeller.add(page);
				lineCount = 0;
			}
			cont.showText("NICHT VERKAUFTE SPIELE");
			cont.newLine();
			lineCount++;
			for (String s : sellerSettlement.getGamesNotSold()) {
				cont.showText(s);
				cont.newLine();
				lineCount++;
				if (lineCount == MAX_LINES_PER_PAGE) {
					cont.endText();
					cont.close();
					page = createAndAddNewPage(doc);
					cont = createNewPageStream(doc, font, page);
					pagesForSeller.add(page);
					lineCount = 0;
				}
			}
			// proving if still enough space left on the page to print the footer
			if (lineCount >= (MAX_LINES_PER_PAGE - 5 - sellerSettlement.getFooter().size())) {
				cont.endText();
				cont.close();
				page = createAndAddNewPage(doc);
				cont = createNewPageStream(doc, font, page);
				pagesForSeller.add(page);
				lineCount = 0;
			}
			cont.showText(LINE);
			cont.newLine();
			for (String str : sellerSettlement.getFooter()) {
				cont.showText(str);
				cont.newLine();
			}
			cont.endText();
			cont.close();
			page = createAndAddNewPage(doc);
			cont = createNewPageStream(doc, font, page);
			pagesForSeller.add(page);
			lineCount = 0;
			content = sellerSettlement.getHeader();
			cont.showText(content);
			cont.newLine();
			cont.showText(LINE);
			cont.newLine();

			for (String str : sellerSettlement.getFooter()) {
				cont.showText(str);
				cont.newLine();
			}
			cont.newLine();
			cont.showText("Ich bestätige, dass die angegebene Summe an mich ausgezahlt wurde");
			cont.newLine();
			cont.showText("und ich meine nicht verkauften Spiele zurück erhalten habe.");
			cont.newLine();
			cont.newLine();
			cont.newLine();
			cont.newLine();
			cont.showText(SIGNATURE_TEXT);
			cont.newLine();
			cont.showText(LINE);
			cont.newLine();
			cont.endText();
			cont.close();
			addPageNumbersForSeller(doc, font, pagesForSeller);
		} catch (Exception e) {
			e.printStackTrace(System.out);
		} finally {
		}
	}

	private void printSummary(PDDocument doc, PDType0Font font, List<SellerSettlement> sellerSettlements) {
		Integer lineCount = 0;
		Integer offsetAtPageBottom = 0;
		PDPageContentStream cont = null;
		PDPage page;
		try {
			page = createAndAddNewPage(doc);
			cont = createNewPageStream(doc, font, page);
			cont.newLine();
			cont.newLine();
			cont.showText("Zusammenfassung inkl. Unterschriften");
			cont.newLine();
			cont.showText(LINE);
			cont.newLine();
			lineCount = 2;
			for (SellerSettlement sellerSettlement : sellerSettlements) {
				cont.showText(sellerSettlement.getHeader());
				cont.newLine();
				for (String str : sellerSettlement.getFooter()) {
					cont.showText(str);
					cont.newLine();
				}
				cont.showText(SIGNATURE_TEXT);
				cont.newLine();
				cont.showText(LINE);
				cont.newLine();
				offsetAtPageBottom = 3 + sellerSettlement.getFooter().size();
				lineCount = lineCount + offsetAtPageBottom;
				if (lineCount > (MAX_LINES_PER_PAGE - offsetAtPageBottom)) {
					cont.endText();
					cont.close();
					page = createAndAddNewPage(doc);
					cont = createNewPageStream(doc, font, page);
					lineCount = 0;
				}
			}
			cont.endText();
			cont.close();
		} catch (Exception e) {
			e.printStackTrace(System.out);
		} finally {
		}

	}

	private void addPageNumbersForSeller(PDDocument doc, PDType0Font font, List<PDPage> pages) {
		int pageNumber = 1;
		int numberOfPages = pages.size();
		PDPageContentStream cont;
		try {
			for (PDPage page : pages) {
				cont = new PDPageContentStream(doc, page, AppendMode.APPEND, false);
				cont.beginText();
				cont.setFont(font, 10);
				cont.setLeading(14.5f);
				cont.newLineAtOffset(240, 810);
				cont.showText("Verkäuferseite " + pageNumber + " von " + numberOfPages);
				cont.endText();
				cont.close();
				pageNumber++;
			}
		} catch (IOException e) {
			e.printStackTrace(System.out);
		}
	}

	private void addPageNumbers(PDDocument doc,  PDType0Font font) {
		int numberOfPages = doc.getNumberOfPages();
		int pageNumber = 1;
		PDPageContentStream cont = null;
		LocalDateTime dateTime = LocalDateTime.now();
		DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy - HH:mm:ss");
		String dateAsString = dateTime.format(dateTimeFormatter);
		try {
			for (PDPage page : doc.getPages()) {
				cont = new PDPageContentStream(doc, page, AppendMode.APPEND, false);
				cont.beginText();
				cont.setFont(font, 10);
				cont.setLeading(14.5f);
				cont.newLineAtOffset(280, 40);
				cont.showText("Seite " + pageNumber + " von " + numberOfPages);
				cont.endText();
				cont.beginText();
				cont.setFont(font, 10);
				cont.setLeading(14.5f);
				cont.newLineAtOffset(260, 26);
				cont.showText(dateAsString);
				cont.endText();				
				cont.close();
				pageNumber++;
			}

		} catch (IOException e) {
			e.printStackTrace(System.out);
		}
	}

	private PDPage createAndAddNewPage(PDDocument doc) {
		PDPage page = new PDPage(new PDRectangle(PDRectangle.A4.getWidth(), PDRectangle.A4.getHeight()));
		doc.addPage(page);
		return page;
	}

	private PDPageContentStream createNewPageStream(PDDocument doc,  PDType0Font font, PDPage page) {
		PDPageContentStream cont = null;
		try {
			cont = new PDPageContentStream(doc, page, AppendMode.APPEND, false);
			cont.beginText();
			cont.setFont(font, 10);
			cont.setLeading(14.5f);
			cont.newLineAtOffset(45, 790);
		} catch (IOException e) {
			e.printStackTrace(System.out);
		}
		return cont;
	}

}
