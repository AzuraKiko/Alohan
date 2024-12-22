package com.anhtester.driver;

import org.openqa.selenium.Capabilities;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.RemoteWebDriver;

public class DriverManager {

    // Sử dụng ThreadLocal để đảm bảo mỗi thread có một instance WebDriver riêng
    private static final ThreadLocal<WebDriver> driver = new ThreadLocal<>();

    private DriverManager() {
        // Private constructor để tránh khởi tạo
    }

    /**
     * Lấy WebDriver hiện tại.
     *
     * @return WebDriver hiện tại hoặc null nếu chưa được khởi tạo
     */
    public static WebDriver getDriver() {
        return driver.get();
    }

    /**
     * Thiết lập WebDriver cho thread hiện tại.
     *
     * @param driver WebDriver instance
     */
    public static void setDriver(WebDriver driver) {
        if (driver == null) {
            throw new IllegalArgumentException("WebDriver instance không được null.");
        }
        DriverManager.driver.set(driver);
    }

    /**
     * Đóng WebDriver hiện tại và xóa ThreadLocal.
     */
    public static void quit() {
        WebDriver currentDriver = DriverManager.getDriver();
        if (currentDriver != null) {
            currentDriver.quit();
            driver.remove(); // Xóa ThreadLocal sau khi quit
        }
    }

    /**
     * Lấy thông tin trình duyệt, phiên bản và hệ điều hành.
     *
     * @return Chuỗi thông tin của trình duyệt
     */
    public static String getInfo() {
        WebDriver currentDriver = DriverManager.getDriver();
        if (currentDriver == null) {
            return "WebDriver chưa được khởi tạo.";
        }
        if (!(currentDriver instanceof RemoteWebDriver)) {
            return "Không thể lấy thông tin từ WebDriver không phải RemoteWebDriver.";
        }
        Capabilities cap = ((RemoteWebDriver) currentDriver).getCapabilities();
        String browserName = cap.getBrowserName();
        String platform = cap.getPlatformName().toString();
        String version = cap.getBrowserVersion();
        return String.format("Browser: %s, Version: %s, Platform: %s", browserName, version, platform);
    }
}
