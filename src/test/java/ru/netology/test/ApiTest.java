package ru.netology.test;

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.logevents.SelenideLogger;
import io.qameta.allure.selenide.AllureSelenide;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.netology.data.DataHelper;
import ru.netology.data.RestApiHelper;
import ru.netology.data.SQLHelper;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static com.codeborne.selenide.Selenide.open;


public class ApiTest {

    @BeforeAll
    static void setUpAll() {
        SelenideLogger.addListener("allure", new AllureSelenide());
    }

    @BeforeEach
    void setUP() {
        Configuration.holdBrowserOpen = true;
    }

    @AfterAll
    static void tearDownAll() {
        SelenideLogger.removeListener("allure");
    }

    @Test
    void shouldApprovePayment() {
        var cardInfo = DataHelper.generateDataWithApprovedCard();
        RestApiHelper.cardInfo(cardInfo, "/api/v1/pay");
        var paymentCardData = SQLHelper.getPaymentCardData();
        assertEquals("APPROVED", paymentCardData.getStatus());
    }

    @Test
    void shouldDeclinePayment() {
        var cardInfo = DataHelper.generateDataWithDeclinedCard();
        RestApiHelper.cardInfo(cardInfo, "/api/v1/pay");
        var paymentCardData = SQLHelper.getPaymentCardData();
        assertEquals("DECLINED", paymentCardData.getStatus());
    }
    @Test
    void shouldApproveCredit() {
        var cardInfo = DataHelper.generateDataWithApprovedCard();
        RestApiHelper.cardInfo(cardInfo, "/api/v1/credit");
        var creditCardData = SQLHelper.getCreditCardData();
        assertEquals("APPROVED", creditCardData.getStatus());
    }

    @Test
    void shouldDeclineCredit() {
        var cardInfo = DataHelper.generateDataWithDeclinedCard();
        RestApiHelper.cardInfo(cardInfo, "/api/v1/credit");
        var creditCardData = SQLHelper.getCreditCardData();
        assertEquals("DECLINED", creditCardData.getStatus());
    }


}