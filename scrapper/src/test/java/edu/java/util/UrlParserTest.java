package edu.java.util;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class UrlParserTest {

    UrlParser parser = new UrlParser();
    String gitHubRepoLink = "https://github.com/VoroninEgor/tinkoff-java-course-2023-backend";
    String stackOverFlowQuestionLink = "https://stackoverflow.com/questions/78162593/how-to-configure";

    @Test
    void fetchRepoNameFromGitHubLink() {
        String repoName = parser.fetchRepoNameFromGitHubLink(gitHubRepoLink);
        Assertions.assertEquals("tinkoff-java-course-2023-backend", repoName);
    }

    @Test
    void fetchUserNameFromGitHubLink() {
        String userName = parser.fetchUserNameFromGitHubLink(gitHubRepoLink);
        Assertions.assertEquals("VoroninEgor", userName);
    }

    @Test
    void fetchQuestionIdFromStackOverFlowLink() {
        Long questionNumber = parser.fetchQuestionIdFromStackOverFlowLink(stackOverFlowQuestionLink);
        Assertions.assertEquals(78162593L, questionNumber);
    }
}
