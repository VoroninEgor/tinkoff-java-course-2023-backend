package edu.java.dto;

import java.net.URI;
import java.time.OffsetDateTime;
import lombok.Builder;

@Builder
public record LastUpdate(
    URI link,
    String description,
    boolean isUpdated,
    OffsetDateTime lastCheck
) {
}
