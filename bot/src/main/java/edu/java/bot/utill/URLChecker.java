package edu.java.bot.utill;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;

@Slf4j
@UtilityClass
public class URLChecker {

    private static final String GITHUBLINK = "^https://github\\.com/[^/]+/[^/]+.*$";
    private static final String STACKOVERFLOWLINK = "^https://stackoverflow\\.com/questions/\\d+/.*$";

    public static boolean isValid(String url) {
        if (!url.matches(GITHUBLINK) && !url.matches(STACKOVERFLOWLINK)) {
            return false;
        }
        HttpURLConnection connection = null;
        try {
            URL u = new URL(url);
            connection = (HttpURLConnection) u.openConnection();
            connection.setRequestMethod("HEAD");
            int code = connection.getResponseCode();
            return code == HttpStatus.OK.value();
        } catch (IOException e) {
            log.warn("incorrect link", e);
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
        return false;
    }
}
