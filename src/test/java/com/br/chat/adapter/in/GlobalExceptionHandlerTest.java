package com.br.chat.adapter.in;

import com.br.chat.core.exception.DomainException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GlobalExceptionHandlerTest {

    @InjectMocks
    private GlobalExceptionHandler handler;

    @Test
    void shouldHandleDomainExceptionReturnProblemDetail() {
        var domainException = mock(DomainException.class);
        var problemDetail = ProblemDetail.forStatus(HttpStatus.BAD_REQUEST);
        when(domainException.toProblemDetail()).thenReturn(problemDetail);
        var result = handler.handleDomainException(domainException);
        assertThat(result).isEqualTo(problemDetail);
    }

    @Test
    void shouldHandleMethodArgumentNotValidExceptionReturnProblemDetailWithInvalidParams() {
        var bindingResult = mock(BindingResult.class);
        var fieldError = new FieldError("object", "field", "must not be blank");
        when(bindingResult.getFieldErrors()).thenReturn(List.of(fieldError));
        var exception = new MethodArgumentNotValidException(null, bindingResult);

        var result = handler.handleMethodArgumentNotValidException(exception);

        assertThat(result.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
        assertThat(result.getTitle()).isEqualTo("Your request parameters didn't validate.");
        assertThat(result.getProperties()).containsKey("invalid-params");
        var invalidParams = (List<?>) result.getProperties().get("invalid-params");
        assertThat(invalidParams).hasSize(1);
    }
}
