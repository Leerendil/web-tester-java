package tests;

import base.BaseTest;
import org.junit.jupiter.api.*;
import pages.LoginPage;
import pages.InventoryPage;

public class InventoryTests extends BaseTest {

    private LoginPage loginPage;
    private InventoryPage inventoryPage;

    @BeforeEach
    public void initPages() {
        loginPage = new LoginPage(driver);
        inventoryPage = new InventoryPage(driver);

        // Логинимся перед каждым тестом, чтобы попасть в каталог
        loginPage.login("standard_user", "secret_sauce");
        Assertions.assertEquals("https://www.saucedemo.com/inventory.html", driver.getCurrentUrl(),
                "Не удалось перейти на страницу каталога после логина");
    }

    // Добавление товара
    @Test
    @DisplayName("Добавление товара в корзину")
    public void addProductToCartTest() {
        inventoryPage.addFirstProductToCart();
        int count = inventoryPage.getCartItemCount();
        System.out.println("Количество товаров в корзине: " + count);
        Assertions.assertEquals(1, count, "После добавления товара в корзине должно быть 1 изделие");
    }

    // Удаление товара
    @Test
    @DisplayName("Удаление товара из корзины")
    public void removeProductFromCartTest() {
        inventoryPage.addFirstProductToCart();
        inventoryPage.removeFirstProductFromCart();
        int count = inventoryPage.getCartItemCount();
        System.out.println("Количество товаров в корзине после удаления: " + count);
        Assertions.assertEquals(0, count, "После удаления корзина должна быть пустой");
    }

    // Проверка сортировки
    @Test
    @DisplayName("Сортировка товаров (A-Z)")
    public void inventorySortingTest() {
        // Выполняем сортировку по имени (A-Z)
        inventoryPage.sortProducts("Name (A to Z)");
        String firstItemName = inventoryPage.getFirstProductName();
        System.out.println("Первый товар после сортировки A-Z: " + firstItemName);

        Assertions.assertEquals("Sauce Labs Backpack", firstItemName,
                "Сортировка по имени A-Z не работает корректно");
    }
}
