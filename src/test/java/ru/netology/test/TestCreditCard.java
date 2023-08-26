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
import static ru.netology.data.SQLHelper.getCreditCardData;

public class TestCreditCard {
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
    void shouldSuccessTransactionWithDeclinedCard() {
        var toCreditCard = mainPage.creditPage();
        var cardInfo = DataHelper.generateDataWithDeclinedCard();
        toCreditCard.insertValidCreditCardDataForBank(cardInfo);
        toCreditCard.checkErrorMessDeclineFromBank();
        var creditCardData = SQLHelper.getCreditCardData();
        assertEquals("DECLINED", creditCardData.getStatus());
    }

    @Test
    void shouldSuccessTransactionWithPaymentCard() {
        var toCreditCard = mainPage.creditPage();
        var cardInfo = DataHelper.generateDataWithApprovedCard();
        toCreditCard.insertValidCreditCardDataForBank(cardInfo);
        toCreditCard.checkApprovedMessFromBank();
        var creditCardData = SQLHelper.getCreditCardData();
        assertEquals("APPROVED", creditCardData.getStatus());
    }

    @Test
    void shouldSuccessTransactionWithMaxAllowedDate() {
        var toCreditCard = mainPage.creditPage();
        var currentMonth = DataHelper.getCurrentMonth();
        var maxYear = Integer.parseInt(DataHelper.getCurrentYear()) + 5;
        var cardInfo = DataHelper.generateDataWithApprovedCardAndParametrizedMonthAndYear(currentMonth,
                String.valueOf(maxYear));
        toCreditCard.insertValidCreditCardDataForBank(cardInfo);
        toCreditCard.checkApprovedMessFromBank();
        var creditCardData = SQLHelper.getCreditCardData();
        assertEquals("APPROVED", creditCardData.getStatus());
    }

    //поле карта
    @Test
    void shouldDeclineWithRandomPaymentCard() {
        var toCreditCard = mainPage.creditPage();
        var cardInfo = DataHelper.generateDataWithNotApprovedCard();
        toCreditCard.insertValidCreditCardDataForBank(cardInfo);
        toCreditCard.checkErrorMessDeclineFromBank();
    }

    @Test
    void shouldShowErrorIfAllCardNumberFieldAreZero() {
        var toCreditCard = mainPage.creditPage();
        var cardInfo = DataHelper.generateDataWithCardNumberFieldAreZero();
        toCreditCard.insertValidCreditCardDataForBank(cardInfo);
        toCreditCard.checkWarningUnderCardNumberField("Неверный формат");
    }

    @Test
    void shouldShowErrorIfAllCardNumberFieldAreEmpty() {
        var toCreditCard = mainPage.creditPage();
        var cardInfo = DataHelper.generateDataWithEmptyCardNumber();
        toCreditCard.insertValidCreditCardDataForBank(cardInfo);
        toCreditCard.checkWarningUnderCardNumberField("Неверный формат");
    }

    @Test
    void shouldShowErrorIfAllCardNumberFieldAreSpecialCharacters() {
        var toCreditCard = mainPage.creditPage();
        var cardInfo = DataHelper.generateDataWithCardNumberSpecialCharacters();
        toCreditCard.insertValidCreditCardDataForBank(cardInfo);
        toCreditCard.checkWarningUnderCardNumberField("Неверный формат");
    }

    //поле месяц
    @Test
    void shouldDeclineWithFalseMonth() {
        var toCreditCard = mainPage.creditPage();
        var cardInfo = DataHelper.generateDataWithApprovedCardAndParametrizedMonthAndYear("90", "23");
        toCreditCard.insertValidCreditCardDataForBank(cardInfo);
        toCreditCard.checkWarningUnderMonthField("Неверно указан срок действия карты");
    }

    @Test
    void shouldShowErrorIfAllMonthNumberFieldAreZero() {
        var toCreditCard = mainPage.creditPage();
        var cardInfo = DataHelper.generateDataWithApprovedCardAndAllMonthNumberFieldAreZero();
        toCreditCard.insertValidCreditCardDataForBank(cardInfo);
        toCreditCard.checkWarningUnderCardNumberField("Неверный формат");
    }

    @Test
    void shouldShowErrorIfAllMonthNumberFieldAreEmpty() {
        var toCreditCard = mainPage.creditPage();
        var cardInfo = DataHelper.generateDataWithApprovedCardAndAllMonthNumberFieldAreEmpty();
        toCreditCard.insertValidCreditCardDataForBank(cardInfo);
        toCreditCard.checkWarningUnderMonthField("Неверный формат");
    }

    @Test
    void shouldShowErrorIfAllMonthNumberFieldAreSpecialCharacters() {
        var toCreditCard = mainPage.creditPage();
        var cardInfo = DataHelper.generateDataWithApprovedCardAndAllMonthNumberFieldAreSpecialCharacters();
        toCreditCard.insertValidCreditCardDataForBank(cardInfo);
        toCreditCard.checkWarningUnderMonthField("Неверный формат");
    }

