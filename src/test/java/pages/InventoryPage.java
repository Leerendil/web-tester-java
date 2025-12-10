package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;
import java.util.List;

public class InventoryPage {
    private WebDriver driver;
    private WebDriverWait wait;

    private By inventoryItem = By.className("inventory_item");
    private By addToCartButton = By.xpath("//button[contains(text(),'Add to cart')]");
    private By removeButton = By.xpath("//button[contains(text(),'Remove')]");
    private By cartBadge = By.className("shopping_cart_badge");
    private By sortDropdown = By.className("product_sort_container");
    private By inventoryItemName = By.className("inventory_item_name");
    private By productsTitle = By.xpath("//span[contains(text(), 'Products')]");

    public InventoryPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    /**
     * Проверяет, что пользователь находится на странице инвентаря
     */
    public boolean isAtInventoryPage() {
        try {
            wait.until(ExpectedConditions.urlContains("/inventory"));
            wait.until(ExpectedConditions.presenceOfElementLocated(productsTitle));
            wait.until(ExpectedConditions.presenceOfElementLocated(inventoryItem));

            return driver.getCurrentUrl().contains("/inventory");
        } catch (Exception e) {
            System.out.println("❌ Не удалось подтвердить загрузку страницы инвентаря: " + e.getMessage());
            System.out.println("Текущий URL: " + driver.getCurrentUrl());
            return false;
        }
    }

    public void addFirstProductToCart() {
        wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(addToCartButton));
        List<WebElement> buttons = driver.findElements(addToCartButton);
        if (!buttons.isEmpty()) {
            wait.until(ExpectedConditions.elementToBeClickable(buttons.get(0)));
            buttons.get(0).click();

            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void removeFirstProductFromCart() {
        wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(removeButton));
        List<WebElement> buttons = driver.findElements(removeButton);
        if (!buttons.isEmpty()) {
            wait.until(ExpectedConditions.elementToBeClickable(buttons.get(0)));
            buttons.get(0).click();

            try {
                Thread.sleep(800);
                wait.until(ExpectedConditions.or(
                        ExpectedConditions.invisibilityOfElementLocated(cartBadge),
                        ExpectedConditions.stalenessOf(buttons.get(0))
                ));
            } catch (Exception e) {
            }
        }
    }

    public int getCartItemCount() {
        try {
            Thread.sleep(300);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        List<WebElement> badges = driver.findElements(cartBadge);
        if (badges.isEmpty()) {
            return 0;
        }
        return Integer.parseInt(badges.get(0).getText());
    }

    public void sortProducts(String sortOptionValue) {
        WebElement dropdown = wait.until(ExpectedConditions.presenceOfElementLocated(sortDropdown));
        Select select = new Select(dropdown);
        select.selectByVisibleText(sortOptionValue);

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public String getFirstProductName() {
        wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(inventoryItemName));
        List<WebElement> items = driver.findElements(inventoryItemName);
        if (!items.isEmpty()) {
            return items.get(0).getText();
        }
        return null;
    }
}