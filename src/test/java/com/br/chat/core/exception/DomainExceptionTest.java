package com.br.chat.core.exception;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import static org.assertj.core.api.Assertions.*;

class DomainExceptionTest {

    @Test
    void shouldReturnInternalServerErrorProblemDetailWithCorrectTitle() {
        var exception = new DomainException();
        var detail = exception.toProblemDetail();

        assertThat(detail.getStatus()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR.value());
        assertThat(detail.getTitle()).isEqualTo("Live-chat internal server error");
    }
}
