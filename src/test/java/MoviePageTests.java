import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.tbc.Constants;
import org.tbc.Utils;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import java.util.List;

public class MoviePageTests {
    WebDriver driver;
    WebDriverWait wait;
    JavascriptExecutor js;
    Actions actions;

    @BeforeClass
    @Parameters("browser")
    public void setup(String browser) {
        if (browser.equalsIgnoreCase("chrome")) {
            WebDriverManager.chromedriver().setup();
            driver = new ChromeDriver();
        }else if (browser.equalsIgnoreCase("firefox")){
            WebDriverManager.firefoxdriver().setup();
            driver = new FirefoxDriver();
        } else if (browser.equalsIgnoreCase("edge")) {
            WebDriverManager.edgedriver().setup();
            driver = new EdgeDriver();
        }
        driver.manage().window().maximize();
        wait = new WebDriverWait(driver, 10);
        js = (JavascriptExecutor) driver;
        actions = new Actions(driver);
    }

    @AfterClass
    public void tearDown() {
        driver.close();
    }
    @Test
    public void movieTest() throws InterruptedException {
        driver.get(Constants.swoopURL);
        WebElement movie = driver.findElement(By.xpath("//li[@class='MoreCategories'][1]"));
        movie.click();
        actions.moveToElement(driver.findElement(By.
                cssSelector(".cinema_container>:first-child"))).perform();
        WebElement buyButton = driver.findElement(By.
                cssSelector(".cinema_container>:first-child .info-cinema-ticket"));
        buyButton.click();
        js.executeScript(Constants.scrollBy400px);
        WebElement eastPoint = driver.findElement(By.id("ui-id-5"));
        wait.until(ExpectedConditions.elementToBeClickable(eastPoint));
        eastPoint.click();
        js.executeScript(Constants.scrollBy150px);
        wait.until(ExpectedConditions.presenceOfElementLocated(By.
                xpath(Constants.sameXpathChildUl)));
        WebElement liContainer = driver.findElement(By.
                xpath(Constants.sameXpathChildUl));
        List<WebElement> listItems = liContainer.findElements(By.tagName("li"));
        for (WebElement listItem: listItems) {
            listItem.click();
            Assert.assertTrue(Utils.checkIfTrue(driver));
            //created method in util to check every date of each cinema
        }
        WebElement lastItem = listItems.get(listItems.size() - 1);
        lastItem.click();
        WebElement container = driver.findElement(By.xpath("//div[@id='384933']/child::div"));
        List<WebElement> checkEastPoint = container.findElements(By.xpath("./div[@aria-expanded='true']"));
        WebElement lastSeance = checkEastPoint.get(checkEastPoint.size() - 1);
        lastSeance.click();
        wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector(Constants.divContentHeader)));
        WebElement cont = driver.findElement(By.cssSelector(Constants.divContentHeader));
        List<WebElement> dateCinema = cont.findElements(By.tagName("p"));
        Assert.assertTrue(Utils.checkValidity(dateCinema));
        wait.until(ExpectedConditions.presenceOfElementLocated(By.
                xpath(Constants.containerXpath)));
        //Thread
        WebElement seatContainer = driver.findElement(By.
                xpath(Constants.containerXpath));
        List<WebElement> freeSeat = seatContainer.findElements(By.xpath(".//div[@class='seat free']"));
        WebElement randomSeat = freeSeat.get(Constants.random.nextInt(freeSeat.size()));
        actions.moveToElement(randomSeat).perform();
        randomSeat.click();
        //we choose randomly which free seat we should take
        WebElement registration = driver.findElement(By.xpath("//div[@class='create pl-2 ']"));
        actions.moveToElement(registration).perform();
        wait.until(ExpectedConditions.visibilityOf(registration));
        registration.click();
        WebElement email = driver.findElement(By.id("email"));
        email.click();
        email.sendKeys(Constants.fakeEmail);
        WebElement password = driver.findElement(By.id("password"));
        wait.until(ExpectedConditions.visibilityOf(password));
        password.click();
        password.sendKeys(Constants.password);
        WebElement repeatPassword = driver.findElement(By.id("PasswordRetype"));
        repeatPassword.click();
        repeatPassword.sendKeys(Constants.password);
        WebElement gender = driver.findElement(By.xpath("//div[@class='radio-checkmark']"));
        gender.click();
        WebElement name = driver.findElement(By.id("name"));
        name.click();
        name.sendKeys(Constants.name);
        WebElement surname = driver.findElement(By.id("surname"));
        js.executeScript(Constants.scrollIntoView, surname);
        js.executeScript(Constants.click, surname);
        surname.sendKeys(Constants.surname);
        Select select = new Select(driver.findElement(By.xpath("//select[@name='birth_year']")));
        select.selectByVisibleText(Constants.birthYear);
        js.executeScript(Constants.scrollToBottom);
        Thread.sleep(500);
        //I know that I must use wait but here I could not do anything but sleep.
        WebElement phone = driver.findElement(By.id("Phone"));
        wait.until(ExpectedConditions.elementToBeClickable(phone));
        phone.click();
        phone.sendKeys(Constants.number);
        WebElement smsSent = driver.findElement(By.id("PhoneCodeSend"));
        smsSent.click();
        WebElement inputSms = driver.findElement(By.id("PhoneCode"));
        inputSms.click();
        inputSms.sendKeys(Constants.code);
        WebElement agreeRules = driver.findElement(By.id("test"));
        WebElement agreeTBC = driver.findElement(By.id("tbcAgreement"));
        js.executeScript(Constants.checkboxTrue, agreeRules);
        js.executeScript(Constants.checkboxTrue, agreeTBC);
        WebElement submit = driver.findElement(By.id("registrationBtn"));
        submit.click();
        WebElement error = driver.findElement(By.id("input-error-email"));
        Assert.assertEquals(error.getText(), Constants.emailError);
        //this assert will fail because there is different error
    }
}
