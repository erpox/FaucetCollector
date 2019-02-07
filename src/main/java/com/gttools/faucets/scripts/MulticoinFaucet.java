package main.java.com.gttools.faucets.scripts;

import main.java.com.gttools.faucets.controller.ImageCaptcha;
import java.io.IOException;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;

public class MulticoinFaucet {

    private final WebDriver driver;
    private final String loginURL = "https://www.multicoinfaucet.com/btc-faucet/login.php";
    public final String diceURL = "https://multicoinfaucet.com/btc-faucet/x87";
    public final String faucetURL = "https://multicoinfaucet.com/btc-faucet/roll";
    public final String slideDice = "(.//*[normalize-space"
            + "(text()) and normalize-space(.)='Slide To Roll'])[1]/following::div[1]";
    public final String slideFaucet = "(.//*[normalize-space"
            + "(text()) and normalize-space(.)='Slide To Claim'])[1]/following::div[1]";
    private String currentURL;
    private final ImageCaptcha service;

    public MulticoinFaucet(WebDriver driver) {
        this.driver = driver;
        this.service = new ImageCaptcha(driver);
    }

    public boolean isLogged() {
        driver.get(loginURL);
        currentURL = driver.getCurrentUrl();
        if (currentURL.equals(loginURL)) {
            return false;
        } else {
            return true;
        }
    }

    public boolean login() {
        driver.findElement(By.id("username")).sendKeys("erpox");
        driver.findElement(By.id("password")).sendKeys("eduardo706");
        solveCaptcha(true);
        driver.findElement(By.name("login")).click();

        currentURL = driver.getCurrentUrl();
        if (!currentURL.equals(loginURL)) {
            return true;
        } else {
            return false;
        }
    }

    public void solveCaptcha(boolean swith) {
        String captchaTitle = driver.findElement(By.xpath("(.//*[normalize-"
                + "space(text()) and normalize-space(.)='answer'])[1]/following::strong[1]")).getText();

        captchaTitle = captchaTitle.substring(captchaTitle.lastIndexOf(" ") + 1);
        int imgID = service.solveCatcha(captchaTitle, swith);
        driver.findElement(By.id("visualCaptcha-img-" + imgID)).click();
    }

    public void doRoll(String url, String slideTag) {
        driver.get(url);
        solveCaptcha(false);
        WebElement slider = driver.findElement(By.xpath(slideTag));
        Actions move = new Actions(driver);
        move.dragAndDropBy(slider, 311, 0).build().perform();
    }
}
