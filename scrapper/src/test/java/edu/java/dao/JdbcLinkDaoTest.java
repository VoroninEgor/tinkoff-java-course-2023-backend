package edu.java.dao;

import edu.java.dao.jdbc.JdbcLinkDao;
import edu.java.dao.jdbc.JdbcTgChatDao;
import edu.java.dao.jdbc.JdbcTgChatLinksDao;
import edu.java.dto.link.LinkResponse;
import edu.java.dto.link.LinkUpdateResponse;
import edu.java.scrapper.IntegrationTest;
import java.time.OffsetDateTime;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.annotation.Transactional;
import static org.junit.jupiter.api.Assertions.assertEquals;

class JdbcLinkDaoTest extends IntegrationTest {

    @Autowired
    JdbcLinkDao jdbcLinkDao;
    @Autowired
    JdbcTgChatDao jdbcTgChatDao;
    @Autowired
    JdbcTemplate jdbcTemplate;
    @Autowired
    JdbcTgChatLinksDao jdbcTgChatLinksDao;

    @Transactional
    @Test
    void save() {
        String link = "link";
        jdbcLinkDao.save(link);
        String response = jdbcTemplate.queryForObject("SELECT url from links WHERE url = ?", String.class, link);
        assertEquals(link, response);
    }

    @Transactional
    @Test
    void remove() {
        String link = "link";
        jdbcLinkDao.save(link);
        Integer countBeforeRemoving = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM links", Integer.class);
        jdbcLinkDao.remove(link);
        Integer countAfterRemoving = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM links", Integer.class);
        assertEquals(1, countBeforeRemoving - countAfterRemoving);
    }

    @Transactional
    @Test
    void findAllByTgChatId() {
        Long link1Id = jdbcLinkDao.save("link1");
        Long link2Id = jdbcLinkDao.save("link2");
        jdbcTgChatDao.save(1L);
        jdbcTgChatLinksDao.save(1L, link1Id);
        jdbcTgChatLinksDao.save(1L, link2Id);

        List<LinkResponse> linkResponses = jdbcLinkDao.findAllByTgChatId(1L);
        assertEquals(2, linkResponses.size());
    }

    @Transactional
    @Test
    void findIdByUrl() {
        String link = "link";
        Long link1Id = jdbcLinkDao.save(link);
        Long idByUrl = jdbcLinkDao.findIdByUrl(link);
        assertEquals(link1Id, idByUrl);
    }

    @Transactional
    @Test
    void findLinksToCheckForUpdates() {
        jdbcLinkDao.save("link1");
        jdbcLinkDao.save("link2");
        List<LinkUpdateResponse> linksToCheckForUpdates = jdbcLinkDao.findLinksToCheckForUpdates(10L);
        assertEquals(2, linksToCheckForUpdates.size());
    }

    @Transactional
    @Test
    void update() {
        String link = "link";
        jdbcLinkDao.save(link);
        int before = jdbcLinkDao.findLinksToCheckForUpdates(10L).size();

        jdbcLinkDao.update(link, OffsetDateTime.now());

        int after = jdbcLinkDao.findLinksToCheckForUpdates(10L).size();

        assertEquals(1, before);
        assertEquals(0, after);
    }
}
