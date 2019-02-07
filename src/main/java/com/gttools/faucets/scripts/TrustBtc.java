/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main.java.com.gttools.faucets.scripts;

import main.java.com.gttools.faucets.controller.ICaptcha;
import static main.java.com.gttools.faucets.controller.ICaptcha.elementExist;
import static main.java.com.gttools.faucets.controller.ICaptcha.setResponse;
import com.twocaptcha.api.TwoCaptchaService;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;

public class TrustBtc implements ICaptcha {

    private final WebDriver driver;
    private final String loginURL = "https://www.trustbtcfaucet.com/index";
    private final String googleKey = "6LfPqSITAAAAAM9gIuN5ANMeCfiwT743CjiPRvCe";
    private final String pageUrl = "https://www.trustbtcfaucet.com";
    final JavascriptExecutor jse;
    private final TwoCaptchaService service;
    private final String alert = "(.//*[normalize-space(text()) and normalize-space(.)="
            + "'random'])[1]/following::div[1]";

    public TrustBtc(WebDriver driver) {
        this.driver = driver;
        jse = (JavascriptExecutor) driver;
        service = new TwoCaptchaService(null, googleKey, pageUrl);
    }

    public boolean isLogged() {
        try {
            driver.get("https://www.trustbtcfaucet.com/login");
            driver.findElement(By.name("login2"));
            System.out.println("SESION NO INICIADA");
            return false;
            
        } catch (NoSuchElementException e) {
            Logger.getLogger(TrustBtc.class.getName()).log(Level.SEVERE, null, e);
            System.out.println("SESION INICIADA");
            return true;
        }

    }

    public void login() {
        driver.findElement(By.id("username")).clear();
        driver.findElement(By.id("username")).sendKeys("erpox");
        driver.findElement(By.id("password")).clear();
        driver.findElement(By.id("password")).sendKeys("eduardo706");
        setResponse(jse, service);
        driver.findElement(By.name("login2")).click();
        String currentUrl = driver.getCurrentUrl();

        if (currentUrl.contains(loginURL)) {
            System.out.println("SESION INICIADA CORRECTAMENTE");
        } else {
            System.out.println("SESION NO INICIADA, REINTENTADO");
            login();
        }
    }

    public void claim() throws InterruptedException {
        driver.get("https://www.trustbtcfaucet.com/freebitcoin");
        jse.executeScript("flip2();", new Object[0]);
        setResponse(jse, service);
        driver.findElement(By.xpath("(.//*[normalize-space(text()) and normalize-space(.)='Close'])[1]/following::span[1]")).click();

        Thread.sleep(3000);
        if (elementExist(driver, "xPath", alert)) {
            claim();
        } else {
            getPrize();
        }
    }

    public void getPrize() {
        String prizeStr = driver.findElement(By.xpath("(.//*[normalize-space(text()) and normalize-space(.)='Now'])[1]/following::span[1]")).getText();
        int prize = Integer.parseInt(prizeStr);
        System.out.println("You win " + prize + " Satoshis in this row");
    }
}
