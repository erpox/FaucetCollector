/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main.java.prubea;

import java.awt.AWTException;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import static main.java.prubea.Driver.chromeDriver;
import static main.java.prubea.Driver.geckoDriver;
import static main.java.prubea.RobotPDF.click;
import static main.java.prubea.StreamTest.download;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.openqa.selenium.By;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.Select;

public class NewClass {

    private static FirefoxDriver driver;
    private static final ArrayList<String> datosColumnas = new ArrayList<>();
    private static String libro;
    private static String recurso;
    private static String carta;
    private static WriteExcel excel;
    private static int couner = 1;

    public static void navigate() throws InterruptedException {
        driver.get("http://corte.poderjudicial.cl/SITCORTEPORWEB/");

        //getDate();
        driver.switchTo().frame("body");
        driver.executeScript("Lengueta('tdNombre');");
        Select selectBox = new Select(driver.findElement(By.xpath("/html/body/form/table[5]/tbody/tr/td/select")));
        selectBox.selectByVisibleText("C.A. de Concepción");
//        driver.executeScript("document.querySelector('#divFecha > "
//                + "table.textoPortal > tbody > tr > td > input:nth-child(2)').value='" + dateIni + "';");
//        driver.executeScript("document.querySelector('#divFecha > "
//                + "table.textoPortal > tbody > tr > td > input:nth-child(4)').value='" + dateFin + "';");
        driver.executeScript("document.querySelector('#divNombre > table.textoPortal "
                + "> tbody > tr > td > input:nth-child(1)').value='CRUZ BLANCA';");
        driver.executeScript("AtPublicoPpalForm.irAccionAtPublico.click();");
    }

    public static void geTable() throws InterruptedException {
        Thread.sleep(3000);
        //DBConnection.createConnection();
        String html = driver.getPageSource();
        Document doc = Jsoup.parse(html);
        Elements table = doc.select("#divRecursos > table");

        Elements rows = table.select("tr");
        for (int i = 1; i < rows.size(); i++) {
            Elements list = rows.get(i).getElementsByTag("td");
            //int primeKey = proccesPk(list.get(0).text(), i);
            //primKey.add(new Roles(primeKey, list.get(0).text()));           
            //DBConnection.recursosTableQuery(list, primeKey);
            String href = list.get(0).toString();
            Pattern p = Pattern.compile("\"([^\"]*)\"");
            Matcher m = p.matcher(href);
            m.find();
            String url = m.group(1).replace("amp;", "");
            System.out.println(url);
            //getHistoryTable(primeKey, url);

        }
    }

    public static void getHistoryTable(Document doc) throws InterruptedException {
        Elements table = doc.select("#divHistoria > table");
        Elements rows = table.select("tr");
        Elements columns = rows.get(rows.size() - 1).select("td");
        String tipoTramite = columns.get(6).text();
        String enlaceDoc;
        if (tipoTramite.contains("Recurso")) {
            enlaceDoc = getRecursoURL(columns.get(2).toString());
            if (!enlaceDoc.contains("existe")) {
                download(enlaceDoc, libro.replace(" ", "-"), "Recursos");
            } else {
                getRecursoFromTexto();
            }
        }
        litigantesTable(doc);
    }

    public static String getRecursoURL(String href) {
        Pattern p = Pattern.compile("\"([^\"]*)\"");
        Matcher m = p.matcher(href);
        while (m.find()) {
            if (m.group(1).contains("Show")) {
                href = m.group(1);
                break;
            } else {
                href = "no existe documento";
            }
        }
        href = href.replace("ShowPDF('", "").replace("')", "").replace("amp;", "");
        return href;
    }

    public static void litigantesTable(Document doc) {
        Elements table = doc.select("#divLitigantes > table");
        Elements rows = table.select("tr");
        String nombreRecurrido = "", nombreRecurrente = "", nombreAbgRecurrente = "",
                rutRecurrido = "", rutRecurrente = "", rutAbRecurrente = "",
                tipoRecurrido = "", tipoRecurrente = "", tipoAbRecurrente = "";
        for (int i = 1; i < rows.size(); i++) {
            Elements column = rows.get(i).select("td");
            String sujeto = column.get(0).text();
            // System.out.println(column.get(0).text().equals("Recurrido"));
            if (StringUtils.containsIgnoreCase(sujeto, "Ab")) {
                tipoAbRecurrente = column.get(1).text();
                rutAbRecurrente = column.get(2).text();
                nombreAbgRecurrente = column.get(3).text();
            } else if (sujeto.equalsIgnoreCase("Recurrido")
                    || sujeto.equalsIgnoreCase("Reqdo.")) {
                tipoRecurrido = column.get(1).text();
                rutRecurrido = column.get(2).text();
                nombreRecurrido = column.get(3).text();
            } else if (sujeto.equalsIgnoreCase("Recurrente")) {
                tipoRecurrente = column.get(1).text();
                rutRecurrente = column.get(2).text();
                nombreRecurrente = column.get(3).text();
            }

        }
        datosColumnas.add(tipoAbRecurrente);
        datosColumnas.add(rutAbRecurrente);
        datosColumnas.add(nombreAbgRecurrente);
        datosColumnas.add(tipoRecurrido);
        datosColumnas.add(rutRecurrido);
        datosColumnas.add(nombreRecurrido);
        datosColumnas.add(tipoRecurrente);
        datosColumnas.add(rutRecurrente);
        datosColumnas.add(nombreRecurrente);

    }

