package edu.java.bot.command;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.client.ScrapperLinkClient;
import edu.java.bot.dto.LinkResponse;
import edu.java.bot.dto.RemoveLinkRequest;
import edu.java.bot.utill.MessageUtils;
import java.net.URI;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class UntrackCommand extends AbstractCommand {
    private final static String COMMAND = "/untrack";
    private final static String DESCRIPTION = "Stop tracking";
    private final static String MESSAGE = "Use a valid tracking URL as a parameter in the form like '/untrack <link>'";

    private final MessageUtils messageUtils;
    private final ScrapperLinkClient scrapperLinkClient;

    public UntrackCommand(
        MessageUtils messageUtils,
        ScrapperLinkClient scrapperLinkClient
    ) {
        super(COMMAND, DESCRIPTION);
        this.messageUtils = messageUtils;
        this.scrapperLinkClient = scrapperLinkClient;
    }

    @Override
    public SendMessage handle(Update update) {
        Long chatId = update.message().chat().id();
        log.info("UntrackCommand for chat: {} handling...", chatId);
        String url = messageUtils.parseUrlFromText(update.message().text());
        LinkResponse linkResponse = scrapperLinkClient
            .removeLinkByChatId(chatId, new RemoveLinkRequest(URI.create(url)));
        if (linkResponse.id() != null) {
            return new SendMessage(chatId, "Successfully stop tracking");
        }
        log.warn("UntrackCommand chat {} sent invalid link", chatId);
        return new SendMessage(chatId, MESSAGE);
    }
}
