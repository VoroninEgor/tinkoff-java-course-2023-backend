package edu.java.dao;

import edu.java.dto.link.LinkResponse;
import edu.java.dto.link.LinkUpdateResponse;
import edu.java.util.mapper.LinkForUpdateResponseMapper;
import edu.java.util.mapper.LinkResponseMapper;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.time.OffsetDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

@RequiredArgsConstructor
@Repository
public class JdbcLinkDao {

    private final JdbcTemplate jdbcTemplate;
    private final LinkResponseMapper linkResponseMapper;
    private final LinkForUpdateResponseMapper linkForUpdateResponseMapper;

    public Long save(String link) {
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection
                .prepareStatement("INSERT INTO links (url) values (?)", Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, link);
            return ps;
        }, keyHolder);

        return (Long) keyHolder.getKeys().get("id");
    }

    public void remove(String link) {
        jdbcTemplate.update("DELETE FROM links where url = ?", link);
    }

    public List<LinkResponse> findAllByTgChatId(Long tgChatId) {
        String sql = "SELECT * FROM (SELECT * FROM tgchat_links where tgchats_id = ?) as \"tl\" "
            + "JOIN links ON tl.links_id = links.id;";
        return jdbcTemplate.query(sql, linkResponseMapper, tgChatId);
    }

    public Long findIdByUrl(String link) {
        return jdbcTemplate.queryForObject("SELECT id from links WHERE url = ?", Long.class, link);
    }

    @SuppressWarnings("checkstyle:MagicNumber")
    public List<LinkUpdateResponse> findLinksToCheckForUpdates(Long forceCheckDelay) {
        Long forceCheckInSec = forceCheckDelay * 60;
        String sql = "SELECT * FROM links WHERE EXTRACT(EPOCH FROM (now() - links.last_check)) > ?";
        return jdbcTemplate.query(sql, linkForUpdateResponseMapper, forceCheckInSec);
    }

    public void update(String link, OffsetDateTime updatedAt) {
        jdbcTemplate.update("UPDATE links SET updated_at=?, last_check=CURRENT_TIMESTAMP WHERE url=?",
            updatedAt, link
        );
    }
}