    public static void atencionPublic(String link) throws InterruptedException {
        driver.get("http://corte.poderjudicial.cl/" + link);
        Document doc = StreamTest.testLink(link);
        Elements table = doc.select("#recurso > table");
        Elements rows = table.select("tr");
        Elements row1 = rows.get(1).select("td");
        libro = row1.get(1).text().replace("Libro : ", "");
        datosColumnas.add(libro);
        datosColumnas.add(row1.get(2).text().replace("Estado Recurso: ", ""));
        datosColumnas.add(row1.get(3).text().replace("Fecha : ", ""));
        Elements row2 = rows.get(3).select("td");
        datosColumnas.add(row2.get(1).text().replace("Estado Procesal: ", ""));
        Elements row3 = rows.get(4).select("td");
        datosColumnas.add(row3.get(0).text().replace("Recurso : ", ""));
        if (row3.get(1).text().contains("Doc")) {
            if (!rows.get(1).toString().contains("existe")) {
                getCartaFromDoc();
            }
        }
        getHistoryTable(doc);
    }

    private static void getCartaFromDoc() throws InterruptedException {

        try {
            driver.executeScript("ShowPopUpRecDocs(9);");
            windowHadler(1);
            Thread.sleep(1000);
            click(27, 118);
            Thread.sleep(1000);
            windowHadler(2);
            String urlArchivo = driver.getCurrentUrl().replace("http://corte.poderjudicial.cl", "");
            download(urlArchivo, libro.replace(" ", "-"), "Cartas");
            backMainWindow();
        } catch (AWTException ex) {
            Logger.getLogger(NewClass.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ArrayIndexOutOfBoundsException ex) {
            System.out.println("Sin archivo ");
            backMainWindow();

        }
    }

    private static void getRecursoFromTexto() throws InterruptedException {
        try {
            driver.executeScript("ShowPopUpCabecera('1')");
            windowHadler(1);
            Thread.sleep(1000);
            click(17, 152);
            Thread.sleep(1000);
            windowHadler(2);
            String urlArchivo = driver.getCurrentUrl().replace("http://corte.poderjudicial.cl", "");
            download(urlArchivo, libro.replace(" ", "-"), "Cartas");
            backMainWindow();
        } catch (AWTException ex) {
            Logger.getLogger(NewClass.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ArrayIndexOutOfBoundsException ex) {
            System.out.println("Sin archivo ");
            backMainWindow();

        }
    }

    public static void windowHadler(int windowIndex) throws ArrayIndexOutOfBoundsException {

        String[] handle = driver.getWindowHandles().toArray(new String[2]);
        driver.switchTo().window(handle[windowIndex]);
    }

    public static void backMainWindow() {
        String[] handle = driver.getWindowHandles().toArray(new String[2]);
        try {
            driver.switchTo().window(handle[1]).close();
            driver.switchTo().window(handle[2]).close();
            driver.switchTo().window(handle[0]);
        } catch (ArrayIndexOutOfBoundsException e) {
            driver.switchTo().window(handle[0]);
        }
    }

    public static void fillExcel(String resourceURL1) {
        recurso = "HYPERLINK(\"Cruz_Blanca\\Recursos\\" + libro.replace(" ", "-") + ".pdf\",\"Recurso\")";
        carta = "HYPERLINK(\"Cruz_Blanca\\Cartas\\" + libro.replace(" ", "-") + ".pdf\",\"Carta\")";

        datosColumnas.add(recurso);
        datosColumnas.add(carta);

        excel.createRow(datosColumnas, couner, resourceURL1);
    }

    public static void main(String[] args) throws InterruptedException, FileNotFoundException {
        LinkedHashSet<String> resourceURL = StreamTest.readFile();
        driver = geckoDriver();
        excel = new WriteExcel();
        try {
            resourceURL.stream().forEach((resourceURL1) -> {

                try {
                    atencionPublic(resourceURL1);
                    fillExcel(resourceURL1);
                    datosColumnas.clear();
                    couner++;
                } catch (InterruptedException | java.lang.NullPointerException ex) {
                    Logger.getLogger(NewClass.class.getName()).log(Level.SEVERE, null, ex);
                }

            });
        } catch (Exception e) {
            excel.writeExcelFile();
        }
        excel.writeExcelFile();
    }
}
