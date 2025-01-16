package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;

import java.time.Duration;
import java.util.List;

public class M3u8Page extends BasePage{
    @FindBy(css = "video[id^='wistia_simple_video' and type='video/m3u8']")
    private WebElement videoPlayer;
    @FindBy(css = "#m3u8-placeholder")
    private WebElement input;
    @FindBy(css = "[title='Play Video']")
    private List<WebElement> playBtn;
    public M3u8Page(WebDriver givenDriver) {
        super(givenDriver);
    }
    public List<WebElement> subtitleRows() {
        return driver.findElement(By.tagName("video")).getShadowRoot()
                .findElements(By.cssSelector(".image-overlay-line .image-overlay-text"));
    }
    public M3u8Page clickPlay() {
        WebElement btn = wait.until(ExpectedConditions.elementToBeClickable(playBtn.get(0)));
        findElement(btn).click();
        return this;
    }
    public M3u8Page pause(int seconds) {
        actions.pause(Duration.ofSeconds(seconds)).perform();
        return this;
    }
}
