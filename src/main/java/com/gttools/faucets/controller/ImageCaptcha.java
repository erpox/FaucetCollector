/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main.java.com.gttools.faucets.controller;

import java.awt.Image;
import java.awt.Toolkit;
import java.awt.image.PixelGrabber;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import ru.yandex.qatools.ashot.AShot;
import ru.yandex.qatools.ashot.Screenshot;
import ru.yandex.qatools.ashot.coordinates.WebDriverCoordsProvider;
import ru.yandex.qatools.ashot.shooting.ShootingStrategies;

/**
 *
 * @author Edu
 */
public class ImageCaptcha {

    private final WebDriver driver;
    private ArrayList<Float> imagList;

    public ImageCaptcha(WebDriver driver) {
        this.driver = driver;

    }

    public void takeScreenshot(String imgID) {
        try {
            WebElement myWebElement = driver.findElement(By.id(imgID));
            Screenshot ss = new AShot()
                    .shootingStrategy(ShootingStrategies.viewportPasting(100))
                    .coordsProvider(new WebDriverCoordsProvider())
                    .takeScreenshot(driver, myWebElement);
            ImageIO.write(ss.getImage(), "PNG", new File("c:\\temp\\" + imgID + ".png"));

        } catch (IOException ex) {
            Logger.getLogger(ImageCaptcha.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void getImages() {
        takeScreenshot("visualCaptcha-img-0");
        takeScreenshot("visualCaptcha-img-1");
        takeScreenshot("visualCaptcha-img-2");
        takeScreenshot("visualCaptcha-img-3");
    }

    public void compareImage(String captchaTitle, String imgID, boolean swith) {
        String file;

        if (swith) {
            file = "src/main/resources/imgcaptcha/loginimg/" + captchaTitle + ".png";
        } else {
            file = "src/main/resources/imgcaptcha/rollimg/" + captchaTitle + ".png";
        }

        String file2 = "C:\\temp\\" + imgID + ".png";
        Image image1 = Toolkit.getDefaultToolkit().getImage(file);
        Image image2 = Toolkit.getDefaultToolkit().getImage(file2);
        int counterIgual = 0;

        try {
            PixelGrabber grab1 = new PixelGrabber(image1, 0, 0, -1, -1, false);
            PixelGrabber grab2 = new PixelGrabber(image2, 0, 0, -1, -1, false);

            int[] data1 = null;

            if (grab1.grabPixels()) {
                int width = grab1.getWidth();
                int height = grab1.getHeight();
                data1 = new int[width * height];
                data1 = (int[]) grab1.getPixels();
            }

            int[] data2 = null;

            if (grab2.grabPixels()) {
                int width = grab2.getWidth();
                int height = grab2.getHeight();
                data2 = new int[width * height];
                data2 = (int[]) grab2.getPixels();
            }

            for (int i = 0; i < data2.length; i++) {
                if (data1[i] == data2[i]) {
                    counterIgual++;
                }

            }
            float equals = (counterIgual * 100) / 1024;
            imagList.add(equals);
        } catch (InterruptedException e1) {
            e1.printStackTrace();
        }

    }

    public int solveCatcha(String captchaTitle, boolean swith) {
        getImages();
        imagList = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            compareImage(captchaTitle, "visualCaptcha-img-" + i, swith);
        }
        float max = imagList.get(0);
        int index = 0;

        for (int i = 0; i < imagList.size(); i++) {
            if (max < imagList.get(i)) {
                max = imagList.get(i);
                index = i;
            }
        }
        return index;
    }

}
