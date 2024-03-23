package edu.java.dto.tgchatlinks;

import java.time.OffsetDateTime;
import lombok.Builder;

@Builder
public record TgChatResponse(
    Long id,

    OffsetDateTime createdAt
) {
}
