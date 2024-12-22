package com.anhtester.Commons;

public class DynamicLocators {
    public static final String DYNAMIC_TEXTBOX_BY_ID = "//input[@id='%s']";
    public static final String DYNAMIC_TEXTBOX_BY_CLASS = "//input[@class='%s']";
    public static final String DYNAMIC_TEXTBOX_BY_NAME = "//input[@name='%s']";
    public static final String DYNAMIC_TEXTBOX_BY_TEXT = "//label[text()='%s']//parent::td//following-sibling::td/input";
    public static final String DYNAMIC_BUTTON_BY_TEXT = "//button[text()='%s']";
    public static final String DYNAMIC_BUTTON_BY_CLASS = "//button[contains(@class, '%s')]";
    public static final String DYNAMIC_DROPDOWN_BY_NAME = "//select[@name='%s']";
    public static final String DYNAMIC_RADIO_BUTTON_BY_LABEL = "//label[contains(text(),'%s')]//preceding-sibling::input";
    public static final String DYNAMIC_CHECKBOX_BY_LABEL = "//label[contains(text(),'%s')]//following-sibling::input";
    public static final String DYNAMIC_CHECKBOX_BY_TITLE = "//td[@title='%s']";
    public static final String DYNAMIC_INPUT = "//input[@placeholder='%s']";
    public static final String DYNAMIC_INPUT_BY_PLACEHOLDER = "//p[contains(normalize-space(),'%s')]//ancestor::div[contains(@class,'%s')]//input";
    public static final String DYNAMIC_ERROR_INPUT_BY_PLACEHOLDER = "//p[contains(normalize-space(),'%s')]//ancestor::div[contains(@class,'%s')]//p[contains(normalize-space(),'%s')]";
    public static final String LOADING_ICON = "//div[@class='loader']";
    public static final String DYNAMIC_ELEMENT_BY_ATTRIBUTE = "//*[@%s='%s']";

//    String dynamicAttribute = "data-id";
//    String attributeValue = "12345";
//    String xpath = String.format(DYNAMIC_ELEMENT_BY_ATTRIBUTE, dynamicAttribute, attributeValue);
//    WebElement element = driver.findElement(By.xpath(xpath));
    public static final String DYNAMIC_LINK_BY_TEXT = "//a[contains(text(), '%s')]";
    public static final String DYNAMIC_LIST_ITEM = "(//ul[@id='item-list']/li)[%d]";
    public static final String DYNAMIC_ELEMENT = "//input[@type='%s' and @name='%s']";
    public static final String DYNAMIC_PARENT_CHILD = "//div[@class='%s']/button[text()='%s']";
//    String parentClass = "form-container";
//    String buttonText = "Submit";
//    String xpath = String.format(DYNAMIC_PARENT_CHILD, parentClass, buttonText);
//    WebElement submitButton = driver.findElement(By.xpath(xpath));

}
