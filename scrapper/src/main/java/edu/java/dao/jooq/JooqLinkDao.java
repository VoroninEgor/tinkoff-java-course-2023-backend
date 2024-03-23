package edu.java.dao.jooq;

import edu.java.dao.LinkDao;
import edu.java.dto.link.LinkResponse;
import edu.java.dto.link.LinkUpdateResponse;
import edu.java.jooq.tables.records.LinksRecord;
import java.net.URI;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.jooq.DSLContext;
import org.springframework.stereotype.Repository;
import static edu.java.jooq.Tables.LINKS;
import static edu.java.jooq.Tables.TGCHAT_LINKS;

@Repository
@RequiredArgsConstructor
public class JooqLinkDao implements LinkDao {

    private final DSLContext dslContext;

    @SuppressWarnings("checkstyle:MagicNumber")
    @Override
    public Long save(String link) {
        LocalDateTime epochDate = LocalDateTime.of(1970, 1, 1, 0, 0, 0);
        LinksRecord linksRecord = dslContext.insertInto(LINKS)
            .set(LINKS.URL, link)
            .set(LINKS.LAST_CHECK, epochDate)
            .returning(LINKS.ID)
            .fetchOne();
        return linksRecord.getId();
    }

    @Override
    public void remove(String link) {
        dslContext.deleteFrom(LINKS)
            .where(LINKS.URL.eq(link))
            .execute();
    }

    @Override
    public List<LinkResponse> findAllByTgChatId(Long tgChatId) {
        return dslContext.select(LINKS.ID, LINKS.URL)
            .from(TGCHAT_LINKS)
            .join(LINKS)
            .on(TGCHAT_LINKS.LINKS_ID.eq(LINKS.ID))
            .where(TGCHAT_LINKS.TGCHATS_ID.eq(tgChatId))
            .fetch()
            .stream().map(res -> new LinkResponse(
                res.getValue(LINKS.ID),
                URI.create(res.getValue(LINKS.URL))
            ))
            .collect(Collectors.toList());
    }

    @Override
    public Long findIdByUrl(String link) {
        return dslContext.select(LINKS.ID)
            .from(LINKS)
            .where(LINKS.URL.eq(link))
            .fetchOneInto(Long.class);
    }

    @Override
    public List<LinkUpdateResponse> findLinksToCheckForUpdates(Long forceCheckDelay) {
        LocalDateTime timeToCheckBefore = OffsetDateTime.now().minusMinutes(forceCheckDelay).toLocalDateTime();
        return dslContext.select(LINKS.ID, LINKS.URL, LINKS.LAST_CHECK)
            .from(LINKS)
            .where(LINKS.LAST_CHECK
                .lessThan(timeToCheckBefore))
            .fetch()
            .stream().map(res -> new LinkUpdateResponse(
                res.getValue(LINKS.ID),
                URI.create(res.getValue(LINKS.URL)),
                res.getValue(LINKS.LAST_CHECK).atOffset(ZoneOffset.UTC)
            ))
            .collect(Collectors.toList());
    }

    @Override
    public void update(String link, OffsetDateTime lastCheck) {
        dslContext.update(LINKS)
            .set(LINKS.LAST_CHECK, lastCheck.toLocalDateTime())
            .where(LINKS.URL.eq(link))
            .execute();
    }
}
