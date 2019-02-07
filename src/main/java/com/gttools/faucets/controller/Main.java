/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main.java.com.gttools.faucets.controller;

import main.java.com.gttools.faucets.scripts.MulticoinFaucet;
import main.java.com.gttools.faucets.scripts.TrustBtc;
import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import main.java.com.gttools.faucets.scripts.SatoshiHero;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

/**
 *
 * @author Edu
 */
public class Main {

    WebDriver driver;
    TrustBtc trus;

    public void setUp() throws InterruptedException, IOException {
        System.setProperty("webdriver.chrome.driver", "C:\\Users\\Edu\\Documents\\chromedriver.exe");
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--start-maximized");
        driver = new ChromeDriver(options);
        driver.manage().timeouts().implicitlyWait(15, TimeUnit.SECONDS);

        trus = new TrustBtc(driver);
    }

    public void runTrustBTC() {
        final Timer timerReloj = new Timer();
        final TimerTask ttReloj = new TimerTask() {

            @Override
            public void run() {
                try {
                    if (trus.isLogged()) {
                        trus.claim();
                    } else {
                        trus.login();
                        trus.claim();
                    }
                } catch (InterruptedException ex) {
                    Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        };
        timerReloj.schedule(ttReloj, 1000, 600000);
    }

    public void runMultiFaucet() {
        MulticoinFaucet multi = new MulticoinFaucet(driver);

        if (multi.isLogged()) {
            multi.doRoll(multi.diceURL, multi.slideDice);
            multi.doRoll(multi.faucetURL, multi.slideFaucet);
        } else {
            multi.login();
            multi.doRoll(multi.diceURL, multi.slideDice);
            multi.doRoll(multi.faucetURL, multi.slideFaucet);
        }
    }

    public void rinSatoshiHero() {
        SatoshiHero sh = new SatoshiHero(driver);
        try {
            sh.login();
            sh.doClaim();
        } catch (InterruptedException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void main(String[] args) throws InterruptedException, IOException {

        Main maini = new Main();
        maini.setUp();
       //  maini.runTrustBTC();
        // maini.runMultiFaucet();
        maini.rinSatoshiHero();

    }
}
