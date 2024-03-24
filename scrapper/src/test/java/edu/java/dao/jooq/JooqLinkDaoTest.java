package edu.java.dao.jooq;

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

class JooqLinkDaoTest extends IntegrationTest {

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
        linkDao.save(link);
        String response = jdbcTemplate.queryForObject("SELECT url from links WHERE url = ?", String.class, link);
        assertEquals(link, response);
    }

    @Transactional
    @Test
    void remove() {
        String link = "link";
        linkDao.save(link);
        Integer countBeforeRemoving = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM links", Integer.class);
        linkDao.remove(link);
        Integer countAfterRemoving = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM links", Integer.class);
        assertEquals(1, countBeforeRemoving - countAfterRemoving);
    }

    @Transactional
    @Test
    void findAllByTgChatId() {
        Long link1Id = linkDao.save("link1");
        Long link2Id = linkDao.save("link2");
        tgChatDao.save(1L);
        tgChatLinksDao.save(1L, link1Id);
        tgChatLinksDao.save(1L, link2Id);

        List<LinkResponse> linkResponses = linkDao.findAllByTgChatId(1L);
        assertEquals(2, linkResponses.size());
    }

    @Transactional
    @Test
    void findIdByUrl() {
        String link = "link";
        Long link1Id = linkDao.save(link);
        Long idByUrl = linkDao.findIdByUrl(link);
        assertEquals(link1Id, idByUrl);
    }

    @Transactional
    @Test
    void findLinksToCheckForUpdates() {
        linkDao.save("link1");
        linkDao.save("link2");
        List<LinkUpdateResponse> linksToCheckForUpdates = linkDao.findLinksToCheckForUpdates(10L);
        assertEquals(2, linksToCheckForUpdates.size());
    }

    @Transactional
    @Test
    void update() {
        String link = "link";
        linkDao.save(link);
        int before = linkDao.findLinksToCheckForUpdates(10L).size();

        linkDao.update(link, OffsetDateTime.now());

        int after = linkDao.findLinksToCheckForUpdates(10L).size();

        assertEquals(1, before);
        assertEquals(0, after);
    }
}
