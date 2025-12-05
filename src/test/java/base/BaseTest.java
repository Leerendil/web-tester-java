package base;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import java.io.File;

/**
 * Базовый класс для всех тестов.
 * Содержит настройку и завершение WebDriver.
 */
public class BaseTest {

    protected WebDriver driver;

    @BeforeEach
    public void setUp() {
        // Настройка WebDriverManager
        WebDriverManager.chromedriver().browserVersion("130").setup();

        // Настройки для Thorium браузера
        ChromeOptions options = new ChromeOptions();
        String thoriumPath = "/Applications/Thorium.app/Contents/MacOS/Thorium";
        File thoriumBinary = new File(thoriumPath);

        if (thoriumBinary.exists()) {
            options.setBinary(thoriumPath);
        } else {
            System.err.println("⚠Thorium не найден по пути: " + thoriumPath);
            System.err.println("Проверьте путь к браузеру!");
        }

        // Запуск браузера
        driver = new ChromeDriver(options);
        driver.manage().window().maximize();

        System.out.println("✅ Драйвер успешно запущен");
    }

    @AfterEach
    public void tearDown() {
        if (driver != null) {
            driver.quit();
            System.out.println("Драйвер закрыт");
        }
    }
}
