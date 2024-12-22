package com.anhtester.Commons;


import org.apache.commons.lang3.RandomStringUtils;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.support.Color;

import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static com.anhtester.Commons.DynamicLocators.*;
import static com.anhtester.Commons.GlobalConstants.*;
import static com.anhtester.driver.DriverManager.getDriver;
import static com.anhtester.report.ExtentTestManager.getExtentTest;
import static org.apache.commons.lang3.RandomStringUtils.randomNumeric;

import com.aventstack.extentreports.Status;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.JavascriptExecutor;
import org.testng.Assert;

public class BasePage {

    private WebDriverWait explicitWait;
    private Alert alert;
    private JavascriptExecutor jsExecutor;
    private final long timeout = LONG_TIMEOUT;
    private final long shortTimeout = SHORT_TIMEOUT;

    /*** ======================== Browser Actions ======================== ***/
    // Factory method để tạo đối tượng BasePage
    public static BasePage getBasePageObject() {
        return new BasePage();
    }

    // Mở một URL bất kỳ
    public void openPageUrl(String pageUrl) {
        getDriver().get(pageUrl);
    }

    // Lấy tiêu đề của trang hiện tại
    protected String getPageTitle() {
        return getDriver().getTitle();
    }

    // Lấy URL hiện tại của trang
    protected String getPageUrl() {
        return getDriver().getCurrentUrl();
    }

    // Lấy mã nguồn HTML của trang
    protected String getPageSourceCode() {
        return getDriver().getPageSource();
    }

    // Điều hướng quay lại trang trước đó
    protected void backToPage() {
        getDriver().navigate().back();
    }

    // Điều hướng tiến tới trang tiếp theo
    protected void forwardToPage() {
        getDriver().navigate().forward();
    }

    // Làm mới (reload) trang hiện tại
    public void refreshCurrentPage() {
        getDriver().navigate().refresh();
    }

    // Lấy tất cả cookie hiện có
    public Set<Cookie> getAllCookies() {
        return getDriver().manage().getCookies();
    }

    // Thêm cookie vào trình duyệt
    public void setCookies(Set<Cookie> cookies) {
        for (Cookie cookie : cookies) {
            getDriver().manage().addCookie(cookie);
        }
        sleepInSecond(3); // Đợi 3 giây để đảm bảo cookie được áp dụng
    }

    // Lấy giá trị sessionId từ cookie
    public static String getSessionId() {
        Cookie sessionCookie = getDriver().manage().getCookieNamed("session_id");
        return (sessionCookie != null) ? sessionCookie.getValue() : null; // Trả về giá trị sessionId nếu tồn tại
    }

