package com.viordache.store.exceptions;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.security.SignatureException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ProblemDetail;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AccountStatusException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;


@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ProblemDetail handleSecurityException(Exception exception) {

        // Log the exception
        // TODO implement logging
        exception.printStackTrace();

        return switch (exception) {

            // JWT exceptions
            case SignatureException e -> createProblemDetail(
                    403,
                    e.getMessage(),
                    "The JWT signature is invalid"
            );

            case ExpiredJwtException e -> createProblemDetail(
                    403,
                    e.getMessage(),
                    "The JWT token has expired"
            );

            case MalformedJwtException e -> createProblemDetail(
                    403,
                    e.getMessage(),
                    "The JWT token has been malformed"
            );

            // Spring Security exceptions
            case BadCredentialsException e -> createProblemDetail(
                    401,
                    e.getMessage(),
                    "The username or password is incorrect"
            );

            case AccountStatusException e -> createProblemDetail(
                    403,
                    e.getMessage(),
                    "The account is locked"
            );

            case AccessDeniedException e -> createProblemDetail(
                    403,
                    e.getMessage(),
                    "You are not authorized to access this resource");

            // other
            case DuplicateKeyException e -> createProblemDetail(
                    409,
                    "The resource has already been created.",
                    "The resource has already been created.");

            case ItemNotFoundException e -> createProblemDetail(
                    404,
                    e.getMessage(),
                    "The resource does not exist"
            );

            case IllegalArgumentException e -> createProblemDetail(
                    400,
                    e.getMessage(),
                    "The argument passed to the request is invalid"
            );

            default -> createProblemDetail(
                    500,
                    exception.getMessage(),
                    "Unknown internal server error.");
        };
    }

    public ProblemDetail createProblemDetail(Integer httpStatus, String message, String description) {

        ProblemDetail pd = ProblemDetail.forStatusAndDetail(HttpStatusCode.valueOf(httpStatus), message);
        pd.setProperty("description", description);
        return pd;
    }
}
