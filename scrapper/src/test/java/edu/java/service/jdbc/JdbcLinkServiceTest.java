package edu.java.service.jdbc;

import edu.java.dto.link.LinkUpdateResponse;
import edu.java.scrapper.IntegrationTest;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;
import static org.junit.jupiter.api.Assertions.assertEquals;

class JdbcLinkServiceTest extends IntegrationTest {

    @Autowired
    JdbcLinkService linkService;
    @Autowired
    JdbcTemplate jdbcTemplate;

    @Transactional
    @Test
    @Sql("/schema/insert-links-table.sql")
    void findLinksToCheckForUpdates() {
        List<LinkUpdateResponse> linksToCheckForUpdates = linkService.findLinksToCheckForUpdates(10L);
        assertEquals(1, linksToCheckForUpdates.size());
        assertEquals("url2", linksToCheckForUpdates.getFirst().url().toString());
    }
}
