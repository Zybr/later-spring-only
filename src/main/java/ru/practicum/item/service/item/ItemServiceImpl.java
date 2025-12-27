package ru.practicum.item.service.item;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.exception.http.NotFoundException;
import ru.practicum.item.dto.reqeust.list.ItemListRequest;
import ru.practicum.item.model.Item;
import ru.practicum.item.repository.ItemRepository;

import ru.practicum.item.service.item.querybuilder.QueryBuilder;

import java.util.Arrays;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ItemServiceImpl implements ItemService {
    private final ItemRepository repository;
    private final QueryBuilder queryBuilder;

    @Override
    public List<Item> getItems(ItemListRequest request) {
        return queryBuilder
                .setUserId(request.getUserId())
                .setState(request.getState())
                .setContentType(request.getContentType())
                .setTags(Arrays.asList(request.getTags()))
                .setLimit(request.getLimit())
                .setSort(request.getSort())
                .fetch();
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
