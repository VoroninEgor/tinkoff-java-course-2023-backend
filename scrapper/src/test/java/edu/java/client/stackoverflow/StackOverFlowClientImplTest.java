package edu.java.client.stackoverflow;

import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import edu.java.client.AbstractTest;
import edu.java.dto.stackoverflow.AnswersFetchResponse;
import edu.java.dto.stackoverflow.QuestionResponse;
import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static java.lang.String.format;
import static org.junit.jupiter.api.Assertions.assertEquals;

@WireMockTest(httpPort = 8080)
class StackOverFlowClientImplTest extends AbstractTest {

    String baseUrl;
    StackOverFlowClientImpl stackOverFlowClient;
    Long id;

    @BeforeEach
    void setUp() {
        baseUrl = "http://localhost:8080";
        stackOverFlowClient = new StackOverFlowClientImpl(baseUrl);
        id = 5L;
    }

    @Test
    void fetchQuestion_shouldReturnQuestionId() {
        String bodyJson = jsonToString("src/test/resources/stackoverflow.json");

        stubFor(get("/questions/" + id + "?site=stackoverflow.com").willReturn(aResponse()
            .withStatus(200)
            .withHeader("Content-Type", "application/json")
            .withBody(bodyJson)));

        QuestionResponse questionResponse = stackOverFlowClient.fetchQuestion(id);
        QuestionResponse.QuestionItem first = questionResponse.items().getFirst();

        assertEquals(6349421L, first.id());
    }

    @Test
    void fetchQuestion_shouldReturnQuestionUpdatedAt() {
        String bodyJson = jsonToString("src/test/resources/stackoverflow.json");

        stubFor(get("/questions/" + id + "?site=stackoverflow.com").willReturn(aResponse()
            .withStatus(200)
            .withHeader("Content-Type", "application/json")
            .withBody(bodyJson)));

        QuestionResponse questionResponse = stackOverFlowClient.fetchQuestion(id);
        QuestionResponse.QuestionItem first = questionResponse.items().getFirst();

        assertEquals(
            OffsetDateTime.ofInstant(Instant.ofEpochSecond(1698748588L), ZoneId.of("UTC")),
            first.updatedAt()
        );
    }

    @Test
    void fetchAnswersSince() {
        String bodyJson = jsonToString("src/test/resources/stackoverflow-answers.json");

        OffsetDateTime since = OffsetDateTime
            .of(2011, 3, 20, 6, 5, 8, 0, ZoneOffset.UTC);

        stubFor(get(format("/questions/%s/answers?site=stackoverflow.com", id)).willReturn(aResponse()
            .withStatus(200)
            .withHeader("Content-Type", "application/json")
            .withBody(bodyJson)));

        AnswersFetchResponse answersFetchResponse = stackOverFlowClient.fetchAnswersSince(5L, since);

        assertEquals(1, answersFetchResponse.items().size());
    }
}
