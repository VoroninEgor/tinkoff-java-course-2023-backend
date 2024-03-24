package edu.java.dao.jooq;

import edu.java.dao.TgChatDao;
import edu.java.dto.tgchatlinks.TgChatResponse;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.jooq.DSLContext;
import org.springframework.stereotype.Repository;
import static edu.java.jooq.Tables.LINKS;
import static edu.java.jooq.Tables.TGCHATS;
import static edu.java.jooq.Tables.TGCHAT_LINKS;

@Repository
@RequiredArgsConstructor
public class JooqTgChatDao implements TgChatDao {

    private final DSLContext dslContext;

    @Override
    public void save(Long id) {
        dslContext.insertInto(TGCHATS)
            .set(TGCHATS.ID, id)
            .set(TGCHATS.CREATED_AT, LocalDateTime.now())
            .execute();
    }

    @Override
    public void remove(Long id) {
        dslContext.deleteFrom(TGCHATS)
            .where(TGCHATS.ID.eq(id))
            .execute();
    }

    @Override
    public List<Long> fetchTgChatsIdByLink(String link) {
        return dslContext.select(TGCHAT_LINKS.TGCHATS_ID)
            .from(TGCHAT_LINKS)
            .join(LINKS).on(TGCHAT_LINKS.LINKS_ID.eq(LINKS.ID))
            .where(LINKS.URL.eq(link))
            .fetch(TGCHAT_LINKS.TGCHATS_ID);
    }

    @Override
    public TgChatResponse fetchById(Long id) {
        return dslContext.select(TGCHATS.ID, TGCHATS.CREATED_AT)
            .from(TGCHATS)
            .where(TGCHATS.ID.eq(id))
            .fetchOneInto(TgChatResponse.class);
    }
}
