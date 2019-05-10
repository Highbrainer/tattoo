package com.jeevaneo.pdf.tattoo;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.text.DateFormat;
import java.util.Date;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.encryption.InvalidPasswordException;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;
import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.Path;

public class PdfBox {

	private File tattoo;

	public PdfBox() throws IOException {
		try {
			URL url = FileLocator.find(Activator.getContext().getBundle(), new Path("/tattoo.png"));
			URL fileUrl = FileLocator.toFileURL(url);
			tattoo = new File(fileUrl.toURI());
		} catch (URISyntaxException e) {
			throw new RuntimeException(e);
		}
	}

	public static void main(String[] args) throws InvalidPasswordException, IOException, URISyntaxException {
			String in = "./in.pdf";
			String out = "./out.pdf";
			new PdfBox().tattoo(in, out);

	}

	public void tattoo(String in, String out) throws InvalidPasswordException, IOException {
		PDDocument document = null;
		document = PDDocument.load(new File(in));
		PDPage page = (PDPage) document.getDocumentCatalog().getPages().get(0);
		PDFont font = PDType1Font.HELVETICA_BOLD;
		PDPageContentStream contentStream = new PDPageContentStream(
			    document, page, PDPageContentStream.AppendMode.APPEND, true
			);
//		    page.getContents().getStream();
		
		
		PDImageXObject pdImage = PDImageXObject.createFromFileByContent(tattoo, document);
		int imageWidth = 48;
		int imageHeight = 48;
		contentStream.drawImage(pdImage, 50, page.getMediaBox().getHeight()-imageHeight-26, imageWidth, imageHeight);

//		contentStream.beginText();
//		contentStream.setFont(font, 8);
//		contentStream.moveTextPositionByAmount(8,  page.getMediaBox().getHeight()-imageHeight-20);
//		contentStream.setStrokingColor(255, 255, 255);
//		contentStream.drawString("Test d'Alexandre pour Magali");
//		contentStream.endText();
//		
//
//		contentStream.beginText();
//		contentStream.setFont(font, 6);
//		contentStream.moveTextPositionByAmount(8,  page.getMediaBox().getHeight()-imageHeight-30);
//		contentStream.setStrokingColor(255, 255, 255);
//		contentStream.drawString("Le " + DateFormat.getDateInstance(DateFormat.LONG).format(new Date()));
//		contentStream.endText();
		
		contentStream.close();
		document.save(out);
		document.close();
		
		Desktop.getDesktop().open(new File(out));
	}

}
