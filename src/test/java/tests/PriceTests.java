package tests;

import base.BaseTest;
import org.junit.jupiter.api.*;
import pages.LoginPage;
import pages.InventoryPage;
import pages.CartPage;
import pages.CheckoutPage;

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

        loginPage.login("standard_user", "secret_sauce");
        Assertions.assertTrue(driver.getCurrentUrl().contains("inventory.html"),
                "Не удалось войти и попасть на страницу каталога");
    }

    @Test
    @DisplayName("Проверка корректности итоговой суммы с одним товаром")
    public void checkSingleItemTotalPriceTest() {
        inventoryPage.addFirstProductToCart();

        driver.get("https://www.saucedemo.com/cart.html");
        cartPage.clickCheckout();

        checkoutPage.fillInformation("Alex", "Ivanov", "12345");
        checkoutPage.clickContinue();

        double total = checkoutPage.getTotalPrice();
        Assertions.assertTrue(total > 0, "Итоговая сумма должна быть больше 0");

        System.out.println("💰 Итоговая сумма заказа (1 товар): $" + total);
    }
}