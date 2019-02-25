/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main.java.prubea;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class WriteExcel {

    private final XSSFWorkbook workbook;
    private final XSSFSheet spreadsheet;
    private XSSFRow row;

    public WriteExcel() {
        this.workbook = new XSSFWorkbook();
        this.spreadsheet = workbook.createSheet("Recurso 2018");
        createHeader();
    }

    private void createHeader() {
        spreadsheet.getLastRowNum();
        row = spreadsheet.createRow(0);
        String colunmNames[] = new String[]{"ROL", "CORTE", "CARATULADO", "FECHA INGRESO",
            "ESTADO CAUSA", "FECHA UBICACION", "UBICACION", "INSTITUCION", "NOMBRE",
            "RUT", "DIRECCION", "FECHA", "CARTA", "RECURSO"};
        int colId = 0;
        for (String colunmName : colunmNames) {
            row.createCell(colId++).setCellValue(colunmName);
        }
    }

    public void createRow(String recurso[], int i) {
        row = spreadsheet.createRow(i);
        int colId = 0;
        for (String recurso1 : recurso) {
            row.createCell(colId++).setCellValue(recurso1);
        }
        row.createCell(12).setCellFormula(recurso[12]);
        row.createCell(13).setCellFormula(recurso[13]);
    }

    public void createRow(ArrayList<String> recurso, int i, String link) {
        row = spreadsheet.createRow(i);
        int colId = 0;
        for (String recurso1 : recurso) {
            row.createCell(colId++).setCellValue(recurso1);
        }
        row.createCell(14).setCellFormula(recurso.get(14));
        row.createCell(15).setCellFormula(recurso.get(15));
        row.createCell(16).setCellFormula("HYPERLINK(\"http://corte.poderjudicial.cl" + link + "\",\"PJUD\")");
    }

    public void writeExcelFile() {
        FileOutputStream out = null;
        try {
            out = new FileOutputStream(new File("C:\\Users\\edupe\\Documents\\MEGAsync\\ISAPRE\\CRUZBLANCA.xlsx"));
            workbook.write(out);
            out.close();
            System.out.println("Writesheet.xlsx written successfully");
        } catch (FileNotFoundException ex) {
            Logger.getLogger(WriteExcel.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(WriteExcel.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                out.close();
            } catch (IOException ex) {
                Logger.getLogger(WriteExcel.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

}
