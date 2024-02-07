package com.Library.services.dtos;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class CartItemDTO {
    private Long userId;
    private Long itemId;
    private Integer count;
}