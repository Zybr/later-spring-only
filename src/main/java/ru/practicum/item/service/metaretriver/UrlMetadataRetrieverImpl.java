package ru.practicum.item.service.metaretriver;

import lombok.*;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.MimeType;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class UrlMetadataRetrieverImpl implements UrlMetadataRetriever {
    private final HttpClient client;

    @Override
    public UrlMetadata retrieve(String url) {
        final URI uri;

        try {
            uri = new URI(url);
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }

        HttpResponse<Void> response = connect(
                uri,
                "HEAD",
                HttpResponse.BodyHandlers.discarding()
        );

        String contentType = response
                .headers()
                .firstValue("Content-Type")
                .orElse(null);

        final MediaType mediaType = MediaType.parseMediaType(contentType);
        final UrlMetadataImp meta;

        if (mediaType.isCompatibleWith(MimeType.valueOf("text/*"))) {
            meta = handleText(uri, HttpResponse.BodyHandlers.ofString());
        } else if (mediaType.isCompatibleWith(MimeType.valueOf("image/*"))) {
            meta = handleImage(uri, HttpResponse.BodyHandlers.ofString());
        } else if (mediaType.isCompatibleWith(MimeType.valueOf("video/*"))) {
            meta = handleVideo(uri, HttpResponse.BodyHandlers.ofString());
        } else {
            throw new RuntimeException("Unsupported media type: " + mediaType);
        }

        return meta
                .toBuilder()
                .normalUrl(uri.toString())
                .resolvedUrl(response.uri().toString())
                .mimeType(mediaType.toString())
                .dateResolved(LocalDate.now())
                .build();
    }

    private <T> HttpResponse<T> connect(
            URI url,
            String method,
            HttpResponse.BodyHandler<T> responseBodyHandler
    ) {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .method(method, HttpRequest.BodyPublishers.noBody())
                .build();

        final HttpResponse<T> response;

        try {
            response = client.send(request, responseBodyHandler);
        } catch (IOException e) {
            throw new RuntimeException("Cannot retrieve data from the URL: " + url, e);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException("Cannot get the metadata for url: " + url
                    + " because the thread was interrupted.", e);
        }

        HttpStatus status = HttpStatus.resolve(response.statusCode());
        if (status == null) {
            throw new RuntimeException("The server returned an unknown status code: " + response.statusCode());
        }

        if (status.equals(HttpStatus.UNAUTHORIZED) || status.equals(HttpStatus.FORBIDDEN)) {
            throw new RuntimeException("There is no access to the resource at the specified URL: " + url);
        }
        if (status.isError()) {
            throw new RuntimeException("Cannot get the data on the item because the server returned an error."
                    + "Response status: " + status);
        }

        return response;
    }

    private UrlMetadataImp handleText(
            URI uri,
            HttpResponse.BodyHandler<String> responseBodyHandler
    ) {
        HttpResponse<String> response = connect(
                uri,
                "GET",
                responseBodyHandler
        );
        Document document = Jsoup.parse(response.body());
        Elements imgElements = document.select("img");
        Elements videoElements = document.select("video");

        return UrlMetadataImp
                .builder()
                .title(document.title())
                .hasImage(!imgElements.isEmpty())
                .hasVideo(!videoElements.isEmpty())
                .build();
    }

    private UrlMetadataImp handleVideo(
            URI uri,
            HttpResponse.BodyHandler<String> responseBodyHandler
    ) {
        return UrlMetadataImp
                .builder()
                .title(getFileName(uri))
                .hasVideo(true)
                .hasImage(false)
                .build();
    }

    private UrlMetadataImp handleImage(
            URI uri,
            HttpResponse.BodyHandler<String> responseBodyHandler
    ) {
        return UrlMetadataImp
                .builder()
                .title(getFileName(uri))
                .hasImage(true)
                .hasVideo(false)
                .build();
    }

    private String getFileName(URI uri) {
        String path = uri.getPath();
        if (path == null || path.isEmpty() || path.equals("/")) {
            return uri.getHost();
        }
        return path.substring(path.lastIndexOf('/') + 1);
    }

    @Value
    @Builder(toBuilder = true)
    static class UrlMetadataImp implements UrlMetadata {
        String normalUrl;
        String resolvedUrl;
        String mimeType;
        String title;
        @Getter(AccessLevel.NONE)
        Boolean hasImage;
        @Getter(AccessLevel.NONE)
        Boolean hasVideo;
        LocalDate dateResolved;

        @Override
        public Boolean hasImage() {
            return hasImage;
        }

        @Override
        public Boolean hasVideo() {
            return hasVideo;
        }
    }
}
