package com.br.chat.core.exception;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import static org.assertj.core.api.Assertions.*;

class ContactRequestNotFoundExceptionTest {

    @Test
    void shouldReturnNotFoundProblemDetailWithCorrectTitleAndNullDetail() {
        var exception = new ContactRequestNotFoundException();
        var detail = exception.toProblemDetail();

        assertThat(detail.getStatus()).isEqualTo(HttpStatus.NOT_FOUND.value());
        assertThat(detail.getTitle()).isEqualTo("Contact request not found.");
        assertThat(detail.getDetail()).isNull();
    }
}
