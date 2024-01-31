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
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class EasyYesLeadsTests extends BaseTest {
    EasyYesLeadsPage easyYesLeadsPage;
    @Test
    @Parameters({"easyYesURL"})
    public void defaultTest(String easyYesURL) throws InterruptedException {
        List<String> audioLinks = new ArrayList<>();
        getDriver().get(easyYesURL);
        DevTools devTools = ((HasDevTools) getDriver()).getDevTools();
        devTools.createSession();
        devTools.send(Network.enable(Optional.empty(), Optional.empty(), Optional.empty()));
        devTools.addListener(Network.requestWillBeSent(), requestConsumer -> {
            Request res = requestConsumer.getRequest();
            if(res.getUrl().endsWith(".mp3")){
                System.out.println(res.getUrl());
                audioLinks.add(res.getUrl());
            }
            try {
                for(String l: audioLinks) {
                    TranscriptUtil.convertTranscriptToFile(audioLinks, "audioLinks");
                }
                Assert.assertFalse(audioLinks.isEmpty());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
        easyYesLeadsPage = new EasyYesLeadsPage(getDriver());
        easyYesLeadsPage.visitIframeSource().getNetworkRequest();
        devTools.close();
    }
}
