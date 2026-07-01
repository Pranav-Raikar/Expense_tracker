package com.example.expense_tracker.model;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "transactions")
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private double amount;
    private String category;
    private String description;
    private LocalDate date;

    @Column(name = "user_id")
    private Long userId;

    // ── Getters & Setters ─────────────────────────────────
    public Long getId()                  { return id; }

    public double getAmount()            { return amount; }
    public void setAmount(double amount) { this.amount = amount; }

    public String getCategory()                    { return category; }
    public void setCategory(String category)       { this.category = category; }

    public String getDescription()                 { return description; }
    public void setDescription(String description) { this.description = description; }

    public LocalDate getDate()           { return date; }
    public void setDate(LocalDate date)  { this.date = date; }

    public Long getUserId()              { return userId; }
    public void setUserId(Long userId)   { this.userId = userId; }
}
