package edu.java.util;

import edu.java.exception.UrlParseException;
import org.springframework.stereotype.Component;

@Component
@SuppressWarnings("checkstyle:MagicNumber")
public class UrlParser {
    private static final String GITHUBLINK = "^https://github\\.com/[^/]+/[^/]+.*$";
    private static final String STACKOVERFLOWLINK = "^https://stackoverflow\\.com/questions/\\d+/.*$";

    public String fetchRepoNameFromGitHubLink(String url) {
        if (!url.matches(GITHUBLINK)) {
            throw new UrlParseException();
        }
        return url.split("/")[4];
    }

    public String fetchUserNameFromGitHubLink(String url) {
        if (!url.matches(GITHUBLINK)) {
            throw new UrlParseException();
        }
        return url.split("/")[3];
    }

    public Long fetchQuestionIdFromStackOverFlowLink(String url) {
        if (!url.matches(STACKOVERFLOWLINK)) {
            throw new UrlParseException();
        }
        return Long.parseLong(url.split("/")[4]);
    }
}
