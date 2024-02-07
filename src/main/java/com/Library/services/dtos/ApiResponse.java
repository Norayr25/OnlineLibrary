package com.Library.services.dtos;

import com.Library.services.entities.Book;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ApiResponse {
    @JsonProperty("data")
    private Book[] data;
}