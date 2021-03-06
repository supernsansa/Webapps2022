/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.webapps2022.entity;

import com.webapps2022.resources.Currency;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;

//Entity representing a monetary transaction between 2 users
@Entity
@NamedQueries({
    @NamedQuery(name = "Payment.findAllFulfilled",
            query = "SELECT p FROM Payment p WHERE p.fulfilled = TRUE"),
    @NamedQuery(name = "Payment.findAllPending",
            query = "SELECT p FROM Payment p WHERE p.fulfilled = FALSE"),
    @NamedQuery(name = "Payment.findAllFulfilledByName",
            query = "SELECT p FROM Payment p WHERE (p.sender = :user OR p.recipient = :user) AND p.fulfilled = TRUE"),
    @NamedQuery(name = "Payment.findAllPendingByName",
            query = "SELECT p FROM Payment p WHERE (p.recipient = :user) AND p.fulfilled = FALSE"),
    @NamedQuery(name = "Payment.findAllNotifsByName",
            query = "SELECT p FROM Payment p WHERE (p.sender = :user) AND p.fulfilled = FALSE"),})

public class Payment implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    //Amount of money being moved in transaction
    @PositiveOrZero
    Double amount;

    //email of sender goes here
    @NotNull
    String sender;

    //email of recipient goes here
    @NotNull
    String recipient;

    //Date, time, and timezone of transaction
    @NotNull
    String dateTime;

    //Whether or not the payment has happened yet (i.e. if the payment is a request or not)
    @NotNull
    Boolean fulfilled;

    //Holds all involved users in a transaction (sender and recipient)
    @NotNull
    @ManyToMany(mappedBy = "payments")
    List<SystemUser> involvedUsers;

    //The currency used to make this payment (GBP, EUR, or USD)
    @NotNull
    @Enumerated(EnumType.STRING)
    Currency currency;

    public Payment() {
    }

    public Payment(Currency currency, Double amount, String sender, String recipient, Boolean fulfilled, String dateTime) {
        BigDecimal bd = new BigDecimal(amount).setScale(2, RoundingMode.HALF_UP);
        amount = bd.doubleValue();
        this.amount = amount;
        this.sender = sender;
        this.recipient = recipient;
        this.dateTime = dateTime;
        this.fulfilled = fulfilled;
        this.involvedUsers = new ArrayList<>();
        this.currency = currency;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double Amount) {
        BigDecimal bd = new BigDecimal(Amount).setScale(2, RoundingMode.HALF_UP);
        this.amount = bd.doubleValue();
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getRecipient() {
        return recipient;
    }

    public void setRecipient(String recipient) {
        this.recipient = recipient;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Boolean getFulfilled() {
        return fulfilled;
    }

    public void setFulfilled(Boolean fulfilled) {
        this.fulfilled = fulfilled;
    }

    public List<SystemUser> getInvolvedUsers() {
        return involvedUsers;
    }

    public void setInvolvedUsers(List<SystemUser> involvedUsers) {
        this.involvedUsers = involvedUsers;
    }

    public Currency getCurrency() {
        return currency;
    }

    public void setCurrency(Currency currency) {
        this.currency = currency;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 97 * hash + Objects.hashCode(this.id);
        hash = 97 * hash + Objects.hashCode(this.amount);
        hash = 97 * hash + Objects.hashCode(this.sender);
        hash = 97 * hash + Objects.hashCode(this.recipient);
        hash = 97 * hash + Objects.hashCode(this.dateTime);
        hash = 97 * hash + Objects.hashCode(this.fulfilled);
        hash = 97 * hash + Objects.hashCode(this.currency);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Payment other = (Payment) obj;
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        if (!Objects.equals(this.amount, other.amount)) {
            return false;
        }
        if (!Objects.equals(this.sender, other.sender)) {
            return false;
        }
        if (!Objects.equals(this.recipient, other.recipient)) {
            return false;
        }
        if (!Objects.equals(this.fulfilled, other.fulfilled)) {
            return false;
        }
        if (!Objects.equals(this.currency, other.currency)) {
            return false;
        }
        return Objects.equals(this.dateTime, other.dateTime);
    }
}
