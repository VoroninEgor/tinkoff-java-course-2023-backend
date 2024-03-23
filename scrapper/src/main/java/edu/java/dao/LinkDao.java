package edu.java.dao;

import edu.java.dto.link.LinkResponse;
import edu.java.dto.link.LinkUpdateResponse;
import java.time.OffsetDateTime;
import java.util.List;

public interface LinkDao {
    Long save(String link);

    void remove(String link);

    List<LinkResponse> findAllByTgChatId(Long tgChatId);

    Long findIdByUrl(String link);

    List<LinkUpdateResponse> findLinksToCheckForUpdates(Long forceCheckDelay);

    void update(String link, OffsetDateTime lastCheck);
}
