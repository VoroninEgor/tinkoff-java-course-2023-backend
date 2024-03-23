package edu.java.dto.github;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.OffsetDateTime;

public record GitHubPullRequestResponse(
    @JsonProperty("created_at")
    OffsetDateTime createdAt
) {

}
