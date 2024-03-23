package edu.java.client.github;

import edu.java.dto.github.GitHubCommitResponse;
import edu.java.dto.github.GitHubPullRequestResponse;
import edu.java.dto.github.RepoResponse;
import java.time.OffsetDateTime;
import java.util.List;

public interface GitHubClient {
    RepoResponse fetchRepo(String username, String repoName);

    List<GitHubCommitResponse> fetchCommitsSince(String username, String repoName, OffsetDateTime since);

    List<GitHubPullRequestResponse> fetchPullRequestsSince(String username, String repoName, OffsetDateTime since);
}
