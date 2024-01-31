package tests;

import base.BaseTest;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;
import pages.EmpathTraumaPage;

import java.io.IOException;

public class EmpathTraumaTests extends BaseTest {
    EmpathTraumaPage empathTraumaPage;
    @BeforeMethod
    @Parameters({"empathTraumaURL"})
    public void setupBrowser(String empathTraumaURL) {
        getDriver().get(empathTraumaURL);
    }
    @Test
    public void clickTranscriptButton() throws InterruptedException, IOException {
        empathTraumaPage = new EmpathTraumaPage(getDriver());
        empathTraumaPage.visitIframeSource().clickTranscript().findTranscripts();
        Thread.sleep(20000L);

    }

}
