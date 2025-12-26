package ru.practicum.item;

import ru.practicum.exception.http.NotFoundException;

import java.util.List;

public interface ItemRepository {
    List<Item> findAllOfUser(long userId);

    Item add(Item item);

    void deleteOfUser(long userId, long itemId) throws NotFoundException;
}
