package com.br.chat.core.exception;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import static org.assertj.core.api.Assertions.*;

class LoginExceptionTest {

    @Test
    void shouldReturnForbiddenProblemDetailWithCorrectTitleAndDetail() {
        var exception = new LoginException();
        var detail = exception.toProblemDetail();

        assertThat(detail.getStatus()).isEqualTo(HttpStatus.FORBIDDEN.value());
        assertThat(detail.getTitle()).isEqualTo("Login exception");
        assertThat(detail.getDetail()).isEqualTo("Invalid credentials.");
    }
}
