package com.br.chat.core.exception;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import static org.assertj.core.api.Assertions.*;

class UserAlreadyExistExceptionTest {

    @Test
    void shouldReturnUnprocessableEntityProblemDetailWithCorrectTitleAndDetail() {
        var exception = new UserAlreadyExistException();
        var detail = exception.toProblemDetail();

        assertThat(detail.getStatus()).isEqualTo(HttpStatus.UNPROCESSABLE_ENTITY.value());
        assertThat(detail.getTitle()).isEqualTo("User Already Exists Exception.");
        assertThat(detail.getDetail()).isEqualTo("This email is already registered.");
    }
}
