import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.tbc.Constants;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

public class LandingPageTests {
    WebDriver driver;
    WebDriverWait wait;

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

    }

    @AfterClass
    public void tearDown() {
        driver.close();
    }
    @Test
    public void activeCategoryTest() {
        driver.get(Constants.swoopURL);
        WebElement categories = driver.findElement(By.
                xpath("//div[@class='NewCategories newcat']"));
        categories.click();
        Actions actions = new Actions(driver);
        actions.moveToElement(driver.findElement(By.xpath("//li[@cat_id='CatId-6']"))).perform();
        WebElement carting = driver.findElement(By.xpath("//a[text()='კარტინგი']"));
        carting.click();
        Assert.assertEquals(driver.getCurrentUrl(), Constants.cartingURL);
        WebElement cartingColor = driver.findElement(By.
                xpath("//div[@id='sidebar-container']//span[@class='main-" +
                        "category-span open searched-category']"));
        String rgba = cartingColor.getCssValue(Constants.cssValue);
        driver.get(Constants.rgbaConverter);
        WebElement input = driver.findElement(By.id("color-hex"));
        wait.until(ExpectedConditions.elementToBeClickable(input));
        input.click();
        input.clear();
        input.sendKeys(Constants.colorHEX);
        WebElement hexToRGBA = driver.findElement(By.
                xpath("//div[@class='text-xl tracking-wider select-all cursor-pointer']/following-sibling::div"));

        Assert.assertEquals(rgba, hexToRGBA.getText());
    }
    @Test
    public void logoTest(){
        driver.get(Constants.swoopURL);
        WebElement rest = driver.findElement(By.xpath("//li[@class='MoreCategories'][3]"));
        rest.click();
        WebElement logo = driver.findElement(By.xpath("//a[@class='Newlogo']"));
        logo.click();
        String currentURL = driver.getCurrentUrl().replace("www.", "");
        String newCurrentURL = currentURL.substring(0, currentURL.length() - 1);
        Assert.assertEquals(newCurrentURL, Constants.swoopURL);
    }

}