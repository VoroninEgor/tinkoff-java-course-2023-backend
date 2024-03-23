package edu.java.service.jdbc;

import edu.java.dao.JdbcLinkDao;
import edu.java.dao.JdbcTgChatLinksDao;
import edu.java.dto.AddLinkRequest;
import edu.java.dto.RemoveLinkRequest;
import edu.java.dto.link.LinkResponse;
import edu.java.dto.link.LinkUpdateResponse;
import edu.java.dto.link.ListLinksResponse;
import edu.java.service.LinkService;
import java.net.URI;
import java.time.OffsetDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class JdbcLinkService implements LinkService {
    private final JdbcLinkDao linkRepository;
    private final JdbcTgChatLinksDao chatLinksRepository;

    @Transactional
    @Override
    public LinkResponse untrackLinkForUser(Long tgChatId, RemoveLinkRequest removeLinkRequest) {
        URI url = removeLinkRequest.link();
        Long linkId = null;
        try {
            linkId = linkRepository.findIdByUrl(url.toString());
            chatLinksRepository.remove(tgChatId, linkId);
        } catch (EmptyResultDataAccessException e) {
            log.warn("Link {} does not exist to delete", url);
        }
        return LinkResponse.builder()
            .id(linkId)
            .link(url)
            .build();
    }

    @Override
    public ListLinksResponse getLinks(Long tgChatId) {
        List<LinkResponse> links = linkRepository.findAllByTgChatId(tgChatId);
        return new ListLinksResponse(links, links.size());
    }

    @Transactional
    @Override
    public LinkResponse trackLinkForUser(Long tgChatId, AddLinkRequest addLinkRequest) {
        URI url = addLinkRequest.link();
        Long linkId = null;
        try {
            linkId = linkRepository.findIdByUrl(url.toString());
        } catch (EmptyResultDataAccessException e) {
            log.warn("Link {} was not added yet", url);
        }
        if (linkId == null) {
            linkId = linkRepository.save(url.toString());
        }
        try {
            chatLinksRepository.save(tgChatId, linkId);
        } catch (DuplicateKeyException e) {
            log.warn("Link was added already");
        }

        return LinkResponse.builder()
            .id(linkId)
            .link(url)
            .build();
    }

    @Override
    public List<LinkUpdateResponse> findLinksToCheckForUpdates(Long forceCheckDelay) {
        log.info("Find links to check for updates...");
        return linkRepository.findLinksToCheckForUpdates(forceCheckDelay);
    }

    @Transactional
    @Override
    public void updateLink(URI link, OffsetDateTime updatedAt) {
        log.info("Update link {}  with new updatedAt: {}", link, updatedAt);
        linkRepository.update(link.toString(), updatedAt);
    }

    @Transactional
    @Override
    public void remove(URI link) {
        linkRepository.remove(link.toString());
    }
}
