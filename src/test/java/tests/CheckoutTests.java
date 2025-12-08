package tests;

import base.BaseTest;
import org.junit.jupiter.api.*;
import pages.LoginPage;
import pages.InventoryPage;
import pages.CartPage;
import pages.CheckoutPage;

public class CheckoutTests extends BaseTest {

    private LoginPage loginPage;
    private InventoryPage inventoryPage;
    private CartPage cartPage;
    private CheckoutPage checkoutPage;

    @BeforeEach
    public void initPages() {
        loginPage = new LoginPage(driver);
        inventoryPage = new InventoryPage(driver);
        cartPage = new CartPage(driver);
        checkoutPage = new CheckoutPage(driver);

        // 1️⃣ Логинимся и добавляем товар
        loginPage.login("standard_user", "secret_sauce");
        inventoryPage.addFirstProductToCart();
        driver.get("https://www.saucedemo.com/cart.html");
        cartPage.clickCheckout();
        Assertions.assertTrue(driver.getCurrentUrl().contains("checkout-step-one.html"),
                "Не удалось попасть на страницу Checkout Step One");
    }

    // 1️⃣ Тест: успешное оформление заказа
    @Test
    @DisplayName("Полное оформление заказа")
    public void completePurchaseFlowTest() {
        checkoutPage.fillInformation("Alex", "Ivanov", "12345");
        checkoutPage.clickContinue();
        checkoutPage.clickFinish();

        Assertions.assertTrue(checkoutPage.isOrderComplete(),
                "Сообщение о завершении заказа не найдено");
        System.out.println("✅ Заказ успешно оформлен!");
    }

    // 2️⃣ Тест: пропуск обязательного поля
    @Test
    @DisplayName("Ошибка при пропуске Postal Code")
    public void checkoutMissingInfoTest() {
        checkoutPage.fillInformation("Alex", "Ivanov", ""); // без индекса
        checkoutPage.clickContinue();

        String error = checkoutPage.getErrorMessage();
        System.out.println("Ошибка: " + error);

        Assertions.assertTrue(error.contains("Error: Postal Code is required"),
                "Ожидалась ошибка о незаполненном поле Postal Code");
    }
}
