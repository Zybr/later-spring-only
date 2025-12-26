package ru.practicum.item.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.item.model.Item;
import ru.practicum.item.service.ItemService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/items")
public class ItemController {
    private final ItemService service;

    @GetMapping
    public List<Item> get(
            @RequestHeader("X-Later-User-Id") Long userId
    ) {
        return service.getItems(userId);
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
