package edu.java.controller.link;

import edu.java.dto.link.AddLinkRequest;
import edu.java.dto.link.LinkResponse;
import edu.java.dto.link.ListLinksResponse;
import edu.java.dto.link.RemoveLinkRequest;
import edu.java.exception.ApiErrorResponse;
import edu.java.service.LinkService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
@Tag(name = "links", description = "the links API")
@RequestMapping("/links")
public class LinkController {

    private final LinkService linksService;

    @Operation(operationId = "linksDelete", summary = "Убрать отслеживание ссылки")
    @ApiResponse(responseCode = "200", description = "Ссылка успешно убрана",
                 content = @Content(schema = @Schema(implementation = LinkResponse.class)))
    @ApiResponse(responseCode = "400", description = "Некорректные параметры запроса",
                 content = @Content(schema = @Schema(implementation = ApiErrorResponse.class)))
    @ApiResponse(responseCode = "404", description = "Ссылка не найдена",
                 content = @Content(schema = @Schema(implementation = ApiErrorResponse.class)))
    @DeleteMapping
    public LinkResponse linksDelete(
        @NotNull @RequestHeader(value = "Tg-Chat-Id") Long tgChatId,
        @Valid @RequestBody RemoveLinkRequest removeLinkRequest
    ) {
        log.info("/links DELETE endpoint");
        return linksService.untrackLinkForUser(tgChatId, removeLinkRequest);
    }

    @Operation(operationId = "linksGet", summary = "Получить все отслеживаемые ссылки")
    @ApiResponse(responseCode = "200", description = "Ссылки успешно получены",
                 content = @Content(schema = @Schema(implementation = ListLinksResponse.class)))
    @ApiResponse(responseCode = "400", description = "Некорректные параметры запроса",
                 content = @Content(schema = @Schema(implementation = ApiErrorResponse.class)))
    @GetMapping
    public ListLinksResponse linksGet(@NotNull @RequestHeader(value = "Tg-Chat-Id") Long tgChatId) {
        log.info("/links GET endpoint");
        return linksService.getLinks(tgChatId);
    }

    @Operation(operationId = "linksPost", summary = "Добавить отслеживание ссылки")
    @ApiResponse(responseCode = "200", description = "Ссылка успешно добавлена",
                 content = @Content(schema = @Schema(implementation = LinkResponse.class)))
    @ApiResponse(responseCode = "400", description = "Некорректные параметры запроса",
                 content = @Content(schema = @Schema(implementation = ApiErrorResponse.class)))
    @PostMapping
    public LinkResponse linksPost(
        @NotNull @RequestHeader(value = "Tg-Chat-Id") Long tgChatId, @Valid @RequestBody AddLinkRequest addLinkRequest
    ) {
        log.info("/links POST endpoint");
        return linksService.trackLinkForUser(tgChatId, addLinkRequest);
    }
}
