package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
// 💡 Новые импорты для явных ожиданий
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;

/**
 * Page Object для страницы входа
 */

public class LoginPage {

    private WebDriver driver;
    private WebDriverWait wait;

    //  Локаторы элементов, которые Selenium использует, чтобы найти определенные веб-элементы
    private final By usernameField = By.id("user-name");
    private final By passwordField = By.id("password");
    private final By loginButton = By.id("login-button");
    private final By errorContainer = By.xpath("//h3[@data-test='error']");

    public LoginPage(WebDriver driver) {
        this.driver = driver;
        // Инициализируем WebDriverWait с таймаутом 10 секунд
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        // Принудительно открываем страницу логина при создании объекта
        driver.get("http://127.0.0.1:5000/");
    }

    public void enterUsername(String username) {
        wait.until(ExpectedConditions.visibilityOfElementLocated(usernameField));
        driver.findElement(usernameField).clear();
        driver.findElement(usernameField).sendKeys(username);
    }

    public void enterPassword(String password) {
        driver.findElement(passwordField).clear();
        driver.findElement(passwordField).sendKeys(password);
    }

    public void clickLoginButton() {
        driver.findElement(loginButton).click();
    }

    public void login(String username, String password) {
        enterUsername(username);
        enterPassword(password);
        clickLoginButton();
    }

    public String getErrorMessage() {
        wait.until(ExpectedConditions.visibilityOfElementLocated(errorContainer));
        return driver.findElement(errorContainer).getText();
    }
}