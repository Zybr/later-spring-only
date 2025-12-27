package ru.practicum.item.dto.reqeust.list;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class ItemListRequest {
    private final Long userId;
    private final State state;
    private final ContentType contentType;
    private final String[] tags;
    private final Sort sort;
    private final Integer limit;
}