    //поле год
    @Test
    void shouldDeclineWithFalseYear() {
        var toCreditCard = mainPage.creditPage();
        var cardInfo = DataHelper.generateDataWithApprovedCardAndParametrizedMonthAndYear("10", "88");
        toCreditCard.insertValidCreditCardDataForBank(cardInfo);
        toCreditCard.checkWarningUnderYearField("Неверно указан срок действия карты");
    }

    @Test
    void shouldShowErrorIfAllYearNumberFieldAreZero() {
        var toCreditCard = mainPage.creditPage();
        var cardInfo = DataHelper.generateDataWithApprovedCardAndAllYearNumberFieldAreZero();
        toCreditCard.insertValidCreditCardDataForBank(cardInfo);
        toCreditCard.checkWarningUnderYearField("Истёк срок действия карты");
    }

    @Test
    void shouldShowErrorIfAllYearNumberFieldAreEmpty() {
        var toCreditCard = mainPage.creditPage();
        var cardInfo = DataHelper.generateDataWithApprovedCardAndAllYearNumberFieldAreEmpty();
        toCreditCard.insertValidCreditCardDataForBank(cardInfo);
        toCreditCard.checkWarningUnderYearField("Неверный формат");
    }

    @Test
    void shouldShowErrorIfAllYearNumberFieldAreSpecialCharacters() {
        var toCreditCard = mainPage.creditPage();
        var cardInfo = DataHelper.generateDataWithApprovedCardAndAllYearNumberFieldAreSpecialCharacters();
        toCreditCard.insertValidCreditCardDataForBank(cardInfo);
        toCreditCard.checkWarningUnderYearField("Неверный формат");
    }

    //поле владелец
    @Test
    void shouldDeclineWithFalseName() {
        var toCreditCard = mainPage.creditPage();
        var cardInfo = DataHelper.generateDataWithParamCardOwnerNameApprovedCard("ПЕТРОВ ВОДКИН");
        toCreditCard.insertValidCreditCardDataForBank(cardInfo);
        toCreditCard.checkWarningUnderCardOwnerField("Неверный формат");
    }

    @Test
    void shouldShowErrorIfAllNameFieldAreZero() {
        var toCreditCard = mainPage.creditPage();
        var cardInfo = DataHelper.generateDataWithParamCardOwnerNameApprovedCard("0000 00000");
        toCreditCard.insertValidCreditCardDataForBank(cardInfo);
        toCreditCard.checkWarningUnderCardOwnerField("Неверный формат");
    }

    @Test
    void shouldShowErrorIfAllNameFieldAreEmpty() {
        var toCreditCard = mainPage.creditPage();
        var cardInfo = DataHelper.generateDataWithParamCardOwnerNameApprovedCard("");
        toCreditCard.insertValidCreditCardDataForBank(cardInfo);
        toCreditCard.checkWarningUnderCardOwnerField("Поле обязательно для заполнения");
    }

    @Test
    void shouldShowErrorIfAllNameFieldAreSpecialCharacters() {
        var toCreditCard = mainPage.creditPage();
        var cardInfo = DataHelper.generateDataWithParamCardOwnerNameApprovedCard("%::.;;(");
        toCreditCard.insertValidCreditCardDataForBank(cardInfo);
        toCreditCard.checkWarningUnderCardOwnerField("Неверный формат");
    }

    //поле cvc
    @Test
    void shouldDeclineWithFalseCvc() {
        var toCreditCard = mainPage.creditPage();
        var cardInfo = DataHelper.generateDataWithApprovedCardIfAllCvcNumberFalse();
        toCreditCard.insertValidCreditCardDataForBank(cardInfo);
        toCreditCard.checkWarningUnderCvcField("Неверный формат");
    }

    @Test
    void shouldShowErrorIfAllCvcNumberFieldAreZero() {
        var toCreditCard = mainPage.creditPage();
        var cardInfo = DataHelper.generateDataWithApprovedCardIfAllCvcNumberFieldAreZero();
        toCreditCard.insertValidCreditCardDataForBank(cardInfo);
        toCreditCard.checkWarningUnderCvcField("Неверный формат");
    }

    @Test
    void shouldShowErrorIfAllCvcNumberFieldAreEmpty() {
        var toCreditCard = mainPage.creditPage();
        var cardInfo = DataHelper.generateDataWithApprovedCardIfAllCvcNumberFieldAreEmpty();
        toCreditCard.insertValidCreditCardDataForBank(cardInfo);
        toCreditCard.checkWarningUnderCvcField("Неверный формат");
    }

    @Test
    void shouldShowErrorIfAllCvcNumberFieldAreSpecialCharacters() {
        var toCreditCard = mainPage.creditPage();
        var cardInfo = DataHelper.generateDataWithApprovedCardIfAllCvcNumberFieldAreSpecialCharacters();
        toCreditCard.insertValidCreditCardDataForBank(cardInfo);
        toCreditCard.checkWarningUnderCvcField("Неверный формат");
    }
}