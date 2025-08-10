package com.example.Enterprise_Resource_Planning.finance.model;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import com.example.Enterprise_Resource_Planning.common.base.BaseEntity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * Represents a journal entry in the double-entry bookkeeping system.
 * Each journal entry contains multiple lines (debits and credits) that must
 * balance.
 * Used for recording all financial transactions in the system.
 */
@Entity
@Table(name = "journal_entries")
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class JournalEntry extends BaseEntity {

    @Column(name = "entry_number", nullable = false, unique = true)
    private String entryNumber;

    @Column(name = "entry_date", nullable = false)
    private LocalDate entryDate;

    @Column(name = "description", nullable = false)
    private String description;

    @Column(name = "reference", length = 100)
    private String reference;

    @Column(name = "total_amount", precision = 12, scale = 2, nullable = false)
    private BigDecimal totalAmount;

    @Column(name = "is_posted", nullable = false)
    private Boolean isPosted = false;

    @OneToMany(mappedBy = "journalEntry", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    private List<JournalEntryLine> journalEntryLines = new ArrayList<>();

    /**
     * Constructor for creating a new journal entry
     */
    public JournalEntry(String entryNumber, LocalDate entryDate, String description, String reference) {
        this.entryNumber = entryNumber;
        this.entryDate = entryDate;
        this.description = description;
        this.reference = reference;
        this.totalAmount = BigDecimal.ZERO;
        this.isPosted = false;
    }

    /**
     * Add a journal entry line (debit or credit)
     */
    public void addJournalEntryLine(JournalEntryLine line) {
        journalEntryLines.add(line);
        line.setJournalEntry(this);
    }

    /**
     * Remove a journal entry line
     */
    public void removeJournalEntryLine(JournalEntryLine line) {
        journalEntryLines.remove(line);
        line.setJournalEntry(null);
    }

    /**
     * Calculate total debits from all lines
     */
    public BigDecimal getTotalDebits() {
        return journalEntryLines.stream()
                .filter(line -> line.getDebitAmount() != null)
                .map(JournalEntryLine::getDebitAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    /**
     * Calculate total credits from all lines
     */
    public BigDecimal getTotalCredits() {
        return journalEntryLines.stream()
                .filter(line -> line.getCreditAmount() != null)
                .map(JournalEntryLine::getCreditAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    /**
     * Check if the journal entry is balanced (debits = credits)
     */
    public boolean isBalanced() {
        return getTotalDebits().compareTo(getTotalCredits()) == 0;
    }

    /**
     * Post the journal entry (mark as final)
     */
    public void post() {
        if (!isBalanced()) {
            throw new IllegalStateException("Cannot post unbalanced journal entry");
        }
        this.isPosted = true;
        this.totalAmount = getTotalDebits(); // or getTotalCredits() since they're equal
    }
}
