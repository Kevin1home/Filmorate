package ru.yandex.practicum.filmorate.excepsions;

public class ValidationException extends Exception {
    private final String inputValue;

    public ValidationException(final String message, final String inputValue) {
        super(message);
        this.inputValue = inputValue;
    }

    public String getDetailMessage() {
        return getMessage() + " Вы ввели: " + inputValue;
    }

}
