package ru.yandex.practicum.filmorate.controller;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResponseTransfer {

    String text;

    public ResponseTransfer(String text) {
        this.text = text;
    }

}
