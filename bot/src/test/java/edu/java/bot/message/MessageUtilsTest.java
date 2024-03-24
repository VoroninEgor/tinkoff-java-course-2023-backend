package edu.java.bot.message;

import edu.java.bot.client.ScrapperLinkClient;
import edu.java.bot.client.ScrapperLinkClientImpl;
import edu.java.bot.client.ScrapperTgChatClient;
import edu.java.bot.client.ScrapperTgChatClientImpl;
import edu.java.bot.command.ListCommand;
import edu.java.bot.command.StartCommand;
import edu.java.bot.dto.LinkResponse;
import edu.java.bot.dto.ListLinksResponse;
import edu.java.bot.utill.MessageUtils;
import java.net.URI;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class MessageUtilsTest {

    ScrapperLinkClient scrapperLinkClient;

    @BeforeEach
    void setUp() {
        scrapperLinkClient = mock(ScrapperLinkClientImpl.class);
    }

    @Test
    public void getEmptyTrackList() {
        when(scrapperLinkClient.getLinksByChatId(any())).thenReturn(new ListLinksResponse(List.of(), 0));
        MessageUtils messageUtils = new MessageUtils(scrapperLinkClient);

        String message = messageUtils.getTrackLinks(5L);

        assertEquals("You don't have tracking resources, use /track", message);
    }

    @Test
    public void getNotEmptyTrackList() {
        when(scrapperLinkClient.getLinksByChatId(any()))
            .thenReturn(new ListLinksResponse(List.of(new LinkResponse(1L, URI.create("Track1"))), 1));
        MessageUtils messageUtils = new MessageUtils(scrapperLinkClient);

        String message = messageUtils.getTrackLinks(5L);

        assertEquals("You've tracked:\n# Track1\n", message);
    }

    @Test
    public void getCommandsDescription() {
        MessageUtils mockMessageUtils = mock(MessageUtils.class);
        ScrapperTgChatClient scrapperTgChatClient = mock(ScrapperTgChatClientImpl.class);

        StartCommand startCommand = new StartCommand(scrapperTgChatClient);
        ListCommand listCommand = new ListCommand(mockMessageUtils);

        MessageUtils messageUtils = new MessageUtils(scrapperLinkClient);

        assertEquals(
            "Available commands:\n/list | Write tracking resources\n/list | Write tracking resources"
            , messageUtils.getCommandsDescription(List.of(startCommand, listCommand, listCommand)));
    }

}
