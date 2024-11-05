package util.listeners;

import com.aventstack.extentreports.Status;
import org.testng.IRetryAnalyzer;
import org.testng.ITestResult;

import java.io.IOException;

public class Retry implements IRetryAnalyzer {

    private int count  = 0;
    private static int maxTry = 1; //Run the failed test 2 times
    @Override
    public boolean retry(ITestResult iTestResult) {
        if (!iTestResult.isSuccess()) {                     //Check if test not succeed
            if (count < maxTry) {                           //Check if maxTry count is reached
                count++;                                    //Increase the maxTry count by 1
                iTestResult.setStatus(ITestResult.FAILURE); //Mark test as failed and take base64Screenshot
                try {
//                    TestUtil.takeScreenshotAtEndOfTest(iTestResult.getMethod().getMethodName());
                    extendReportsFailOperations(iTestResult);   //ExtentReports fail operations
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                return true;                                //Tells TestNG to re-run the test
            }
        } else {
            iTestResult.setStatus(ITestResult.SUCCESS);     //If test passes, TestNG marks it as passed
        }
        return false;
    }
    public void extendReportsFailOperations(ITestResult iTestResult) throws IOException {
        TestListener.test.get().log(Status.FAIL, "Test Failed").addScreenCaptureFromPath("/reports/extent-reports/screenshots", iTestResult.getMethod().getMethodName() + ".png");
    }
}

