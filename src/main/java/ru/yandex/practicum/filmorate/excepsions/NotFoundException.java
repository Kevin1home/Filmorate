package ru.yandex.practicum.filmorate.excepsions;

public class NotFoundException extends RuntimeException {
    public NotFoundException(String message){
        super(message);
    }
}
