package edu.java.util.checker;

import edu.java.dto.LastUpdate;
import java.time.OffsetDateTime;

public interface UrlChecker {
    boolean isLinkTrackable(String url);

    LastUpdate getLastUpdate(String url, OffsetDateTime since);
}
