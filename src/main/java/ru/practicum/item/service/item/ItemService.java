package ru.practicum.item.service.item;

import ru.practicum.item.dto.reqeust.list.ItemListRequest;
import ru.practicum.item.dto.reqeust.update.ItemUpdateDto;
import ru.practicum.item.model.Item;

import java.util.List;

public interface ItemService {
    List<Item> findItems(ItemListRequest request);

    Item createItem(long userId, Item creation);

    Item updateItem(long userId, ItemUpdateDto update);

    void deleteItem(long userId, long itemId);
}
