package ru.netology.test;

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.logevents.SelenideLogger;
import io.qameta.allure.selenide.AllureSelenide;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.netology.data.DataHelper;
import ru.netology.data.SQLHelper;
import ru.netology.pages.MainPage;

import static com.codeborne.selenide.Selenide.open;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class PaymentTest {
    MainPage mainPage = open("http://localhost:8080/", MainPage.class);

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
    void shouldSuccessTransactionApprovedCard() {
        var toPaymentPage = mainPage.paymentPage();
        var cardInfo = DataHelper.generateDataWithApprovedCard();
        toPaymentPage.insertValidPaymentCardDataForBank(cardInfo);
        toPaymentPage.checkApprovedMessFromBank();
        var paymentCardData = SQLHelper.getPaymentCardData();
        assertEquals("APPROVED", paymentCardData.getStatus());
    }

    @Test
    void shouldSuccessTransactionWithDeclinedCard() {
        var toPaymentPage = mainPage.paymentPage();
        var cardInfo = DataHelper.generateDataWithDeclinedCard();
        toPaymentPage.insertValidPaymentCardDataForBank(cardInfo);
        toPaymentPage.checkErrorMessDeclineFromBank();
        var paymentCardData = SQLHelper.getPaymentCardData();
        assertEquals("DECLINED", paymentCardData.getStatus());
    }

    @Test
    void shouldSuccessTransactionWithMaxAllowedDate() {
        var toPaymentPage = mainPage.paymentPage();
        var currentMonth = DataHelper.getCurrentMonth();
        var maxYear = Integer.parseInt(DataHelper.getCurrentYear()) + 5;
        var cardInfo = DataHelper.generateDataWithApprovedCardAndParametrizedMonthAndYear(currentMonth,
                String.valueOf(maxYear));
        toPaymentPage.insertValidPaymentCardDataForBank(cardInfo);
        toPaymentPage.checkApprovedMessFromBank();
        var paymentCardData = SQLHelper.getPaymentCardData();
        assertEquals("APPROVED", paymentCardData.getStatus());
    }

    //поле карта
    @Test
    void shouldDeclineWithRandomPaymentCard() {
        var toPaymentPage = mainPage.paymentPage();
        var cardInfo = DataHelper.generateDataWithNotApprovedCard();
        toPaymentPage.insertValidPaymentCardDataForBank(cardInfo);
        toPaymentPage.checkErrorMessDeclineFromBank();
    }

    @Test
    void shouldShowErrorIfAllCardNumberFieldAreZero() {
        var toPaymentPage = mainPage.paymentPage();
        var cardInfo = DataHelper.generateDataWithCardNumberFieldAreZero();
        toPaymentPage.insertValidPaymentCardDataForBank(cardInfo);
        toPaymentPage.checkWarningUnderCardNumberField("Неверный формат");
    }

    @Test
    void shouldShowErrorIfAllCardNumberFieldAreEmpty() {
        var toPaymentPage = mainPage.paymentPage();
        var cardInfo = DataHelper.generateDataWithEmptyCardNumber();
        toPaymentPage.insertValidPaymentCardDataForBank(cardInfo);
        toPaymentPage.checkWarningUnderCardNumberField("Неверный формат");
    }

    @Test
    void shouldShowErrorIfAllCardNumberFieldAreSpecialCharacters() {
        var toPaymentPage = mainPage.paymentPage();
        var cardInfo = DataHelper.generateDataWithCardNumberSpecialCharacters();
        toPaymentPage.insertValidPaymentCardDataForBank(cardInfo);
        toPaymentPage.checkWarningUnderCardNumberField("Неверный формат");
    }

    //поле месяц
    @Test
    void shouldDeclineWithFalseMonth() {
        var toPaymentPage = mainPage.paymentPage();
        var cardInfo = DataHelper.generateDataWithApprovedCardAndParametrizedMonthAndYear("90", "23");
        toPaymentPage.insertValidPaymentCardDataForBank(cardInfo);
        toPaymentPage.checkWarningUnderMonthField("Неверно указан срок действия карты");
    }

    @Test
    void shouldShowErrorIfAllMonthNumberFieldAreZero() {
        var toPaymentPage = mainPage.paymentPage();
        var cardInfo = DataHelper.generateDataWithApprovedCardAndAllMonthNumberFieldAreZero();
        toPaymentPage.insertValidPaymentCardDataForBank(cardInfo);
        toPaymentPage.checkWarningUnderCardNumberField("Неверно указан срок действия карты");
    }

    @Test
    void shouldShowErrorIfAllMonthNumberFieldAreEmpty() {
        var toPaymentPage = mainPage.paymentPage();
        var cardInfo = DataHelper.generateDataWithApprovedCardAndAllMonthNumberFieldAreEmpty();
        toPaymentPage.insertValidPaymentCardDataForBank(cardInfo);
        toPaymentPage.checkWarningUnderMonthField("Неверный формат");
    }

    @Test
    void shouldShowErrorIfAllMonthNumberFieldAreSpecialCharacters() {
        var toPaymentPage = mainPage.paymentPage();
        var cardInfo = DataHelper.generateDataWithApprovedCardAndAllMonthNumberFieldAreSpecialCharacters();
        toPaymentPage.insertValidPaymentCardDataForBank(cardInfo);
        toPaymentPage.checkWarningUnderMonthField("Неверный формат");
    }

    //поле год
    @Test
    void shouldDeclineWithFalseYear() {
        var toPaymentPage = mainPage.paymentPage();
        var cardInfo = DataHelper.generateDataWithApprovedCardAndParametrizedMonthAndYear("10", "88");
        toPaymentPage.insertValidPaymentCardDataForBank(cardInfo);
        toPaymentPage.checkWarningUnderYearField("Неверно указан срок действия карты");
    }

    @Test
    void shouldShowErrorIfAllYearNumberFieldAreZero() {
        var toPaymentPage = mainPage.paymentPage();
        var cardInfo = DataHelper.generateDataWithApprovedCardAndAllYearNumberFieldAreZero();
        toPaymentPage.insertValidPaymentCardDataForBank(cardInfo);
        toPaymentPage.checkWarningUnderYearField("Истёк срок действия карты");
    }

    @Test
    void shouldShowErrorIfAllYearNumberFieldAreEmpty() {
        var toPaymentPage = mainPage.paymentPage();
        var cardInfo = DataHelper.generateDataWithApprovedCardAndAllYearNumberFieldAreEmpty();
        toPaymentPage.insertValidPaymentCardDataForBank(cardInfo);
        toPaymentPage.checkWarningUnderYearField("Неверный формат");
    }

    @Test
    void shouldShowErrorIfAllYearNumberFieldAreSpecialCharacters() {
        var toPaymentPage = mainPage.paymentPage();
        var cardInfo = DataHelper.generateDataWithApprovedCardAndAllYearNumberFieldAreSpecialCharacters();
        toPaymentPage.insertValidPaymentCardDataForBank(cardInfo);
        toPaymentPage.checkWarningUnderYearField("Неверный формат");
    }

    //поле владелец
    @Test
    void shouldDeclineWithFalseName() {
        var toPaymentPage = mainPage.paymentPage();
        var cardInfo = DataHelper.generateDataWithParamCardOwnerNameApprovedCard("ПЕТРОВ ВОДКИН");
        toPaymentPage.insertValidPaymentCardDataForBank(cardInfo);
        toPaymentPage.checkWarningUnderCardOwnerField("Неверный формат");
    }

    @Test
    void shouldShowErrorIfAllNameFieldAreZero() {
        var toPaymentPage = mainPage.paymentPage();
        var cardInfo = DataHelper.generateDataWithParamCardOwnerNameApprovedCard("0000 00000");
        toPaymentPage.insertValidPaymentCardDataForBank(cardInfo);
        toPaymentPage.checkWarningUnderCardOwnerField("Неверный формат");
    }

    @Test
    void shouldShowErrorIfAllNameFieldAreEmpty() {
        var toPaymentPage = mainPage.paymentPage();
        var cardInfo = DataHelper.generateDataWithParamCardOwnerNameApprovedCard("");
        toPaymentPage.insertValidPaymentCardDataForBank(cardInfo);
        toPaymentPage.checkWarningUnderCardOwnerField("Поле обязательно для заполнения");
    }

    @Test
    void shouldShowErrorIfAllNameFieldAreSpecialCharacters() {
        var toPaymentPage = mainPage.paymentPage();
        var cardInfo = DataHelper.generateDataWithParamCardOwnerNameApprovedCard("%::.;;(");
        toPaymentPage.insertValidPaymentCardDataForBank(cardInfo);
        toPaymentPage.checkWarningUnderCardOwnerField("Неверный формат");
    }

    //поле cvc
    @Test
    void shouldDeclineWithFalseCvc() {
        var toPaymentPage = mainPage.paymentPage();
        var cardInfo = DataHelper.generateDataWithApprovedCardIfAllCvcNumberFalse();
        toPaymentPage.insertValidPaymentCardDataForBank(cardInfo);
        toPaymentPage.checkWarningUnderCvcField("Неверный формат");
    }

    @Test
    void shouldShowErrorIfAllCvcNumberFieldAreZero() {
        var toPaymentPage = mainPage.paymentPage();
        var cardInfo = DataHelper.generateDataWithApprovedCardIfAllCvcNumberFieldAreZero();
        toPaymentPage.insertValidPaymentCardDataForBank(cardInfo);
        toPaymentPage.checkWarningUnderCvcField("Неверный формат");
    }

    @Test
    void shouldShowErrorIfAllCvcNumberFieldAreEmpty() {
        var toPaymentPage = mainPage.paymentPage();
        var cardInfo = DataHelper.generateDataWithApprovedCardIfAllCvcNumberFieldAreEmpty();
        toPaymentPage.insertValidPaymentCardDataForBank(cardInfo);
        toPaymentPage.checkWarningUnderCvcField("Неверный формат");
    }


    @Test
    void shouldShowErrorIfAllCvcNumberFieldAreSpecialCharacters() {
        var toPaymentPage = mainPage.paymentPage();
        var cardInfo = DataHelper.generateDataWithApprovedCardIfAllCvcNumberFieldAreSpecialCharacters();
        toPaymentPage.insertValidPaymentCardDataForBank(cardInfo);
        toPaymentPage.checkWarningUnderCvcField("Неверный формат");
    }
}
