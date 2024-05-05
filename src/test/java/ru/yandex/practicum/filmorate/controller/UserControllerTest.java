package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.excepsions.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;


class UserControllerTest {

    UserController userController;
    private User user1;
    private User user2;
    private User user3;
    private User user4;
    private User user5;

    @BeforeEach
    public void setUp() {
        userController = new UserController();
        initModel();
    }

    public void initModel() {
    user1 = new User(" ", "login", "name",LocalDate.of(2000, 12, 28));
    user2 = new User("mail@mail.com", " ", "name", LocalDate.of(2000, 12, 28));
    user3 = new User("mail@mail.com", "login", " ", LocalDate.of(2000, 12, 28));
    user4 = new User("mail@mail.com", "login", "name", LocalDate.of(2100, 12, 28));
    user5 = new User("mail@mail.com", "login", "name", LocalDate.of(2000, 12, 28));
    }

    // testing method addUser(User user)
    @Test
    void shouldThrowValidationExceptionByEmptyEmailByAddingMethod() {

        final ValidationException exception = Assertions.assertThrows(
                ValidationException.class,
                () -> userController.addUser(user1)
        );

        Assertions.assertEquals("Электронная почта не может быть пустой и должна содержать символ @.", exception.getMessage());
    }

    @Test
    void shouldThrowValidationExceptionByEmptyDescriptionByAddingMethod() {

        final ValidationException exception = Assertions.assertThrows(
                ValidationException.class,
                () -> userController.addUser(user2)
        );

        Assertions.assertEquals("Логин не может быть пустым и содержать пробелы.",
                exception.getMessage());
    }

    @Test
    void shouldWriteNameAsLoginByEmptyNameByAddingMethod() throws ValidationException {

        userController.addUser(user3);

        String usersLogin = user3.getName();
        String usersName = user3.getLogin();

        boolean isTrue = usersLogin.equals(usersName);

        Assertions.assertTrue(isTrue);
    }

    @Test
    void shouldThrowValidationExceptionByBirthdayInFutureByAddingMethod() {

        final ValidationException exception = Assertions.assertThrows(
                ValidationException.class,
                () -> userController.addUser(user4)
        );

        Assertions.assertEquals("Дата рождения не может быть в будущем.",
                exception.getMessage());
    }

    // testing method addOrUpdateUser(User user)
    @Test
    void shouldThrowValidationExceptionByEmptyEmailByAddingOrUpdatingMethod() {

        final ValidationException exception = Assertions.assertThrows(
                ValidationException.class,
                () -> userController.addOrUpdateUser(user1)
        );

        Assertions.assertEquals("Электронная почта не может быть пустой и должна содержать символ @.", exception.getMessage());
    }

    @Test
    void shouldThrowValidationExceptionByEmptyDescriptionByAddingOrUpdatingMethod() {

        final ValidationException exception = Assertions.assertThrows(
                ValidationException.class,
                () -> userController.addOrUpdateUser(user2)
        );

        Assertions.assertEquals("Логин не может быть пустым и содержать пробелы.",
                exception.getMessage());
    }

    @Test
    void shouldWriteNameAsLoginByEmptyNameByAddingOrUpdatingMethod() throws ValidationException {

        userController.addUser(user3);
        String usersLogin = user3.getName();
        String usersName = user3.getLogin();

        boolean isTrue = usersLogin.equals(usersName);

        Assertions.assertTrue(isTrue);
    }

    @Test
    void shouldThrowValidationExceptionByByBirthdayInFutureByAddingOrUpdatingMethod() {

        final ValidationException exception = Assertions.assertThrows(
                ValidationException.class,
                () -> userController.addUser(user4)
        );

        Assertions.assertEquals("Дата рождения не может быть в будущем.",
                exception.getMessage());
    }

    // testing method getAllUsers()
    @Test
    void shouldAddNewUserByAddingMethod() throws ValidationException {

        userController.addUser(user5);

        int expectedSize = 1;
        int actualSize = userController.getAllUsers().size();

        Assertions.assertEquals(expectedSize, actualSize);
    }

    @Test
    void shouldAddNewUserByAddingOrUpdatingMethod() throws ValidationException {

        userController.addOrUpdateUser(user5);

        int expectedSize = 1;
        int actualSize = userController.getAllUsers().size();

        Assertions.assertEquals(expectedSize, actualSize);
    }

    @Test
    void shouldNotAddExistingUserByAddingOrUpdatingMethod() throws ValidationException {

        userController.addOrUpdateUser(user5);
        userController.addOrUpdateUser(user5);

        int expectedSize = 1;
        int actualSize = userController.getAllUsers().size();

        Assertions.assertEquals(expectedSize, actualSize);
    }



}