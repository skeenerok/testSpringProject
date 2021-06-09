package com.example.util;

import com.codeborne.selenide.Selenide;
import io.qameta.allure.Attachment;
import org.openqa.selenium.logging.LogType;

import java.util.List;

public class BrowserLogs {
    private static List logList;

    private static List getConsoleLog() {
        return Selenide.getWebDriverLogs(LogType.BROWSER);
    }

    public static void checkLogs() {
        logList = getConsoleLog();
        if (logList.size() > 0)
            addConsoleLogToReport();
    }

    @Attachment(value = "Browser console log", type = "text/plain")
    private static String addConsoleLogToReport() {
        StringBuilder sb = new StringBuilder();
        for (Object line : logList) {
            sb.append(line);
            sb.append("\t");
        }
        return sb.toString();
    }
}