package com.Library.services.dtos;

import lombok.Getter;
import lombok.Setter;

/**
 * A generic response data transfer object (DTO) representing an HTTP response.
 *
 * @param <T> The type of data contained in the response.
 */
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