package ru.practicum.item.service.metaretriver;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpHeaders;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UrlMetadataRetrieverImplTest {

    @Mock
    private HttpClient httpClient;

    @Mock
    private HttpResponse<Object> httpResponse;

    @Mock
    private HttpResponse<String> stringHttpResponse;

    private UrlMetadataRetrieverImpl retriever;

    @BeforeEach
    void setUp() {
        retriever = new UrlMetadataRetrieverImpl(httpClient);
    }

    @Test
    void retrieve_shouldReturnMetadataForHtml() throws IOException, InterruptedException {
        String url = "http://example.com/index.html";
        URI uri = URI.create(url);

        // Mock HEAD response
        HttpHeaders headers = HttpHeaders.of(
                java.util.Map.of("Content-Type", java.util.List.of("text/html")),
                (s1, s2) -> true
        );
        when(httpResponse.headers()).thenReturn(headers);
        when(httpResponse.statusCode()).thenReturn(200);
        when(httpResponse.uri()).thenReturn(uri);

        // Mock GET response for handleText
        when(stringHttpResponse.statusCode()).thenReturn(200);
        when(stringHttpResponse.body()).thenReturn("<html><head><title>Example Title</title></head><body><img src='img.png'/></body></html>");

        // Chain stubbing for first call (HEAD) and second call (GET)
        when(httpClient.send(any(HttpRequest.class), any(HttpResponse.BodyHandler.class)))
                .thenReturn(httpResponse)
                .thenReturn(stringHttpResponse);

        UrlMetadata metadata = retriever.retrieve(url);

        assertNotNull(metadata);
        assertEquals("Example Title", metadata.getTitle());
        assertTrue(metadata.hasImage());
        assertFalse(metadata.hasVideo());
        assertEquals("text/html", metadata.getMimeType());
        assertEquals(url, metadata.getNormalUrl());
        assertEquals(LocalDate.now(), metadata.getDateResolved());
    }

    @Test
    void retrieve_shouldReturnMetadataForImage() throws IOException, InterruptedException {
        String url = "http://example.com/image.png";
        URI uri = URI.create(url);

        HttpHeaders headers = HttpHeaders.of(
                java.util.Map.of("Content-Type", java.util.List.of("image/png")),
                (s1, s2) -> true
        );
        when(httpResponse.headers()).thenReturn(headers);
        when(httpResponse.statusCode()).thenReturn(200);
        when(httpResponse.uri()).thenReturn(uri);
        when(httpClient.send(any(HttpRequest.class), any(HttpResponse.BodyHandler.class)))
                .thenReturn(httpResponse);

        UrlMetadata metadata = retriever.retrieve(url);

        assertNotNull(metadata);
        assertEquals("image.png", metadata.getTitle());
        assertTrue(metadata.hasImage());
        assertFalse(metadata.hasVideo());
        assertEquals("image/png", metadata.getMimeType());
    }

    @Test
    void retrieve_shouldReturnMetadataForVideo() throws IOException, InterruptedException {
        String url = "http://example.com/video.mp4";
        URI uri = URI.create(url);

        HttpHeaders headers = HttpHeaders.of(
                java.util.Map.of("Content-Type", java.util.List.of("video/mp4")),
                (s1, s2) -> true
        );
        when(httpResponse.headers()).thenReturn(headers);
        when(httpResponse.statusCode()).thenReturn(200);
        when(httpResponse.uri()).thenReturn(uri);
        when(httpClient.send(any(HttpRequest.class), any(HttpResponse.BodyHandler.class)))
                .thenReturn(httpResponse);

        UrlMetadata metadata = retriever.retrieve(url);

        assertNotNull(metadata);
        assertEquals("video.mp4", metadata.getTitle());
        assertFalse(metadata.hasImage());
        assertTrue(metadata.hasVideo());
        assertEquals("video/mp4", metadata.getMimeType());
    }

    @Test
    void retrieve_shouldThrowExceptionForUnsupportedMediaType() throws IOException, InterruptedException {
        String url = "http://example.com/data.pdf";

        HttpHeaders headers = HttpHeaders.of(
                java.util.Map.of("Content-Type", java.util.List.of("application/pdf")),
                (s1, s2) -> true
        );
        when(httpResponse.headers()).thenReturn(headers);
        when(httpResponse.statusCode()).thenReturn(200);
        when(httpClient.send(any(HttpRequest.class), any(HttpResponse.BodyHandler.class)))
                .thenReturn(httpResponse);

        RuntimeException exception = assertThrows(RuntimeException.class, () -> retriever.retrieve(url));
        assertTrue(exception.getMessage().contains("Unsupported media type"));
    }

    @Test
    void retrieve_shouldThrowExceptionFor404Error() throws IOException, InterruptedException {
        String url = "http://example.com/notfound";

        when(httpResponse.statusCode()).thenReturn(404);
        when(httpClient.send(any(HttpRequest.class), any(HttpResponse.BodyHandler.class)))
                .thenReturn(httpResponse);

        RuntimeException exception = assertThrows(RuntimeException.class, () -> retriever.retrieve(url));
        assertTrue(exception.getMessage().contains("server returned an error"));
    }

    @Test
    void retrieve_shouldThrowExceptionFor401Unauthorized() throws IOException, InterruptedException {
        String url = "http://example.com/private";

        when(httpResponse.statusCode()).thenReturn(401);
        when(httpClient.send(any(HttpRequest.class), any(HttpResponse.BodyHandler.class)))
                .thenReturn(httpResponse);

        RuntimeException exception = assertThrows(RuntimeException.class, () -> retriever.retrieve(url));
        assertTrue(exception.getMessage().contains("no access to the resource"));
    }

    @Test
    void retrieve_shouldHandleIOException() throws IOException, InterruptedException {
        String url = "http://example.com/error";

        when(httpClient.send(any(HttpRequest.class), any(HttpResponse.BodyHandler.class)))
                .thenThrow(new IOException("Network error"));

        RuntimeException exception = assertThrows(RuntimeException.class, () -> retriever.retrieve(url));
        assertTrue(exception.getMessage().contains("Cannot retrieve data from the URL"));
    }

    @Test
    void retrieve_shouldHandleInterruptedException() throws IOException, InterruptedException {
        String url = "http://example.com/interrupted";

        when(httpClient.send(any(HttpRequest.class), any(HttpResponse.BodyHandler.class)))
                .thenThrow(new InterruptedException("Interrupted"));

        RuntimeException exception = assertThrows(RuntimeException.class, () -> retriever.retrieve(url));
        assertTrue(exception.getMessage().contains("thread was interrupted"));
        assertTrue(Thread.interrupted());
    }
}
