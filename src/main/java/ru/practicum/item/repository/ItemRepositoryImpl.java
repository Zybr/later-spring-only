package ru.practicum.item.repository;

import org.springframework.stereotype.Repository;
import ru.practicum.exception.http.NotFoundException;
import ru.practicum.item.model.Item;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class ItemRepositoryImpl implements ItemRepository {
    private final Map<Long, Item> items = new HashMap<>();

    @Override
    public List<Item> findAllOfUser(long userId) {
        return items
                .values()
                .stream()
                .filter(item -> item.getUserId() == userId)
                .toList();
    }

    @Override
    public Item add(Item item) {
        item.setId(getNextId());
        return items.put(item.getId(), item);
    }

    @Override
    public void deleteOfUser(long userId, long itemId) throws RuntimeException {
        Item item = items.getOrDefault(itemId, null);

        if (item == null) {
            throw new NotFoundException("Item not found");
        }

        if (item.getUserId() != userId) {
            throw new NotFoundException("Item not belongs to user");
        }

        items.remove(itemId);
    }

    private Long getNextId() {
        return items
                .keySet()
                .stream()
                .max(Long::compareTo)
                .orElse(0L) + 1;
    }
}
