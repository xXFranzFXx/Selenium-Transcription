package util.listeners;

import base.BaseTest;
import com.aventstack.extentreports.*;
import com.aventstack.extentreports.markuputils.ExtentColor;
import com.aventstack.extentreports.markuputils.MarkupHelper;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Sequence;
import org.openqa.selenium.support.events.WebDriverListener;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;
import util.TestUtil;
import util.extentReports.ExtentManager;
import util.logs.Log;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.List;
import java.util.Set;

public class TestListener  implements ITestListener, WebDriverListener {
    //Extent Report Declarations
    static ExtentReports extent = ExtentManager.getInstance();
    static ThreadLocal<ExtentTest> test = new ThreadLocal<>();
    @Override
    public synchronized void onStart(ITestContext context) {
       Log.info("Extent Reports started!");
    }
    @Override
    public synchronized void onFinish(ITestContext context) {
        Log.info("Extent Reports ending!");
        extent.flush();
    }
    @Override
    public synchronized void onTestStart(ITestResult result) {
        Log.info(result.getMethod().getMethodName() + " started!");
        ExtentTest extentTest = extent.createTest(result.getMethod().getMethodName(),result.getMethod().getDescription());
        test.set(extentTest);
    }
    @Override
    public synchronized void onTestSuccess(ITestResult result) {
        Log.info(result.getMethod().getMethodName() + " passed!");
        test.get().pass(MarkupHelper.createLabel("Test Passed", ExtentColor.GREEN));
    }
    @Override
    public synchronized void onTestFailure(ITestResult result) {
        Log.error(result.getMethod().getMethodName() + " failed!");
        try {
            TestUtil.takeScreenshotAtEndOfTest(result.getMethod().getMethodName(), BaseTest.getDriver());
            test.get().log(Status.FAIL, "fail ❌").addScreenCaptureFromPath("/reports/extent-reports/screenshots/" + result.getMethod().getMethodName() + ".png");
            Log.info("screen shot taken for failed test " + result.getMethod().getMethodName());
        } catch (IOException e) {
            e.printStackTrace();
        }
            test.get().fail(result.getThrowable());
    }
    @Override
    public synchronized void onTestSkipped(ITestResult result) {
        Log.warn(result.getMethod().getMethodName() + " skipped");
        test.get().skip(MarkupHelper.createLabel("Skipped", ExtentColor.AMBER));
        test.get().log(Status.SKIP,  result.getThrowable());
    }
    @Override
    public void onTestFailedButWithinSuccessPercentage(ITestResult result) {
        Log.info("onTestFailedButWithinSuccessPercentage for " + result.getMethod().getMethodName());

    }
    public void beforeAnyCall(Object target, Method method, Object[] args) {
        Log.debug( "Before calling method: " + method.getName());
    }

    public void afterAnyCall(Object target, Method method, Object[] args, Object result) {
        Log.debug("After calling method: " + method.getName());
    }

    public void onError(Object target, Method method, Object[] args, InvocationTargetException e) {
        Log.fatal( "Error while calling method: " + method.getName() + " - " + e.getMessage());
    }

    public void beforeAnyWebDriverCall(WebDriver driver, Method method, Object[] args) {
        Log.debug("Before calling WebDriver method: " + method.getName());
    }

    public void afterAnyWebDriverCall(WebDriver driver, Method method, Object[] args, Object result) {
        Log.debug("After calling WebDriver method: " + method.getName());
    }

    public void beforeGet(WebDriver driver, String url) {
        Log.debug("Before navigating to URL: " + url);
    }

    public void afterGet(WebDriver driver, String url) {
        Log.debug("After navigating to URL: " + url);
    }

    public void beforeGetCurrentUrl(WebDriver driver) {
        Log.debug("Before getting current URL.");
    }

    public void afterGetCurrentUrl(String result, WebDriver driver) {
        Log.debug("After getting current URL: " + result);
    }
    public void beforeGetTitle(WebDriver driver) {
        Log.debug("Before getting page title.");
    }

    public void afterGetTitle(WebDriver driver, String result) {
        Log.debug("After getting page title: " + result);
    }

    public void beforeFindElement(WebDriver driver, By locator) {
        Log.debug("Before finding element by: " + locator);
    }

    public void afterFindElement(WebDriver driver, By locator, WebElement result) {
        Log.debug("After finding element by: " + locator);
    }

    public void beforeFindElements(WebDriver driver, By locator) {
        Log.debug("Before finding elements by: " + locator);
    }

    public void afterFindElements(WebDriver driver, By locator, List<WebElement> result) {
        Log.debug("After finding elements by: " + locator);
    }

    public void beforeGetPageSource(WebDriver driver) {
        Log.debug("Before getting page source.");
    }

    public void afterGetPageSource(WebDriver driver, String result) {
        Log.debug("After getting page source.");
    }

