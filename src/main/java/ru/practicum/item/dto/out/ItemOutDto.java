package ru.practicum.item.dto.out;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;

@Getter
@RequiredArgsConstructor
public class ItemOutDto {
    private final Long id;
    private final Long userId;
    private final String url;
    private final String resolvedUrl;
    private final String mimeType;
    private final String title;
    private final Boolean hasImage;
    private final Boolean hasVideo;
    private final LocalDate dateResolved;
    private final Boolean unread;
}
