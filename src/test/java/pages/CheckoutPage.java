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

    private By firstNameField = By.id("first-name");
    private By lastNameField = By.id("last-name");
    private By postalCodeField = By.id("postal-code");
    private By continueButton = By.id("continue");
    private By finishButton = By.id("finish");
    private By errorMessage = By.xpath("//h3[@data-test='error']");
    private By completeHeader = By.className("complete-header");
    private By totalPrice = By.className("summary_total_label");
    private By checkoutOverviewTitle = By.xpath("//span[contains(text(), 'Checkout: Overview')]");
    private By checkoutInfoTitle = By.xpath("//span[contains(text(), 'Checkout: Your Information')]");

    public CheckoutPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    /**
     * Заполняет информацию о покупателе
     * ВАЖНО: НЕ использует clear() чтобы не сбрасывать значения при повторных вызовах
     */
    public void fillInformation(String firstName, String lastName, String postalCode) {
        wait.until(ExpectedConditions.presenceOfElementLocated(checkoutInfoTitle));

        WebElement firstNameEl = wait.until(ExpectedConditions.visibilityOfElementLocated(firstNameField));
        if (!firstNameEl.getAttribute("value").isEmpty()) {
            firstNameEl.clear();
        }
        firstNameEl.sendKeys(firstName);

        WebElement lastNameEl = driver.findElement(lastNameField);
        if (!lastNameEl.getAttribute("value").isEmpty()) {
            lastNameEl.clear();
        }
        lastNameEl.sendKeys(lastName);

        WebElement postalEl = driver.findElement(postalCodeField);
        if (!postalEl.getAttribute("value").isEmpty()) {
            postalEl.clear();
        }
        postalEl.sendKeys(postalCode);
    }

    /**
     * Получает итоговую цену со страницы Checkout: Overview
     */
    public double getTotalPrice() {
        wait.until(ExpectedConditions.urlContains("checkout-step-two"));
        wait.until(ExpectedConditions.presenceOfElementLocated(checkoutOverviewTitle));

        WebElement priceElement = wait.until(ExpectedConditions.presenceOfElementLocated(totalPrice));
        String priceText = priceElement.getText();

        String numericValue = priceText.replaceAll("[^0-9.]", "");
        return Double.parseDouble(numericValue);
    }

    /**
     * Нажимает кнопку Continue
     */
    public void clickContinue() {
        WebElement continueBtn = wait.until(ExpectedConditions.elementToBeClickable(continueButton));
        continueBtn.click();

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * Нажимает кнопку Finish на странице Checkout: Overview
     */
    public void clickFinish() {
        wait.until(ExpectedConditions.urlContains("checkout-step-two"));
        wait.until(ExpectedConditions.presenceOfElementLocated(checkoutOverviewTitle));

        WebElement finishBtn = wait.until(ExpectedConditions.elementToBeClickable(finishButton));
        finishBtn.click();

        try {
            Thread.sleep(800);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * Получает сообщение об ошибке
     */
    public String getErrorMessage() {
        WebElement errorEl = wait.until(ExpectedConditions.visibilityOfElementLocated(errorMessage));
        return errorEl.getText();
    }

    /**
     * Проверяет, что заказ успешно оформлен
     */
    public boolean isOrderComplete() {
        try {
            wait.until(ExpectedConditions.urlContains("checkout-complete"));
            WebElement header = wait.until(ExpectedConditions.visibilityOfElementLocated(completeHeader));
            String headerText = header.getText().toLowerCase();
            return headerText.contains("thank you") || headerText.contains("complete");
        } catch (Exception e) {
            return false;
        }
    }
}