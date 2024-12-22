package com.anhtester.projects.cms.testcases;

import com.anhtester.Commons.BasePage;
import com.anhtester.driver.DriverManager;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.Test;
import io.github.bonigarcia.wdm.WebDriverManager;

import java.time.Duration;

public class SignUpAccount extends BasePage {

    @Test
    public void signUp() {
        WebDriverManager.chromedriver().setup();
        WebDriver driver = new ChromeDriver();
        DriverManager.setDriver(driver);


        driver.manage().window().maximize();
        driver.get("https://alohan.bigmouth.com.vn/");
        waitForPageLoaded();

        clickToElement("//div[@class='nav-header_pc']//div[@class='nav-header_menu_item'][contains(text(),'Liên hệ')]");
        System.out.println(getWebElement("//div[@class='nav-header_pc']//div[@class='nav-header_menu_item'][contains(text(),'Liên hệ')]").getText());

//        driver.quit();
    }
}