package com.Library.services.dtos;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ResponseDTO<T> {
    private int status;
    private String message;
    private T data;
    public ResponseDTO() {
    }
    public ResponseDTO(int status, String message, T data) {
        this.status = status;
        this.message = message;
        this.data = data;
    }
}