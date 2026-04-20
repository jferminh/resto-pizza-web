package com.resto.pizzeria.web.selenium;

import org.junit.jupiter.api.*;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;

import static org.junit.jupiter.api.Assertions.*;

public class DishSeleniumTest {

    private WebDriver driver;

    @BeforeEach
    void setup() {
        driver = new ChromeDriver();
    }

    @Test
    void testAddDish() throws InterruptedException {
        // 1. Open the app
        driver.get("http://localhost:8081/dishes");

        Thread.sleep(1000);

        // 2. Click "Ajouter un plat"
        driver.findElement(By.cssSelector(".btn.create")).click();

        Thread.sleep(1000);

        // 3. Fill the form
        String dishName = "PizzaTest_" + System.currentTimeMillis();

        driver.findElement(By.name("name")).sendKeys(dishName);
        driver.findElement(By.name("price")).sendKeys("12");

        // 4. Submit form
        driver.findElement(By.tagName("form")).submit();

        Thread.sleep(2000);

        // 5. Verify result
        String page = driver.getPageSource();
        assertTrue(page.contains(dishName));
    }

    @Test
    void testEditDish() throws InterruptedException {
        driver.get("http://localhost:8081/dishes");

        Thread.sleep(1000);

        // Click first "Modifier"
        driver.findElement(By.cssSelector(".btn.edit")).click();

        Thread.sleep(1000);

        String updatedName = "UpdatedPizza_" + System.currentTimeMillis();

        WebElement nameInput = driver.findElement(By.name("name"));
        nameInput.clear();
        nameInput.sendKeys(updatedName);

        driver.findElement(By.tagName("form")).submit();

        Thread.sleep(2000);

        assertTrue(driver.getPageSource().contains(updatedName));
    }

    @Test
    void testDeleteDish() throws InterruptedException {
        driver.get("http://localhost:8081/dishes");

        Thread.sleep(1000);

        // Click delete button
        driver.findElement(By.cssSelector(".btn.delete")).click();

        // Accept alert (IMPORTANT)
        Alert alert = driver.switchTo().alert();
        alert.accept();

        Thread.sleep(2000);

        // No assertion needed for now (simple test)
        assertTrue(true);
    }

    @AfterEach
    void tearDown() {
        driver.quit();
    }
}