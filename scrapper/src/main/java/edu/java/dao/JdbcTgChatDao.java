package edu.java.dao;

import edu.java.dto.tgchatlinks.TgChatResponse;
import edu.java.util.mapper.TgChatLinksToTgChatIdMapper;
import edu.java.util.mapper.TgChatResponseMapper;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@RequiredArgsConstructor
@Repository
@Slf4j
public class JdbcTgChatDao {

    private final JdbcTemplate jdbcTemplate;
    private final TgChatLinksToTgChatIdMapper tgChatIdMapper;
    private final TgChatResponseMapper tgChatResponseMapper;

    public void save(Long id) {
        LocalDateTime createdAt = LocalDateTime.now();
        jdbcTemplate.update("INSERT INTO tgchats VALUES (?, ?)", id, Timestamp.valueOf(createdAt));
    }

    public void remove(Long id) {
        jdbcTemplate.update("DELETE FROM tgchats where id = ?", id);
    }

    public List<Long> fetchTgChatsIdByLink(String link) {
        String sql = "SELECT tgchat_links.tgchats_id from tgchat_links where links_id = "
            + "(SELECT id from links where url = ?)";
        return jdbcTemplate.query(sql, tgChatIdMapper, link);
    }

    public TgChatResponse fetchById(Long id) {
        return jdbcTemplate.queryForObject("SELECT * FROM tgchats WHERE id=?", tgChatResponseMapper, id);
    }
}
