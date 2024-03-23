package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.CacheLookup;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class YoutubePage extends BasePage{
    @CacheLookup
    @FindBy(css = "#expand")
    private WebElement moreLocator;
    @FindBy(css = "#label-text")
    private WebElement transcriptLang;
    @FindBy(css = "#title > h1 > yt-formatted-string")
    private WebElement title;
    @FindBy(css = "ytd-transcript-segment-renderer[rounded-container] .segment-timestamp.ytd-transcript-segment-renderer")
    private List<WebElement> transcrSegTimeStamp;
    @FindBy(css = ".segment.ytd-transcript-segment-renderer .segment-text.ytd-transcript-segment-renderer")
    private List<WebElement> transcrSegText;
    @CacheLookup
    @FindBy(css = "#primary #primary-button .yt-spec-touch-feedback-shape")
    private WebElement transcriptButton;
    @CacheLookup
    @FindBy(css="#subscribe-button-shape .yt-spec-touch-feedback-shape__fill")
    private WebElement subscribeButton;
    private By subscribeBtn = By.cssSelector("#subscribe-button-shape button");
    public YoutubePage(WebDriver givenDriver) {
        super(givenDriver);
    }

    public boolean isSubscribeBtnVisible() {
        WebElement elementWait = new WebDriverWait(driver, Duration.ofSeconds(35)).until(ExpectedConditions.presenceOfElementLocated(subscribeBtn));
        return find(subscribeBtn).isDisplayed();
    }
    public YoutubePage clickMore() {
        if (isSubscribeBtnVisible()) {
            actions.sendKeys(Keys.SPACE).perform();
            findElement(moreLocator).click();
        }
        return this;
    }
    public YoutubePage clickTranscript() {
        actions.moveToElement(transcriptButton).perform();
        clickElement(transcriptButton);
        return this;
    }

    public List<String> getTimeStamps() {
        List<WebElement> timeStamps = wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(By.cssSelector("ytd-transcript-segment-renderer[rounded-container] .segment-timestamp.ytd-transcript-segment-renderer")));
        return timeStamps.stream().map(WebElement::getText).toList();
    }
    public List<String> getSegmentText() {
     return transcrSegText.stream().map(WebElement::getText).toList();
    }
    public Map<String, String> createMap() {
        return IntStream.range(0, getTimeStamps().size())
                .boxed()
                .collect(Collectors.toMap(
                        getTimeStamps()::get, getSegmentText()::get));
    }
    public List<String> mapToList() {
        Map<String, String> newMap = new HashMap<>(createMap());
        Set<Map.Entry<String, String>> entries = newMap.entrySet();
        List<String> transcriptList = new ArrayList<>(entries.stream().map(entry -> entry.getKey() + " " + entry.getValue()).sorted().toList());
        transcriptList.addFirst(getTranscriptLanguage());
        transcriptList.addFirst("video url: " + driver.getCurrentUrl());
        transcriptList.addFirst(getTitle());
        return transcriptList;
    }

    public String getTitle() {
        return findElement(title).getText();
    }
    public String getTranscriptLanguage() {
        return findElement(transcriptLang).getText();
    }
}