    /*** ======================== Utility Methods ======================== ***/
    // Tạm dừng chương trình trong một khoảng thời gian
    private void sleepInSecond(int seconds) {
        try {
            Thread.sleep(seconds * 1000L);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    public void overrideGlobalTimeout(long timeout) {
        getDriver().manage().timeouts().implicitlyWait(Duration.ofSeconds(timeout));
    }

    // Convert RGBA color value to Hex format
    public String getHexaColorFromRGBA(String rgbaValue) {
        return Color.fromString(rgbaValue).asHex();  // Convert the RGBA value to Hex format
    }

    // Get the number of elements matching a locator
    public int getElementsSize(String locatorType) {
        return getListWebElement(locatorType).size();  // Return the size of the list of elements
    }

    // Get the number of elements matching a locator with dynamic values
    public int getElementsSize(String locatorType, String... dynamicValues) {
        return getListWebElement(getDynamicXpath(locatorType, dynamicValues)).size();  // Return the size of the list of elements
    }

    public static String getRandomInt() {
        return randomNumeric(5);
    }

    public static String getRandomString() {
        return RandomStringUtils.randomAlphabetic(5);
    }

    /*** ======================== JavaScript Actions ======================== ***/
    // Thực thi JavaScript
    public Object executeForBrowser(String javaScript, Object... args) {
        JavascriptExecutor jsExecutor = (JavascriptExecutor) getDriver();
        return jsExecutor.executeScript(javaScript, args);
    }

    // Nhập văn bản bằng JavaScript.
    public void sendKeysByJS(String locatorType, String value) {
        WebElement element = this.getWebElement(locatorType);
        String script = "arguments[0].value = arguments[1];";
        executeForBrowser(script, element, value);
    }

    // Lấy thuộc tính của phần tử qua JavaScript
    public String getElementAttributeByJS(String locatorType, String attribute) {
        WebElement element = getWebElement(locatorType);
        return (String) executeForBrowser("return arguments[0].getAttribute(arguments[1]);", element, attribute);
    }

    // Thay đổi giá trị của một thuộc tính phần tử bằng JavaScript
    public void changeValueAttribute(String locatorType, String attributeName, String value) {
        WebElement element = this.getWebElement(locatorType);
        String script = "arguments[0].setAttribute(arguments[1], arguments[2]);";
        executeForBrowser(script, element, attributeName, value);
    }

    // Get element text using JavaScript (in case normal getText() fails)
    public String getElementTextByJS(String locatorType) {
        WebElement element = this.getWebElement(locatorType);
        String script = "return arguments[0].innerText;";
        return (String) executeForBrowser(script, element);
    }

    // Get element text using JavaScript with dynamic values in the locator
    public String getElementTextByJS(String locatorType, String... dynamicValues) {
        WebElement element = this.getWebElement(getDynamicXpath(locatorType, dynamicValues));
        String script = "return arguments[0].innerText;";
        return (String) executeForBrowser(script, element);
    }

    // Scroll to the bottom of the page
    public void scrollToBottomPage() {
        executeForBrowser("window.scrollBy(0, document.body.scrollHeight);");
    }

    // Scroll to the top of the page
    public void scrollToTopPage() {
        executeForBrowser("window.scrollTo(0, 0);");
    }

    // Highlight an element by adding a dashed red border temporarily
    public void highlightElement(String locatorType) {
        WebElement element = this.getWebElement(locatorType);
        String originalStyle = element.getAttribute("style");
        executeForBrowser("arguments[0].setAttribute(arguments[1], arguments[2])", element, "style", "border: 2px solid red; border-style: dashed;");
        sleepInSecond(1);
        executeForBrowser("arguments[0].setAttribute(arguments[1], arguments[2])", element, "style", originalStyle);
    }

    // Overloaded method to highlight an element with dynamic values
    public void highlightElement(String locatorType, String... dynamicValues) {
        WebElement element = this.getWebElement(getDynamicXpath(locatorType, dynamicValues));
        String originalStyle = element.getAttribute("style");
        executeForBrowser("arguments[0].setAttribute(arguments[1], arguments[2])", element, "style", "border: 2px solid red; border-style: dashed;");
        sleepInSecond(1);
        executeForBrowser("arguments[0].setAttribute(arguments[1], arguments[2])", element, "style", originalStyle);
    }

    // Click on an element using JavaScript
    public void clickToElementByJS(String locatorType) {
        executeForBrowser("arguments[0].click();", this.getWebElement(locatorType));
    }

    public void clickToElementByJS(String locatorType, String... dynamicValues) {
        executeForBrowser("arguments[0].click();", this.getWebElement(getDynamicXpath(locatorType, dynamicValues)));
    }

    // Scroll to a specific element
    public void scrollToElement(String locator) {
        executeForBrowser("arguments[0].scrollIntoView(true);", this.getWebElement(locator));
    }

    public void scrollToElement(String locator, String... dynamicValues) {
        executeForBrowser("arguments[0].scrollIntoView(true);", this.getWebElement(getDynamicXpath(locator, dynamicValues)));
    }

    // Remove a specified attribute from an element in the DOM
    public void removeAttributeInDOM(String locator, String attributeRemove) {
        executeForBrowser("arguments[0].removeAttribute('" + attributeRemove + "');", this.getWebElement(locator));
    }

    // Get the inner HTML text of an element using JavaScript
    public String getTextByJs(String locatorType) {
        WebElement element = getDriver().findElement(By.xpath(locatorType));
        return (String) executeForBrowser("return arguments[0].innerHTML;", element);
    }

    // Get the validation message for a form element
    public String getElementValidationMessage(String locator) {
        return (String) executeForBrowser("return arguments[0].validationMessage;", this.getWebElement(locator));
    }

    // Check if an image is fully loaded
    public boolean isImageLoaded(String locatorType) {
        return (boolean) executeForBrowser("return arguments[0].complete && typeof arguments[0].naturalWidth != 'undefined' && arguments[0].naturalWidth > 0;", getWebElement(locatorType));
    }

    // Overloaded method to check if an image is fully loaded with dynamic values
    public boolean isImageLoaded(String locatorType, String... dynamicValues) {
        return (boolean) executeForBrowser("return arguments[0].complete && typeof arguments[0].naturalWidth != 'undefined' && arguments[0].naturalWidth > 0",
                getWebElement(getDynamicXpath(locatorType, dynamicValues)));
    }

    // Upload a file by sending the file path to an input element
    public void uploadFile(String locator, String imagePath) {
        WebElement element = this.getWebElement(locator);
        element.sendKeys(imagePath);
    }

    // Lấy giá trị trong sessionStorage qua JavaScript
    public static String getSessionStorageValue(String key) {
        JavascriptExecutor jsExecutor = (JavascriptExecutor) getDriver();
        return (String) jsExecutor.executeScript("return sessionStorage.getItem('" + key + "')");
    }

    /*** ======================== Dynamic Locators ======================== ***/
    //Lấy XPath động bằng cách định dạng với các giá trị động.
    private String getDynamicXpath(String locator, String... dynamicValues) {
        return String.format(locator, (Object[]) dynamicValues);
    }

    public By getDynamicLocator(String locatorType, String... dynamicValues) {
        String finalLocator = String.format(locatorType, (Object[]) dynamicValues);
        return By.xpath(finalLocator);
    }

    /*** ======================== Alert Handling ======================== ***/
    // Chờ đợi alert xuất hiện
    protected Alert waitForAlertPresence() {
        explicitWait = new WebDriverWait(getDriver(), Duration.ofSeconds(timeout));
        return explicitWait.until(ExpectedConditions.alertIsPresent());
    }

    // Chấp nhận (OK) alert
    protected void acceptAlert() {
        alert = waitForAlertPresence();
        alert.accept();
    }

    // Hủy bỏ (Cancel) alert
    protected void cancelAlert() {
        alert = waitForAlertPresence();
        alert.dismiss();
    }

    // Lấy text từ alert
    public String getAlertText() {
        alert = waitForAlertPresence();
        return alert.getText();
    }

    // Gửi text đến alert (prompt)
    public void sendKeyToAlert(String value) {
        alert = waitForAlertPresence();
        alert.sendKeys(value);
    }

    /*** ======================== Window Handling ======================== ***/
    // Chuyển sang cửa sổ (tab) khác theo ID
    public void switchToWindowByID(String parentID) {
        Set<String> allTabIDs = getDriver().getWindowHandles();
        for (String id : allTabIDs) {
            if (!id.equals(parentID)) {
                getDriver().switchTo().window(id);
                break;
            }
        }
    }

    // Chuyển sang cửa sổ (tab) khác theo tiêu đề
    public void switchToWindowByTitle(String tabTitle) {
        Set<String> allTabIDs = getDriver().getWindowHandles();
        for (String id : allTabIDs) {
            getDriver().switchTo().window(id);
            String currentTitle = getDriver().getTitle();
            if (currentTitle != null && currentTitle.equals(tabTitle)) {
                break;
            }
        }
    }

    // Đóng tất cả các tab trừ tab cha
    public void closeAllTabWithoutParent(String parentID) {
        Set<String> allTabIDs = getDriver().getWindowHandles();
        for (String id : allTabIDs) {
            if (!id.equals(parentID)) {
                getDriver().switchTo().window(id);
                getDriver().close();
            }
        }
        getDriver().switchTo().window(parentID);
    }

    /*** ======================== Element Interaction ======================== ***/
    /* Basic Web Element Interaction */
    // Convert locator string to By object
    public By getByXpath(String locator) {
        return By.xpath(locator);  // Return the locator as By.xpath
    }

    // Lấy WebElement từ XPath.
    public WebElement getWebElement(String locatorType) {
        return getDriver().findElement(By.xpath(locatorType));
    }

    // Lấy danh sách các WebElement từ XPath.
    public List<WebElement> getListWebElement(String locatorType) {
        return getDriver().findElements(By.xpath(locatorType));
    }

    // Nhấn chuột vào phần tử bằng XPath.
    public void clickToElement(String locatorType) {
        try {
            this.getWebElement(locatorType).click();
        } catch (Exception e) {
            handleException(e, locatorType);
        }
    }

    // Nhập văn bản vào phần tử.
    public void sendKeyToElement(String locatorType, String textValue) {
        WebElement element = this.getWebElement(locatorType);
        element.clear();
        element.sendKeys(textValue);
    }

    // Nhập văn bản vào nhiều phần tử
    public void sendKeysToElements(String locatorTypes, String textValue) {
        List<WebElement> elemments = this.getListWebElement(locatorTypes);
        for (WebElement element : elemments) {
            element.sendKeys(textValue);
        }
    }

    // Get the text from an element
    public String getElementText(String locatorType) {
        return this.getWebElement(locatorType).getText();
    }

    // Get the value of an attribute from an element
    public String getElementAttribute(String locatorType, String attributeName) {
        return this.getWebElement(locatorType).getAttribute(attributeName);  // Return the attribute value
    }

    // Get the CSS value of an element (like background-color, font-size, etc.)
    public String getCssValue(String locator, String propertyName) {
        return this.getWebElement(locator).getCssValue(propertyName);  // Return the CSS property value
    }

    /* Handling Dynamic Locators */

    // Nhấn chuột vào phần tử bằng XPath động.
    public void clickToElement(String locatorType, String... dynamicValues) {
        try {
            this.getWebElement(getDynamicXpath(locatorType, dynamicValues)).click();
        } catch (Exception e) {
            handleException(e, locatorType, dynamicValues);
        }
    }

    // Nhập văn bản vào phần tử bằng XPath động.
    public void sendKeyToElement(String locatorType, String textValue, String... dynamicValues) {
        WebElement element = this.getWebElement(getDynamicXpath(locatorType, dynamicValues));
        element.clear();
        element.sendKeys(textValue);
    }

    // Get the text from an element with dynamic values in the locator
    public String getElementText(String locatorType, String... dynamicValues) {
        return this.getWebElement(getDynamicXpath(locatorType, dynamicValues)).getText();
    }

    // Get the value of an attribute with dynamic values in the locator
    public String getElementAttribute(String locatorType, String attributeName, String... dynamicValues) {
        return this.getWebElement(getDynamicXpath(locatorType, dynamicValues)).getAttribute(attributeName);  // Return the attribute value
    }

    /*** ======================== Handling Error Conditions ======================== ***/
    // Xử lý ngoại lệ khi tương tác với phần tử.
    private void handleException(Exception e, String locatorType, String... dynamicValues) {
        String message = "Error interacting with element: " + getDynamicLocator(locatorType, dynamicValues);
        if (e instanceof NoSuchElementException) {
            message += " - Element not found.";
        } else if (e instanceof ElementNotInteractableException) {
            message += " - Element not interactable.";
        } else if (e instanceof TimeoutException) {
            message = "Element not visible after timeout.";
        } else {
            message += " - " + e.getClass().getSimpleName() + ": " + e.getMessage();
        }
        getExtentTest().log(Status.FAIL, message);
        throw new RuntimeException(message);
    }

    /*** ======================== Keyboard & Mouse Actions ======================== ***/
    // Nhấn phím Enter.
    public void pressEnter(int times) {
        Actions actions = new Actions(getDriver());
        for (int i = 0; i < times; i++) {
            actions.sendKeys(Keys.ENTER).perform();
        }
        sleepInSecond(1);
    }

    // Nhấn phím mũi tên một số lần.
    public void pressArrowKey(String keyType, int times) {
        Actions actions = new Actions(getDriver());
        Keys key = switch (keyType.toLowerCase()) {
            case "up" -> Keys.ARROW_UP;
            case "down" -> Keys.ARROW_DOWN;
            case "left" -> Keys.ARROW_LEFT;
            case "right" -> Keys.ARROW_RIGHT;
            default -> throw new IllegalArgumentException("Invalid arrow key type: " + keyType);
        };

        for (int i = 0; i < times; i++) {
            actions.sendKeys(key).perform();
        }
        sleepInSecond(1);
    }

    // Nhấn và giữ chuột vào phần tử.
    public void clickAndHold(String locatorType) {
        Actions actions = new Actions(getDriver());
        actions.clickAndHold(this.getWebElement(locatorType)).moveByOffset(-1000, 0).release().build().perform();
    }

    /**
     * Thực hiện kéo và thả giữa hai phần tử.
     *
     * @param locatorFrom XPath của phần tử nguồn
     * @param locatorTo   XPath của phần tử đích
     */
    public void dragAndDrop(String locatorFrom, String locatorTo) {
        Actions actions = new Actions(getDriver());
        actions.dragAndDrop(this.getWebElement(locatorFrom), this.getWebElement(locatorTo)).build().perform();
    }

    // Clear the value in the input field by sending Ctrl+A + Delete key combination
    public void clearValueInElementByDeleteKey(String locatorType) {
        WebElement element = this.getWebElement(locatorType);
        element.sendKeys(Keys.chord(Keys.CONTROL, "a", Keys.DELETE));
    }

    /**
     * Di chuyển con trỏ chuột đến phần tử được xác định bởi locatorType.
     */
    public void hoverMouseToElement(String locatorType) {
        Actions action = new Actions(getDriver());
        action.moveToElement(this.getWebElement(locatorType)).perform();
    }

    /**
     * Nhấn một phím cụ thể trên phần tử được xác định bởi locatorType.
     *
     * @param locatorType Bộ định vị của phần tử cần tương tác.
     * @param key         Phím cần nhấn (ví dụ: Keys.ENTER, Keys.TAB).
     */
    public void pressKeyToElement(String locatorType, Keys key) {
        Actions action = new Actions(getDriver());
        action.sendKeys(this.getWebElement(locatorType), key).perform();
    }

    /**
     * Nhấn một phím cụ thể trên phần tử được xác định bởi locator động.
     */
    public void pressKeyToElement(String locatorType, Keys key, String... dynamicValues) {
        Actions action = new Actions(getDriver());
        action.sendKeys(this.getWebElement(getDynamicXpath(locatorType, dynamicValues)), key).perform();
    }

    /*** ======================== Dropdown Handling ======================== ***/
    // Select an item from a dropdown using the visible text
    public void selectItemInDefaultDropdown(String locatorType, String textItem) {
        Select select = new Select(this.getWebElement(locatorType));
        select.selectByVisibleText(textItem);  // Select the item by its visible text
    }

    // Select an item from a dropdown using the visible text with dynamic values
    public void selectItemInDefaultDropdown(String locatorType, String textItem, String... dynamicValues) {
        Select select = new Select(this.getWebElement(getDynamicXpath(locatorType, dynamicValues)));
        select.selectByVisibleText(textItem);  // Select the item by its visible text
    }

    // Get the currently selected item from a dropdown
    public String getSelectedItemDefaultDropdown(String locatorType) {
        Select select = new Select(this.getWebElement(locatorType));
        return select.getFirstSelectedOption().getText();  // Return the selected option's text
    }

    // Get the currently selected item from a dropdown with dynamic values
    public String getSelectedItemDefaultDropdown(String locatorType, String... dynamicValues) {
        Select select = new Select(this.getWebElement(getDynamicXpath(locatorType, dynamicValues)));
        return select.getFirstSelectedOption().getText();  // Return the selected option's text
    }

    // Check if the dropdown allows multiple selections
    public boolean isDropDownMultiple(String locatorType) {
        Select select = new Select(this.getWebElement(locatorType));
        return select.isMultiple();  // Return true if the dropdown allows multiple selections
    }

    // Select an item from a custom dropdown (click the parent and then select the item)
    public void selectItemInCustomDropdown(String parentLocator, String childItemLocator, String expectedItem) {
        getWebElement(parentLocator).click();  // Click on the dropdown to expand it
        sleepInSecond(1);  // Sleep for a brief moment to allow the dropdown to open

        explicitWait = new WebDriverWait(getDriver(), Duration.ofSeconds(timeout));  // Wait until elements are present
        List<WebElement> allItems = explicitWait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(getByXpath(childItemLocator)));

        // Iterate through the options and click the one that matches the expected text
        for (WebElement item : allItems) {
            if (item.getText().trim().equals(expectedItem)) {
                jsExecutor = (JavascriptExecutor) getDriver();
                jsExecutor.executeScript("arguments[0].scrollIntoView(true);", item);  // Scroll to the element to ensure it's visible
                sleepInSecond(1);  // Wait a moment
                item.click();  // Click the item
                sleepInSecond(1);  // Wait a moment before proceeding
                break;
            }
        }
    }

    /*** ======================== Checkbox or Radio button Handling ======================== ***/
    // Check element is selected
    public boolean isElementSelected(String locatorType) {
        return this.getWebElement(locatorType).isSelected();
    }

    // Check if a checkbox or radio button is checked, if not, check it
    public void checkToDefaultCheckBoxOrRadio(String locatorType) {
        WebElement element = this.getWebElement(locatorType);
        if (!element.isSelected()) {
            element.click();  // Click to check the checkbox or radio button
        }
    }

    // Check if a checkbox or radio button is checked with dynamic values
    public void checkToDefaultCheckBoxOrRadio(String locatorType, String... dynamicValues) {
        WebElement element = this.getWebElement(getDynamicXpath(locatorType, dynamicValues));
        if (!element.isSelected()) {
            element.click();  // Click to check the checkbox or radio button
        }
    }

    // Uncheck a checkbox if it is checked
    public void uncheckToDefaultCheckBox(String locatorType) {
        WebElement element = this.getWebElement(locatorType);
        if (element.isSelected()) {
            element.click();  // Click to uncheck the checkbox
        }
    }

    // Uncheck a checkbox if it is checked with dynamic values
    public void uncheckToDefaultCheckBox(String locatorType, String... dynamicValues) {
        WebElement element = this.getWebElement(getDynamicXpath(locatorType, dynamicValues));
        if (element.isSelected()) {
            element.click();  // Click to uncheck the checkbox
        }
    }

    /*** ======================== Element Interaction Utilities ======================== ***/
    // Check if an element is displayed
    public boolean isElementDisplay(String locatorType) {
        try {
            return this.getWebElement(locatorType).isDisplayed();  // Return true if the element is visible
        } catch (NoSuchElementException e) {
            return false; // Return false if the element is invisible
        }
    }

    // Check if an element is displayed with dynamic values
    public boolean isElementDisplay(String locatorType, String... dynamicValues) {
        try {
            return getWebElement(getDynamicXpath(locatorType, dynamicValues)).isDisplayed();  // Return true if the element is visible
        } catch (NoSuchElementException e) {
            return false;
        }
    }

    // Wait for an element to be visible
    public void waitForElementVisible(String locator) {
        try {
            explicitWait = new WebDriverWait(getDriver(), Duration.ofSeconds(timeout));
            explicitWait.until(ExpectedConditions.visibilityOfElementLocated(getByXpath(locator)));  // Wait for visibility
        } catch (Exception e) {
            handleException(e, locator);  // Catch any exceptions and handle them
        }
    }

    // Wait for an element to be visible with dynamic values
    public void waitForElementVisible(String locator, String... dynamicValues) {
        try {
            explicitWait = new WebDriverWait(getDriver(), Duration.ofSeconds(timeout));
            explicitWait.until(ExpectedConditions.visibilityOfElementLocated(getByXpath(getDynamicXpath(locator, dynamicValues))));  // Wait for visibility
        } catch (Exception e) {
            handleException(e, locator, dynamicValues);  // Catch any exceptions and handle them
        }
    }

    // Wait for all elements to be visible
    public void waitForAllElementVisible(String locatorType) {
        try {
            explicitWait = new WebDriverWait(getDriver(), Duration.ofSeconds(timeout));
            explicitWait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(getByXpath(locatorType)));  // Wait for all elements to be visible
        } catch (Exception e) {
            handleException(e, locatorType);  // Catch any exceptions and handle them
        }
    }

