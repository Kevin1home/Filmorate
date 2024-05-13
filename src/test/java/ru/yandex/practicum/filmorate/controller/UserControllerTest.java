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

    @BeforeEach
    public void setUp() {
        userController = new UserController();
        initModel();
    }

    public void initModel() {
        user1 = new User("mail@mail.com", "login login", LocalDate.of(2000, 1, 1));
        user1.setName("name");

        user2 = new User("mail@mail.com", "login", LocalDate.of(2000, 1, 1));

        user3 = new User("mail@mail.com", "login", LocalDate.of(2000, 1, 1));
        user3.setName("name");

        user4 = new User("mail@mail.com", "login2", LocalDate.of(2000, 1, 1));
        user4.setId(1);
        user4.setName("name2");
    }

    // testing method addUser(User user)
    @Test
    void shouldThrowValidationExceptionByLoginWithSpaceByAddingMethod() {

        final ValidationException exception = Assertions.assertThrows(
                ValidationException.class,
                () -> userController.addUser(user1)
        );

        Assertions.assertEquals("Валидация параметров пользователя не пройдена.", exception.getMessage());
    }

    @Test
    void shouldWriteNameAsLoginByEmptyNameByAddingMethod() throws ValidationException {

        userController.addUser(user2);

        String usersLogin = user2.getName();
        String usersName = user2.getLogin();

        boolean isTrue = usersLogin.equals(usersName);

        Assertions.assertTrue(isTrue);
    }

    @Test
    void shouldThrowValidationExceptionByAddingExistingFilmByAddingMethod() throws ValidationException {

        userController.addUser(user3);

        final ValidationException exception = Assertions.assertThrows(
                ValidationException.class,
                () -> userController.addUser(user3)
        );

        Assertions.assertEquals("Такой пользователь уже есть.", exception.getMessage());
    }

    @Test
    void shouldAddUserWithRightParameters() throws ValidationException {

        userController.addUser(user3);

        int expectedSize = 1;
        int actualSize = userController.getAllUsers().size();

        Assertions.assertEquals(expectedSize, actualSize);
    }

    // testing method updateUser(User user)

    @Test
    void shouldThrowValidationExceptionByLoginWithSpaceByUpdatingMethod() {

        final ValidationException exception = Assertions.assertThrows(
                ValidationException.class,
                () -> userController.updateUser(user1)
        );

        Assertions.assertEquals("Валидация параметров пользователя не пройдена.", exception.getMessage());
    }

    @Test
    void shouldWriteNameAsLoginByEmptyNameByUpdatingMethod() throws ValidationException {

        userController.addUser(user2);
        String usersLogin = user2.getName();
        String usersName = user2.getLogin();

        boolean isTrue = usersLogin.equals(usersName);

        Assertions.assertTrue(isTrue);
    }

    @Test
    void shouldThrowValidationExceptionByUpdatingNotExistingUserByUpdatingMethod() {

        final ValidationException exception = Assertions.assertThrows(
                ValidationException.class,
                () -> userController.updateUser(user4)
        );

        Assertions.assertEquals("Такого id нет.", exception.getMessage());
    }

    @Test
    void shouldUpdateUserWithRightParameters() throws ValidationException {

        userController.addUser(user3);
        userController.updateUser(user4);

        String expectedName = "name2";
        String actualName = userController.getAllUsers().get(0).getName();

        Assertions.assertEquals(expectedName, actualName);
    }

    // testing method getAllUsers()
    @Test
    void shouldAddNewUserByAddingMethod() throws ValidationException {

        userController.addUser(user3);

        int expectedSize = 1;
        int actualSize = userController.getAllUsers().size();

        Assertions.assertEquals(expectedSize, actualSize);
    }

    @Test
    void shouldNotAddNewFilmByUpdatingMethod() throws ValidationException {

        userController.addUser(user3);
        userController.updateUser(user4);

        int expectedSize = 1;
        int actualSize = userController.getAllUsers().size();

        Assertions.assertEquals(expectedSize, actualSize);
    }

}