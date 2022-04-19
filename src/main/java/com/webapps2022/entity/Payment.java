/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.webapps2022.entity;

import java.io.Serializable;
import java.time.OffsetDateTime;
import java.util.Objects;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.validation.constraints.NotNull;

//Entity representing a monetary transaction between 2 users
@Entity
@NamedQuery(name = "Payment.findAllByName",
        query = "SELECT p FROM Payment p WHERE p.sender = :user OR p.recipient = :user")

public class Payment implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    //Amount of money being moved in transaction
    @NotNull
    Double amount;

    //email of sender goes here
    @NotNull
    String sender;

    //email of recipient goes here
    @NotNull
    String recipient;

    //Date, time, and timezone of transaction
    @NotNull
    OffsetDateTime dateTime;

    //Whether or not the payment has happened yet (i.e. if the payment is a request or not)
    @NotNull
    Boolean fulfilled;

    public Payment() {
    }

    public Payment(Double Amount, String sender, String recipient, OffsetDateTime dateTime, Boolean fulfilled) {
        this.amount = Amount;
        this.sender = sender;
        this.recipient = recipient;
        this.dateTime = dateTime;
        this.fulfilled = fulfilled;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double Amount) {
        this.amount = Amount;
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

    public OffsetDateTime getDateTime() {
        return dateTime;
    }

    public void setDateTime(OffsetDateTime dateTime) {
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

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 97 * hash + Objects.hashCode(this.id);
        hash = 97 * hash + Objects.hashCode(this.amount);
        hash = 97 * hash + Objects.hashCode(this.sender);
        hash = 97 * hash + Objects.hashCode(this.recipient);
        hash = 97 * hash + Objects.hashCode(this.dateTime);
        hash = 97 * hash + Objects.hashCode(this.fulfilled);
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
        return Objects.equals(this.dateTime, other.dateTime);
    }
}
