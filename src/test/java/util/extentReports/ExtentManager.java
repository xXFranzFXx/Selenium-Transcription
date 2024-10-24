package util.extentReports;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;
import org.openqa.selenium.Platform;

import java.io.File;

public class ExtentManager {
    private static ExtentReports extent;
    private static Platform platform;
    private static String reportFileName = "Test-automation-report.html";
    private static String macPath = System.getProperty("user.dir") + "/reports/extent-reports";
    private static String windowsPath = System.getProperty("user.dir") + "\\reports\\extent-reports";
    private static String macReportFileLoc = macPath + "/" + reportFileName;
    private static String winReportFileLoc = windowsPath + "\\" + reportFileName;
    public static ExtentReports getInstance() {
        if (extent == null)
            createInstance();
        return extent;
    }
    public static ExtentReports createInstance() {
        platform = getCurrentPlatform();
        String fileName = getReportFileLocation(platform);
        ExtentSparkReporter htmlReporter = new ExtentSparkReporter(fileName);

        htmlReporter.config().setTheme(Theme.DARK);
        htmlReporter.config().setDocumentTitle("Extent Report");
        htmlReporter.config().setEncoding("utf-8");
        htmlReporter.config().setReportName("Iframes testing");
        extent = new ExtentReports();
        extent.attachReporter(htmlReporter);
        extent.setSystemInfo("Environment", "TEST");
        extent.setSystemInfo("Author", "Franz Fernando");
        return extent;
    }
    //Select the extent report file location based on platform
    private static String getReportFileLocation (Platform platform) {
        String reportFileLocation = null;
        switch (platform) {
            case MAC:
                reportFileLocation = macReportFileLoc;
                createReportPath(macPath);
                System.out.println("ExtentReport Path for MAC: " + macPath + "\n");
                break;
            case WINDOWS:
                reportFileLocation = winReportFileLoc;
                createReportPath(windowsPath);
                System.out.println("ExtentReport Path for WINDOWS: " + windowsPath + "\n");
                break;
            default:
                System.out.println("ExtentReport path has not been set! There is a problem!\n");
                break;
        }
        return reportFileLocation;
    }
    //Create the report path if it does not exist
    private static void createReportPath (String path) {
        File testDirectory = new File(path);
        if (!testDirectory.exists()) {
            if (testDirectory.mkdir()) {
                System.out.println("Directory: " + path + " is created!" );
            } else {
                System.out.println("Failed to create directory: " + path);
            }
        } else {
            System.out.println("Using current reports directory: " + path);
        }
    }
    //Get current platform
    private static Platform getCurrentPlatform () {
        if (platform == null) {
            String operSys = System.getProperty("os.name").toLowerCase();
            if (operSys.contains("win")) {
                platform = Platform.WINDOWS;
            } else if (operSys.contains("nix") || operSys.contains("nux")
                    || operSys.contains("aix")) {
                platform = Platform.LINUX;
            } else if (operSys.contains("mac")) {
                platform = Platform.MAC;
            }
        }
        return platform;
    }
}
