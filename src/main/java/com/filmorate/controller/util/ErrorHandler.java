package com.filmorate.controller.util;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import com.filmorate.excepsion.*;
import com.filmorate.excepsion.film.FilmAlreadyExistException;
import com.filmorate.excepsion.film.FilmNotFoundException;
import com.filmorate.excepsion.filmLike.FilmLikeNotFoundException;
import com.filmorate.excepsion.friendship.FriendshipNotFoundException;
import com.filmorate.excepsion.gender.GenreAlreadyExistException;
import com.filmorate.excepsion.gender.GenreNotFoundException;
import com.filmorate.excepsion.rating.RatingNotFoundException;
import com.filmorate.excepsion.user.UserAlreadyExistException;
import com.filmorate.excepsion.user.UserNotFoundException;

/**
 * Global error handler for REST controllers.
 * Intercepts application-specific exceptions and transforms them into
 * consistent error responses with appropriate HTTP status codes.
 */
@RestControllerAdvice
public class ErrorHandler {

    /**
     * Handles generic "not found" exceptions.
     */
    @ExceptionHandler({
            NotFoundException.class,
            UserNotFoundException.class,
            FilmNotFoundException.class,
            FriendshipNotFoundException.class,
            GenreNotFoundException.class,
            RatingNotFoundException.class,
            FilmLikeNotFoundException.class
    })
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleNotFoundExceptions(final RuntimeException exception) {
        return buildErrorResponse(exception);
    }

    /**
     * Handles validation-related exceptions.
     */
    @ExceptionHandler({ValidationException.class, IncorrectParameterException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleValidationExceptions(final RuntimeException exception) {
        if (exception instanceof IncorrectParameterException paramEx) {
            return new ErrorResponse("Validation error",
                    String.format("Please check field \"%s\"", paramEx.getParameter()));
        }
        return buildErrorResponse(exception);
    }

    /**
     * Handles "already exists" conflicts.
     */
    @ExceptionHandler({UserAlreadyExistException.class, FilmAlreadyExistException.class, GenreAlreadyExistException.class})
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorResponse handleConflictExceptions(final RuntimeException exception) {
        return buildErrorResponse(exception);
    }

    /**
     * Builds a standard error response from an exception.
     */
    private ErrorResponse buildErrorResponse(Exception exception) {
        return new ErrorResponse(exception.getClass().getSimpleName(), exception.getMessage());
    }

    /**
     * Standardized error response object.
     */
    @Getter
    public static class ErrorResponse {
        private final String error;       // Error type (short name)
        private final String description; // Detailed error message

        public ErrorResponse(String error) {
            this(error, null);
        }

        public ErrorResponse(String error, String description) {
            this.error = error;
            this.description = description;
        }
    }
}
