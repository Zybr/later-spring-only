package ru.practicum.exception.http;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class NotFoundUserItemException extends NotFoundException {
    public NotFoundUserItemException(Long userId, Long itemId) {
        super(String.format(
                "There is not Item \"%d\" for User \"%d\"",
                itemId,
                userId
        ));
    }
}
