package edu.java.dao.jooq;

import edu.java.dto.tgchatlinks.TgChatResponse;
import edu.java.scrapper.IntegrationTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class JooqTgChatDaoTest extends IntegrationTest {

    @Autowired
    JooqLinkDao linkDao;
    @Autowired
    JooqTgChatDao tgChatDao;
    @Autowired
    JooqTgChatLinksDao tgChatLinksDao;
    @Autowired
    JdbcTemplate jdbcTemplate;

    @Transactional
    @Test
    void save() {
        Integer countBefore = jdbcTemplate.queryForObject("SELECT COUNT(*) from tgchats", Integer.class);
        tgChatDao.save(1L);
        Integer countAfter = jdbcTemplate.queryForObject("SELECT COUNT(*) from tgchats", Integer.class);
        assertEquals(1, countAfter - countBefore);
    }

    @Transactional
    @Test
    void remove() {
        tgChatDao.save(1L);
        Integer countBefore = jdbcTemplate.queryForObject("SELECT COUNT(*) from tgchats", Integer.class);
        tgChatDao.remove(1L);
        Integer countAfter = jdbcTemplate.queryForObject("SELECT COUNT(*) from tgchats", Integer.class);

        assertEquals(1, countBefore - countAfter);
    }

    @Transactional
    @Test
    void fetchTgChatsIdByLink() {
        String link = "link";
        Long linkId = linkDao.save(link);
        tgChatDao.save(1L);
        tgChatDao.save(2L);
        tgChatLinksDao.save(1L, linkId);
        tgChatLinksDao.save(2L, linkId);

        List<Long> tgChatIds = tgChatDao.fetchTgChatsIdByLink(link);
        assertEquals(2, tgChatIds.size());
    }

    @Transactional
    @Test
    void fetchById() {
        tgChatDao.save(1L);
        TgChatResponse tgChatResponse = tgChatDao.fetchById(1L);
        assertEquals(1L, tgChatResponse.id());
    }
}
