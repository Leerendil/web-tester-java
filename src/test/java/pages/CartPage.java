package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.List;

public class CartPage {

    private WebDriver driver;

    private By cartItems = By.className("cart_item");
    private By checkoutButton = By.id("checkout");
    private By continueShoppingButton = By.id("continue-shopping");
    private By removeButton = By.xpath("//button[contains(text(),'Remove')]");


    public CartPage(WebDriver driver) {
        this.driver = driver;
    }

    // Возвращает количество товаров в корзине
    public int getCartItemCount() {
        List<WebElement> items = driver.findElements(cartItems);
        return items.size();
    }

    // Нажимает кнопку Checkout (начало оформления заказа)
    public void clickCheckout() {
        driver.findElement(checkoutButton).click();
    }

    // Удаляет первый товар из корзины
    public void removeFirstItem() {
        List<WebElement> buttons = driver.findElements(removeButton);
        if (!buttons.isEmpty()) {
            buttons.get(0).click();
        }
    }

    // Возвращает true, если пользователь находится на странице корзины
    public boolean isAtCartPage() {
        return driver.getCurrentUrl().contains("cart.html");
    }
}
