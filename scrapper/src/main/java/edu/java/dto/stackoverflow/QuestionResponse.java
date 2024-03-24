package edu.java.dto.stackoverflow;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.OffsetDateTime;
import java.util.List;

public record QuestionResponse(List<QuestionItem> items) {
    public record QuestionItem(
        @JsonProperty("question_id")
        Long id,
        @JsonProperty("last_activity_date")
        OffsetDateTime updatedAt) {
    }
}
