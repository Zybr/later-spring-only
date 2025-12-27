package ru.practicum.item.dto.reqeust.list;

public enum Sort {
    NEWEST("newest"),
    OLDEST("oldest"),
    TITLE("title");

    public final String label;

    Sort(String label) {
        this.label = label;
    }
}
