package com.example.Enterprise_Resource_Planning.finance.model;

import java.math.BigDecimal;

import com.example.Enterprise_Resource_Planning.common.base.BaseEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * Represents a single line item in a journal entry.
 * Each line contains either a debit or credit amount for a specific account.
 * Multiple lines make up a complete journal entry in double-entry bookkeeping.
 */
@Entity
@Table(name = "journal_entry_lines")
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class JournalEntryLine extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "journal_entry_id", nullable = false)
    private JournalEntry journalEntry;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id", nullable = false)
    private Account account;

    @Column(name = "description")
    private String description;

    @Column(name = "debit_amount", precision = 12, scale = 2)
    private BigDecimal debitAmount;

    @Column(name = "credit_amount", precision = 12, scale = 2)
    private BigDecimal creditAmount;

    /**
     * Constructor for creating a debit entry line
     */
    public static JournalEntryLine createDebit(Account account, BigDecimal amount, String description) {
        JournalEntryLine line = new JournalEntryLine();
        line.setAccount(account);
        line.setDebitAmount(amount);
        line.setCreditAmount(null);
        line.setDescription(description);
        return line;
    }

    /**
     * Constructor for creating a credit entry line
     */
    public static JournalEntryLine createCredit(Account account, BigDecimal amount, String description) {
        JournalEntryLine line = new JournalEntryLine();
        line.setAccount(account);
        line.setCreditAmount(amount);
        line.setDebitAmount(null);
        line.setDescription(description);
        return line;
    }

    /**
     * Get the amount (either debit or credit)
     */
    public BigDecimal getAmount() {
        return debitAmount != null ? debitAmount : creditAmount;
    }

    /**
     * Check if this line is a debit
     */
    public boolean isDebit() {
        return debitAmount != null && debitAmount.compareTo(BigDecimal.ZERO) > 0;
    }

    /**
     * Check if this line is a credit
     */
    public boolean isCredit() {
        return creditAmount != null && creditAmount.compareTo(BigDecimal.ZERO) > 0;
    }
}
