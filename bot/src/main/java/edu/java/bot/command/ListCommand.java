package edu.java.bot.command;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.utill.MessageUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class ListCommand extends AbstractCommand {
    private final static String COMMAND = "/list";
    private final static String DESCRIPTION = "Write tracking resources";
    private final MessageUtils messageUtils;

    public ListCommand(MessageUtils messageUtils) {
        super(COMMAND, DESCRIPTION);
        this.messageUtils = messageUtils;
    }

    @Override
    public SendMessage handle(Update update) {
        Long chatId = update.message().chat().id();
        log.info("ListCommand for chat: {} handling...", chatId);
        String message = messageUtils.getTrackLinks(chatId);
        return new SendMessage(chatId, message);
    }
}
