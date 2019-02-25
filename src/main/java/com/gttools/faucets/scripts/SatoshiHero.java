/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main.java.com.gttools.faucets.scripts;

import com.twocaptcha.api.TwoCaptchaService;
import javax.swing.JOptionPane;
import main.java.com.gttools.faucets.controller.ICaptcha;
import static main.java.com.gttools.faucets.controller.ICaptcha.elementExist;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;

/**
 *
 * @author Edu
 */
public class SatoshiHero implements ICaptcha {

    private final WebDriver driver;
    private final String pageURL = "https://satoshihero.com/en/login";
    private String currentURL;
    private final String loginBtn = "(.//*[normalize-space(text()) and normalize-"
            + "space(.)='Forgot your password?'])[1]/following::input[1]";
    private final String getFreeBtc = "Get free bitcoins now";
    private final String tryAgain = "Try my luck again";
    private final String rwrdLabel = "xpath=(.//*[normalize-space(text()) and "
            + "normalize-space(.)='Congrats!'])[1]/following::span[1]";
    private JavascriptExecutor jse;
    private final TwoCaptchaService service;

    public SatoshiHero(WebDriver driver) {
        this.driver = driver;

        service = new TwoCaptchaService("key", "6LdzSxwUAAAAABzV0lDJ6T1LxprFtn2g053NE05R", "https://satoshihero.com");
        jse = (JavascriptExecutor) driver;
    }

    public void login() throws InterruptedException {
        driver.get(pageURL);
        driver.findElement(By.name("email")).clear();
        driver.findElement(By.name("email")).sendKeys("eduper706@gmail.com");
        driver.findElement(By.name("password")).clear();
        driver.findElement(By.name("password")).sendKeys("eduardo706");
        Thread.sleep(8000);
        driver.findElement(By.xpath(loginBtn)).click();
        Thread.sleep(5000);
        if (elementExist(driver, "xPath", loginBtn)) {
            System.err.println("SESION NO INICIADA");
            login();
        } else {
            System.out.println("SESION INICIADA");
        }
    }

    public void doClaim() {
        if (elementExist(driver, "linkText", getFreeBtc)) {
            driver.findElement(By.linkText(getFreeBtc)).click();
            solveCaptcha();
            JOptionPane.showConfirmDialog(null, "Ready?");
            driver.findElement(By.linkText("Continue to claim")).click();

        } else if (elementExist(driver, "linkTex", tryAgain)) {
            driver.findElement(By.linkText(tryAgain)).click();
            driver.findElement(By.linkText(getFreeBtc)).click();
            //solveCapthce method call
            JOptionPane.showConfirmDialog(null, "Ready?");
            driver.findElement(By.linkText("Continue to claim")).click();
        } else {

        }

    }

    public synchronized void afterClaim() {

    }

    public void solveCaptcha() {

        jse.executeScript("document.getElementById('g-recaptcha-response')"
                + ".style='block';", new Object[0]);
        // jse.executeScript("recaptchaSolved", new Object[0]);
    }
}
