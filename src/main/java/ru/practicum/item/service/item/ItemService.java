package ru.practicum.item.service.item;

import ru.practicum.item.dto.reqeust.list.ItemListRequest;
import ru.practicum.item.model.Item;

import java.util.List;

public interface ItemService {
    public List<Item> getItems(ItemListRequest request);

    public Item addNewItem(long userId, Item item);

    public void deleteItem(long userId, long itemId);
}
