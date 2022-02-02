package data;

import com.github.javafaker.Faker;
import lombok.SneakyThrows;
import lombok.Value;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.ScalarHandler;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Locale;

public class DataHelper {

    private static final QueryRunner runner = new QueryRunner();
    private static final Connection conn = connection();

    private DataHelper() {
    }

    @Value
    public static class AuthInfo {
        private String login;
        private String password;
    }

    public static AuthInfo getAuthInfo() {
        return new AuthInfo("vasya", "qwerty123");
    }

    public static AuthInfo getWrongAuthInfo() {
        Faker faker = new Faker(new Locale("en"));
        return new AuthInfo("petya", faker.internet().password());
    }

    @SneakyThrows
    public static Connection connection() {
        return DriverManager.getConnection("jdbc:mysql://localhost:3306/app", "app", "pass");
    }

    @SneakyThrows
    public static String getVerificationCode() {
        return runner.query(conn, "SELECT DISTINCT LAST_VALUE(code) over (ORDER BY created ASC\n" + "RANGE BETWEEN UNBOUNDED PRECEDING AND UNBOUNDED FOLLOWING) FROM auth_codes", new ScalarHandler<>());
    }

    @SneakyThrows
    public static void deleteData() {
        runner.execute(conn, "DELETE FROM auth_codes WHERE created < NOW() - INTERVAL 0 MINUTE");
        runner.execute(conn, "DELETE FROM card_transactions WHERE created < NOW() - INTERVAL 0 MINUTE");
        runner.execute(conn, "DELETE FROM cards WHERE cards.number IS NOT NULL");
        runner.execute(conn, "DELETE FROM users WHERE login IS NOT NULL");
    }
}
