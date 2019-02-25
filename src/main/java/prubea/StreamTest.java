/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main.java.prubea;

import java.io.BufferedInputStream;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.Scanner;
import java.util.TreeSet;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

/**
 *
 * @author edupe
 */
public class StreamTest {

    private static final String link = "http://corte.poderjudicial.cl";

    public static ByteArrayInputStream getPDF(String partialLink, String name, String tipo) {
        String outputFile = "C:\\Users\\edupe\\Downloads\\ISAPRE\\Cruz_Blanca\\" + tipo + "\\" + name + ".pdf";
        try {
            Connection.Response response = Jsoup.connect(link + partialLink)
                    .ignoreContentType(true)
                    .execute();
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            BufferedInputStream inputStream = response.bodyStream();
            byte[] buffer = new byte[1024];
            int len;
            while ((len = inputStream.read(buffer)) != -1) {
                baos.write(buffer, 0, len);
            }
            ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
            IOUtils.copy(bais, new FileOutputStream(outputFile));
            return bais;
        } catch (IOException ex) {

        }
        return null;
    }

    public static Document testLink(String partialLink) {

        Document doc = null;
        try {
            doc = Jsoup.connect(link + partialLink).userAgent("Mozilla/5.0").timeout(10000).get();
        } catch (IOException ex) {
            Logger.getLogger(StreamTest.class.getName()).log(Level.SEVERE, null, ex);
        }

        return doc;

    }

    public static LinkedHashSet<String> readFile() throws FileNotFoundException {
        LinkedHashSet<String> resourceURL = new LinkedHashSet<>();
        Scanner in = new Scanner(new File("C:\\Users\\edupe\\Documents\\CRUZ_BLANCA.txt"));

        while (in.hasNextLine()) {
            String agentNum = in.nextLine();
            resourceURL.add(agentNum);
        }
        return resourceURL;
    }

    public static void download(String partialLink, String name, String tipo)  {

        try {
            String outputFile = "C:\\Users\\edupe\\Documents\\MEGAsync\\ISAPRE\\Cruz_Blanca\\" + tipo + "\\" + name + ".pdf";
            
            System.out.println("opening connection");
            URL url = new URL(link + partialLink);
            InputStream in = url.openStream();
            FileOutputStream fos = new FileOutputStream(new File(outputFile));
            
            System.out.println("reading from resource and writing to file...");
            int length = -1;
            byte[] buffer = new byte[1024];// buffer for portion of data from connection
            while ((length = in.read(buffer)) > -1) {
                fos.write(buffer, 0, length);
            }
            fos.close();
            in.close();
            System.out.println("File downloaded");
        } catch (IOException ex) {
            Logger.getLogger(StreamTest.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public static void readFileTCT() throws FileNotFoundException {
        int mes = 0;
        ArrayList<String> resourceURL = new ArrayList<>();
        Scanner in = new Scanner(new File("C:\\Users\\edupe\\Documents\\prueba.txt"));

        while (in.hasNextLine()) {
            String agentNum = in.nextLine();
            resourceURL.add(agentNum);
        }
        for (String resourceURL1 : resourceURL) {
            if (!StringUtils.contains(resourceURL1, "N/D")) {
                if (StringUtils.containsIgnoreCase(resourceURL1, "Enero")) {
                    mes = 1;
                }
                if (StringUtils.containsIgnoreCase(resourceURL1, "Febrero")) {
                    mes = 2;
                }
                if (StringUtils.containsIgnoreCase(resourceURL1, "Marzo")) {
                    mes = 3;
                }
                if (StringUtils.containsIgnoreCase(resourceURL1, "Abril")) {
                    mes = 4;
                }
                if (StringUtils.containsIgnoreCase(resourceURL1, "Mayo")) {
                    mes = 5;
                }
                if (StringUtils.containsIgnoreCase(resourceURL1, "Junio")) {
                    mes = 6;
                }
                if (StringUtils.containsIgnoreCase(resourceURL1, "Julio")) {
                    mes = 7;
                }
                if (StringUtils.containsIgnoreCase(resourceURL1, "Agosto")) {
                    mes = 8;
                }
                if (StringUtils.containsIgnoreCase(resourceURL1, "Septiembre")) {
                    mes = 9;
                }
                if (StringUtils.containsIgnoreCase(resourceURL1, "Octubre")) {
                    mes = 10;
                }
                if (StringUtils.containsIgnoreCase(resourceURL1, "Noviembre")) {
                    mes = 11;
                }
                if (StringUtils.containsIgnoreCase(resourceURL1, "Diciembre")) {
                    mes = 12;
                }
                try {
                    resourceURL1 = resourceURL1.replaceAll("\\D+", "");
                    String dia = resourceURL1.substring(0, 1);
                    String años = resourceURL1.substring(2, resourceURL1.length());
                    System.out.println(dia + "/" + mes + "/" + años);
                } catch (StringIndexOutOfBoundsException e) {
                    System.out.println(resourceURL1);
                }
            } else {
                System.out.println(resourceURL1);
            }

        }

    }

    public static void main(String[] args) throws FileNotFoundException {
        download("/SITCORTEPORWEB/DownloadFile.do?TIP_Documento=2&TIP_Archivo=3&COD_Opcion=1&COD_Corte=30&CRR_IdEscrito=10513720&CRR_IdDocEscrito=6622996",
                "Cartapruasdasdebsa", "Cartas");
    }
}
