package com.foodzy.web.az.core.utils;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDType1Font;

import java.io.IOException;

/**
 * @author Ngoc
 * @since 1.0.0
 */

public class PDDocumentBuilder {

    private PDDocument pdDocument;
    private PDPage pdPage;
    private PDPageContentStream pdPageContentStream;

    public static PDDocumentBuilder from(PDRectangle pdRectangle) throws IOException {
        PDDocumentBuilder builder = new PDDocumentBuilder();
        builder.pdDocument = new PDDocument();
        builder.pdPage = new PDPage(pdRectangle);
        builder.pdPageContentStream = new PDPageContentStream(builder.pdDocument, builder.pdPage);
        return builder;
    }

    public PDDocumentBuilder drawText(String text, float x, float y) throws IOException {
        pdPageContentStream.beginText();
        pdPageContentStream.setFont(PDType1Font.HELVETICA, 12);
        pdPageContentStream.newLineAtOffset(x, y);
        pdPageContentStream.showText(text);
        pdPageContentStream.endText();
        pdPageContentStream.close();  // close to avoid memory leak
        return this;
    }

    // Make sure to add a method to close the document properly
    public PDDocument close() throws IOException {
        pdPageContentStream.close();
        pdDocument.addPage(pdPage);
        pdDocument.save("example.pdf");
        pdDocument.close();
        return pdDocument;
    }
}

