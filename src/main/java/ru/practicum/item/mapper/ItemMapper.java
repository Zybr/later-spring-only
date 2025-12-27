package ru.practicum.item.mapper;

import org.mapstruct.Mapper;
import ru.practicum.item.dto.out.ItemOutDto;
import ru.practicum.item.model.Item;

@Mapper(componentModel = "spring")
public interface ItemMapper {
    ItemOutDto toOutDto(Item item);
}
