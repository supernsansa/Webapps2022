package com.webapps2022.entity;

import com.webapps2022.resources.Currency;
import com.webapps2022.restservice.ConversionRestClient;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;

@Entity
@NamedQueries({
    @NamedQuery(name = "SystemUser.findAllUsers",
            query = "SELECT s FROM SystemUser s"),
    @NamedQuery(name = "SystemUser.findUserByName",
            query = "SELECT s FROM SystemUser s WHERE s.username = :user"),})
public class SystemUser implements Serializable {

    @Id
    private String username;

    @NotNull
    private String userpassword;

    //The amount of GBP the user is holding (cannot be negative)
    @PositiveOrZero
    private Double balance;

    //Each user belongs to a single user group (user or admin)
    private String systemUserGroup;

    //each user has many payment records and each record belongs to two users
    @ManyToMany(cascade = CascadeType.ALL, targetEntity = Payment.class)
    @JoinTable(
            name = "systemUserPayments",
            joinColumns = @JoinColumn(name = "systemUser_username"),
            inverseJoinColumns = @JoinColumn(name = "payment_id"))
    private List<Payment> payments;

    //Users can choose what currency their money will be stored as
    @NotNull
    @Enumerated(EnumType.STRING)
    private Currency currency;

    public SystemUser() {
    }

    public SystemUser(String username, String userpassword, String systemUserGroup, Currency currency) {
        this.username = username;
        this.userpassword = userpassword;
        this.systemUserGroup = systemUserGroup;
        this.payments = new ArrayList<>();
        this.currency = currency;
        //Give user 1000 GBP to start (convert to chosen currency)
        Double startingBalance = ConversionRestClient.runConversionRestOperation(Currency.GBP, currency, 1000.0);
        BigDecimal bd = new BigDecimal(startingBalance).setScale(2, RoundingMode.HALF_UP);
        this.balance = bd.doubleValue();
    }

    //Constructor for making SystemUser with custom starting balance
    public SystemUser(String username, String userpassword, String systemUserGroup, Currency currency, Double amount) {
        this.username = username;
        this.userpassword = userpassword;
        this.systemUserGroup = systemUserGroup;
        this.payments = new ArrayList<>();
        this.currency = currency;
        BigDecimal bd = new BigDecimal(amount).setScale(2, RoundingMode.HALF_UP);
        this.balance = bd.doubleValue();
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUserpassword() {
        return userpassword;
    }

    public void setUserpassword(String userpassword) {
        this.userpassword = userpassword;
    }

    public Double getBalance() {
        return balance;
    }

    public void setBalance(Double balance) {
        BigDecimal bd = new BigDecimal(balance).setScale(2, RoundingMode.HALF_UP);
        balance = bd.doubleValue();
        this.balance = balance;
    }

    public String getSystemUserGroup() {
        return systemUserGroup;
    }

    public void setSystemUserGroup(String systemUserGroup) {
        this.systemUserGroup = systemUserGroup;
    }

    public List<Payment> getPayments() {
        return payments;
    }

    public void setPayments(List<Payment> payments) {
        this.payments = payments;
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
        hash = 97 * hash + Objects.hashCode(this.username);
        hash = 97 * hash + Objects.hashCode(this.userpassword);
        hash = 97 * hash + Objects.hashCode(this.balance);
        hash = 97 * hash + Objects.hashCode(this.systemUserGroup);
        hash = 97 * hash + Objects.hashCode(this.payments);
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
        final SystemUser other = (SystemUser) obj;
        if (!Objects.equals(this.userpassword, other.userpassword)) {
            return false;
        }
        if (!Objects.equals(this.balance, other.balance)) {
            return false;
        }
        if (!Objects.equals(this.systemUserGroup, other.systemUserGroup)) {
            return false;
        }
        if (!Objects.equals(this.payments, other.payments)) {
            return false;
        }
        if (!Objects.equals(this.currency, other.currency)) {
            return false;
        }
        return Objects.equals(this.username, other.username);
    }

}
