package edu.java.scheduler;

import edu.java.client.bot.BotClient;
import edu.java.dto.LastUpdate;
import edu.java.dto.link.LinkUpdateRequest;
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

            LastUpdate lastUpdate
                = linkUpdateChecker.getLastUpdate(linkToCheck.url().toString(), linkToCheck.lastCheck());
            OffsetDateTime lastCheckTime = lastUpdate.lastCheck();

            if (lastUpdate.isUpdated()) {
                List<Long> tgChatsId = tgChatService.fetchTgChatsIdByLink(linkToCheck.url());
                LinkUpdateRequest linkToUpdate = LinkUpdateRequest.builder()
                    .id(linkToCheck.id())
                    .url(linkToCheck.url())
                    .tgChatIds(tgChatsId)
                    .description(lastUpdate.description())
                    .build();
                linksToUpdate.add(linkToUpdate);
            }
            linkService.updateLink(linkToCheck.url(), lastCheckTime);
        }

        return linksToUpdate;
    }
}
