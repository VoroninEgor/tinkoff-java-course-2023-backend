package edu.java.client.github;

import edu.java.dto.github.GitHubCommitResponse;
import edu.java.dto.github.GitHubPullRequestResponse;
import edu.java.dto.github.RepoResponse;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.reactive.function.client.WebClient;
import static java.lang.String.format;

@Slf4j
public class GitHubClientImpl implements GitHubClient {
    private final static String BASEURL = "https://api.github.com/";
    private final WebClient webClient;

    public GitHubClientImpl(String baseUrl) {
        webClient = WebClient.create(baseUrl);
    }

    public GitHubClientImpl() {
        webClient = WebClient.create(BASEURL);
    }

    @Override
    public RepoResponse fetchRepo(String username, String repoName) {
        return webClient
            .get()
            .uri(format("repos/%s/%s", username, repoName))
            .retrieve()
            .bodyToMono(RepoResponse.class)
            .block();
    }

    @Override
    public List<GitHubCommitResponse> fetchCommitsSince(String username, String repoName, OffsetDateTime since) {
        return webClient.get()
            .uri(uriBuilder -> uriBuilder
                .path(format("repos/%s/%s/commits", username, repoName))
                .queryParam("since", since)
                .build())
            .retrieve()
            .bodyToFlux(GitHubCommitResponse.class)
            .collectList()
            .block();
    }

    @Override
    public List<GitHubPullRequestResponse> fetchPullRequestsSince(
        String username, String repoName, OffsetDateTime since
    ) {
        List<GitHubPullRequestResponse> allPullRequest = webClient.get()
            .uri(format("repos/%s/%s/pulls", username, repoName))
            .retrieve()
            .bodyToFlux(GitHubPullRequestResponse.class)
            .collectList()
            .block();
        if (allPullRequest == null || allPullRequest.isEmpty()) {
            return List.of();
        }
        return allPullRequest.stream()
            .filter(pr -> pr.createdAt().isAfter(since))
            .collect(Collectors.toList());
    }
}
