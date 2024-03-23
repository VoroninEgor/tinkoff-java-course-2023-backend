package edu.java.dto.link;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.net.URI;
import java.util.List;
import lombok.Builder;

@Builder
public record LinkUpdateRequest(
    @NotNull
    Long id,

    URI url,

    @NotBlank
    String description,

    @NotNull
    List<Long> tgChatIds
) {
}
