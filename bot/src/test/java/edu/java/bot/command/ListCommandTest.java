package edu.java.bot.command;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.client.ScrapperLinkClient;
import edu.java.bot.dto.LinkResponse;
import edu.java.bot.dto.ListLinksResponse;
import java.net.URI;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class ListCommandTest extends AbstractCommandTest {

    @Autowired
    ListCommand listCommand;
    @MockBean
    ScrapperLinkClient scrapperLinkClient;

    @Test
    public void handleEmptyTrackList() {
        when(scrapperLinkClient.getLinksByChatId(any())).thenReturn(new ListLinksResponse(List.of(), 0));
        Update update = getMockUpdate(1L, "text");

        SendMessage sendMessage = listCommand.handle(update);

        assertEquals("You don't have tracking resources, use /track"
            , sendMessage.getParameters().get("text"));
    }

    @Test
    public void handleNotEmptyTrackList() {
        List<LinkResponse> list = List.of(new LinkResponse(5L, URI.create("http://github.com")),
            new LinkResponse(5L, URI.create("http://stackoverflow.com")));
        ListLinksResponse response = new ListLinksResponse(list, 2);
        when(scrapperLinkClient.getLinksByChatId(any())).thenReturn(response);
        Update update = getMockUpdate(2L, "text");

        SendMessage sendMessage = listCommand.handle(update);

        assertEquals("You've tracked:\n# http://github.com\n# http://stackoverflow.com\n"
            , sendMessage.getParameters().get("text"));
    }

}
