package edu.java.dao;

public interface TgChatLinksDao {
    void save(Long tgChatId, Long linkId);

    void remove(Long tgChatId, Long linkId);
}
