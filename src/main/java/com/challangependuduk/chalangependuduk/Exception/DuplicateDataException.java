package com.challangependuduk.chalangependuduk.Exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class DuplicateDataException extends RuntimeException{

    public DuplicateDataException(String message) {
        super(message);
    }
}
