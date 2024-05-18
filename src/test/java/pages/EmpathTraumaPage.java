package pages;

import org.openqa.selenium.*;
import org.openqa.selenium.support.FindBy;

import java.util.ArrayList;
import java.util.List;

public class EmpathTraumaPage extends BasePage {
    @FindBy(css = "#transcript-list.TranscriptList_lazy_module_list__d5b2f860")
    private WebElement transcriptList;
    @FindBy(xpath = "//div[@id='custom-code-ecQYSk353l']/div/iframe")
    private WebElement iframe;
    @FindBy(xpath = "//button[@id='transcript-control-bar-button']")
    private WebElement transcriptButton;
    @FindBy(xpath = "//ul[@id='transcript-list']/li/span[1]")
    private List<WebElement> transcriptTexts;
    @FindBy(xpath = "//ul[@id='transcript-list']/li[1]")
    private WebElement firstTranscript;
    public EmpathTraumaPage(WebDriver givenDriver) {
        super(givenDriver);
    }
    public EmpathTraumaPage switchToIframe() {
        switchToIframe(iframe);
        return this;
    }
    public EmpathTraumaPage clickTranscript() {
        WebElement button = driver.findElement(By.id("transcript-control-bar-button"));
        button.click();
        return this;
    }
    private WebElement lazyLoadElement(int i) {
        String locator = String.format("#transcript-cue-%s span:nth-child(1)", i);
        return find(By.cssSelector(locator));
    }
    public List<String> findTranscripts() {
        int count = 0;
        int limit = 14;
        int maxSections = 868;
        List<String> allText = new ArrayList<>();
        try{
            while (count < limit) {
                String text = lazyLoadElement(count).getText();
                allText.add(text);
                count++;
                }
            } catch(TimeoutException e){
               e.printStackTrace();
            } finally {
                for (int j = count; j <= maxSections; j ++) {
                    scrollIntoView(j-1);
                    String transcriptText = lazyLoadElement(j).getText();
                    allText.add(transcriptText);
                    count++;
                }
            System.out.println(allText);
         }
        return allText;
    }
    private void scrollIntoView(int count) {
        WebElement textPortion = lazyLoadElement(count);
        javascriptExecutor.executeScript("arguments[0].scrollIntoView(true);", textPortion);
    }
    private String getTranscriptListInnerText() {
        String transcriptInnerText = findElement(transcriptList).getAttribute("innerText");
        System.out.println(transcriptInnerText);
        return transcriptInnerText;
    }

     public EmpathTraumaPage visitIframeSource() {
        String iframeSrc = getAttribute(iframe, "src");
        driver.switchTo().newWindow(WindowType.WINDOW);
        driver.get(iframeSrc);
        return this;
    }
}
