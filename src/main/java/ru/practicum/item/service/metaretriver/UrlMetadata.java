package ru.practicum.item.service.metaretriver;

import java.time.LocalDate;

public interface UrlMetadata {
    String getNormalUrl();

    String getResolvedUrl();

    String getMimeType();

    String getTitle();

    Boolean hasImage();

    Boolean hasVideo();

    LocalDate getDateResolved();
}
