package ru.practicum.item.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.item.model.Item;

import java.util.List;

public interface ItemRepository extends JpaRepository<Item, Long> {
    List<Item> findByUserId(long userId);
}
