package edu.java.controller.tgchat;

import edu.java.exception.ApiErrorResponse;
import edu.java.service.TgChatService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
@Tag(name = "tg-chat", description = "the tg-chat API")
@RequestMapping("/tg-chat")
public class TgChatController {

    private final TgChatService jdbcTgChatService;

    @Operation(operationId = "tgChatIdDelete", summary = "Удалить чат")
    @ApiResponse(responseCode = "200", description = "Чат успешно добавлен")
    @ApiResponse(responseCode = "400", description = "Некорректные параметры запроса",
                 content = @Content(schema = @Schema(implementation = ApiErrorResponse.class)))
    @ApiResponse(responseCode = "404", description = "Чат не существует",
                 content = @Content(schema = @Schema(implementation = ApiErrorResponse.class)))
    @DeleteMapping("/{id}")
    public void tgChatIdDelete(@PathVariable("id") Long id) {
        log.info("/tg-chat/{id} DELETE endpoint");
        jdbcTgChatService.unregister(id);
    }

    @Operation(operationId = "tgChatIdPost", summary = "Зарегистрировать чат")
    @ApiResponse(responseCode = "200", description = "Чат зарегистрирован")
    @ApiResponse(responseCode = "400", description = "Некорректные параметры запроса",
                 content = @Content(schema = @Schema(implementation = ApiErrorResponse.class)))
    @ApiResponse(responseCode = "409", description = "Повтороное добавление",
                 content = @Content(schema = @Schema(implementation = ApiErrorResponse.class)))
    @PostMapping("/{id}")
    public void tgChatIdPost(@PathVariable("id") Long id) {
        log.info("/tg-chat/{id} POST endpoint");
        jdbcTgChatService.register(id);
    }
}
