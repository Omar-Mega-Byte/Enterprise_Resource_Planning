package com.example.Enterprise_Resource_Planning.finance.model;

import java.math.BigDecimal;

import com.example.Enterprise_Resource_Planning.common.base.BaseEntity;
import com.example.Enterprise_Resource_Planning.finance.model.enums.AccountType;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * Represents a financial account entity with details such as account number,
 * holder name, balance, account type, and bank name.
 * Used for managing account information in the finance module.
 */

@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@NoArgsConstructor
public class Account extends BaseEntity {
    @Column(name = "account_number", nullable = false, unique = true)
    private String accountNumber;

    @Column(name = "account_holder_name", nullable = false)
    private String accountHolderName;

    @Column(precision = 12, scale = 2, name = "balance", nullable = false)
    private BigDecimal balance;

    @Column(name = "account_type", nullable = false)
    @Enumerated(EnumType.STRING)
    private AccountType accountType;

    @Column(name = "bank_name", nullable = false)
    private String bankName;

}
