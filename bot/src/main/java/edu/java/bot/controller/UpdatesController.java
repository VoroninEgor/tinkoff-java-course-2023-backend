package edu.java.bot.controller;

import edu.java.bot.dto.LinkUpdateRequest;
import edu.java.bot.exception.ApiErrorResponse;
import edu.java.bot.service.UpdatesService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
@Tag(name = "updates", description = "the updates API")
@RequestMapping("/updates")
public class UpdatesController {

    private final UpdatesService updatesService;

    @Operation(operationId = "updatesPost", summary = "Отправить обновление")
    @ApiResponse(responseCode = "200", description = "Обновление обработано")
    @ApiResponse(responseCode = "400", description = "Некорректные параметры запроса",
                 content = @Content(schema = @Schema(implementation = ApiErrorResponse.class)))
    @PostMapping
    public void updatesPost(@Valid @RequestBody LinkUpdateRequest linkUpdate) {
        log.info("/updates POST endpoint");
        log.info("link {} update for chats: {}", linkUpdate.url(), linkUpdate.tgChatIds());
        updatesService.handleUpdate(linkUpdate);
    }
}
