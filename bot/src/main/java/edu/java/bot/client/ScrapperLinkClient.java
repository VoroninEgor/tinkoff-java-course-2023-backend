package edu.java.bot.client;

import edu.java.bot.dto.AddLinkRequest;
import edu.java.bot.dto.LinkResponse;
import edu.java.bot.dto.ListLinksResponse;
import edu.java.bot.dto.RemoveLinkRequest;

public interface ScrapperLinkClient {
    LinkResponse removeLinkByChatId(Long tgChatId, RemoveLinkRequest removeLinkRequest);

    ListLinksResponse getLinksByChatId(Long tgChatId);

    LinkResponse postLinkByChatId(Long tgChatId, AddLinkRequest addLinkRequest);
}
