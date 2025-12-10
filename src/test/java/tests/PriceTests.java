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

public class PriceTests extends BaseTest {

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

        loginPage.login(ConfigReader.getStandardUser(), ConfigReader.getPassword());

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Проверяем успешный логин
        assertTrue(inventoryPage.isAtInventoryPage(),
                "Не удалось войти и попасть на страницу каталога");
    }

    @Test
    @DisplayName("Проверка итоговой цены за один товар")
    public void checkSingleItemTotalPriceTest() {
        inventoryPage.addFirstProductToCart();

        driver.get(ConfigReader.getBaseUrl() + "cart");
        assertTrue(cartPage.isAtCartPage(), "Не удалось попасть в корзину");

        cartPage.clickCheckout();

        checkoutPage.fillInformation("John", "Doe", "12345");
        checkoutPage.clickContinue();

        // Получаем итоговую цену
        double totalPrice = checkoutPage.getTotalPrice();

        assertTrue(totalPrice > 30 && totalPrice < 35,
                "Итоговая цена должна быть около $32.39, получено: $" + totalPrice);
    }
}