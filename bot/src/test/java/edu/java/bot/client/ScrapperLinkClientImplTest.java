package edu.java.bot.client;

import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import edu.java.bot.dto.AddLinkRequest;
import edu.java.bot.dto.LinkResponse;
import edu.java.bot.dto.ListLinksResponse;
import edu.java.bot.dto.RemoveLinkRequest;
import java.net.URI;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static com.github.tomakehurst.wiremock.client.WireMock.containing;
import static com.github.tomakehurst.wiremock.client.WireMock.delete;
import static com.github.tomakehurst.wiremock.client.WireMock.equalToJson;
import static com.github.tomakehurst.wiremock.client.WireMock.okJson;
import static com.github.tomakehurst.wiremock.client.WireMock.post;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@WireMockTest(httpPort = 8080)
class ScrapperLinkClientImplTest extends AbstractTest{

    ScrapperLinkClientImpl client;

    @BeforeEach
    void setUp() {
        var baseUrl = "http://localhost:8080";
        client = new ScrapperLinkClientImpl(baseUrl);
    }

    @Test
    void linksDelete() {
        RemoveLinkRequest removeLinkRequest = new RemoveLinkRequest(URI.create("url"));
        String expectedJson = jsonToString("src/test/resources/remove-link.json");
        String responseJson = jsonToString("src/test/resources/response-link.json");

        stubFor(delete("/links")
            .withHeader("Tg-Chat-Id", containing("5"))
            .withRequestBody(equalToJson(expectedJson))
            .willReturn(okJson(responseJson)));

        LinkResponse linkResponse = client.removeLinkByChatId(5L, removeLinkRequest);

        assertThat(linkResponse)
            .extracting(LinkResponse::id, LinkResponse::link)
            .containsExactly(5L, URI.create("url"));
    }

    @Test
    void linksGet() {
        String responseJson = jsonToString("src/test/resources/response-link-list.json");
        stubFor(WireMock.get("/links")
            .withHeader("Tg-Chat-Id", containing("5"))
            .willReturn(okJson(responseJson)));

        ListLinksResponse listLinksResponse = client.getLinksByChatId(5L);
        LinkResponse linkResponse = listLinksResponse.links().getFirst();

        assertThat(linkResponse)
            .extracting(LinkResponse::id, LinkResponse::link)
            .containsExactly(1L, URI.create("url"));
        assertEquals(1, listLinksResponse.size());
    }

    @Test
    void linksPost() {
        AddLinkRequest addLinkRequest = new AddLinkRequest(URI.create("url"));
        String expectedJson = jsonToString("src/test/resources/add-link.json");
        String responseJson = jsonToString("src/test/resources/response-link.json");

        stubFor(post("/links")
            .withHeader("Tg-Chat-Id", containing("5"))
            .withRequestBody(equalToJson(expectedJson))
            .willReturn(okJson(responseJson)));

        LinkResponse linkResponse = client.postLinkByChatId(5L, addLinkRequest);

        assertThat(linkResponse)
            .extracting(LinkResponse::id, LinkResponse::link)
            .containsExactly(5L, URI.create("url"));
    }
}
