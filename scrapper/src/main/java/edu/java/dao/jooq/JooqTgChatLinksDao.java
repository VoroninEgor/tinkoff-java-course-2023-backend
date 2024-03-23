package edu.java.dao.jooq;

import edu.java.dao.TgChatLinksDao;
import lombok.RequiredArgsConstructor;
import org.jooq.DSLContext;
import org.springframework.stereotype.Repository;
import static edu.java.jooq.Tables.TGCHAT_LINKS;

@Repository
@RequiredArgsConstructor
public class JooqTgChatLinksDao implements TgChatLinksDao {

    private final DSLContext dslContext;

    @Override
    public void save(Long tgChatId, Long linkId) {
        dslContext.insertInto(TGCHAT_LINKS)
            .set(TGCHAT_LINKS.TGCHATS_ID, tgChatId)
            .set(TGCHAT_LINKS.LINKS_ID, linkId)
            .execute();
    }

    @Override
    public void remove(Long tgChatId, Long linkId) {
        dslContext.deleteFrom(TGCHAT_LINKS)
            .where(
                TGCHAT_LINKS.TGCHATS_ID.eq(tgChatId),
                TGCHAT_LINKS.LINKS_ID.eq(linkId)
            )
            .execute();
    }
}
