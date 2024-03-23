package edu.java.scheduler;

import edu.java.client.bot.BotClient;
import edu.java.dto.LinkUpdateRequest;
import edu.java.dto.link.LinkUpdateResponse;
import edu.java.service.LinkService;
import edu.java.service.TgChatService;
import edu.java.util.LinkUpdateChecker;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@EnableScheduling
@RequiredArgsConstructor
@Slf4j
public class LinkUpdaterScheduler {
    private final LinkService linkService;
    private final TgChatService tgChatService;
    private final LinkUpdateChecker linkUpdateChecker;
    private final BotClient botClient;
    private final Long forceCheckDelay;

    @Scheduled(fixedDelayString = "#{@schedulerIntervalMs}")
    public void update() {
        log.info("LinkUpdaterScheduler hase seen update");

        List<LinkUpdateResponse> linksToCheckForUpdates = linkService.findLinksToCheckForUpdates(forceCheckDelay);
        List<LinkUpdateRequest> linksToUpdate = getLinksToUpdate(linksToCheckForUpdates);

        log.info("Updated links: {}", linksToUpdate);

        linksToUpdate.forEach(botClient::updatesPost);
    }

    private List<LinkUpdateRequest> getLinksToUpdate(List<LinkUpdateResponse> linksToCheckForUpdates) {
        List<LinkUpdateRequest> linksToUpdate = new ArrayList<>();

        for (LinkUpdateResponse linkToCheck : linksToCheckForUpdates) {
            OffsetDateTime oldUpdateTime = linkToCheck.updatedAt();
            OffsetDateTime lastUpdateTime = linkUpdateChecker.getLastUpdateTime(linkToCheck.url().toString());
            if (lastUpdateTime == null) {
                log.warn("link not found update");

                linkService.remove(linkToCheck.url());
            } else if (oldUpdateTime == null || oldUpdateTime.isBefore(lastUpdateTime)) {
                log.info("old update time: {} | new update time: {}", oldUpdateTime, lastUpdateTime);

                List<Long> tgChatsId = tgChatService.fetchTgChatsIdByLink(linkToCheck.url());
                LinkUpdateRequest linkToUpdate = LinkUpdateRequest.builder()
                    .id(linkToCheck.id())
                    .url(linkToCheck.url())
                    .tgChatIds(tgChatsId)
                    .description("There is update for " + linkToCheck.url())
                    .build();
                linksToUpdate.add(linkToUpdate);
            }
            linkService.updateLink(linkToCheck.url(), lastUpdateTime);
        }

        return linksToUpdate;
    }
}
