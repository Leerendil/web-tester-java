package tests;

import base.BaseTest;
import org.junit.jupiter.api.*;
import pages.LoginPage;
import pages.InventoryPage;
import pages.CartPage;

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
        Assertions.assertTrue(driver.getCurrentUrl().contains("inventory.html"),
                "Не удалось войти и попасть на страницу каталога");
    }

    // Проверка перехода в корзину
    @Test
    @DisplayName("Переход в корзину после добавления товара")
    public void goToCartAfterAddingProduct() {
        inventoryPage.addFirstProductToCart();
        driver.get("https://www.saucedemo.com/cart.html");

        Assertions.assertTrue(cartPage.isAtCartPage(), "Не удалось попасть на страницу корзины");
        Assertions.assertEquals(1, cartPage.getCartItemCount(), "В корзине должен быть 1 товар");
    }

    // Удаление товара в корзине
    @Test
    @DisplayName("Удаление товара в корзине")
    public void removeItemFromCart() {
        inventoryPage.addFirstProductToCart();
        driver.get("https://www.saucedemo.com/cart.html");

        cartPage.removeFirstItem();
        Assertions.assertEquals(0, cartPage.getCartItemCount(), "Корзина должна быть пустой после удаления");
    }

    // Переход к оформлению заказа
    @Test
    @DisplayName("Переход на страницу Checkout")
    public void proceedToCheckoutTest() {
        inventoryPage.addFirstProductToCart();
        driver.get("https://www.saucedemo.com/cart.html");

        cartPage.clickCheckout();
        Assertions.assertTrue(driver.getCurrentUrl().contains("checkout-step-one.html"),
                "Переход на страницу оформления заказа не произошёл");
    }
}
