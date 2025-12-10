package tests;

import base.BaseTest;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import pages.LoginPage;
import utils.ConfigReader;

import java.time.Duration;


public class LoginTest extends BaseTest {

    private LoginPage loginPage;

    @BeforeEach
    public void initPage() {
        loginPage = new LoginPage(driver);
    }

    // --- Тест 0: Успешный запуск сайта ---
    @Test
    @DisplayName("Успешный запуск сайта")
    public void openWebsite() {
        driver.get(ConfigReader.getBaseUrl());
        String title = driver.getTitle();
        System.out.println("URL: " + ConfigReader.getBaseUrl());
        System.out.println("Страница открыта: " + title);
        Assertions.assertTrue(title.contains("Swag Labs"));
    }

    // --- Тест 1: Успешный вход ---
    @Test
    @DisplayName("Успешная авторизация с корректными данными")
    public void successfulLoginTest() {
        loginPage.login("standard_user", "secret_sauce");

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        wait.until(ExpectedConditions.urlContains("inventory.html"));

        String currentUrl = driver.getCurrentUrl();
        Assertions.assertTrue(currentUrl.contains("inventory.html"),
                "Ожидалась страница каталога после успешного входа");
    }

    // --- Тест 2: Неверные данные ---
    @Test
    @DisplayName("Ошибка при неверных данных авторизации")
    public void invalidCredentialsTest() {
        loginPage.login("wrong_user", "wrong_password");

        String error = loginPage.getErrorMessage();
        Assertions.assertTrue(error.contains("Username and password do not match"),
                "Ожидалось сообщение о неверных данных");
    }

    // --- Тест 3: Заблокированный пользователь ---
    @Test
    @DisplayName("Попытка входа заблокированного пользователя")
    public void lockedUserTest() {
        loginPage.login("locked_out_user", "secret_sauce");
        String errorText = loginPage.getErrorMessage();
        Assertions.assertTrue(errorText.contains("Sorry, this user has been locked out."),
                "Ожидаемое сообщение об ошибке отсутствует");
    }

    // --- Тест 4: Пустые поля ---
    @Test
    @DisplayName("Ошибка при пустых полях формы логина")
    public void emptyFieldsTest() {
        loginPage.login("", "");
        String errorText = loginPage.getErrorMessage();
        Assertions.assertTrue(errorText.contains("Username is required"),
                "Сообщение об обязательности логина отсутствует");
    }

    @ParameterizedTest
    @DisplayName("Проверка различных невалидных комбинаций логина/пароля")
    @CsvSource({
            "wrong_user, wrong_password, Username and password do not match",
            "standard_user, wrong_password, Username and password do not match",
            "wrong_user, secret_sauce, Username and password do not match",
            "'', secret_sauce, Username is required",
            "standard_user, '', Password is required"
    })
    public void invalidLoginCombinationsTest(String username, String password, String expectedError) {
        loginPage.login(username, password);
        String actualError = loginPage.getErrorMessage();

        Assertions.assertTrue(actualError.contains(expectedError),
                "Ожидалась ошибка содержащая: " + expectedError + ", но получена: " + actualError);
    }

}