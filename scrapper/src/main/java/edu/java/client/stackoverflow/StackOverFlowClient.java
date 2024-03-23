package edu.java.client.stackoverflow;

import edu.java.dto.stackoverflow.AnswersFetchResponse;
import edu.java.dto.stackoverflow.QuestionResponse;
import java.time.OffsetDateTime;

public interface StackOverFlowClient {
    QuestionResponse fetchQuestion(Long id);

    AnswersFetchResponse fetchAnswersSince(Long questionId, OffsetDateTime since);
}
