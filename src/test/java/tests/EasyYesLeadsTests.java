package tests;

import base.BaseTest;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.devtools.DevTools;
import org.openqa.selenium.devtools.HasDevTools;

import org.openqa.selenium.devtools.v121.network.Network;
import org.openqa.selenium.devtools.v121.network.model.Request;
import org.openqa.selenium.devtools.v121.network.model.Response;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;
import pages.EasyYesLeadsPage;
import util.TestUtil;
import util.TranscriptUtil;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class EasyYesLeadsTests extends BaseTest {
    EasyYesLeadsPage easyYesLeadsPage;
    private final String fileName = "audioLinks";
    private final int pause = 12000;
    private final int expectedListSize = 6;
    @Test(description = "Use selenium devtools to capture network request from click event, then write the captured request data to a txt file")
    @Parameters({"easyYesURL"})
    public void defaultTest(String easyYesURL) throws InterruptedException {
        List<String> audioLinks = new ArrayList<>();
        getDriver().get(easyYesURL);
        DevTools devTools = ((HasDevTools) getDriver()).getDevTools();
        devTools.createSession();
        devTools.send(Network.enable(Optional.empty(), Optional.empty(), Optional.empty()));
        devTools.addListener(Network.requestWillBeSent(), requestConsumer -> {
            Request req = requestConsumer.getRequest();
            if(req.getUrl().endsWith(".mp3")){
                System.out.println(req.getUrl());
                audioLinks.add(req.getUrl());
            }
            try {
                for(String l: audioLinks) {
                    TranscriptUtil.convertTranscriptToFile(audioLinks, fileName);
                }
                Assert.assertFalse(audioLinks.isEmpty());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
        easyYesLeadsPage = new EasyYesLeadsPage(getDriver());
        easyYesLeadsPage.visitIframeSource().getNetworkRequest();
        Assert.assertEquals(audioLinks.size(), expectedListSize);
        devTools.close();

    }
    @Test(description = "Read file of links created from previous test to a list, then transcribe each link from that list to a new file")
    public void transcribeLinksFromFile() throws IOException {
        easyYesLeadsPage = new EasyYesLeadsPage(getDriver());
        List<String> audioLinks = (TranscriptUtil.readFileToList(fileName));
        for (String link: audioLinks) {
            try {
                TranscriptUtil.convertTranscriptToFileFromLink(link, "transcribedLinksFromFile");
                easyYesLeadsPage.pause(pause);
            }catch (SocketTimeoutException e) {
                e.printStackTrace();
            }
        }
        Assert.assertEquals(audioLinks.size(), expectedListSize);
    }
}
