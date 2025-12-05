package tests;

import base.BaseTest;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.*;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import pages.LoginPage;

import java.io.File;

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
        driver.get("https://www.saucedemo.com/");
        String title = driver.getTitle();
        System.out.println("Страница открыта: " + title);
        Assertions.assertTrue(title.contains("Swag Labs"));
    }
    // --- Тест 1: Успешный вход ---
    @Test
    @DisplayName("Успешная авторизация с корректными данными")
    public void successfulLoginTest() {
        loginPage.login("standard_user", "secret_sauce");

        // Проверяем, что мы перешли на страницу каталога
        String currentUrl = driver.getCurrentUrl();
        Assertions.assertTrue(currentUrl.contains("inventory.html"),
                "Ожидалась страница каталога после успешного входа");
    }

    // --- Тест 2: Неверные данные ---
    @Test
    @DisplayName("Ошибка при неверных данных авторизации")
    public void invalidCredentialsTest() {
        loginPage.login("wrong_user", "wrong_password");

        // Проверяем наличие ошибки
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
        loginPage.login("", ""); // Пустые поля
        String errorText = loginPage.getErrorMessage();
        Assertions.assertTrue(errorText.contains("Username is required"),
                "Сообщение об обязательности логина отсутствует");
    }

}

/*
    ✅ Тест №1 — successfulLoginTest()
Успешная авторизация с валидными данными

* Цель теста:
Проверить, что при вводе корректного логина и пароля пользователь успешно входит в систему
и попадает на страницу каталога товаров.

* Шаги выполнения:
1. Открыть страницу входа.
2. Ввести логин `standard_user`.
3. Ввести пароль `secret_sauce`.
4. Нажать кнопку «Login».
5. Проверить, что после авторизации сайт перенаправил пользователя на страницу `/inventory.html`.

* Ожидаемый результат:
Пользователь оказывается на странице каталога товаров.
URL страницы должен содержать подстроку `inventory.html`.

    ✅ Тест №2 — invalidCredentialsTest()
Ошибка при неверном логине или пароле

* Цель теста:
Проверить, что при вводе неправильных данных система **корректно отображает сообщение об ошибке**
и **не допускает входа** на сайт.

* Шаги выполнения:
1. Открыть страницу входа.
2. Ввести некорректный логин `wrong_user`.
3. Ввести некорректный пароль `wrong_password`.
4. Нажать кнопку «Login».
5. Считать текст ошибки, который появляется под формой.

* Ожидаемый результат:
Появляется сообщение об ошибке:
    Epic sadface: Username and password do not match any user in this service
URL страницы при этом **не должен измениться**, то есть пользователь остаётся на странице входа.

    ✅ Тест №3 — lockedUserTest()
Ошибка при попытке входа заблокированного пользователя

* Цель теста:
Убедиться, что система правильно обрабатывает ситуацию, когда пользователь заблокирован.

* Шаги выполнения:
1. Открыть страницу входа.
2. Ввести логин `locked_out_user`.
3. Ввести пароль `secret_sauce`.
4. Нажать кнопку «Login».
5. Считать сообщение об ошибке.

* Ожидаемый результат:
Появляется сообщение:
    Epic sadface: Sorry, this user has been locked out.

    ✅ Тест №4 — emptyFieldsTest()
Проверка поведения при пустых полях формы

* Цель теста:
Убедиться, что сайт не пропускает пустую форму входа и показывает предупреждение.

* Шаги выполнения:
1. Открыть страницу входа.
2. Не заполнять поля логина и пароля.
3. Нажать кнопку «Login».
4. Считать сообщение об ошибке.

* Ожидаемый результат:
Появляется сообщение:
    Epic sadface: Username is required

Итоговая таблица для документации
|  №  | Название теста         | Цель проверки                                      | Ожидаемый результат                                            |
| :-: | :--------------------- | :------------------------------------------------- | :------------------------------------------------------------- |
|  1  | successfulLoginTest    | Проверить корректную авторизацию                   | Пользователь перенаправлен на `/inventory.html`                |
|  2  | invalidCredentialsTest | Проверить поведение при неверных данных            | Отображается ошибка “Username and password do not match...”    |
|  3  | lockedUserTest         | Проверить реакцию на заблокированного пользователя | Отображается сообщение “Sorry, this user has been locked out.” |
|  4  | emptyFieldsTest        | Проверить обязательность полей формы               | Ошибка “Username is required”                                  |
 */