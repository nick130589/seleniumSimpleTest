import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

import java.util.concurrent.TimeUnit;

public class SimpleTest {

    private static WebDriver driver;

    @BeforeClass
    public static void setupClass() {
        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver();
    }

//    @BeforeMethod
//    public void setupTest() {
//        driver = new ChromeDriver();
//    }

    @AfterClass
    public void teardown() {
        if (driver != null) {
            driver.quit();
        }
    }


    @Test
    public void navigateToRozetkaWebSiteAndSearchIphone()
    {
        driver.manage().window().maximize();
        driver.get("https://rozetka.com.ua");
        WebElement searchField = driver.findElement(By.xpath("//input[@name='search']"));
        searchField.clear();
        searchField.sendKeys("Iphone 6");
        driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
        WebElement searchButton = driver.findElement(By.xpath("//button[@type='submit']/span[@class='button-inner']"));
        searchButton.click();
    }

    @Test
    public void navigateToRozetkaWebSiteAndSearchIphone2()
    {
        driver.manage().window().maximize();
        driver.get("https://rozetka.com.ua");
        WebElement searchField = driver.findElement(By.xpath("//input[@name='search']"));
        searchField.clear();
        searchField.sendKeys("Iphone 6");
        driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
        WebElement searchButton = driver.findElement(By.xpath("//button[@type='submit']/span[@class='button-inner']"));
        searchButton.click();
    }

}
