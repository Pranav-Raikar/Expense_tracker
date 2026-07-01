package com.example.expense_tracker.model;

import jakarta.persistence.*;

@Entity
@Table(name = "users")   // maps to the 'users' table in MySQL
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;
    private String password;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    private String email;

    // ── Getters & Setters ─────────────────────────────────
    public Long getId()           { return id; }
    public String getUsername()   { return username; }
    public String getPassword()   { return password; }
    public String getFirstName()  { return firstName; }
    public String getLastName()   { return lastName; }
    public String getEmail()      { return email; }

    public void setUsername(String username)  { this.username = username; }
    public void setPassword(String password)  { this.password = password; }
    public void setFirstName(String firstName){ this.firstName = firstName; }
    public void setLastName(String lastName)  { this.lastName = lastName; }
    public void setEmail(String email)        { this.email = email; }
}
