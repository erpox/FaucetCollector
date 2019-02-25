
package main.java.com.gttools.faucets.scripts;

import main.java.com.gttools.faucets.controller.ICaptcha;
import static main.java.com.gttools.faucets.controller.ICaptcha.*;
import com.twocaptcha.api.TwoCaptchaService;
import java.util.logging.Level;
import java.util.logging.Logger;
import static main.java.com.gttools.faucets.connection.DBConnection.updateQuery;
import main.java.com.gttools.faucets.controller.Main;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;

public class TrustBtc implements ICaptcha {

    private final String loginURL = "https://www.trustbtcfaucet.com/index";
    private final String googleKey = "6LfPqSITAAAAAM9gIuN5ANMeCfiwT743CjiPRvCe";
    private final String pageUrl = "https://www.trustbtcfaucet.com";
    private final String alert = "(.//*[normalize-space(text()) and normalize-space(.)="
            + "'random'])[1]/following::div[1]";
    private final WebDriver driver;
    private final JavascriptExecutor jse;
    private final TwoCaptchaService service;

    private final String userName;
    private final String userPass;

    public TrustBtc(WebDriver driver, String userName, String userPass) {
        this.userName = userName;
        this.userPass = userPass;
        this.driver = driver;
        jse = (JavascriptExecutor) driver;
        service = new TwoCaptchaService(Main.serviceKey, googleKey, pageUrl);
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
        driver.findElement(By.id("username")).sendKeys(userName);
        driver.findElement(By.id("password")).clear();
        driver.findElement(By.id("password")).sendKeys(userPass);
        setResponse(jse, service);
        driver.findElement(By.name("login2")).click();
        String currentUrl = driver.getCurrentUrl();

        if (currentUrl.contains(loginURL)) {
            System.out.println("SESION INICIADA CORRECTAMENTE");
            updateBalance();
        } else {
            System.out.println("SESION NO INICIADA, REINTENTADO");
            login();
        }
    }

    public void claim() throws InterruptedException {
        driver.get("https://www.trustbtcfaucet.com/freebitcoin");
        jse.executeScript("flip2();", new Object[0]);
        setResponse(jse, service);
        driver.findElement(By.xpath("(.//*[normalize-space(text()) and "
                + "normalize-space(.)='Close'])[1]/following::span[1]")).click();

        Thread.sleep(3000);
        if (afterClaim()) {
            claim();
        } else {
            postBalances();
        }
    }

    public boolean afterClaim() {
        String alertStr = driver.findElement(By.xpath(alert)).getText();
        if (alertStr.contains("cannot")) {
            return true;
        } else {
            return false;
        }

    }

    public void postBalances() {
        String amountStr = driver.findElement(By.xpath("(.//*[normalize-space(text()) "
                + "and normalize-space(.)='Now'])[1]/following::span[1]")).getText();
        int amount = Integer.parseInt(amountStr);

        String query = "UPDATE APP.FAUCETS "
                + "SET FAUCET_USER_BALANCE=" + getBalance()
                + "WHERE USER_NAME='" + userName + "' AND FAUCET_NAME='TrustBTC'";
        updateQuery(query);
    }

    public void updateBalance() {

    }

    public double getBalance() {
        String balanceStr = driver.findElement(By.id("balance_global")).getText();
        double balanceDou = Double.parseDouble(balanceStr);
        return balanceDou * 100000000;
    }
}
