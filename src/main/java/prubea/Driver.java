package main.java.prubea;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import static main.java.prubea.GetLinesFromPDF.getPDFLines;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;

/**
 *
 * @author edupe
 */
public class Driver {

    public static ChromeDriver chromeDriver() {
        System.setProperty("webdriver.chrome.driver", "src/main/resources/chromedriver.exe");
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--start-maximized");
        Map<String, Object> prefs = new HashMap<>();
        prefs.put("plugins.always_open_pdf_externally", true);
        options.setExperimentalOption("prefs", prefs);
        options.addExtensions(new File("C:\\Users\\edupe\\Downloads\\extension_2_1_0_0.crx"));
        return new ChromeDriver(options);
    }

    public static FirefoxDriver geckoDriver() {
        System.setProperty("webdriver.gecko.driver", "src/main/resources/geckodriver.exe");
        FirefoxOptions options = new FirefoxOptions();
        options.addPreference("browser.link.open_newwindow.restriction", 0);
        return new FirefoxDriver(options);
    }

    public static ArrayList<String> findData() throws IOException, InterruptedException {
        String lines[] = getPDFLines();
        ArrayList<String> datosRecurso = new ArrayList<>();
        datosRecurso.add(getName(lines));
        datosRecurso.add(getRUT(lines));
        datosRecurso.add(getDomicilio(lines));
        datosRecurso.add(getFecha(lines));

        return datosRecurso;
    }

    private static String getName(String lines[]) {
        String recurrente = "";
        try {
            for (String line : lines) {
                if (line.contains("RECURRENTE:")) {
                    recurrente = line.replace("RECURRENTE: ", "");
                    break;
                }
            }
        } catch (Exception e) {

        }
        return recurrente;
    }

    private static String getRUT(String lines[]) {
        String rut = "";
        try {
            for (String line : lines) {
                if (line.contains("RUT:")) {
                    rut = line.replace("RUT: ", "");
                    break;
                }
            }
        } catch (Exception e) {

        }
        return rut;
    }

    private static String getDomicilio(String lines[]) {
        String domicilio = "";
        try {
            for (int i = 7; i < lines.length; i++) {
                if (lines[i].contains("domicilio")) {
                    domicilio = lines[i + 1].concat(lines[i + 2].concat(lines[i + 3]));
                    domicilio = domicilio.substring(domicilio.lastIndexOf("domicilio ") + 10);
                    domicilio = domicilio.substring(0, domicilio.lastIndexOf(","));
                    break;
                }
            }
        } catch (Exception e) {

        }
        return domicilio;
    }

    public static String getFecha(String lines[]) {
        String fecha = "";
        try {
            for (int i = 30; i < lines.length; i++) {
                if (StringUtils.containsIgnoreCase(lines[i], "Fecha")) {
                    fecha = lines[i].concat(lines[i + 1]);
                    fecha = fecha.toLowerCase();
                    fecha = fecha.substring(fecha.lastIndexOf("fecha ") + 5);
                    System.out.println(fecha);
                    break;
                }
            }
        } catch (Exception e) {
        }
        return fecha;
    }

    public static void moveFiles(String tipoDoc, String name) throws InterruptedException {
        File sourceFile = new File("C:\\Users\\edupe\\Downloads\\Documento.pdf");
        try {
            File destinationFolder = new File("C:\\Users\\edupe\\Downloads\\Excel\\" + name);
            destinationFolder.mkdirs();

            String copyTO = "C:\\Users\\edupe\\Downloads\\Excel\\" + name + "\\" + tipoDoc + "-" + name + ".pdf";

            for (int i = 0; i < 1000; i++) {
                if (sourceFile.exists()) {
                    break;
                } else {
                    Thread.sleep(2000);
                }
            }
            File destinationFile = new File(copyTO);

            FileUtils.moveFile(sourceFile, destinationFile);
            sourceFile.delete();
            if (sourceFile.exists()) {
                sourceFile.delete();
            } else {
            }
        } catch (IOException ex) {
            Logger.getLogger(Driver.class.getName()).log(Level.SEVERE, null, ex);
            sourceFile.delete();
        }
    }

    public static void main(String[] args) throws IOException {

        File sourceFile = new File("C:\\Users\\edupe\\Downloads\\Documento.pdf");

    }
}
