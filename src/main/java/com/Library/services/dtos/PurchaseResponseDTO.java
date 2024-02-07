package com.Library.services.dtos;

import com.Library.services.common.PaymentStatus;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PurchaseResponseDTO {
    private PaymentStatus paymentStatus;
    private String message;

    public PurchaseResponseDTO(PaymentStatus paymentStatus, String message) {
        this.paymentStatus = paymentStatus;
        this.message = message;
    }
}