package edu.java.bot;

import com.pengrad.telegrambot.ExceptionHandler;
import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.TelegramException;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.BotCommand;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.BaseRequest;
import com.pengrad.telegrambot.request.SetMyCommands;
import com.pengrad.telegrambot.response.BaseResponse;
import edu.java.bot.command.AbstractCommand;
import edu.java.bot.message.UserMessageProcessor;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class Bot implements AutoCloseable, UpdatesListener, ExceptionHandler {
    private final UserMessageProcessor userMessageProcessor;
    private final TelegramBot bot;

    @EventListener(ApplicationReadyEvent.class)
    public void start() {
        log.info("The bot has started working...");

        bot.setUpdatesListener(this, this);

        SetMyCommands setMyCommands = new SetMyCommands(
            userMessageProcessor.getCommands().stream()
                .filter(command -> !command.getCommand().equals("/start"))
                .map(AbstractCommand::toApiCommand)
                .toArray(BotCommand[]::new));
        execute(setMyCommands);
    }

    public <T extends BaseRequest<T, R>, R extends BaseResponse> void execute(BaseRequest<T, R> request) {
        bot.execute(request);
    }

    @Override
    public int process(List<Update> updates) {
        updates.stream().map(userMessageProcessor::process).forEach(this::execute);

        return UpdatesListener.CONFIRMED_UPDATES_ALL;
    }

    @Override
    public void onException(TelegramException e) {
        log.error("Telegram API exception", e);
    }

    @Override
    public void close() {
        bot.shutdown();
    }
}
