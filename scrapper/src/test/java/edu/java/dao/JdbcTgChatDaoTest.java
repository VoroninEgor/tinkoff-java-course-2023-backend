package edu.java.dao;

import edu.java.dao.jdbc.JdbcLinkDao;
import edu.java.dao.jdbc.JdbcTgChatDao;
import edu.java.dao.jdbc.JdbcTgChatLinksDao;
import edu.java.dto.tgchatlinks.TgChatResponse;
import edu.java.scrapper.IntegrationTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import static org.junit.jupiter.api.Assertions.assertEquals;

class JdbcTgChatDaoTest extends IntegrationTest {

    @Autowired
    JdbcTgChatDao jdbcTgChatDao;
    @Autowired
    JdbcLinkDao jdbcLinkDao;
    @Autowired
    JdbcTgChatLinksDao jdbcTgChatLinksDao;
    @Autowired
    JdbcTemplate jdbcTemplate;

    @Transactional
    @Test
    void save() {
        Integer countBefore = jdbcTemplate.queryForObject("SELECT COUNT(*) from tgchats", Integer.class);
        jdbcTgChatDao.save(1L);
        Integer countAfter = jdbcTemplate.queryForObject("SELECT COUNT(*) from tgchats", Integer.class);
        assertEquals(1, countAfter - countBefore);
    }

    @Transactional
    @Test
    void remove() {
        jdbcTgChatDao.save(1L);
        Integer countBefore = jdbcTemplate.queryForObject("SELECT COUNT(*) from tgchats", Integer.class);
        jdbcTgChatDao.remove(1L);
        Integer countAfter = jdbcTemplate.queryForObject("SELECT COUNT(*) from tgchats", Integer.class);

        assertEquals(1, countBefore - countAfter);
    }

    @Transactional
    @Test
    void fetchTgChatsIdByLink() {
        String link = "link";
        Long linkId = jdbcLinkDao.save(link);
        jdbcTgChatDao.save(1L);
        jdbcTgChatDao.save(2L);
        jdbcTgChatLinksDao.save(1L, linkId);
        jdbcTgChatLinksDao.save(2L, linkId);

        List<Long> tgChatIds = jdbcTgChatDao.fetchTgChatsIdByLink(link);
        assertEquals(2, tgChatIds.size());
    }

    @Transactional
    @Test
    void fetchById() {
        jdbcTgChatDao.save(1L);
        TgChatResponse tgChatResponse = jdbcTgChatDao.fetchById(1L);
        assertEquals(1L, tgChatResponse.id());
    }
}
