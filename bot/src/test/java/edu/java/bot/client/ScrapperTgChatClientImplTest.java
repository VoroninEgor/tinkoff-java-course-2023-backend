package edu.java.bot.client;

import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.delete;
import static com.github.tomakehurst.wiremock.client.WireMock.post;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static org.junit.jupiter.api.Assertions.*;

@WireMockTest(httpPort = 8080)
class ScrapperTgChatClientImplTest {

    ScrapperTgChatClientImpl client;

    @BeforeEach
    void setUp() {
        String baseUrl = "http://localhost:8080";
        client = new ScrapperTgChatClientImpl(baseUrl);
    }

    @Test
    void tgChatIdDelete() {
        stubFor(delete("/tg-chat/5").willReturn(aResponse()));

        assertDoesNotThrow(() -> client.removeById(5L));
    }

    @Test
    void tgChatIdPost() {
        stubFor(post("/tg-chat/5").willReturn(aResponse()));

        assertDoesNotThrow(() -> client.create(5L));
    }
}
