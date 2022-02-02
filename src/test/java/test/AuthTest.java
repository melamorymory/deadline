package test;

import com.codeborne.selenide.Configuration;
import data.DataHelper;
import org.junit.jupiter.api.*;
import page.LoginPage;

import static com.codeborne.selenide.Selenide.open;
import static data.DataHelper.deleteData;

public class AuthTest {
    @BeforeEach
    void setUp() {
        open("http://localhost:9999");
        Configuration.holdBrowserOpen = true;
    }

    @AfterAll
    static void cleanDatabase() {
        deleteData();
    }

    @Test
    void shouldLogin() {
        var loginPage = new LoginPage();
        var authInfo = DataHelper.getAuthInfo();
        var verificationPage = loginPage.validLogin(authInfo);
        var dashboardPage = verificationPage.validVerify(DataHelper.getVerificationCode());
        dashboardPage.getTextOnPage();
    }

    @Test
    void shouldNotLogin() {
        var loginPage = new LoginPage();
        var authInfo = DataHelper.getWrongAuthInfo();
        loginPage.invalidLogin(authInfo);
        loginPage.getErrorNotification();
    }

    @Test
    void shouldBlockTheAccount() {
        var loginPage = new LoginPage();
        var authInfo = DataHelper.getWrongAuthInfo();
        loginPage.invalidLogin(authInfo);
        loginPage.wrongPassword();
        loginPage.wrongPassword();
        loginPage.getBlockNotification();
    }
}
