package com.example.Enterprise_Resource_Planning.finance.model;

import java.math.BigDecimal;
import java.time.LocalDate;

import com.example.Enterprise_Resource_Planning.common.base.BaseEntity;
import com.example.Enterprise_Resource_Planning.finance.model.enums.PaymentStatus;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Data
@EqualsAndHashCode(callSuper = true)
public class Payment extends BaseEntity{
    @Column(name = "payment_status", nullable = false)
    private PaymentStatus status;

    @Column(name = "amount", precision = 12, scale = 2, nullable = false)
    private BigDecimal amount;

    @Column(name = "payment_date", nullable = false)
    private LocalDate paymentDate;

    @Column(name = "payment_method", nullable = false)
    private String paymentMethod;
}
