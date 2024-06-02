package com.school.project.dto;

import com.school.project.model.enumeration.EnumPaymentMethod;
import com.school.project.model.enumeration.EnumPaymentStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;


@Data
public class PaymentDto {
    BigDecimal totalPayment;
    @Schema(example = "PAYPAL")
    @NotNull(message = "Payment methode can't  be null")
    EnumPaymentMethod paymentMethod;
    @Schema(example = "COMPLETED")
    @NotNull(message = "Payment Status can't  be null")
    EnumPaymentStatus paymentStatus;
    @NotNull
    List<Long> coursesId;
    @NotNull
    Long promotionsId;
}