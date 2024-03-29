package edu.java.service;

import edu.java.dto.AddLinkRequest;
import edu.java.dto.RemoveLinkRequest;
import edu.java.dto.link.LinkResponse;
import edu.java.dto.link.LinkUpdateResponse;
import edu.java.dto.link.ListLinksResponse;
import java.net.URI;
import java.time.OffsetDateTime;
import java.util.List;

public interface LinkService {
    LinkResponse untrackLinkForUser(Long tgChatId, RemoveLinkRequest removeLinkRequest);

    ListLinksResponse getLinks(Long tgChatId);

    LinkResponse trackLinkForUser(Long tgChatId, AddLinkRequest addLinkRequest);

    List<LinkUpdateResponse> findLinksToCheckForUpdates(Long forceCheckDelay);

    void updateLink(URI link, OffsetDateTime updatedAt);

    void remove(URI link);
}
