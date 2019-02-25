package main.java.prubea;

import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import static main.java.prubea.Driver.findData;
import static main.java.prubea.Driver.chromeDriver;
import static main.java.prubea.Driver.moveFiles;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.chrome.ChromeDriver;

public class Intranet {

    private final ChromeDriver driver;
    private final String oficinaVirtual = "https://oficinajudicialvirtual.pjud.cl/";
    private ArrayList<String> datosExtras;
    private String rol;
    private String corte;
    private String caratulado;
    private String fechaIng;
    private String estado;
    private String fechaUbic;
    private String ubicacion;
    private String institucion;
    private String nombre;
    private String rut;
    private String direccion;
    private String fecha;
    private String carta;
    private String recurso;
    private final WriteExcel writeExcel;
    private int couner;

    public Intranet() {
        this.couner = 1;
        this.driver = chromeDriver();
        this.writeExcel = new WriteExcel();
    }

    public void logIntranet() throws InterruptedException {
        driver.get(oficinaVirtual);
        driver.findElement(By.id("cunica")).click();
        driver.findElement(By.id("inputUsuario")).sendKeys("16765101-1");
        driver.findElement(By.id("inputPassword")).sendKeys("Egedialvaro83");
        driver.findElement(By.xpath("//*[@id=\"formClaveUnica\"]/form/fieldset/"
                + "div[5]/div/div[1]/div[3]/div/button")).click();
        driver.switchTo().frame("a3");
        driver.executeScript("buqxrut();");
    }

    public void doSearch(String fechaIni, String fechaFin) throws InterruptedException {
        driver.switchTo().defaultContent();
        driver.switchTo().frame("a4");
        
        driver.findElement(By.id("ui-id-2")).click();
        driver.executeScript("document.querySelector('#inicioApelaciones').value='" + fechaIni + "'");
        driver.executeScript("document.querySelector('#terminoApelaciones').value='" + fechaFin + "'");
        driver.findElement(By.id("filtroFechaApelaciones")).click();
    }

    public void readFromApelacionesTable() throws InterruptedException, IOException, NoSuchElementException {
        Thread.sleep(6000);
        String Html = driver.findElement(By.id("fragment-2")).getAttribute("outerHTML");
        Document doc = Jsoup.parseBodyFragment(Html);
        Elements table = doc.select("#fragment-2 > table");
        Elements rows = table.select("tr");

        System.out.println(rows.size());
        for (int i = 1; i < rows.size(); i++) {
            Elements column = rows.get(i).select("td");
            rol = column.get(1).text();
            corte = column.get(2).text();
            caratulado = column.get(3).text();
            fechaIng = column.get(4).text();
            estado = column.get(5).text();
            fechaUbic = column.get(6).text();
            ubicacion = column.get(7).text();
            institucion = column.get(8).text();
            openRecurso(i + 1);
            fillExcel();
            couner++;

            if (i == 10) {
                break;
            }
        }

        try {

            driver.findElement(By.id("corte_siguiente")).click();
            readFromApelacionesTable();
        } catch (Exception e) {
            Logger.getLogger(Intranet.class.getName()).log(Level.SEVERE, null, e);
            System.out.println("FIN de la tabla alcanzado");
        }
    }

    public void openRecurso(int index) throws InterruptedException, IOException {
        try {
            driver.findElement(By.xpath("//*[@id=\"fragment-2\"]/table/tbody/tr[" + index + "]/td[1]/form/input[8]")).click();
            windowHadler(1);

            String Html = driver.findElement(By.id("tabs-1")).getAttribute("outerHTML");
            Document doc = Jsoup.parseBodyFragment(Html);
            Elements table = doc.select("#tabs-1 > table");

            Elements rows = table.select("tr");

            Element lastRow = rows.get(rows.size() - 1);
            Elements colunm = lastRow.select("td");
            String script = colunm.get(1).toString();
            driver.executeScript(getFuntion(script));
            Thread.sleep(3000);
            datosExtras = findData();

            moveFiles("Recurso", rol);
            nombre = datosExtras.get(0);
            rut = datosExtras.get(1);
            direccion = datosExtras.get(2);
            fecha = datosExtras.get(3);
        } catch (Exception e) {
            nombre = "N/D";
            rut = "N/D";
            direccion = "N/D";
            fecha = "N/D";

            Logger.getLogger(Intranet.class.getName()).log(Level.SEVERE, null, e);
        }
        saveCarta();
    }

    public void saveCarta() throws InterruptedException {
        try {
            driver.executeScript(openCarta());
            windowHadler(2);
            driver.findElement(By.xpath("/html/body/div/div/table/tbody[2]/tr/td[1]/form/input[2]")).click();
            moveFiles("Carta", rol);
        } catch (Exception e) {
            Logger.getLogger(WriteExcel.class.getName()).log(Level.SEVERE, null, e);
        }
        backMainWindow();
        driver.switchTo().frame("a4");
    }

    public void fillExcel() {
        carta = "HYPERLINK(\"" + rol + "\\Carta-" + rol + ".pdf\",\"Carta-" + rol + "\")";
        recurso = "HYPERLINK(\"" + rol + "\\Recurso-" + rol + ".pdf\",\"Recurso-" + rol + "\")";

        String recur[] = new String[]{
            rol, corte, caratulado, fechaIng, estado, fechaUbic,
            ubicacion, institucion, nombre, rut, direccion, fecha, carta, recurso};

        writeExcel.createRow(recur, couner);
    }

    public String openCarta() {
        String Html = driver.findElement(By.id("tabs-1")).getAttribute("outerHTML");
        Document doc = Jsoup.parseBodyFragment(Html);
        Elements table = doc.select("#tabs-1 > table");
        Elements rows = table.select("tr");
        Elements column = rows.get(3).select("td");
        String popUp = column.get(1).toString();
        popUp = popUp.substring(popUp.indexOf("onclick=\"") + 9);
        popUp = popUp.substring(0, popUp.lastIndexOf(";") + 1);
        return popUp;
    }

    public void doiy() {
        writeExcel.writeExcelFile();
    }

    public static void main(String[] args) throws InterruptedException, IOException {
        Intranet intra = new Intranet();
        intra.logIntranet();
        intra.doSearch("01/11/2018", "01/01/2019");
        intra.readFromApelacionesTable();
        intra.doiy();

    }

    private String getFuntion(String data) throws IllegalStateException {
        Pattern p = Pattern.compile("\"([^\"]*)\"");
        Matcher m = p.matcher(data);
        m.find();
        m.find();
        return m.group(1);

    }

    public void windowHadler(int windowIndex) {
        String[] handle = driver.getWindowHandles().toArray(new String[3]);
        driver.switchTo().window(handle[windowIndex]);
    }

    public void backMainWindow() throws InterruptedException {
        String[] handle = driver.getWindowHandles().toArray(new String[2]);
        try {
            driver.switchTo().window(handle[1]).close();
            driver.switchTo().window(handle[2]).close();
            driver.switchTo().window(handle[0]);
        } catch (ArrayIndexOutOfBoundsException e) {
            driver.switchTo().window(handle[0]);
        }
    }

}
