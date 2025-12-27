package ru.practicum.item.service.item;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.exception.http.NotFoundException;
import ru.practicum.item.model.Item;
import ru.practicum.item.repository.ItemRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ItemServiceImpl implements ItemService {
    private final ItemRepository repository;

    @Override
    public List<Item> getItems(long userId) {
        return repository.findByUserId(userId);
    }

    @Override
    @Transactional
    public Item addNewItem(long userId, Item item) {
        item.setUserId(userId);
        return repository.save(item);
    }

    @Override
    @Transactional
    public void deleteItem(long userId, long itemId) {
        Item item = repository.findById(itemId)
                .orElseThrow(() -> new NotFoundException("Item not found"));

        if (item.getUserId() != userId) {
            throw new NotFoundException("Item does not belong to user");
        }

        repository.deleteById(itemId);
    }
}
