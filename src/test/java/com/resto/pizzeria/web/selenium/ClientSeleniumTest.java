package com.resto.pizzeria.web.selenium;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@Tag("selenium")
class ClientSeleniumTest {
    private WebDriver driver;
    private WebDriverWait wait;

    @BeforeEach
    void setUp() {
        driver = new ChromeDriver();
        wait = new WebDriverWait(driver, Duration.ofSeconds(5));
    }

    @AfterEach
    void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }

    void verifyForm(
            final String firstNameText,
            final String lastNameText) {
        final WebElement firstName = driver.findElement(By.id("firstName"));
        final WebElement lastName = driver.findElement(By.id("lastName"));

        firstName.clear();
        lastName.clear();

        firstName.sendKeys(firstNameText);
        lastName.sendKeys(lastNameText);

        driver.findElement(By.cssSelector("button[type='submit']")).click();

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));

        wait.until(d ->
                d.findElements(By.cssSelector("tr.table-entry"))
                        .stream()
                        .anyMatch(r -> r.getText().contains(lastNameText))
        );

        assertTrue(
                driver.findElements(By.cssSelector("tr.table-entry"))
                        .stream()
                        .anyMatch(r -> r.getText().contains(firstNameText))
        );

        assertTrue(
                driver.findElements(By.cssSelector("tr.table-entry"))
                        .stream()
                        .anyMatch(r -> r.getText().contains(lastNameText))
        );
    }

    void verifyGoodCreation(
            final String firstNameText,
            final String lastNameText) {
        driver.findElement(By.cssSelector("a.btn.create")).click();
        verifyForm(firstNameText, lastNameText);
    }

    void verifyGoodModification(
            final String firstNameText,
            final String lastNameText,
            final String newFirstNameText,
            final String newLastNameText) {
        final List<WebElement> rows = driver.findElements(By.cssSelector("tr.table-entry"));

        WebElement targetRow = null;

        for (final WebElement row : rows) {
            final String text = row.getText();

            if (text.contains(firstNameText) && text.contains(lastNameText)) {
                targetRow = row;
                break;
            }
        }

        assertNotNull(targetRow);

        targetRow.findElement(By.cssSelector("a.btn.edit")).click();

        verifyForm(newFirstNameText, newLastNameText);
    }

    void verifySuppression(
            final String firstNameText,
            final String lastNameText) {
        final List<WebElement> rows = driver.findElements(By.cssSelector("tr.table-entry"));

        WebElement deleteBtn = null;

        for (final WebElement row : rows) {
            if (row.getText().contains(firstNameText)) {

                deleteBtn = row.findElement(By.cssSelector("button.btn.delete"));

                break;
            }
        }

        assertNotNull(deleteBtn);

        deleteBtn.click();

        driver.findElement(By.id("confirmDeleteBtn")).click();

        final WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));

        wait.until(d -> {
            for (WebElement row : d.findElements(By.cssSelector("tr.table-entry"))) {
                try {
                    if (row.getText().contains(firstNameText)) {
                        return false;
                    }
                } catch (Exception ignored) {}
            }
            return true;
        });

        assertTrue(
                driver.findElements(By.cssSelector("tr.table-entry"))
                        .stream()
                        .noneMatch(r -> r.getText().contains(firstNameText))
        );
    }

    @Test
    void validateCrud() {
        driver.get("http://localhost:8081/clients");

        final String suffix = UUID.randomUUID().toString().substring(0, 8);
        final String firstNameText = "TEST_FIRST_NAME_" + suffix;
        final String lastNameText = "TEST_LAST_NAME_" + suffix;

        verifyGoodCreation(firstNameText, lastNameText);

        final String newLastNameText = lastNameText + "_U";
        final String newFirstNameText = firstNameText + "_U";

        verifyGoodModification(
                firstNameText,
                lastNameText,
                newFirstNameText,
                newLastNameText);

        verifySuppression(newFirstNameText, newLastNameText);
    }
}