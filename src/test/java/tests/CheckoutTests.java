package tests;

import base.BaseTest;
import org.junit.jupiter.api.*;
import pages.LoginPage;
import pages.InventoryPage;
import pages.CartPage;
import pages.CheckoutPage;
import utils.ConfigReader;

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

        // Логинимся и добавляем товар
        loginPage.login("standard_user", "secret_sauce");
        inventoryPage.addFirstProductToCart();
        driver.get("https://www.saucedemo.com/cart.html");
        cartPage.clickCheckout();
        Assertions.assertTrue(driver.getCurrentUrl().contains("checkout-step-one.html"),
                "Не удалось попасть на страницу Checkout Step One");
    }

    // Успешное оформление заказа
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

    // Пропуск обязательного поля
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

    @Test
    @DisplayName("Проверка корректности итоговой суммы")
    public void checkTotalPriceTest() {
        // Добавляем второй товар для проверки суммирования
        driver.get(ConfigReader.getBaseUrl() + "inventory.html");
        inventoryPage.addFirstProductToCart();

        driver.get(ConfigReader.getBaseUrl() + "cart.html");
        cartPage.clickCheckout();

        checkoutPage.fillInformation("Alex", "Ivanov", "12345");
        checkoutPage.clickContinue();

        // Проверяем, что итоговая сумма больше 0
        double total = checkoutPage.getTotalPrice();
        Assertions.assertTrue(total > 0, "Итоговая сумма должна быть больше 0");

        System.out.println("💰 Итоговая сумма заказа: $" + total);
    }
}
