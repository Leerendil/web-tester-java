package tests;

import base.BaseTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import pages.CartPage;
import pages.CheckoutPage;
import pages.InventoryPage;
import pages.LoginPage;
import utils.ConfigReader;

import static org.junit.jupiter.api.Assertions.*;

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

        // Выполняем логин
        loginPage.login(ConfigReader.getStandardUser(), ConfigReader.getPassword());

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        assertTrue(inventoryPage.isAtInventoryPage(),
                "Не удалось перейти на страницу каталога после логина");

        inventoryPage.addFirstProductToCart();
        driver.get(ConfigReader.getBaseUrl() + "cart");

        assertTrue(cartPage.isAtCartPage(), "Не удалось перейти в корзину");

        cartPage.clickCheckout();
    }

    @Test
    @DisplayName("Полный процесс покупки")
    public void completePurchaseFlowTest() {
        checkoutPage.fillInformation("John", "Doe", "12345");
        checkoutPage.clickContinue();

        checkoutPage.clickFinish();

        assertTrue(checkoutPage.isOrderComplete(),
                "Заказ должен быть успешно оформлен");
    }

    @Test
    @DisplayName("Проверка валидации при отсутствии Postal Code")
    public void checkoutMissingInfoTest() {

        checkoutPage.fillInformation("John", "Doe", "");
        checkoutPage.clickContinue();

        String error = checkoutPage.getErrorMessage();

        assertTrue(error.contains("Postal Code"),
                "Ожидалась ошибка о незаполненном поле Postal Code. Получено: " + error);
    }

    @Test
    @DisplayName("Проверка корректности итоговой суммы")
    public void checkTotalPriceTest() {
        checkoutPage.fillInformation("John", "Doe", "12345");
        checkoutPage.clickContinue();

        double totalPrice = checkoutPage.getTotalPrice();

        assertTrue(totalPrice > 30 && totalPrice < 35,
                "Итоговая цена должна быть в диапазоне $30-$35, получено: $" + totalPrice);
    }
}