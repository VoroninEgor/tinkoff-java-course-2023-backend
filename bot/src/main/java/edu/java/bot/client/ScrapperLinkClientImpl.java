package edu.java.bot.client;

import edu.java.bot.dto.AddLinkRequest;
import edu.java.bot.dto.LinkResponse;
import edu.java.bot.dto.ListLinksResponse;
import edu.java.bot.dto.RemoveLinkRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpMethod;
import org.springframework.web.reactive.function.client.WebClient;

@Slf4j
public class ScrapperLinkClientImpl implements ScrapperLinkClient {
    private final static String BASEURL = "http://localhost:8080/";
    private final static String BASE_ENDPOINT = "/links";
    private final static String TGCHAT_ID_HEADER = "Tg-Chat-Id";

    private final WebClient webClient;

    public ScrapperLinkClientImpl() {
        webClient = WebClient.create(BASEURL);
    }

    public ScrapperLinkClientImpl(String baseUrl) {
        webClient = WebClient.create(baseUrl);
    }

    @Override
    public LinkResponse removeLinkByChatId(Long tgChatId, RemoveLinkRequest removeLinkRequest) {
        return webClient.method(HttpMethod.DELETE)
            .uri(BASE_ENDPOINT)
            .header(TGCHAT_ID_HEADER, tgChatId.toString())
            .bodyValue(removeLinkRequest)
            .retrieve()
            .bodyToMono(LinkResponse.class)
            .block();
    }

    @Override
    public ListLinksResponse getLinksByChatId(Long tgChatId) {
        return webClient.get()
            .uri(BASE_ENDPOINT)
            .header(TGCHAT_ID_HEADER, tgChatId.toString())
            .retrieve()
            .bodyToMono(ListLinksResponse.class)
            .block();
    }

    @Override
    public LinkResponse postLinkByChatId(Long tgChatId, AddLinkRequest addLinkRequest) {
        log.info("Post link: {} to tgChat: {}", addLinkRequest, tgChatId);
        return webClient.post()
            .uri(BASE_ENDPOINT)
            .header(TGCHAT_ID_HEADER, tgChatId.toString())
            .bodyValue(addLinkRequest)
            .retrieve()
            .bodyToMono(LinkResponse.class)
            .block();
    }
}
