package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.CacheLookup;
import org.openqa.selenium.support.FindBy;

import java.time.Duration;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class YoutubePage extends BasePage{
    @CacheLookup
    @FindBy(css = "#expand")
    private WebElement moreLocator;
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
        actions.pause(40).perform();
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
        actions.pause(Duration.ofSeconds(7)).perform();
        return this;
    }

    public List<String> getTimeStamps() {
       return transcrSegTimeStamp.stream().map(WebElement::getText).toList();
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



}
