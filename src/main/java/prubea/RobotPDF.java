/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main.java.prubea;

import java.awt.AWTException;
import java.awt.Robot;
import java.awt.event.InputEvent;

public class RobotPDF {
    
    public static void click(int x, int y) throws AWTException{
    Robot bot = new Robot();
    bot.mouseMove(x, y);    
    bot.mousePress(InputEvent.BUTTON1_DOWN_MASK);
    bot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
}
}
