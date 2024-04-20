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

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.stream.Collectors;

import static util.TranscriptUtil.convertTranscriptToFileFromLink;

public class EasyYesLeadsTests extends BaseTest {
    EasyYesLeadsPage easyYesLeadsPage;
    private final String fileName = "audioLinks";
    private final int pause = 12000;
    private final int expectedListSize = 6;


    @Test(description = "Use selenium devtools to capture network request from click event, then write the captured request data to a txt file")
    @Parameters({"easyYesURL"})
    public void defaultTest(String easyYesURL)  {
        List<String> audioLinks = new ArrayList<>();
        getDriver().get(easyYesURL);
        DevTools devTools = ((HasDevTools) getDriver()).getDevTools();
        devTools.createSession();
        devTools.send(Network.enable(Optional.empty(), Optional.empty(), Optional.empty()));
        devTools.addListener(Network.requestWillBeSent(), requestConsumer -> {
            Request req = requestConsumer.getRequest();
            if (req.getUrl().endsWith(".mp3")) {
                System.out.println(req.getUrl());
                audioLinks.add(req.getUrl());
            }
            try {
                for (String l : audioLinks) {
                    Path filePath = Path.of(System.getProperty("easyYesLeadsDir") + File.separator +fileName+".txt");
                    TranscriptUtil.convertTranscriptToFile(audioLinks, filePath);
                }
                Assert.assertFalse(audioLinks.isEmpty());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
        easyYesLeadsPage = new EasyYesLeadsPage(getDriver());
        easyYesLeadsPage.visitIframeSource().getNetworkRequest();
        Assert.assertEquals(audioLinks.size(), expectedListSize);
        devTools.clearListeners();
        devTools.close();

    }
    @Test(description = "Read file of links created from previous test to a list, then transcribe each link from that list to a new file")
    public void transcribeLinksFromFile() throws IOException {
        easyYesLeadsPage = new EasyYesLeadsPage(getDriver());
        List<String> audioLinks = (TranscriptUtil.readFileToList(fileName));
        for (String link : audioLinks) {
            try {
                convertTranscriptToFileFromLink(link, "transcribedLinksFromFile");
                easyYesLeadsPage.pause(pause);
            } catch (SocketTimeoutException e) {
                e.printStackTrace();
            }
        }
        Assert.assertEquals(audioLinks.size(), expectedListSize);
    }
    @Test(description = "Asynchronously read links from file into a list, then transcribe the links to a new file using CompletableFuture")
    public void transcribe() throws ExecutionException, InterruptedException {
        easyYesLeadsPage = new EasyYesLeadsPage(getDriver());
        TranscriptUtil.asyncTranscribeLinks();
        Path filePath = Path.of("src/test/resources/asyncTranscribeFromLinks.txt");
        boolean exists = Files.exists(filePath);
        Assert.assertTrue(exists);
    }
}