package ru.netology.data;

import lombok.SneakyThrows;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;


public class SQLHelper {

    static String url = System.getProperty("db.url");//"jdbc:mysql://localhost:3306/app";//
    static String userName = System.getProperty("db.user");// пользователь "app";//
    static String password = System.getProperty("db.password");// пароль "pass";//

   private static QueryRunner runner = new QueryRunner();

    public SQLHelper() {

    }

    @SneakyThrows
    private static Connection getConn() {
        return DriverManager.getConnection(url, userName, password);
    }

    @SneakyThrows
    public static DataHelper.CreditCardData getCreditCardData() {
        var cardDataSQL = "SELECT * FROM credit_request_entity ORDER BY created DESC LIMIT 1";
        var conn = getConn();
        var result = runner.query(conn, cardDataSQL,
                    new BeanHandler<>(DataHelper.CreditCardData.class));
            return result;
    }

    @SneakyThrows
    public static DataHelper.PaymentCardData getPaymentCardData() {
        var cardDataSQL = "SELECT * FROM payment_entity ORDER BY created DESC LIMIT 1";
        var conn = getConn();
        var result = runner.query(conn, cardDataSQL,
                    new BeanHandler<>(DataHelper.PaymentCardData.class));
            return result;
    }

    public static DataHelper.TableOrderEntity getTableOrderEntity() throws SQLException {
        var orderEntityDataSQL = "SELECT * FROM order_entity ORDER BY created DESC LIMIT 1";
        var conn = getConn();
        var result = runner.query(conn, orderEntityDataSQL,
                    new BeanHandler<>(DataHelper.TableOrderEntity.class));
            return result;
    }

    @SneakyThrows
    public static void databaseCleanUp() {
        var conn = getConn();
        runner.execute(conn, "DELETE FROM order_entity");
        runner.execute(conn, "DELETE FROM payment_entity");
        runner.execute(conn, "DELETE FROM credit_request_entity");
    }
}