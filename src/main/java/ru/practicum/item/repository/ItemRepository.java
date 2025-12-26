package ru.practicum.item.repository;

import ru.practicum.exception.http.NotFoundException;
import ru.practicum.item.model.Item;

import java.util.List;

public interface ItemRepository {
    List<Item> findAllOfUser(long userId);

    Item add(Item item);

    void deleteOfUser(long userId, long itemId) throws NotFoundException;
}
