package com.example.expense_tracker.model;

import jakarta.persistence.*;

@Entity
@Table(name = "balance")
public class Balance {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id")
    private Long userId;

    @Column(name = "total_balance")
    private double totalBalance;

    // ── Getters & Setters ─────────────────────────────────
    public Long getId()              { return id; }

    // BUG WAS HERE: was returning 'id' instead of 'userId'
    public Long getUserId()          { return userId; }
    public void setUserId(Long userId){ this.userId = userId; }

    public double getTotalBalance()  { return totalBalance; }
    public void setTotalBalance(double totalBalance){ this.totalBalance = totalBalance; }
}
