package ru.practicum.item.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.item.dto.out.ItemOutDto;
import ru.practicum.item.dto.reqeust.list.ContentType;
import ru.practicum.item.dto.reqeust.list.ItemListRequest;
import ru.practicum.item.dto.reqeust.list.Sort;
import ru.practicum.item.dto.reqeust.list.State;
import ru.practicum.item.mapper.ItemMapper;
import ru.practicum.item.model.Item;
import ru.practicum.item.service.item.ItemService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/items")
public class ItemController {
    private final ItemService service;
    private final ItemMapper mapper;

    @GetMapping
    public List<ItemOutDto> getList(
            @RequestHeader("X-Later-User-Id") Long userId,
            @RequestParam(name = "state", required = false, defaultValue = "ALL") State state,
            @RequestParam(name = "contentType", required = false, defaultValue = "ALL") ContentType contentType,
            @RequestParam(name = "tags", required = false, defaultValue = "") String[] tags,
            @RequestParam(name = "sort", required = false, defaultValue = "NEWEST") Sort sort,
            @RequestParam(name = "limit", required = false, defaultValue = "10") Integer limit
    ) {
        return service
                .getItems(new ItemListRequest(
                        userId, state, contentType, tags, sort, limit
                ))
                .stream()
                .map(mapper::toOutDto)
                .toList();
    }

    @PostMapping
    @ResponseStatus(org.springframework.http.HttpStatus.CREATED)
    public Item addNewItem(
            @RequestHeader("X-Later-User-Id") Long userId,
            @RequestBody Item item
    ) {
        return service.addNewItem(userId, item);
    }

    @DeleteMapping("/{itemId}")
    public void deleteItem(
            @RequestHeader("X-Later-User-Id") Long userId,
            @PathVariable(name = "itemId") long itemId
    ) {
        service.deleteItem(userId, itemId);
    }
}
