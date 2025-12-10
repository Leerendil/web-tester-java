package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;
import java.util.List;

public class CartPage {

    private WebDriver driver;
    private WebDriverWait wait;

    private By cartItems = By.className("cart_item");
    private By checkoutButton = By.id("checkout");
    private By continueShoppingButton = By.id("continue-shopping");
    private By removeButton = By.xpath("//button[contains(text(),'Remove')] | //a[contains(text(),'Remove')]");
    private By cartTitle = By.xpath("//span[@class='title' and text()='Your Cart']");

    public CartPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    /**
     * Возвращает количество товаров в корзине
     */
    public int getCartItemCount() {
        try {
            wait.until(ExpectedConditions.presenceOfElementLocated(cartTitle));
            Thread.sleep(300);
        } catch (Exception e) {
        }

        List<WebElement> items = driver.findElements(cartItems);
        return items.size();
    }

    /**
     * Нажимает кнопку Checkout (начало оформления заказа)
     */
    public void clickCheckout() {
        WebElement checkout = wait.until(ExpectedConditions.elementToBeClickable(checkoutButton));
        checkout.click();
    }

    /**
     * Удаляет первый товар из корзины
     */
    public void removeFirstItem() {
        wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(removeButton));
        List<WebElement> buttons = driver.findElements(removeButton);
        if (!buttons.isEmpty()) {
            wait.until(ExpectedConditions.elementToBeClickable(buttons.get(0)));
            buttons.get(0).click();

            try {
                Thread.sleep(800);
                wait.until(ExpectedConditions.or(
                        ExpectedConditions.invisibilityOfAllElements(buttons),
                        ExpectedConditions.stalenessOf(buttons.get(0))
                ));
            } catch (Exception e) {
            }
        }
    }

    /**
     * Возвращает true, если пользователь находится на странице корзины
     */
    public boolean isAtCartPage() {
        try {
            wait.until(ExpectedConditions.urlContains("/cart"));
            wait.until(ExpectedConditions.presenceOfElementLocated(cartTitle));
            return driver.getCurrentUrl().contains("/cart");
        } catch (Exception e) {
            return false;
        }
    }
}