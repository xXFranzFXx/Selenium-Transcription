package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.WindowType;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;

import java.util.List;

public class EasyYesLeadsPage extends BasePage{
    @FindBy(css = "#clickpop.et_pb_section.et_pb_section_2.popup.et_pb_with_background.et_section_regular.with-close.dark.is-open")
    private WebElement popUp;
    @FindBy(css = "span.da-close-wrap.evr-close_wrap a.da-close.evr-close")
    private WebElement closeButton;
    @FindBy(xpath = "//article[@id='post-3858']//div/iframe")
    private WebElement iframe;
    @FindBy(xpath ="//div[@id='amplitude-right']//div[2]/span")
    private List<WebElement> tracks;
    public EasyYesLeadsPage(WebDriver givenDriver) {
        super(givenDriver);
    }

    public EasyYesLeadsPage switchToIframe() {
       switchToIframe(iframe);
        return this;
    }
    public EasyYesLeadsPage closePopup() {
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("#clickpop.et_pb_section.et_pb_section_2.popup.et_pb_with_background.et_section_regular.with-close.dark.is-open")));
        actions.moveToElement(closeButton).click().perform();
        return this;
    }
    public EasyYesLeadsPage getNetworkRequest() {
        for(WebElement t: tracks) {
            actions.moveToElement(t).click().perform();
        }
        return this;
    }
    public String getIframeSrc() {
        return getAttribute(iframe, "src");
    }
    public EasyYesLeadsPage visitIframeSource() {
        String iframeSrc = getIframeSrc();
        driver.switchTo().newWindow(WindowType.WINDOW);
        driver.get(iframeSrc);
        return this;
    }
    public EasyYesLeadsPage pause(int seconds) {
        actions.pause(seconds).perform();
        return this;
    }

}
