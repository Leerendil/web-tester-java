package tests;

import base.BaseTest;
import org.junit.jupiter.api.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import pages.LoginPage;
import pages.InventoryPage;
import pages.CartPage;
import utils.ConfigReader;

import java.time.Duration;

public class CartTests extends BaseTest {

    private LoginPage loginPage;
    private InventoryPage inventoryPage;
    private CartPage cartPage;

    @BeforeEach
    public void initPages() {
        loginPage = new LoginPage(driver);
        inventoryPage = new InventoryPage(driver);
        cartPage = new CartPage(driver);

        // Логинимся перед тестами
        loginPage.login("standard_user", "secret_sauce");

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        wait.until(ExpectedConditions.urlContains("inventory.html"));

        Assertions.assertTrue(driver.getCurrentUrl().contains("inventory.html"),
                "Не удалось войти и попасть на страницу каталога");
    }

    // Проверка перехода в корзину
    @Test
    @DisplayName("Переход в корзину после добавления товара")
    public void goToCartAfterAddingProduct() {
        inventoryPage.addFirstProductToCart();
        // Переходим на /cart, а не на /cart.html
        driver.get(ConfigReader.getBaseUrl() + "cart");

        Assertions.assertTrue(cartPage.isAtCartPage(), "Не удалось попасть на страницу корзины");
        Assertions.assertEquals(1, cartPage.getCartItemCount(), "В корзине должен быть 1 товар");
    }

    // Удаление товара в корзине
    @Test
    @DisplayName("Удаление товара в корзине")
    public void removeItemFromCart() {
        inventoryPage.addFirstProductToCart();
        driver.get(ConfigReader.getBaseUrl() + "cart");

        cartPage.removeFirstItem();
        Assertions.assertEquals(0, cartPage.getCartItemCount(), "Корзина должна быть пустой после удаления");
    }

    // Переход к оформлению заказа
    @Test
    @DisplayName("Переход на страницу Checkout")
    public void proceedToCheckoutTest() {
        inventoryPage.addFirstProductToCart();
        driver.get(ConfigReader.getBaseUrl() + "cart");

        cartPage.clickCheckout();
        Assertions.assertTrue(driver.getCurrentUrl().contains("checkout-step-one"),
                "Переход на страницу оформления заказа не произошёл");
    }
}