package ru.practicum.item.dto.reqeust.list;

public enum State {
    ALL("all"),
    READ("read"),
    UNREAD("unread");

    public final String label;

    State(String label) {
        this.label = label;
    }
}
