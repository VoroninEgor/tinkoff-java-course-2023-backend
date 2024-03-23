package edu.java.bot.command;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.client.ScrapperLinkClient;
import edu.java.bot.dto.LinkResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import java.net.URI;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class UntrackCommandTest extends AbstractCommandTest {

    @Autowired
    UntrackCommand untrackCommand;
    @MockBean
    ScrapperLinkClient scrapperLinkClient;

    @Test
    void handleCorrectUrl_shouldReturnSuccessResponse() {
        String commandMessage = "/untrack https://github.com/VoroninEgor/tinkoff-java-course-2023-backend";
        when(scrapperLinkClient.removeLinkByChatId(any(), any())).thenReturn(new LinkResponse(5L, URI.create("url")));
        Update update = getMockUpdate(2L, commandMessage);

        SendMessage sendMessage = untrackCommand.handle(update);

        assertEquals("Successfully stop tracking", sendMessage.getParameters().get("text"));
    }

    @Test
    void handleIncorrectUrl() {
        when(scrapperLinkClient.removeLinkByChatId(any(), any())).thenReturn(new LinkResponse(null, URI.create("url")));
        String commandMessage = "/untrack http://invalidurl";
        Update update = getMockUpdate(2L, commandMessage);

        SendMessage sendMessage = untrackCommand.handle(update);

        assertEquals(
            "Use a valid tracking URL as a parameter in the form like '/untrack <link>'",
            sendMessage.getParameters().get("text")
        );
    }
}
