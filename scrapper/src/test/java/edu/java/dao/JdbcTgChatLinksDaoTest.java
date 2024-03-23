package edu.java.dao;

import edu.java.scrapper.IntegrationTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

class JdbcTgChatLinksDaoTest extends IntegrationTest {

    @Autowired
    JdbcTgChatLinksDao jdbcTgChatLinksDao;
    @Autowired
    JdbcLinkDao jdbcLinkDao;
    @Autowired
    JdbcTgChatDao jdbcTgChatDao;
    @Autowired
    JdbcTemplate jdbcTemplate;

    @Transactional
    @Test
    void save() {
        String link = "link";
        Long linkId = jdbcLinkDao.save(link);
        jdbcTgChatDao.save(1L);
        jdbcTgChatDao.save(2L);
        jdbcTgChatLinksDao.save(1L, linkId);
        jdbcTgChatLinksDao.save(2L, linkId);

        Integer count = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM tgchat_links", Integer.class);
        assertEquals(2, count);
    }

    @Transactional
    @Test
    void remove() {
        String link = "link";
        Long linkId = jdbcLinkDao.save(link);
        jdbcTgChatDao.save(1L);
        jdbcTgChatDao.save(2L);
        jdbcTgChatLinksDao.save(1L, linkId);
        Integer countBefore = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM tgchat_links", Integer.class);

        jdbcTgChatLinksDao.remove(1L, linkId);
        Integer countAfter = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM tgchat_links", Integer.class);

        assertEquals(1, countBefore - countAfter);

    }
}
