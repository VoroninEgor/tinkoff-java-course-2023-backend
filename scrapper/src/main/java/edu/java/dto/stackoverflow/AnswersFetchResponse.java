package edu.java.dto.stackoverflow;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.OffsetDateTime;
import java.util.List;

public record AnswersFetchResponse(List<Answer> items) {
    public record Answer(
        Owner owner,
        @JsonProperty("creation_date")
        OffsetDateTime createdAt) {
    }

    public record Owner(@JsonProperty("display_name") String name) {
    }
}
