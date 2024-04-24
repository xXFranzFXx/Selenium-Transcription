package pages;

import org.checkerframework.checker.signature.qual.FqBinaryName;
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

public class YoutubePage extends BasePage{
    @CacheLookup
    @FindBy(css = "#expand")
    private WebElement moreLocator;
    @FindBy(css =".ytp-chrome-bottom button.ytp-subtitles-button.ytp-button")
    private WebElement closedCaptionButtons;
    @FindBy(css=".ytp-button.ytp-settings-button.ytp-hd-quality-badge")
    private WebElement settingsButton;
    @FindBy(css = "//div[@id='ytp-id-56']//div[@class='ytp-menuitem']")
    private List<WebElement> settingsMenu;
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
    @FindBy(css = ".ytp-ad-skip-button-modern.ytp-button")
    private WebElement skipButton;
    @CacheLookup
    @FindBy(css="#subscribe-button-shape .yt-spec-touch-feedback-shape__fill")
    private WebElement subscribeButton;
    @FindBy(xpath = "//h1//yt-formatted-string[@class='style-scope ytd-watch-metadata']")
    private WebElement videoTitle;
    private final By subscribeBtn = By.cssSelector("#subscribe-button-shape button");
    private final By cc = By.cssSelector(".ytp-chrome-bottom button.ytp-subtitles-button.ytp-button");
    public YoutubePage(WebDriver givenDriver) {
        super(givenDriver);
    }

    private boolean isSubscribeBtnVisible() {
        WebElement elementWait = new WebDriverWait(
                driver, Duration.ofSeconds(35))
                .until(ExpectedConditions.presenceOfElementLocated(subscribeBtn));
        return elementWait.isDisplayed();
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
    public YoutubePage clickDismiss() {
        WebElement dismissBtn = new WebDriverWait(
                driver, Duration.ofSeconds(35))
                .until(ExpectedConditions.presenceOfElementLocated(By.xpath("//yt-button-renderer[@id='dismiss-button']")));
       if(dismissBtn.isDisplayed()) {
          dismissBtn.click();
           clickMore();
           clickTranscript();
       } else {
           clickMore();
           clickTranscript();
       }
       return this;
    }
    public int getTimeStamps() {
        return getTimeStampsList().size();
    }
    private List<String> getTimeStampsList() {
        List<WebElement> timeStamps = wait.until(
                ExpectedConditions.visibilityOfAllElementsLocatedBy(
                        By.cssSelector("ytd-transcript-segment-renderer[rounded-container] .segment-timestamp.ytd-transcript-segment-renderer")));
        return timeStamps.stream()
                .map(WebElement::getText)
                .toList();
    }
    private List<String> getSegmentText() {
     return transcrSegText.stream()
             .map(WebElement::getText)
             .toList();
    }
    public String videoTitle() {
        return findElement(videoTitle).getText();
    }
    public String ccSubtitleLanguage() {
        findElement(settingsButton).click();
        WebElement ccSetings = settingsMenu.get(2);
        return ccSetings.getText();
    }

    public Map<String, String> createMap() {
        return IntStream.range(0, getTimeStamps())
                .boxed()
                .collect(Collectors.toMap(
                        getTimeStampsList()::get, getSegmentText()::get));
    }
    public List<String> createTranscriptList() {
        List<String> timeStamps = getTimeStampsList();
        List<String> segmentTexts = getSegmentText();
        List<String> transcript = new ArrayList<>(
                IntStream.range(0, Math.min(timeStamps.size(), segmentTexts.size()))
               .mapToObj(i -> timeStamps.get(i) + " : " + segmentTexts.get(i))
               .toList());
        transcript.addFirst("language: " + getTranscriptLanguage());
        transcript.addFirst("video url: " + driver.getCurrentUrl());
        transcript.addFirst("title: " + getTitle());
        return transcript;
    }
    private String getTitle() {
        return findElement(title).getText();
    }
    private String getTranscriptLanguage() {
        return findElement(transcriptLang).getText();
    }
    public YoutubePage clickCCBtn(){

        if (isSubscribeBtnVisible()) {
            actions.sendKeys(Keys.SPACE).pause(Duration.ofSeconds(7)).perform();
            WebElement skp = wait.pollingEvery(Duration.ofSeconds(7))
                    .until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("[class^='ytp-ad-skip-button-modern']")));
                actions.moveToElement(skp).click().perform();
        }
        findElement(closedCaptionButtons).click();
        actions.pause(Duration.ofSeconds(2)).perform();
        return this;
    }
    public YoutubePage clickCCBtnNoAds(){
        if(elementIsPresent(cc)) {
            clickElement(closedCaptionButtons);
        } else if (isSubscribeBtnVisible()) {
            actions.sendKeys(Keys.SPACE).perform();
            clickElement(closedCaptionButtons);
        }
        actions.pause(Duration.ofSeconds(2)).perform();
        return this;
    }

}
