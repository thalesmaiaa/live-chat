package com.br.chat.core.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;

public class LoginException extends DomainException {
    @Override
    public ProblemDetail toProblemDetail() {
        var pb = ProblemDetail.forStatus(HttpStatus.FORBIDDEN);
        pb.setTitle("Login exception");
        pb.setDetail("Invalid credentials.");

        return pb;
    }
}
