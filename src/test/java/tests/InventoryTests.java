package tests;

import base.BaseTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import pages.CartPage;
import pages.InventoryPage;
import pages.LoginPage;
import utils.ConfigReader;

import static org.junit.jupiter.api.Assertions.*;

public class InventoryTests extends BaseTest {

    private LoginPage loginPage;
    private InventoryPage inventoryPage;
    private CartPage cartPage;

    @BeforeEach
    public void initPages() {
        loginPage = new LoginPage(driver);
        inventoryPage = new InventoryPage(driver);
        cartPage = new CartPage(driver);

        loginPage.login(ConfigReader.getStandardUser(), ConfigReader.getPassword());

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        assertTrue(inventoryPage.isAtInventoryPage(),
                "Не удалось перейти на страницу каталога");
    }

    @Test
    @DisplayName("Добавление товара в корзину")
    public void addProductToCartTest() {
        inventoryPage.addFirstProductToCart();

        int cartCount = inventoryPage.getCartItemCount();
        assertEquals(1, cartCount, "В корзине должен быть 1 товар");
    }

    @Test
    @DisplayName("Удаление товара из корзины на странице инвентаря")
    public void removeProductFromCartTest() {
        inventoryPage.addFirstProductToCart();
        assertEquals(1, inventoryPage.getCartItemCount(),
                "В корзине должен быть 1 товар");

        inventoryPage.removeFirstProductFromCart();

        assertEquals(0, inventoryPage.getCartItemCount(),
                "После удаления корзина должна быть пустой");
    }

    @Test
    @DisplayName("Сортировка товаров по имени (A to Z)")
    public void inventorySortingTest() {
        inventoryPage.sortProducts("Name (A to Z)");

        String firstProductName = inventoryPage.getFirstProductName();

        assertEquals("Sauce Labs Backpack", firstProductName,
                "После сортировки A-Z первым должен быть Sauce Labs Backpack");
    }
}