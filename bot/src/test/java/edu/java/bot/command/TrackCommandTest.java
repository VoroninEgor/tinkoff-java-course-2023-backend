package edu.java.bot.command;

import com.pengrad.telegrambot.model.Chat;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.utill.URLChecker;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.when;

@SpringBootTest
class TrackCommandTest {

    @Autowired
    TrackCommand trackCommand;

    @BeforeAll
    static void setUp() {
        mockStatic(URLChecker.class);
    }

    @Test
    void handleCorrectUrl() {

        String validUrl = "http://github.com";
        String commandMessage = "/track http://github.com";
        when(URLChecker.isValid(validUrl)).thenReturn(true);

        Update update = mock(Update.class);
        Chat chat = mock(Chat.class);
        when(chat.id()).thenReturn(5L);
        Message message = mock(Message.class);
        when(message.text()).thenReturn(commandMessage);
        when(message.chat()).thenReturn(chat);
        when(update.message()).thenReturn(message);

        SendMessage sendMessage = trackCommand.handle(update);

        assertEquals("Successfully added!", sendMessage.getParameters().get("text"));
        trackCommand.handle(update);
    }

    @Test
    void handleIncorrectUrl() {
        String invalidUrl = "http://github.com";
        String commandMessage = "/track http://github.com";
        when(URLChecker.isValid(invalidUrl)).thenReturn(false);

        Update update = mock(Update.class);
        Chat chat = mock(Chat.class);
        when(chat.id()).thenReturn(5L);
        Message message = mock(Message.class);
        when(message.text()).thenReturn(commandMessage);
        when(message.chat()).thenReturn(chat);
        when(update.message()).thenReturn(message);

        SendMessage sendMessage = trackCommand.handle(update);

        assertEquals(
            "Use a valid URL as a parameter in the form like '/track <url>'",
            sendMessage.getParameters().get("text")
        );
        trackCommand.handle(update);
    }
}