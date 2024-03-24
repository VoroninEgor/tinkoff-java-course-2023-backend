package edu.java.dao.jooq;

import edu.java.scrapper.IntegrationTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

class JooqTgChatLinksDaoTest extends IntegrationTest {

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
        String link = "link";
        Long linkId = linkDao.save(link);
        tgChatDao.save(1L);
        tgChatDao.save(2L);
        tgChatLinksDao.save(1L, linkId);
        tgChatLinksDao.save(2L, linkId);

        Integer count = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM tgchat_links", Integer.class);
        assertEquals(2, count);
    }

    @Transactional
    @Test
    void remove() {
        String link = "link";
        Long linkId = linkDao.save(link);
        tgChatDao.save(1L);
        tgChatDao.save(2L);
        tgChatLinksDao.save(1L, linkId);
        Integer countBefore = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM tgchat_links", Integer.class);

        tgChatLinksDao.remove(1L, linkId);
        Integer countAfter = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM tgchat_links", Integer.class);

        assertEquals(1, countBefore - countAfter);
    }
}
