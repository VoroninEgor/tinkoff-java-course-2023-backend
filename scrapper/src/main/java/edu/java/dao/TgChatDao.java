package edu.java.dao;

import edu.java.dto.tgchatlinks.TgChatResponse;
import java.util.List;

public interface TgChatDao {
    void save(Long id);

    void remove(Long id);

    List<Long> fetchTgChatsIdByLink(String link);

    TgChatResponse fetchById(Long id);
}
