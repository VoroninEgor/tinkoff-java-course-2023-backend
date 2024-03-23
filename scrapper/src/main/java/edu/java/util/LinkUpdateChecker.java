package edu.java.util;

import edu.java.dto.LastUpdate;
import edu.java.util.checker.UrlChecker;
import java.time.OffsetDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class LinkUpdateChecker {

    private final List<UrlChecker> urlCheckers;

    public LastUpdate getLastUpdate(String url, OffsetDateTime lastCheck) {
        log.info("get last update time for {}", url);

        for (UrlChecker urlChecker : urlCheckers) {
            if (urlChecker.isLinkTrackable(url)) {
                return urlChecker.getLastUpdate(url, lastCheck);
            }
        }

        return LastUpdate.builder().isUpdated(false).build();
    }
}
