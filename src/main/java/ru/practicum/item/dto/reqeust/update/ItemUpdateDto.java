package ru.practicum.item.dto.reqeust.update;

import lombok.Value;

import java.time.LocalDate;

@Value
public class ItemUpdateDto {
    Long id;
    Boolean unread;
    Boolean replaceTags;
    String[] tags;
}
