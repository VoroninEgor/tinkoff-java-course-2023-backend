package edu.java.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import java.time.OffsetDateTime;
import org.springframework.validation.annotation.Validated;

@Validated
public record RepoResponse(
    @JsonProperty("pushed_at")
    @NotNull
    OffsetDateTime updatedAt) {
}
