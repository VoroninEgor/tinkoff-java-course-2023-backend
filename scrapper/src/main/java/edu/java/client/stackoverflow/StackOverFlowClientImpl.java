package edu.java.client.stackoverflow;

import edu.java.dto.stackoverflow.AnswersFetchResponse;
import edu.java.dto.stackoverflow.QuestionResponse;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.reactive.function.client.WebClient;
import static java.lang.String.format;

@Slf4j
public class StackOverFlowClientImpl implements StackOverFlowClient {
    private final static String BASEURL = "https://api.stackexchange.com/2.3/";
    private final static String ANSWERS_URL = "questions/{id}/answers?site=stackoverflow.com";


    private final WebClient webClient;

    public StackOverFlowClientImpl(String baseUrl) {
        webClient = WebClient.create(baseUrl);
    }

    public StackOverFlowClientImpl() {
        webClient = WebClient.create(BASEURL);
    }

    @Override
    public QuestionResponse fetchQuestion(Long id) {
        return webClient
            .get()
            .uri(uriBuilder -> uriBuilder
                .path(format("questions/%s", id))
                .queryParam("site", "stackoverflow.com")
                .build())
            .retrieve()
            .bodyToMono(QuestionResponse.class)
            .block();
    }

    @Override
    public AnswersFetchResponse fetchAnswersSince(Long questionId, OffsetDateTime since) {
        List<AnswersFetchResponse.Answer> answers = webClient
            .get()
            .uri(ANSWERS_URL, questionId)
            .retrieve()
            .bodyToMono(AnswersFetchResponse.class)
            .block()
            .items();

        if (answers == null || answers.isEmpty()) {
            return new AnswersFetchResponse(List.of());
        }

        List<AnswersFetchResponse.Answer> answersSince = answers.stream()
            .filter(ans -> ans.createdAt().isAfter(since))
            .collect(Collectors.toList());
        return new AnswersFetchResponse(answersSince);
    }
}
