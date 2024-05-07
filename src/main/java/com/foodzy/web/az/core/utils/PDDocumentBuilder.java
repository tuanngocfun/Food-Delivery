package com.foodzy.web.az.core.utils;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDType1Font;

import java.io.IOException;

/**
 * Utility class to build and manipulate PDF documents.
 * Provides a fluent interface for creating PDFs and drawing text elements.
 * 
 * @author Ngoc
 * @since 1.0.0
 */
public class PDDocumentBuilder {

    private PDDocument pdDocument;
    private PDPage pdPage;
    private PDPageContentStream pdPageContentStream;

    private PDDocumentBuilder() throws IOException {
        this.pdDocument = new PDDocument();
    }

    public static PDDocumentBuilder from(PDRectangle pdRectangle) throws IOException {
        PDDocumentBuilder builder = new PDDocumentBuilder();
        builder.pdPage = new PDPage(pdRectangle);
        builder.pdPageContentStream = new PDPageContentStream(builder.pdDocument, builder.pdPage, PDPageContentStream.AppendMode.APPEND, true, true);
        return builder;
    }

    /**
     * Adds text to the PDF at the specified position.
     * 
     * @param text the text to add
     * @param x the x-coordinate where the text should start
     * @param y the y-coordinate where the text should start
     * @return this builder instance for chaining
     * @throws IOException if writing to the page content stream fails
     */
    public PDDocumentBuilder drawText(String text, float x, float y) throws IOException {
        pdPageContentStream.beginText();
        pdPageContentStream.setFont(PDType1Font.HELVETICA, 12);
        pdPageContentStream.newLineAtOffset(x, y);
        pdPageContentStream.showText(text);
        pdPageContentStream.endText();
        return this;
    }

    /**
     * Saves the document to a specified path and closes it.
     * 
     * @param path the file path to save the document
     * @throws IOException if saving or closing the document fails
     */
    public void saveAndClose(String path) throws IOException {
        try {
            pdPageContentStream.close();
            pdDocument.addPage(pdPage);
            pdDocument.save(path);
        } finally {
            pdDocument.close();
        }
    }
}
