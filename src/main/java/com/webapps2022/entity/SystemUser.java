package com.webapps2022.entity;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Query;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;

@Entity
public class SystemUser implements Serializable {

    /**
     * @Id @GeneratedValue(strategy = GenerationType.AUTO) private Long id;
     */
    // here on could use Bean Validation annotations to enforce specific rules - this could be alternatively implemented when validating the form in the web tier
    // for now we check only for Null values
    @Id
    private String username;

    // here on could use Bean Validation annotations to enforce specific rules - this could be alternatively implemented when validating the form in the web tier
    // for now we check only for Null values
    @NotNull
    private String userpassword;

    //The amount of GBP the user is holding (cannot be negative)
    @PositiveOrZero
    private Double balance;

    //Each user belongs to a single user group (user or admin)
    private String systemUserGroup;

    //each user has many debit transaction records and each record belongs to one user
    @OneToMany(cascade = CascadeType.ALL, targetEntity = Payment.class)
    private List<Payment> payments;

    public SystemUser() {
    }

    public SystemUser(String username, String userpassword, String systemUserGroup) {
        this.username = username;
        this.userpassword = userpassword;
        this.balance = 2000.00;
        this.systemUserGroup = systemUserGroup;
        //this.name = name;
        //this.surname = surname;
    }

    /**
     * public Long getId() { return id; }
     *
     * public void setId(Long id) { this.id = id; }
     *
     * @return
     */
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

    public void setPayments(List<Payment> debits) {
        this.payments = debits;
    }

    /**
     * public String getName() { return name; }
     *
     * public void setName(String name) { this.name = name; }
     *
     * public String getSurname() { return surname; }
     *
     * public void setSurname(String surname) { this.surname = surname; }
     */
    @Override
    public int hashCode() {
        int hash = 5;
        //hash = 97 * hash + Objects.hashCode(this.id);
        hash = 97 * hash + Objects.hashCode(this.username);
        hash = 97 * hash + Objects.hashCode(this.userpassword);
        hash = 97 * hash + Objects.hashCode(this.balance);
        //hash = 97 * hash + Objects.hashCode(this.surname);
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
        /**
         * if (!Objects.equals(this.id, other.id)) { return false; }
         *
         */
        if (!Objects.equals(this.userpassword, other.userpassword)) {
            return false;
        }
        if (!Objects.equals(this.balance, other.balance)) {
            return false;
        }
        return Objects.equals(this.username, other.username);
    }

}