    // Wait for all elements to be visible with dynamic values
    public void waitForAllElementVisible(String locator, String... dynamicValues) {
        try {
            explicitWait = new WebDriverWait(getDriver(), Duration.ofSeconds(timeout));
            explicitWait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(By.xpath(getDynamicXpath(locator, dynamicValues))));
        } catch (Exception e) {
            handleException(e, locator);  // Catch any exceptions and handle them
        }
    }

    public void waitForElementInvisible(String locator) {
        WebDriverWait explicitWait = new WebDriverWait(getDriver(), Duration.ofSeconds(timeout));
        explicitWait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath(locator)));
    }

    public void waitForAllElementInvisible(String locator) {
        explicitWait = new WebDriverWait(getDriver(), Duration.ofSeconds(timeout));
        explicitWait.until(ExpectedConditions.invisibilityOfAllElements(getListWebElement(locator)));
    }

    public void waitForElementUndisplayed(String locator) {
        explicitWait = new WebDriverWait(getDriver(), Duration.ofSeconds(shortTimeout));
        overrideGlobalTimeout(shortTimeout);
        explicitWait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath(locator)));
        overrideGlobalTimeout(timeout);
    }

    public boolean isElementUndisplayed(String locator, String... params) {
        try {
            overrideGlobalTimeout(shortTimeout);
            List<WebElement> elements = getListWebElement(getDynamicXpath(locator, params));
            overrideGlobalTimeout(timeout);

            return elements.isEmpty() || !elements.get(0).isDisplayed();
        } catch (NoSuchElementException e) {
            return true;
        } catch (Exception e) {
            // Ghi lại ngoại lệ để hỗ trợ gỡ lỗi
            System.err.println("Đã xảy ra lỗi: " + e.getMessage());
            return false;
        } finally {
            // Đảm bảo thời gian chờ được đặt lại ngay cả khi xảy ra ngoại lệ
            overrideGlobalTimeout(timeout);
        }
    }

    // Check if an element is enabled
    public boolean isElementEnable(String locatorType) {
        try {
            return this.getWebElement(locatorType).isEnabled();
        } catch (NoSuchElementException e) {
            return false;
        }
    }

    // Wait for an element to be clickable
    public void waitForElementClickable(String locator) {
        explicitWait = new WebDriverWait(getDriver(), Duration.ofSeconds(timeout));
        explicitWait.until(ExpectedConditions.elementToBeClickable(By.xpath(locator)));
    }

    // Wait for an element to be clickable with dynamic values
    public void waitForElementClickable(String locator, String... dynamicValues) {
        explicitWait = new WebDriverWait(getDriver(), Duration.ofSeconds(timeout));
        explicitWait.until(ExpectedConditions.elementToBeClickable(By.xpath(getDynamicXpath(locator, dynamicValues))));
    }

    // Wait for LoadingIcon to be visible
    public void waitForLoadingIconInvisible() {
        explicitWait = new WebDriverWait(getDriver(), Duration.ofSeconds(timeout));
        explicitWait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath(LOADING_ICON)));
    }


    /*** ======================== DynamicLocators Handling ======================== ***/
    // Enter to dynamic Textbox by ID
    public void inputToTextboxByID(String textboxID, String value) {
        waitForElementVisible(DYNAMIC_TEXTBOX_BY_ID, textboxID);
        sendKeyToElement(DYNAMIC_TEXTBOX_BY_ID, value, textboxID);
    }

    public void inputByPlaceholder(String inputText, String value) {
        waitForElementClickable(DYNAMIC_INPUT_BY_PLACEHOLDER, inputText);
        sendKeyToElement(DYNAMIC_INPUT_BY_PLACEHOLDER, value, inputText);
    }

    public void inputDynamic(String inputText, String value) {
        waitForElementClickable(DYNAMIC_INPUT, inputText);
        sendKeyToElement(DYNAMIC_INPUT, value, inputText);
    }

    public String getErrorInputByPlaceholder(String errorTextInput) {
        waitForElementVisible(DYNAMIC_ERROR_INPUT_BY_PLACEHOLDER, errorTextInput);
        return getElementText(DYNAMIC_ERROR_INPUT_BY_PLACEHOLDER, "value", errorTextInput);
    }

    // Get Dynamic value in Textbox by ID
    public String getTextboxValueByID(String textboxID) {
        waitForElementVisible(DYNAMIC_TEXTBOX_BY_ID, textboxID);
        return getElementAttribute(DYNAMIC_TEXTBOX_BY_ID, "value", textboxID);
    }

    // Click to dynamic Button by text
    public void clickToButtonByText(String buttonText) {
        waitForElementClickable(DYNAMIC_BUTTON_BY_TEXT, buttonText);
        clickToElement(DYNAMIC_BUTTON_BY_TEXT, buttonText);
    }

    public void clickToInputByPlaceholder(String inputText) {
        waitForElementClickable(DYNAMIC_INPUT_BY_PLACEHOLDER, inputText);
        clickToElement(DYNAMIC_INPUT_BY_PLACEHOLDER, inputText);
    }

    //Select to Dynamic Dropdown by name attribute
    public void selectToDropdownByName(String dropDownAttributeName, String itemValue) {
        waitForElementClickable(DYNAMIC_DROPDOWN_BY_NAME, dropDownAttributeName);
        selectItemInDefaultDropdown(DYNAMIC_DROPDOWN_BY_NAME, itemValue, dropDownAttributeName);
    }

    //Select to Dynamic Radio by label
    public void clickToRadioButtonByLabel(String radioButtonLabelName) {
        waitForElementVisible(DYNAMIC_RADIO_BUTTON_BY_LABEL, radioButtonLabelName);
        checkToDefaultCheckBoxOrRadio(DYNAMIC_RADIO_BUTTON_BY_LABEL, radioButtonLabelName);
    }

    //Select to Dynamic Checkbox by label name
    public void clickToCheckbox(String locator, String checkboxLabelName) {
        waitForElementVisible(DYNAMIC_CHECKBOX_BY_TITLE, checkboxLabelName);
        checkToDefaultCheckBoxOrRadio(DYNAMIC_CHECKBOX_BY_TITLE, checkboxLabelName);
    }

    /*** ======================== Iframe Handling ======================== ***/
    // Chuyển sang iframe được xác định bởi locator.
    public void switchToFrameIframe(String locator) {
        getDriver().switchTo().frame(this.getWebElement(locator));
    }

    // Chuyển đổi trở lại nội dung chính (default content) của trang web.
    public void switchToDefaultContent() {
        getDriver().switchTo().defaultContent();
    }

    /*** ======================== Check All Elements ======================== ***/

    // Kiểm tra tất cả các mục có trạng thái như mong đợi hay không
    public boolean verifyAllItemsHaveStatus(String locatorTypes, String statusToCheck) {
        List<WebElement> items = this.getListWebElement(locatorTypes);
        for (WebElement item : items) {
            String status = item.getText();
            if (!status.equals(statusToCheck)) {
                return false;
            }
        }
        return true;
    }

    // Nhấp vào phần tử nhiều lần
    public void clickMultiTimesToElement(String locator, int timeToClick) {
        for (int i = 0; i < timeToClick; i++) {
            clickToElement(locator);
        }
    }

    // Kiểm tra xem nút có được kích hoạt hay không
    public boolean isButtonEnabled(String locatorType) {
        boolean buttonEnabled = false;
        WebElement button = this.getWebElement(locatorType);
        Assert.assertFalse(button.isEnabled());
        return buttonEnabled;
    }

    // Lấy danh sách các văn bản của phần tử từ một locator
    public List<String> getListElementsText(String locatorTypes) {
        List<WebElement> elements = this.getListWebElement(locatorTypes);
        List<String> result = new ArrayList<>();
        for (WebElement e : elements) {
            result.add(e.getText()); // Lấy văn bản của từng phần tử và thêm vào danh sách
        }
        return result;
    }

    // Chờ cho trang tải hoàn tất
    public void waitForPageLoaded() {
        explicitWait = new WebDriverWait(getDriver(), Duration.ofSeconds(timeout));
        try {
            explicitWait.until(driver ->
                    "complete".equals(executeForBrowser("return document.readyState")));
        } catch (TimeoutException e) {
            System.out.println("Timeout waiting for Page Load Request to complete.");
        }
    }

    // Kiểm tra trường nhập liệu có trống hay không
    public boolean isInputFieldBlank(String locatorType) {
        String value = this.getWebElement(locatorType).getAttribute("value");
        return value == null || value.isEmpty();
    }

    // Xóa nội dung trong trường nhập liệu trước khi nhập giá trị mới
    public void clearThenSendKeyToElement(String locatorType, String value) {
        WebElement element = this.getWebElement(locatorType);
        clearValueInElementByDeleteKey(locatorType); // Xóa giá trị hiện tại trong trường
        element.sendKeys(value); // Gửi giá trị mới vào trường
    }

    // Chọn bản ghi đầu tiên trong bảng
    public void selectFirstRecordOnTable(String locatorTable) {
        WebElement table = this.getWebElement(locatorTable);
        scrollToElement(locatorTable + "//child::tbody//child::tr[1]"); // Scroll đến dòng đầu tiên
        clickToElement(locatorTable + "//child::tbody//child::tr[1]"); // Click vào dòng đầu tiên
    }

    // Lấy ngày hiện tại theo định dạng "dd MMM yyyy"
    public static String getCurrentDate() {
        SimpleDateFormat formatter = new SimpleDateFormat("dd MMM yyyy");
        LocalDate currentDate = LocalDate.now();
        return formatter.format(currentDate) + " 06:30:"; // Trả về ngày hiện tại kèm giờ cố định
    }

    // Đếm số dòng trong bảng
    public int countRowNumber(String locatorTable) {
        WebElement table = this.getWebElement(locatorTable);
        List<WebElement> rows = table.findElements(By.xpath("//table/tbody/tr"));
        return rows.size(); // Trả về số lượng dòng trong bảng
    }

    // Lấy giá trị từ bảng
    public List<String> getValueFromTable(String locatorTypes) {
        List<WebElement> columnElements = this.getListWebElement(locatorTypes);
        List<String> columnData = new ArrayList<>();
        for (WebElement element : columnElements) {
            columnData.add(element.getText()); // Lấy giá trị văn bản của từng phần tử trong bảng
        }
        return columnData;
    }

    // Xóa nội dung của textarea qua JavaScript
    public void clearTextAreaByJS(String locatorType) {
        WebElement textArea = this.getWebElement(locatorType);
        executeForBrowser("arguments[0].value='';", textArea); // Xóa nội dung của textarea
    }
}
