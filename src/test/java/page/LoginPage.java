package page;

import com.codeborne.selenide.SelenideElement;
import com.github.javafaker.Faker;
import data.DataHelper;
import org.openqa.selenium.Keys;

import java.util.Locale;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.$;

public class LoginPage {
    private SelenideElement loginField = $("[data-test-id=login] input");
    private SelenideElement passwordField = $("[data-test-id=password] input");
    private SelenideElement loginButton = $("[data-test-id=action-login]");
    private SelenideElement error = $("[data-test-id=error-notification]");

    public VerificationPage validLogin(DataHelper.AuthInfo info) {
        loginField.setValue(info.getLogin());
        passwordField.setValue(info.getPassword());
        loginButton.click();
        return new VerificationPage();
    }

    public void invalidLogin(DataHelper.AuthInfo info) {
        loginField.setValue(info.getLogin());
        passwordField.setValue(info.getPassword());
        loginButton.click();
    }

    public void wrongPassword() {
        Faker faker = new Faker(new Locale("en"));
        passwordField.click();
        String deleteString = Keys.chord(Keys.CONTROL, "a") + Keys.DELETE;
        passwordField.sendKeys(deleteString);
        passwordField.setValue(faker.internet().password());
        loginButton.click();
    }

    public void getErrorNotification() {
        error.shouldBe(visible).shouldHave(exactText("Ошибка Ошибка! Неверно указан логин или пароль"));
    }

    public void getBlockNotification() {
        error.shouldBe(visible).shouldHave(exactText("Количество попыток ввода пароля исчерпано! Аккаунт заблокирован"));
    }
}
