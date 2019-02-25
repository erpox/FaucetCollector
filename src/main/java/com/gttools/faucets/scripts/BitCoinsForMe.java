/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main.java.com.gttools.faucets.scripts;

import com.twocaptcha.api.TwoCaptchaService;
import java.io.IOException;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import static main.java.com.gttools.faucets.connection.DBConnection.updateQuery;
import main.java.com.gttools.faucets.controller.ICaptcha;
import static main.java.com.gttools.faucets.controller.ICaptcha.setResponse;
import main.java.com.gttools.faucets.controller.Main;
import static main.java.com.gttools.faucets.controller.Main.getJse;
import static main.java.com.gttools.faucets.controller.Main.serviceKey;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptException;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;

/**
 *
 * @author edupe
 */
public class BitCoinsForMe implements ICaptcha {

    private final String rollStr = "/html/body/div[1]/div/div[2]/div[2]/div[1]/div[2]/div/strong";
    private final String googleKey = "6Le_ER0UAAAAAMmcPIT_saW5Pv9UKJtaFpjeWHeV";
    private final String pageURL = "https://bitcoinsfor.me";
    private final WebDriver driver;
    private final String userPass;
    private final String userEmail;
    private final TwoCaptchaService service;

    public BitCoinsForMe(WebDriver driver, String userPass, String userEmail) {
        this.service = new TwoCaptchaService(serviceKey, googleKey, pageURL);
        this.driver = driver;
        this.userPass = userPass;
        this.userEmail = userEmail;

    }

    public boolean isLogged() {
        driver.get("https://bitcoinsfor.me/user.php");
        return !driver.getCurrentUrl().contains("login");
    }

    public void login() {
        driver.findElement(By.name("email")).clear();
        driver.findElement(By.name("email")).sendKeys(userEmail);
        driver.findElement(By.name("password")).clear();
        driver.findElement(By.name("password")).sendKeys(userPass);
        setResponse(Main.getJse(), service);
        driver.findElement(By.name("login")).click();

        if (!driver.getCurrentUrl().contains("user")) {
            login();
        } else {
            driver.findElement(By.xpath("/html/body/div[2]/div/div/div/div[3]/div[2]/a/div/div[1]/span")).click();
        }
    }

    public void claim() {

        setResponse(Main.getJse(), service);
        openAdvertise();
        driver.findElement(By.id("nobutton-92423")).click();

        if (isElementPresent(By.xpath(rollStr))) {
            getBalances();
        } else {
            claim();
        }
    }

    public void getBalances() {
        String balanceStr = driver.findElement(By.xpath("/html/body/nav/div/div[1]/ul/li/a")).getText();
        balanceStr = balanceStr.replace("Balance: ", "").trim();
        double balance = Double.parseDouble(balanceStr);
        balance = balance * 100000000;

        String rollS = driver.findElement(By.xpath(rollStr)).getText();
        int roll = Integer.parseInt(rollS);

        postBalance((int) balance, roll);
    }

    private void postBalance(int balance, int roll) {
        String query = "UPDATE APP.USER_FAUCET "
                + "SET BALANCE=" + balance
                + "WHERE USER_ID='" + 1 + "' AND FAUCET_ID=3";
        updateQuery(query);
    }

    private boolean isElementPresent(By by) {
        try {
            driver.findElement(by);
            return true;
        } catch (NoSuchElementException e) {
            return false;
        }
    }

    public static void main(String[] args) throws InterruptedException, IOException {
        Main mai = new Main();
        mai.setUp();
        BitCoinsForMe me = new BitCoinsForMe(mai.driver, "Ag22858496", "adelia@airmailten.com");
        if (me.isLogged()) {

        } else {
            me.login();
            me.claim();
        }

    }

    private void openAdvertise() {
        try {
            getJse().executeScript("cca_pop_click();", new Object[0]);

            String[] handle = driver.getWindowHandles().toArray(new String[2]);
            driver.switchTo().window(handle[1]);
            driver.close();
            Thread.sleep(4000);
            driver.switchTo().window(handle[0]);
        } catch (JavascriptException ex) {
            System.err.println("PUBLICIDAD NO ACTIVA");
        } catch (InterruptedException ex) {
            Logger.getLogger(BitCoinsForMe.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
