/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main.java.prubea;

import java.io.File;
import java.io.IOException;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.apache.pdfbox.text.PDFTextStripperByArea;

/**
 * This is an example on how to extract text line by line from pdf document
 */
public class GetLinesFromPDF {
    
    private static String lines[];
    
    public static String[] getPDFLines() throws IOException, InterruptedException {
        File file = new File("C:\\Users\\edupe\\Downloads\\Documento.pdf");
        
        for (int i = 0; i < 1000; i++) {
            if (file.exists()) {
                break;
            } else {
                Thread.sleep(2000);
            }
        }
        try (PDDocument document = PDDocument.load(file)) {
            
            document.getClass();
            
            if (!document.isEncrypted()) {
                
                PDFTextStripperByArea stripper = new PDFTextStripperByArea();
                stripper.setSortByPosition(true);
                
                PDFTextStripper tStripper = new PDFTextStripper();
                
                String pdfFileInText = tStripper.getText(document);
                //System.out.println("Text:" + st);

                // split by whitespace
                lines = pdfFileInText.split("\\r?\\n");
                document.close();
            }
            
        }
        
        return lines;
        
    }
}
