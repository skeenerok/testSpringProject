package com.example.util;

import com.automation.remarks.testng.UniversalVideoListener;
import com.automation.remarks.video.enums.RecordingMode;
import com.automation.remarks.video.recorder.VideoRecorder;
import org.openqa.selenium.WebDriver;
import org.testng.*;

import java.awt.image.BufferedImage;
import java.util.List;
import java.util.Map;

public class TestListener extends UniversalVideoListener implements ITestListener  {

    @Override
    public void onTestStart(ITestResult result) {
        super.onTestStart(result);
        Reporter.log("[STARTED] " + getName(result.getMethod()), true);
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        BrowserLogs.checkLogs();
        Reporter.log("[PASSED] " +  getName(result.getMethod()), true);
    }

    @Override
    public void onTestFailure(ITestResult result) {
        super.onTestFailure(result);
        Reporter.log("[FAILED] " +  getName(result.getMethod()), true);
        try {
            WebDriver driver = ((IDriverSupplier) result.getInstance()).getDriver();
            if (driver != null) {
                AttachmentHelper.addScreenshot("failed_" + result.getMethod().getMethodName(), driver);
                Reporter.log("Failed at: " + driver.getCurrentUrl(), true);
            }
            if (VideoRecorder.conf().videoEnabled() && VideoRecorder.conf().mode().equals(RecordingMode.ALL)) {
                AttachmentHelper.addVideo(result.getMethod().getMethodName());
            }
            attachTestArtifacts(result);
            BrowserLogs.checkLogs();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        super.onTestSkipped(result);
        Reporter.log("[SKIPPED] " +  getName(result.getMethod()), true);
    }

    @Override
    public void onTestFailedButWithinSuccessPercentage(ITestResult result) {
        super.onTestFailedButWithinSuccessPercentage(result);
    }

    @Override
    public void onStart(ITestContext context) {
        super.onStart(context);
    }

    @Override
    public void onFinish(ITestContext context) {
        super.onFinish(context);
    }

    private static void attachTestArtifacts(ITestResult result) {
        List<Map<String, BufferedImage>> artifacts = ((ITestArtifactProvider) result.getInstance()).getArtifacts();
        if (artifacts != null && artifacts.size() > 0) {
            artifacts.forEach(map -> map.forEach( (String key, BufferedImage image) -> {
                Reporter.log("[INFO] Attaching to the report: " + key, true);
                AttachmentHelper.addScreenshot(key, image);
            }));
        }
    }

    private String getName(ITestNGMethod method) {
        return method.getMethodName() + " (" + method.getRealClass() + ")";
    }
}
