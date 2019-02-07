/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main.java.com.gttools.faucets.controller;

import com.twocaptcha.api.TwoCaptchaService;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;

/**
 *
 * @author Edu
 */
public interface ICaptcha {

    /**
     *
     * @param jse
     * @param service
     */
    public static void setResponse(JavascriptExecutor jse, TwoCaptchaService service) {
        try {
            String res = service.solveCaptcha();
            jse.executeScript("document.getElementById('g-recaptcha-response')"
                    + ".value='" + res + "';", new Object[0]);

        } catch (InterruptedException | IOException ex) {
            Logger.getLogger(ICaptcha.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static boolean elementExist(WebDriver driver, String locator, String element) {
        try {
            switch (locator) {
                case "xPath":
                    driver.findElement(By.xpath(element));
                    return true;
                case "linkText":
                    driver.findElement(By.linkText(element));
                    return true;
                default:
                    return true;
            }

        } catch (Exception e) {
            System.err.println(" --- Element doesnt exist ---");
            return false;
        }
    }

    public static void dataCallBack() {

    }
}
