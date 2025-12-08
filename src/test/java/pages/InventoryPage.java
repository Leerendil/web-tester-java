package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;
import java.util.List;

public class InventoryPage {
    private WebDriver driver;

    private By inventoryItem = By.className("inventory_item");
    private By addToCartButton = By.xpath("//button[contains(text(),'Add to cart')]");
    private By removeButton = By.xpath("//button[contains(text(),'Remove')]");
    private By cartBadge = By.className("shopping_cart_badge");
    private By sortDropdown = By.className("product_sort_container");
    private By inventoryItemName = By.className("inventory_item_name");

    public InventoryPage(WebDriver driver) {
        this.driver = driver;
    }

    public void addFirstProductToCart() {
        List<WebElement> buttons = driver.findElements(addToCartButton);
        if (!buttons.isEmpty()) {
            buttons.get(0).click();
        }
    }

    public void removeFirstProductFromCart() {
        List<WebElement> buttons = driver.findElements(removeButton);
        if (!buttons.isEmpty()) {
            buttons.get(0).click();
        }
    }

    public int getCartItemCount() {
        List<WebElement> badges = driver.findElements(cartBadge);
        if (badges.isEmpty()) {
            return 0;
        }
        return Integer.parseInt(badges.get(0).getText());
    }

    public void sortProducts(String sortOptionValue) {
        WebElement dropdown = driver.findElement(sortDropdown);
        Select select = new Select(dropdown);
        select.selectByVisibleText(sortOptionValue);
    }

    public String getFirstProductName() {
        List<WebElement> items = driver.findElements(inventoryItemName);
        if (!items.isEmpty()) {
            return items.get(0).getText();
        }
        return null;
    }
}
