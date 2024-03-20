package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.CacheLookup;
import org.openqa.selenium.support.FindBy;

import java.time.Duration;

public class YoutubePage extends BasePage{
    @CacheLookup
    @FindBy(css = "#expand .tp-yt-paper-button #waves")
    private WebElement moreLocator;
    @CacheLookup
    @FindBy(css = "#primary #primary-button .yt-spec-touch-feedback-shape")
    private WebElement transcriptButton;
    @CacheLookup
    @FindBy(css="#subscribe-button-shape .yt-spec-touch-feedback-shape__fill")
    private WebElement subscribeButton;
    private By subscribeBtn = By.cssSelector("#subscribe-button-shape .yt-spec-touch-feedback-shape__fill");
    public YoutubePage(WebDriver givenDriver) {
        super(givenDriver);
    }

    public boolean isSubscribeBtnVisible() {
        actions.pause(30).perform();
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


}
