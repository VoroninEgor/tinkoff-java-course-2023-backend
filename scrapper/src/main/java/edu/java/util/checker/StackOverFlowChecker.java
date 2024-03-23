package edu.java.util.checker;

import edu.java.client.stackoverflow.StackOverFlowClient;
import edu.java.dto.LastUpdate;
import edu.java.dto.stackoverflow.AnswersFetchResponse;
import edu.java.util.UrlParser;
import java.net.URI;
import java.time.OffsetDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class StackOverFlowChecker implements UrlChecker {
    private static final String URL_REGEX = "^https://stackoverflow\\.com/questions/\\d+/.*$";

    private final UrlParser urlParser;
    private final StackOverFlowClient stackOverFlowClient;

    @Override
    public boolean isLinkTrackable(String url) {
        return url.matches(URL_REGEX);
    }

    @Override
    public LastUpdate getLastUpdate(String url, OffsetDateTime since) {
        Long questionId = urlParser.fetchQuestionIdFromStackOverFlowLink(url);
        AnswersFetchResponse answers = stackOverFlowClient.fetchAnswersSince(questionId, since);

        return LastUpdate.builder()
            .link(URI.create(url))
            .isUpdated(isUpdated(answers))
            .description(getDescription(answers, url))
            .lastCheck(OffsetDateTime.now())
            .build();
    }

    private String getDescription(AnswersFetchResponse answers, String url) {
        if (isUpdated(answers)) {
            return url + " has new comment";
        }
        return "No comment";
    }

    private boolean isUpdated(AnswersFetchResponse answersFetchResponse) {
        return !answersFetchResponse.items().isEmpty();
    }
}
