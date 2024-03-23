package edu.java.util.checker;

import edu.java.client.github.GitHubClient;
import edu.java.dto.LastUpdate;
import edu.java.dto.github.GitHubCommitResponse;
import edu.java.dto.github.GitHubPullRequestResponse;
import edu.java.util.UrlParser;
import java.net.URI;
import java.time.OffsetDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class GitHubChecker implements UrlChecker {
    private static final String URL_REGEX = "^https://github\\.com/[^/]+/[^/]+.*$";
    private static final long TIME_DIFFERENCE_IN_HOURS = 3L;

    private final GitHubClient gitHubClient;
    private final UrlParser urlParser;

    @Override
    public boolean isLinkTrackable(String url) {
        return url.matches(URL_REGEX);
    }

    @Override
    public LastUpdate getLastUpdate(String url, OffsetDateTime since) {
        String repoName = urlParser.fetchRepoNameFromGitHubLink(url);
        String username = urlParser.fetchUserNameFromGitHubLink(url);

        List<GitHubCommitResponse> commits =
            gitHubClient.fetchCommitsSince(username, repoName, since.minusHours(TIME_DIFFERENCE_IN_HOURS));
        List<GitHubPullRequestResponse> pullRequests =
            gitHubClient.fetchPullRequestsSince(username, repoName, since.minusHours(TIME_DIFFERENCE_IN_HOURS));
        return LastUpdate.builder()
            .link(URI.create(url))
            .isUpdated(isUpdated(commits, pullRequests))
            .description(getDescription(commits, pullRequests, url))
            .lastCheck(OffsetDateTime.now())
            .build();
    }

    private String getDescription(
        List<GitHubCommitResponse> commits, List<GitHubPullRequestResponse> pullRequests, String url
    ) {
        if (!isUpdated(commits, pullRequests)) {
            return "No updates";
        }
        StringBuilder description = new StringBuilder();
        description.append(url).append(" hase ");
        if (!commits.isEmpty()) {
            description.append("new commit ");
        }
        if (!pullRequests.isEmpty()) {
            description.append("new pull request");
        }
        return description.toString();
    }

    private boolean isUpdated(List<GitHubCommitResponse> commits, List<GitHubPullRequestResponse> pullRequests) {
        return !commits.isEmpty() || !pullRequests.isEmpty();
    }
}