    public void beforeClose(WebDriver driver) {
        Log.debug("Before closing the WebDriver.");
    }

    public void afterClose(WebDriver driver) {
        Log.debug("After closing the WebDriver.");
    }

    public void beforeQuit(WebDriver driver) {
        Log.debug("Before quitting the WebDriver.");
    }

    public void afterQuit(WebDriver driver) {
        Log.debug("After quitting the WebDriver.");
    }

    public void beforeGetWindowHandles(WebDriver driver) {
        Log.debug("Before getting window handles.");
    }

    public void afterGetWindowHandles(WebDriver driver, Set<String> result) {
        Log.debug("After getting window handles.");
    }

    public void beforeGetWindowHandle(WebDriver driver) {
        Log.debug("Before getting window handle.");
    }

    public void afterGetWindowHandle(WebDriver driver, String result) {
        Log.debug("After getting window handle.");
    }

    public void beforeExecuteScript(WebDriver driver, String script, Object[] args) {
        Log.debug("Before executing script: " + script);
    }

    public void afterExecuteScript(WebDriver driver, String script, Object[] args, Object result) {
        Log.debug("After executing script: " + script);
    }

    public void beforeExecuteAsyncScript(WebDriver driver, String script, Object[] args) {
        Log.debug("Before executing async script: " + script);
    }

    public void afterExecuteAsyncScript(WebDriver driver, String script, Object[] args, Object result) {
        Log.debug("After executing async script: " + script);
    }

    public void beforePerform(WebDriver driver, Collection<Sequence> actions) {
        Log.debug("Before performing actions.");
    }

    public void afterPerform(WebDriver driver, Collection<Sequence> actions) {
        Log.debug("After performing actions.");
    }

    public void beforeResetInputState(WebDriver driver) {
        Log.debug("Before resetting input state.");
    }

    public void afterResetInputState(WebDriver driver) {
        Log.debug("After resetting input state.");
    }

    public void beforeAnyWebElementCall(WebElement element, Method method, Object[] args) {
        Log.debug("Before calling WebElement method: " + method.getName());
    }

    public void afterAnyWebElementCall(WebElement element, Method method, Object[] args, Object result) {
        Log.debug("After calling WebElement method: " + method.getName());
    }

    public void beforeClick(WebElement element) {
        Log.debug("Before clicking on element.");
    }

    public void afterClick(WebElement element) {
        Log.debug("After clicking on element.");
    }

    public void beforeSubmit(WebElement element) {
        Log.debug("Before submitting a form element.");
    }

    public void afterSubmit(WebElement element) {
        Log.debug("After submitting a form element.");
    }

    public void beforeSendKeys(WebElement element, CharSequence... keysToSend) {
        Log.debug("Before sending keys to element.");
    }

    public void afterSendKeys(WebElement element, CharSequence... keysToSend) {
        Log.debug("After sending keys to element.");
    }

    public void beforeClear(WebElement element) {
        Log.debug("Before clearing the text of an element.");
    }

    public void afterClear(WebElement element) {
        Log.debug("After clearing the text of an element.");
    }

    public void beforeGetTagName(WebElement element) {
        Log.debug("Before getting the tag name of an element.");
    }

    public void afterGetTagName(WebElement element, String result) {
        Log.debug("After getting the tag name of an element: " + result);
    }

    public void beforeGetAttribute(WebElement element, String name) {
        Log.debug("Before getting an attribute of an element: " + name);
    }

    public void afterGetAttribute(WebElement element, String name, String result) {
        Log.debug("After getting an attribute of an element: " + name);
    }

    public void beforeIsSelected(WebElement element) {
        Log.debug("Before checking if element is selected.");
    }

    public void afterIsSelected(WebElement element, boolean result) {
        Log.debug("After checking if element is selected: " + result);
    }

    public void beforeIsEnabled(WebElement element) {
        Log.debug("Before checking if element is enabled.");
    }

    public void afterIsEnabled(WebElement element, boolean result) {
        Log.debug("After checking if element is enabled: " + result);
    }

    public void beforeGetText(WebElement element) {
        Log.debug("Before getting text from element.");
    }

    public void afterGetText(WebElement element, String result) {
        Log.debug("After getting text from element: " + result);
    }

    public void beforeFindElement(WebElement element, By locator) {
        Log.debug("Before finding element within element: " + locator);
    }

    public void afterFindElement(WebElement element, By locator, WebElement result) {
        Log.debug("After finding element within element: " + locator);
    }

    public void beforeFindElements(WebElement element, By locator) {
        Log.debug("Before finding elements within element: " + locator);
    }

    public void afterFindElements(WebElement element, By locator, List<WebElement> result) {
        Log.debug("After finding elements within element: " + locator);
    }
}