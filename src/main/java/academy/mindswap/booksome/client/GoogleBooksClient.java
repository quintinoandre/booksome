package academy.mindswap.booksome.client;

import academy.mindswap.booksome.dto.book.BookClientDto;
import academy.mindswap.booksome.exception.client.Client4xxErrorException;
import academy.mindswap.booksome.exception.client.Client5xxErrorException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.LinkedList;
import java.util.List;

import static academy.mindswap.booksome.client.GoogleBooksClientConstant.*;

@Service
public class GoogleBooksClient {
    private static final Logger LOGGER = LoggerFactory.getLogger(GoogleBooksClient.class);

    @Value("${google_books_client.api_key}")
    private String apiKey;

    private final WebClient webClient;

    @Autowired
    public GoogleBooksClient(WebClient.Builder builder) {
        this.webClient = builder
                .baseUrl(BASE_URL)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .build();
    }

    private Mono<? extends Throwable> handle4xxError(ClientResponse clientResponse) {
        Mono<String> errorMessage = clientResponse.bodyToMono(String.class);

        return errorMessage.flatMap(message -> {
            LOGGER.error("{}: {}", clientResponse.rawStatusCode(), message);

            throw new Client4xxErrorException(message);
        });
    }

    private Mono<? extends Throwable> handle5xxError(ClientResponse clientResponse) {
        Mono<String> errorMessage = clientResponse.bodyToMono(String.class);

        return errorMessage.flatMap(message -> {
            LOGGER.error("{}: {}", clientResponse.rawStatusCode(), message);

            throw new Client5xxErrorException(message);
        });
    }

    private String getTextInfo(JsonNode items, Integer position, String path1, String path2) {
        return items.get(position).path(path1).path(path2).asText();
    }

    private JsonNode getJsonNodeInfo(JsonNode items, Integer position, String path1, String path2) {
        return items.get(position).path(path1).path(path2);
    }

    private String buildUri(String title, String authors, String subject, String isbn) {
        final String URL_PREFIX = "/".concat(RESOURCE_VOLUMES).concat("?").concat(Q_PARAMETER).concat("=")
                .concat("");

        if (isbn != null) {
            return
                    URL_PREFIX.concat("+").concat(ISBN_SPECIAL_KEYWORD).concat(":").concat(isbn)
                            .concat("&key=").concat(apiKey);
        }

        return URL_PREFIX
                .concat(title != null ? "+" : "").concat(title != null ? INTITLE_SPECIAL_KEYWORD : "")
                .concat(title != null ? ":" : "").concat(title != null ? title : "")
                .concat(authors != null ? "+" : "").concat(authors != null ? INAUTHOR_SPECIAL_KEYWORD : "")
                .concat(authors != null ? ":" : "").concat(authors != null ? authors : "")
                .concat(subject != null ? "+" : "").concat(subject != null ? SUBJECT_SPECIAL_KEYWORD : "")
                .concat(subject != null ? ":" : "").concat(subject != null ? subject : "").concat("&key=")
                .concat(apiKey);
    }

    public List<BookClientDto> findAll(String title, String authors, String subject, String isbn) {
        String response = webClient
                .get()
                .uri(buildUri(title, authors, subject, isbn))
                .retrieve()
                .onStatus(HttpStatus::is4xxClientError, this::handle4xxError)
                .onStatus(HttpStatus::is5xxServerError, this::handle5xxError)
                .bodyToMono(String.class)
                .block();

        JsonNode root;

        try {
            root = new ObjectMapper().readTree(response);
        } catch (JsonProcessingException exception) {
            throw new RuntimeException(exception);
        }

        JsonNode items = root.path(ITEMS);

        List<BookClientDto> bookDtoList = new LinkedList<>();

        for (int i = 0; i < items.size(); i++) {
            String titleInfo = getTextInfo(items, i, VOLUME_INFO, TITLE);
            JsonNode authorsInfo = getJsonNodeInfo(items, i, VOLUME_INFO, AUTHORS);
            String publisher = getTextInfo(items, i, VOLUME_INFO, PUBLISHER);
            String publishedDate = getTextInfo(items, i, VOLUME_INFO, PUBLISHED_DATE);
            String description = getTextInfo(items, i, VOLUME_INFO, DESCRIPTION);
            JsonNode category = getJsonNodeInfo(items, i, VOLUME_INFO, CATEGORIES);

            String isnb = null;

            if (getJsonNodeInfo(items, i, VOLUME_INFO, INDUSTRY_IDENTIFIERS).size() == 2) {
                if (getJsonNodeInfo(items, i, VOLUME_INFO, INDUSTRY_IDENTIFIERS).get(0).path(TYPE)
                        .asText().equals(ISBN_13)) {
                    isnb = getJsonNodeInfo(items, i, VOLUME_INFO, INDUSTRY_IDENTIFIERS).get(0).path(IDENTIFIER)
                            .asText();

                } else if (getJsonNodeInfo(items, i, VOLUME_INFO, INDUSTRY_IDENTIFIERS).get(0).path(TYPE)
                        .asText().equals(ISBN_10)) {
                    isnb = getJsonNodeInfo(items, i, VOLUME_INFO, INDUSTRY_IDENTIFIERS).get(1)
                            .path(IDENTIFIER).asText();
                }
            } else if (getJsonNodeInfo(items, i, VOLUME_INFO, INDUSTRY_IDENTIFIERS).size() == 1) {
                if (getJsonNodeInfo(items, i, VOLUME_INFO, INDUSTRY_IDENTIFIERS).get(0).path(TYPE).asText()
                        .equals(ISBN_13) ||
                        getJsonNodeInfo(items, i, VOLUME_INFO, INDUSTRY_IDENTIFIERS).get(0).path(TYPE).asText()
                                .equals(ISBN_10)) {
                    isnb = getJsonNodeInfo(items, i, VOLUME_INFO, INDUSTRY_IDENTIFIERS).get(0).path(IDENTIFIER)
                            .asText();
                }
            }

            if (isnb == null) {
                continue;
            }

            List<String> authorsList = new LinkedList<>();
            authorsInfo.forEach(authorInfo -> authorsList.add(authorInfo.asText()));

            List<String> categoryList = new LinkedList<>();
            category.forEach(categoryInfo -> categoryList.add(categoryInfo.asText()));

            bookDtoList.add(BookClientDto.builder()
                    .title(titleInfo)
                    .authors(authorsList)
                    .publisher(publisher)
                    .publishedDate(publishedDate)
                    .description(description)
                    .isbn(isnb)
                    .category(categoryList)
                    .build());
        }

        return bookDtoList;
    }
}
