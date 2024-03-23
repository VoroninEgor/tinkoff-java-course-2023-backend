package edu.java.service;

import edu.java.dto.tgchatlinks.TgChatResponse;
import java.net.URI;
import java.util.List;

public interface TgChatService {
    void unregister(Long id);

    void register(Long id);

    List<Long> fetchTgChatsIdByLink(URI link);

    TgChatResponse fetchById(Long id);
}
