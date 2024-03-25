package tests;

import base.BaseTest;
import org.testng.Assert;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import pages.YoutubePage;
import util.TranscriptUtil;

import java.io.IOException;
import java.util.List;


public class YoutubePageTests extends BaseTest {
    YoutubePage youtubePage;

    @BeforeMethod
    @Parameters({"youtubeURL"})
    public void setupBrowser(String youtubeURL) {
        getDriver().get(youtubeURL);
    }

    @Test(description = "Get transcript text from every audio excerpt and write to a file")
    public void clickTranscriptButton() throws IOException {
        youtubePage = new YoutubePage(getDriver());
        youtubePage.clickMore().clickTranscript();
        List<String> transcriptList =  youtubePage.createTranscriptList();
        TranscriptUtil.convertTranscriptToFile(transcriptList, "youtubeTranscript");
        Assert.assertEquals(youtubePage.createMap().keySet().size(), youtubePage.getTimeStamps());
    }
}
