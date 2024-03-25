package org.tbc;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.List;

public class Utils {
    public static boolean checkIfTrue(WebDriver driver) {
        int count = 0;
        WebElement container = driver.findElement(By.xpath("//div[@id='384933']/child::div"));
        List<WebElement> checkEastPoint = container.findElements(By.xpath("./div[@aria-expanded='true']"));
        for (WebElement cinema : checkEastPoint) {
            String element = cinema.findElement(By.xpath(".//p[@class='cinema-title']")).getText();
            if (!element.equals(Constants.eastPoint))
                count++;
        }
        return count == 0;
    }
    public static boolean checkValidity(List<WebElement> lst){
        int count = 0;
        for (WebElement element: lst) {
            if (element.getAttribute("innerHTML").equals(Constants.movieName) || element.getAttribute(
                    "innerHTML").equals(Constants.eastPoint) || element.getAttribute("innerHTML").
                    equals(Constants.movieDate)){
                count++;
            }
        }
        return count > 0;
    }
}
