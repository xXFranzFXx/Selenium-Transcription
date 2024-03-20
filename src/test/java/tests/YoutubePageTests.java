package tests;

import base.BaseTest;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;
import pages.BasePage;
import pages.EmpathTraumaPage;
import pages.YoutubePage;

import java.io.IOException;
import java.util.List;

public class YoutubePageTests extends BaseTest {
    YoutubePage youtubePage;
    @BeforeMethod
    @Parameters({"youtubeURL"})
    public void setupBrowser(String youtubeURL) {
        getDriver().get(youtubeURL);
    }
    @Test(description = "Get transcript text from every audio excerpt and store it in a List")
    public void clickTranscriptButton(){
        youtubePage = new YoutubePage(getDriver());
        youtubePage.clickMore().clickTranscript();
    }
}
