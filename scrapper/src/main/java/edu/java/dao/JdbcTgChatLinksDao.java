package edu.java.dao;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@RequiredArgsConstructor
@Repository
@Slf4j
public class JdbcTgChatLinksDao {

    private final JdbcTemplate jdbcTemplate;

    public void save(Long tgChatId, Long linkId) {
        jdbcTemplate.update("INSERT INTO tgchat_links values (?, ?)", tgChatId, linkId);
    }

    public void remove(Long tgChatId, Long linkId) {
        jdbcTemplate.update("DELETE FROM tgchat_links WHERE tgchats_id=? AND links_id=?", tgChatId, linkId);
    }
}
