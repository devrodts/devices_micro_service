package com.devrodts.devices.infrastructure.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class DuplicateDeviceException extends RuntimeException {
    public DuplicateDeviceException(String name) {
        super("A device with the name '" + name + "' already exists");
    }
}
