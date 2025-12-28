package ru.practicum.item.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.item.model.Item;

import java.util.List;
import java.util.Optional;

public interface ItemRepository extends JpaRepository<Item, Long> {
    List<Item> findAllByUserId(long userId);

    Optional<Item> findOneByUserIdAndId(long userId, long itemId);
}
