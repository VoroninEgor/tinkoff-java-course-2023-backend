package edu.java.client.github;

import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import edu.java.client.AbstractTest;
import edu.java.dto.github.GitHubCommitResponse;
import edu.java.dto.github.GitHubPullRequestResponse;
import edu.java.dto.github.RepoResponse;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static java.lang.String.format;
import static org.junit.jupiter.api.Assertions.assertEquals;

@WireMockTest(httpPort = 8080)
class GitHubClientImplTest extends AbstractTest {

    String baseUrl;
    GitHubClientImpl gitHubClient;
    String owner;
    String repo;

    @BeforeEach
    void setUp() {
        baseUrl = "http://localhost:8080";
        gitHubClient = new GitHubClientImpl(baseUrl);
        owner = "owner";
        repo = "repo";
    }

    @Test
    void fetchRepo_shouldReturnRepoResponseWithUpdatedAt() {
        String bodyJson = jsonToString("src/test/resources/github.json");

        stubFor(get(format("/repos/%s/%s", owner, repo)).willReturn(aResponse()
            .withStatus(200)
            .withHeader("Content-Type", "application/json")
            .withBody(bodyJson)));
        RepoResponse repoResponse = gitHubClient.fetchRepo(owner, repo);

        assertEquals(OffsetDateTime.parse("2024-02-02T15:17:25Z"), repoResponse.updatedAt());
    }

    @Test
    void fetchCommitsSince() {
        String bodyJson = jsonToString("src/test/resources/commits-since.json");

        OffsetDateTime since = OffsetDateTime
            .of(2024, 1, 22, 6, 5, 8, 0, ZoneOffset.UTC);

        stubFor(get(format("/repos/%s/%s/commits?since=%s", owner, repo, since)).willReturn(aResponse()
            .withStatus(200)
            .withHeader("Content-Type", "application/json")
            .withBody(bodyJson)));

        List<GitHubCommitResponse> gitHubCommitResponses = gitHubClient.fetchCommitsSince(owner, repo, since);

        assertEquals(3, gitHubCommitResponses.size());
    }

    @Test
    void fetchPullRequestsSince() {
        String bodyJson = jsonToString("src/test/resources/pulls-since.json");

        OffsetDateTime since = OffsetDateTime
            .of(2024, 3, 20, 6, 5, 8, 0, ZoneOffset.UTC);

        stubFor(get(format("/repos/%s/%s/pulls", owner, repo)).willReturn(aResponse()
            .withStatus(200)
            .withHeader("Content-Type", "application/json")
            .withBody(bodyJson)));

        List<GitHubPullRequestResponse> gitHubPullRequestResponses
            = gitHubClient.fetchPullRequestsSince(owner, repo, since);

        assertEquals(2, gitHubPullRequestResponses.size());
    }
}
