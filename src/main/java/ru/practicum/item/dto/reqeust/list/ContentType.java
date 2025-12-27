package ru.practicum.item.dto.reqeust.list;

public enum ContentType {
    ALL("all"),
    VIDEO("video"),
    IMAGE("image"),
    ARTICLE("article");

    private final String label;

    ContentType(String label) {
        this.label = label;
    }
}
