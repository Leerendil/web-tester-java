package base;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import utils.ConfigReader;

import java.io.File;
import java.time.Duration;

/**
 * Базовый класс для всех тестов.
 * Содержит настройку и завершение WebDriver.
 */
public class BaseTest {

    protected WebDriver driver;

    @BeforeEach
    public void setUp() {
        WebDriverManager.chromedriver()
                .browserVersion(ConfigReader.getProperty("browser.version"))
                .setup();

        ChromeOptions options = new ChromeOptions();
        String browserPath = ConfigReader.getBrowserPath();
        File browserBinary = new File(browserPath);

        if (browserBinary.exists()) {
            options.setBinary(browserPath);
        } else {
            System.out.println("⚠️ Браузер не найден по пути: " + browserPath);
            System.out.println("Используется Chrome по умолчанию");
        }

        driver = new ChromeDriver(options);
        driver.manage().window().maximize();

        // Настройка таймаутов из конфигурации
        int implicitTimeout = Integer.parseInt(ConfigReader.getProperty("timeout.implicit"));
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(implicitTimeout));

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
