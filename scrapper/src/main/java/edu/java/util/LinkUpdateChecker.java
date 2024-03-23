package edu.java.util;

import edu.java.client.github.GitHubClient;
import edu.java.client.stackoverflow.StackOverFlowClient;
import edu.java.dto.QuestionResponse;
import edu.java.dto.RepoResponse;
import java.time.OffsetDateTime;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class LinkUpdateChecker {

    private final StackOverFlowClient stackOverFlowClient;
    private final GitHubClient gitHubClient;
    private final UrlParser urlParser;

    public OffsetDateTime getLastUpdateTime(String url) {
        log.info("get last update time for " + url);

        String gitHubRegex = "^https://github\\.com/[^/]+/[^/]+.*$";
        String stackOverFlowRegex = "^https://stackoverflow\\.com/questions/\\d+/.*$";

        if (url.matches(gitHubRegex)) {
            String userName = urlParser.fetchUserNameFromGitHubLink(url);
            String repoName = urlParser.fetchRepoNameFromGitHubLink(url);
            RepoResponse response = gitHubClient.fetchRepo(userName, repoName);
            return response.updatedAt();
        }
        if (url.matches(stackOverFlowRegex)) {
            Long id = urlParser.fetchQuestionIdFromStackOverFlowLink(url);
            QuestionResponse response = stackOverFlowClient.fetchQuestion(id);
            return response.items().getFirst().updatedAt();
        }

        return null;
    }
}
