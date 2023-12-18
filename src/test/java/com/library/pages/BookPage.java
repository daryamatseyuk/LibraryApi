package com.library.pages;

import com.library.utility.db.DB_Util;
import com.library.utility.ui.Driver;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class BookPage extends BasePage {

    @FindBy(xpath = "//table/tbody/tr/td")
    public List<WebElement> allRows;

    @FindBy(xpath = "//thead/tr/th")
    public List<WebElement> allHeaders;

    @FindBy(xpath = "//input[@type='search']")
    public WebElement search;

    @FindBy(id = "book_categories")
    public WebElement mainCategoryElement;

    @FindBy(name = "name")
    public WebElement bookName;


    @FindBy(xpath = "(//input[@type='text'])[4]")
    public WebElement author;

    @FindBy(xpath = "//div[@class='portlet-title']//a")
    public WebElement addBook;

    @FindBy(xpath = "//button[@type='submit']")
    public WebElement saveChanges;

    @FindBy(xpath = "//div[@class='toast-message']")
    public WebElement toastMessage;

    @FindBy(name = "year")
    public WebElement year;

    @FindBy(name = "isbn")
    public WebElement isbn;

    @FindBy(id = "book_group_id")
    public WebElement categoryDropdown;


    @FindBy(id = "description")
    public WebElement description;



    public WebElement editBook(String book) {
        String xpath = "//td[3][.='" + book + "']/../td/a";
        return Driver.getDriver().findElement(By.xpath(xpath));
    }

    public WebElement borrowBook(String book) {
        String xpath = "//td[3][.='" + book + "']/../td/a";
        return Driver.getDriver().findElement(By.xpath(xpath));
    }

    public Map<String, Object> mapOfBookInfo(){
        Map<String, Object> bookInfo = new LinkedHashMap<>();
        WebDriverWait wait = new WebDriverWait(Driver.getDriver(), Duration.ofSeconds(5));
        wait.until(ExpectedConditions.refreshed((ExpectedConditions.visibilityOfAllElements(allRows))));
        wait.until(ExpectedConditions.refreshed((ExpectedConditions.visibilityOfAllElements(allHeaders))));


        for (int i = 1; i < allHeaders.size()-1; i++) {
            bookInfo.put(allHeaders.get(i).getText().toLowerCase(),allRows.get(i).getText());
        }

        return bookInfo;
    }






}
