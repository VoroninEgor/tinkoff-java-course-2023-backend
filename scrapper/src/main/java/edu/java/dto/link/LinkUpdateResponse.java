package edu.java.dto.link;

import jakarta.validation.constraints.NotNull;
import java.net.URI;
import java.time.OffsetDateTime;
import lombok.Builder;

@Builder
public record LinkUpdateResponse(
    Long id,

    @NotNull
    URI url,

    OffsetDateTime lastCheck
) {
}
