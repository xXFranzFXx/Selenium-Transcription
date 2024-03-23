package tests;

import base.BaseTest;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import pages.YoutubePage;
import util.TranscriptUtil;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class YoutubePageTests extends BaseTest {
    YoutubePage youtubePage;

    @BeforeMethod
    @Parameters({"youtubeURL"})
    public void setupBrowser(String youtubeURL) {
        getDriver().get(youtubeURL);
    }

    @Test(description = "Get transcript text from every audio excerpt and store it in a List")
    public void clickTranscriptButton() throws IOException {
        youtubePage = new YoutubePage(getDriver());
        youtubePage.clickMore().clickTranscript();
        List<String> transcriptList =  youtubePage.mapToList();
        TranscriptUtil.convertTranscriptToFile(transcriptList, "youtubeTranscript");
        Assert.assertEquals(youtubePage.createMap().keySet().size(), youtubePage.getTimeStamps().size());
    }
}
