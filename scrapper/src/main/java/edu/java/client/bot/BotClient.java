package edu.java.client.bot;

import edu.java.dto.link.LinkUpdateRequest;

public interface BotClient {
    void updatesPost(LinkUpdateRequest linkUpdate);
}
