package ru.practicum.item.service.item;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.exception.http.NotFoundUserItemException;
import ru.practicum.item.dto.reqeust.list.ItemListRequest;
import ru.practicum.item.dto.reqeust.update.ItemUpdateDto;
import ru.practicum.item.model.Item;
import ru.practicum.item.repository.ItemRepository;
import ru.practicum.item.service.item.querybuilder.QueryBuilder;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ItemServiceImpl implements ItemService {
    private final ItemRepository repository;
    private final QueryBuilder queryBuilder;

    @Override
    public List<Item> findItems(ItemListRequest request) {
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
    public Item createItem(long userId, Item creation) {
        creation.setUserId(userId);
        return repository.save(creation);
    }

    @Override
    public Item updateItem(long userId, ItemUpdateDto update) {
        // Get updating Item
        Item item = repository
                .findOneByUserIdAndId(userId, update.getId())
                .orElseThrow(
                        () -> new NotFoundUserItemException(
                                userId,
                                update.getId()
                        )
                );

        // Update "Unread" attribute
        if (update.getUnread() != null) {
            item.setUnread(update.getUnread());
        }

        // Define passing Tags
        boolean replaceTags = update.getReplaceTags() != null ? update.getReplaceTags() : false;
        String[] updateTags = update.getTags() != null ? update.getTags() : new String[0];

        // Define existed Tags
        String[] itemTags = item.getTags();
        Set<String> tags = replaceTags
                ? new HashSet<>()
                : new HashSet<>(itemTags == null ? List.of() : Arrays.asList(itemTags));

        // Define result Tags
        tags.addAll(Arrays.asList(updateTags));
        item.setTags(tags.toArray(new String[0]));

        return repository.saveAndFlush(item);
    }

    @Override
    @Transactional
    public void deleteItem(long userId, long itemId) {
        repository.deleteById(
                repository.findOneByUserIdAndId( // Check existence
                                userId,
                                itemId
                        )
                        .orElseThrow(
                                () -> new NotFoundUserItemException(
                                        userId,
                                        itemId
                                )
                        )
                        .getId()
        );
    }
}
