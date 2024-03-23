package edu.java.bot.command;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.client.ScrapperLinkClient;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import static org.junit.jupiter.api.Assertions.assertEquals;

class TrackCommandTest extends AbstractCommandTest {
    @Autowired
    TrackCommand trackCommand;
    @MockBean
    ScrapperLinkClient scrapperLinkClient;

    @Test
    void handleCorrectUrl_shouldReturnSuccessResponse() {
        String commandMessage = "/track https://github.com/VoroninEgor/tinkoff-java-course-2023-backend";
        Update update = getMockUpdate(5L, commandMessage);

        SendMessage sendMessage = trackCommand.handle(update);

        assertEquals("Successfully added!", sendMessage.getParameters().get("text"));
    }

    @Test
    void handleIncorrectUrl() {
        String commandMessage = "/track http://github.com";
        Update update = getMockUpdate(5L, commandMessage);

        SendMessage sendMessage = trackCommand.handle(update);

        assertEquals(
            "Use a valid URL as a parameter in the form like '/track <link>'",
            sendMessage.getParameters().get("text")
        );
    }
}
