package ru.practicum.item.service;

import ru.practicum.item.model.Item;

import java.util.List;

public interface ItemService {
    public List<Item> getItems(long userId);

    public Item addNewItem(long userId, Item item);

    public void deleteItem(long userId, long itemId);
}
