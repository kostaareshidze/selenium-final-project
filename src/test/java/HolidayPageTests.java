import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.tbc.Constants;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import java.util.List;

public class HolidayPageTests {
    WebDriver driver;
    WebDriverWait wait;

    @BeforeClass
    @Parameters("browser")
    public void setup(String browser) {
        if (browser.equalsIgnoreCase("chrome")) {
            WebDriverManager.chromedriver().setup();
            driver = new ChromeDriver();
        } else if (browser.equalsIgnoreCase("firefox")) {
            WebDriverManager.firefoxdriver().setup();
            driver = new FirefoxDriver();
        } else if (browser.equalsIgnoreCase("edge")) {
            WebDriverManager.edgedriver().setup();
            driver = new EdgeDriver();
        }
        driver.manage().window().maximize();
        wait = new WebDriverWait(driver, 10);

    }

    @AfterClass
    public void tearDown() {
        driver.close();
    }

    @Test
    public void descendingOrderTest() {
        driver.get(Constants.swoopURL);
        WebElement rest = driver.findElement(By.xpath("//li[@class='MoreCategories'][3]"));
        rest.click();
        Select dropDownMenu = new Select(driver.findElement(By.id("sort")));
        dropDownMenu.selectByValue(Constants.selectValueOne);
        wait.until(ExpectedConditions.stalenessOf(driver.findElement(By
                .xpath(Constants.sameXpath))));
        WebElement container = driver.findElement(By
                .xpath(Constants.sameXpath));
        List<WebElement> listOfOffers = container.findElements(By.
                xpath("//div[@class='special-offer']//div[@class='discounted-prices']"));
        String maxPrice = listOfOffers.get(0).findElement(By.
                xpath(".//p[@class='deal-voucher-price' and not(@style)]")).getText();
        //above we take first elements price as you see, we will use it for validation
        List<Integer> prices = listOfOffers.stream().map(element -> element.findElement(By.
                        xpath(".//p[@class='deal-voucher-price' and not(@style)]")).getText())
                .map(element -> element.substring(0, element.length() - 1))
                .map(Integer::parseInt).toList();
        int maxPriceByList = 0;
        for (Integer integer : prices) {
            if (integer > maxPriceByList) {
                maxPriceByList = integer;
            }
        }
        String maxPriceToString = maxPriceByList + Constants.GEL;
        Assert.assertEquals(maxPriceToString, maxPrice);
    }

    @Test
    public void ascendingOrderTest() {
        driver.get(Constants.swoopURL);
        WebElement rest = driver.findElement(By.xpath("//li[@class='MoreCategories'][3]"));
        rest.click();
        Select dropDownMenu = new Select(driver.findElement(By.id("sort")));
        dropDownMenu.selectByValue(Constants.selectValueTwo);
        wait.until(ExpectedConditions.stalenessOf(driver.findElement(By
                .xpath(Constants.sameXpath))));
        WebElement container = driver.findElement(By
                .xpath(Constants.sameXpath));
        List<WebElement> listOfOffers = container.findElements(By.
                xpath("//div[@class='special-offer']//div[@class='discounted-prices']"));
        String minPrice = listOfOffers.get(0).findElement(By.
                xpath(".//p[@class='deal-voucher-price' and not(@style)]")).getText();
        //above we take first elements price as you see, we will use it for validation
        List<Integer> prices = listOfOffers.stream().map(element -> element.findElement(By.
                        xpath(".//p[@class='deal-voucher-price' and not(@style)]")).getText())
                .map(element -> element.substring(0, element.length() - 1))
                .map(Integer::parseInt).toList();
        int minValue = prices.get(0);
        for (Integer integer : prices) {
            if (integer < minValue) {
                minValue = integer;
            }
        }
        String minPriceToString = minValue + "₾";
        Assert.assertEquals(minPriceToString, minPrice);
        //this will fail because when I order with ascending minimum value is not first offer
    }

    @Test
    public void filterTest(){
        driver.get(Constants.swoopURL);
        WebElement rest = driver.findElement(By.xpath("//li[@class='MoreCategories'][3]"));
        rest.click();
        WebElement checkBox = driver.findElement(By.
                xpath("//div[@id='sidebar-container']//input[@type='checkbox' and @value='2']"));
        if (!checkBox.isSelected())
            checkBox.click();
        WebElement container = driver.findElement(By.
                xpath(Constants.sameXpath));
        List<WebElement> offers = container.findElements(By.
                xpath(".//div[@class='special-offer-text special-gruoped-text']/p/a"));
        for (WebElement offer : offers) {
            Assert.assertTrue(offer.getText().contains(Constants.cottage));
        }
        Select dropDownMenu = new Select(driver.findElement(By.id("sort")));
        dropDownMenu.selectByValue(Constants.selectValueTwo);
        wait.until(ExpectedConditions.stalenessOf(container));
        container = driver.findElement(By.
                xpath(Constants.sameXpath));
        List<WebElement> listOfOffers = container.findElements(By.
                xpath("//div[@class='special-offer']//div[@class='discounted-prices']"));
        String minPrice = listOfOffers.get(0).findElement(By.
                xpath(".//p[@class='deal-voucher-price' and not(@style)]")).getText();
        List<Integer> prices = listOfOffers.stream()
                .map(element -> element.findElement(By.
                        xpath(".//p[@class='deal-voucher-price' and not(@style)]")).getText())
                .map(element -> element.substring(0, element.length() - 1))
                .map(Integer::parseInt)
                .toList();
        int minValue = prices.stream().min(Integer::compareTo).orElse(0);
        String minPriceToString = minValue + Constants.GEL;
        Assert.assertEquals(minPriceToString, minPrice);
        //assert will fail because there is two offer where is not mentioned cottage


    }

    @Test
    public void priceRangeTest() throws InterruptedException {
        driver.get(Constants.swoopURL);
        WebElement rest = driver.findElement(By.xpath("//li[@class='MoreCategories'][3]"));
        rest.click();

        WebElement minPrice = driver.findElement(By.
                xpath("//div[@id='sidebar-container']//input[@id='minprice']"));
        minPrice.click();
        minPrice.sendKeys("445");
        WebElement searchButton = driver.findElement(By.
                xpath("//div[@id='sidebar-container']//div[@class='submit-button']"));
        searchButton.click();
        Thread.sleep(10000);
        //I know that I must use wait but here I could not do anything but sleep.
        WebElement container = driver.findElement(By.
                xpath(Constants.sameXpath));
        List<WebElement> listOfOffers = container.findElements(By.
                xpath("//div[@class='special-offer']//div[@class='discounted-prices']"));
        List<Integer> prices = listOfOffers.stream()
                .map(element -> element.findElement(By.
                        xpath(".//p[@class='deal-voucher-price' and not(@style)]")).getText())
                .map(element -> element.substring(0, element.length() - 1))
                .map(Integer::parseInt)
                .toList();
        for (Integer price : prices) {
            Assert.assertTrue(price >= 445);
        }
    }
}

