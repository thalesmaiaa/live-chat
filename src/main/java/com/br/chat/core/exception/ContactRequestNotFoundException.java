package com.br.chat.core.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;

public class ContactRequestNotFoundException extends DomainException {

    @Override
    public ProblemDetail toProblemDetail() {
        var pb = ProblemDetail.forStatus(HttpStatus.NOT_FOUND);
        pb.setTitle("Contact request not found.");

        return pb;
    }
}
