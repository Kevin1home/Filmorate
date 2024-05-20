package ru.yandex.practicum.filmorate.service;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.excepsions.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.List;

@Service
@Slf4j
public class FilmService {

    @Getter
    FilmStorage filmStorage;
    @Getter
    UserStorage userStorage;

    @Autowired
    public FilmService(FilmStorage filmStorage, UserStorage userStorage) {
        this.filmStorage = filmStorage;
        this.userStorage = userStorage;
    }

    public void addLike(Integer filmId, Integer userId) {
        if (filmId == null || filmId <= 0) {
            throw new NotFoundException("filmId");
        }
        if (userId == null || userId <= 0) {
            throw new NotFoundException("userId");
        }
        if (!filmStorage.getAllFilms().containsKey(filmId)) {
            throw new NotFoundException("filmId");
        }
        if (!userStorage.getAllUsers().containsKey(userId)) {
            throw new NotFoundException("userId");
        }
        filmStorage.getAllFilms().get(filmId).getLikes().add(userId);
        log.info("Пользователь с ID {} поставил лайк фильму с ID {}", userId, filmId);
    }

    public void deleteLike(Integer filmId, Integer userId) {
        if (filmId == null || filmId <= 0) {
            throw new NotFoundException("filmId");
        }
        if (userId == null || userId <= 0) {
            throw new NotFoundException("userId");
        }
        if (!filmStorage.getAllFilms().containsKey(filmId)) {
            throw new NotFoundException("filmId");
        }
        if (!userStorage.getAllUsers().containsKey(userId)) {
            throw new NotFoundException("userId");
        }
        filmStorage.getAllFilms().get(filmId).getLikes().remove(userId);
        log.info("Пользователь с ID {} убрал лайк фильму с ID {}", userId, filmId);
    }

    public List<Film> findMostPopularFilms(Integer count) {
        if (count == null || count <= 0) {
            throw new NotFoundException("count");
        }
        List<Film> mostPopularFilms = filmStorage.getAllFilms().values().stream()
                .sorted((f1, f2) -> f2.getLikes().size() - f1.getLikes().size())
                .limit(count)
                .toList();

        log.info("Лучшие фильмы(id): {}", mostPopularFilms);
        return mostPopularFilms;
    }

}
