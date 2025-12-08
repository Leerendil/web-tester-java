package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;

public class CheckoutPage {

    private WebDriver driver;
    private WebDriverWait wait;

    // ---------- Локаторы ----------
    private By firstNameField = By.id("first-name");
    private By lastNameField = By.id("last-name");
    private By postalCodeField = By.id("postal-code");
    private By continueButton = By.id("continue");
    private By finishButton = By.id("finish");
    private By errorMessage = By.xpath("//h3[@data-test='error']");
    private By completeHeader = By.className("complete-header");

    // ---------- Конструктор ----------
    public CheckoutPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(5));
    }

    // ---------- Методы ----------

    public void fillInformation(String firstName, String lastName, String postalCode) {
        WebElement firstNameEl = wait.until(ExpectedConditions.visibilityOfElementLocated(firstNameField));
        firstNameEl.sendKeys(firstName);

        driver.findElement(lastNameField).sendKeys(lastName);
        driver.findElement(postalCodeField).sendKeys(postalCode);
    }

    public void clickContinue() {
        WebElement continueBtn = wait.until(ExpectedConditions.elementToBeClickable(continueButton));
        continueBtn.click();
    }

    public void clickFinish() {
        WebElement finishBtn = wait.until(ExpectedConditions.elementToBeClickable(finishButton));
        finishBtn.click();
    }

    public String getErrorMessage() {
        WebElement errorEl = wait.until(ExpectedConditions.visibilityOfElementLocated(errorMessage));
        return errorEl.getText();
    }

    public boolean isOrderComplete() {
        WebElement header = wait.until(ExpectedConditions.visibilityOfElementLocated(completeHeader));
        return header.getText().contains("Thank you for your order!");
    }
}
