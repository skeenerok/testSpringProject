package com.example.util;

import com.automation.remarks.video.recorder.VideoRecorder;
import io.qameta.allure.Attachment;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.testng.Reporter;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.concurrent.TimeUnit;

import static org.awaitility.Awaitility.await;

public class AttachmentHelper {

    @Attachment(value = "{0}", type = "image/png")
    static public byte[] addScreenshot(String title, BufferedImage bufferedImage) {
        try {
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            ImageIO.write(bufferedImage, "png", bos );
            return bos.toByteArray();
        } catch (IOException ex) {
            Reporter.log("Unable to get the file " + bufferedImage, true);
            return new byte[0];
        }
    }

    @Attachment(value = "{0}", type = "image/png")
    static public byte[] addScreenshot(String title, String screenshotPath) {
        try {
            System.out.println("[INFO] key: " + title + "; " + " path: " + screenshotPath);
            return Files.readAllBytes(Paths.get(screenshotPath));
        } catch (IOException ex) {
            Reporter.log("Unable to get the file " + screenshotPath, true);
            return new byte[0];
        }
    }

    @Attachment(value = "{0}", type = "image/png")
    public static byte[] addScreenshot(String name, WebDriver driver) {
        if (driver == null) {
            Reporter.log("WARNING: Unable to get WebDriver to make a screenshot", true);
            return new byte[0];
        }
        byte[] screenShot;
        try {
            screenShot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
        } catch (Exception e) {
            Reporter.log("Can't take a screenshot: " + e.getMessage(), true);
            return new byte[0];
        }
        return screenShot;
    }

    @Attachment(value = "video", type = "video/mp4")
    public static byte[] addVideo(String name) throws InterruptedException {
        try {
            File video = VideoRecorder.getLastRecording();
            await().atMost(5, TimeUnit.SECONDS)
                    .pollDelay(1, TimeUnit.SECONDS)
                    .ignoreExceptions()
                    .until(() -> video != null);
            return Files.readAllBytes(Paths.get(video.getAbsolutePath()));
        } catch (IOException e) {
            Reporter.log("Can't take video recording: " + e.getMessage(), true);
            return new byte[0];
        }
    }
}