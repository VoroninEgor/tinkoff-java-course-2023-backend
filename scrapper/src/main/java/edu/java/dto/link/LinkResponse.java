package edu.java.dto.link;

import jakarta.validation.constraints.NotNull;
import java.net.URI;
import lombok.Builder;

@Builder
public record LinkResponse(
    Long id,

    @NotNull
    URI link
) {
}
