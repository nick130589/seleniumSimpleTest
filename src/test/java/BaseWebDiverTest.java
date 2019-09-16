import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;
import org.junit.rules.MethodRule;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.Statement;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.remote.BrowserType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.Select;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.NoSuchElementException;

@Execution(ExecutionMode.CONCURRENT)
public abstract class BaseWebDiverTest {

    public enum DriverType {
        CHROME, HEADLESS_CHROME, FIREFOX
    }

    protected static WebDriver driver;
    protected static DriverType driverType = DriverType.CHROME;
    protected static DesiredCapabilities capabilities = new DesiredCapabilities();

    protected boolean acceptNextAlert = true;

    @Rule
    public SceenshotRule screenshotRule = new SceenshotRule();

    @BeforeAll
    public static void prepareDriver() throws Exception {
        ChromeOptions chromeOptions;

        switch (driverType) {
            case CHROME:
                WebDriverManager.chromedriver().setup();
                chromeOptions = new ChromeOptions().merge(capabilities);
                driver = new ChromeDriver(chromeOptions);
                break;
            case HEADLESS_CHROME:
                WebDriverManager.chromedriver().setup();
                chromeOptions = new ChromeOptions().merge(capabilities).addArguments("--headless");
                driver = new ChromeDriver(chromeOptions);
                break;
            case FIREFOX:
                WebDriverManager.firefoxdriver().setup();
                capabilities.setBrowserName(BrowserType.FIREFOX);
                driver = new FirefoxDriver(new FirefoxOptions(capabilities));
                break;
        }

    }

    @AfterAll
    public static void cleanup() {
        driver.quit();
    }

    /**
     * Verifies that the given {@Link Select} instance has the specified
     * options.
     *
     * @param select
     * @param optionNames
     */
    protected void verifySelectOptions(Select select, String... optionNames) {
        List<WebElement> options = select.getOptions();
        Assert.assertEquals("Options", optionNames.length, options.size());
        for (int i = 0; i < optionNames.length; i++) {
            Assert.assertEquals("Wrong option text", optionNames[i], options.get(i).getText().trim());
        }
    }

    protected boolean isElementPresent(By by) {
        try {
            driver.findElement(by);
            return true;
        } catch (NoSuchElementException e) {
            return false;
        }
    }

    protected boolean isAlertPresent() {
        try {
            driver.switchTo().alert();
            return true;
        } catch (NoAlertPresentException e) {
            return false;
        }
    }

    protected String closeAlertAndGetItsText() {
        try {
            Alert alert = driver.switchTo().alert();
            String alertText = alert.getText();
            if (acceptNextAlert) {
                alert.accept();
            } else {
                alert.dismiss();
            }
            return alertText;
        } finally {
            acceptNextAlert = true;
        }
    }

    protected void takeScreenshot(String name) {
        try {
            String outputDirName = "target/surfire-reports/screenshots/";
            new File(outputDirName).mkdirs();
            File screenshotFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
            File outputDir = new File(outputDirName + name + ".png");
            Files.copy(screenshotFile.toPath(), outputDir.toPath(), StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            System.out.println("Error creating screenshot");
            e.printStackTrace();
        }
    }

    class SceenshotRule implements MethodRule {
        @Override
        public Statement apply(final Statement statement, final FrameworkMethod frameworkMethod, final Object o) {
            return new Statement() {
                @Override
                public void evaluate() throws Throwable {
                    try {
                        statement.evaluate();
                    } catch (Throwable t) {
                        takeScreenshot(frameworkMethod.getName() + "-failure");
                        throw t;
                    }
                }
            };
        }
    }
}
