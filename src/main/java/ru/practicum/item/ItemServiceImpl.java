package ru.practicum.item;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {
    private final ItemRepository repository;

    @Override
    public List<Item> getItems(long userId) {
        return repository.findAllOfUser(userId);
    }

    @Override
    public Item addNewItem(long userId, Item item) {
        item.setUserId(userId);
        repository.add(item);
        return item;
    }

    @Override
    public void deleteItem(long userId, long itemId) {
        repository.deleteOfUser(userId, itemId);
    }
}
