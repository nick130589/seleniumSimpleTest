import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;
import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

import static org.hamcrest.CoreMatchers.containsString;
import static org.junit.Assert.*;;

@Epic("Login Tests Epic")
@Feature("Invalid Login Features")
public class VirtualHomePageIT extends BaseWebDiverTest {

    @BeforeEach
    public void init() {
        driver.get("http://virtua.tech");
        driver.manage().window().setSize(new Dimension(1600, 1000));
        driver.manage().timeouts().implicitlyWait(10000, TimeUnit.MILLISECONDS);
    }

    @Test
    @Story("User tries to login the system with invalid username and invalid password.")
    @Description("Invalid Login Test with Invalid Username and Invalid Password.")
    public void verifyHomePage() throws InterruptedException {
        assertThat(driver.getTitle(), containsString("Virtua"));

        List<WebElement> featureTitles = driver.findElements(By.cssSelector(".feature .overlay-caption h3"));
        assertEquals("Wrong number of features", 3, featureTitles.size());
        assertEquals(featureTitles.get(0).getText(), "Consulting");
        assertEquals(featureTitles.get(1).getText(), "Training");
        assertEquals(featureTitles.get(2).getText(), "Content");

        takeScreenshot("homepage");
        String name = Thread.currentThread().getName();
        System.out.println("First test " + name);
        Thread.sleep(5000);
    }

    @Test
    @Story("User tries to login the system with invalid username and invalid password.")
    @Description("Invalid Login Test with Invalid Username and Invalid Password.")
    public void verifyLearnMoreLinks() throws InterruptedException {
        List<WebElement> featureLinks = getFeatureLinks();
        List<String> links = new ArrayList<>();
        for (WebElement element : featureLinks) {
            links.add(element.getAttribute("href"));
        }
        assertEquals("Wrong number of feature links found", 3, featureLinks.size());
        getFeatureLinks().get(0).click();
        assertThat(driver.getTitle(), containsString("Consulting"));

        driver.navigate().back();
        driver.get(links.get(1));
        assertThat(driver.getTitle(), containsString("Training"));

        driver.navigate().back();
        getFeatureLinks().get(2).click();
        (new WebDriverWait(driver, 5)).until(ExpectedConditions.textMatches(By.cssSelector("h1[class=\"virtua-highlight\"]"),
                Pattern.compile("Content")));
        String name = Thread.currentThread().getName();
        System.out.println("Second test " + name);
    }

    @Test
    public void verifyLiferayLoaded() throws InterruptedException {
        ((JavascriptExecutor) driver).executeScript("Liferay != undefined");
        String name = Thread.currentThread().getName();
        System.out.println("Third test " + name);
        Thread.sleep(5000);
    }
//
    private List<WebElement> getFeatureLinks() {
        return driver.findElements(By.xpath("//a[@class='read-more']"));
    }
}
