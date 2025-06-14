package com.br.chat.core.exception;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import static org.assertj.core.api.Assertions.*;

class ChatNotFoundExceptionTest {

    @Test
    void shouldReturnNotFoundProblemDetailWithCorrectTitleAndDetail() {
        var exception = new ChatNotFoundException();
        var detail = exception.toProblemDetail();

        assertThat(detail.getStatus()).isEqualTo(HttpStatus.NOT_FOUND.value());
        assertThat(detail.getTitle()).isEqualTo("NotFound exception");
        assertThat(detail.getDetail()).isEqualTo("Chat not found.");
    }
}
