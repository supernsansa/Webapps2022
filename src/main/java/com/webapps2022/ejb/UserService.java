package com.webapps2022.ejb;

import com.webapps2022.entity.Payment;
import com.webapps2022.entity.SystemUser;
import com.webapps2022.entity.SystemUserGroup;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.Resource;
import javax.ejb.EJBContext;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

@Stateless
public class UserService {

    @PersistenceContext
    EntityManager em;
    @Resource
    EJBContext ejbContext;

    public UserService() {
    }

    public void registerUser(String username, String userpassword) {
        try {
            SystemUser sys_user;
            SystemUserGroup sys_user_group;

            MessageDigest md = MessageDigest.getInstance("SHA-256");
            String passwd = userpassword;
            md.update(passwd.getBytes("UTF-8"));
            byte[] digest = md.digest();
            BigInteger bigInt = new BigInteger(1, digest);
            String paswdToStoreInDB = bigInt.toString(16);

            // apart from the default constructor which is required by JPA
            // you need to also implement a constructor that will make the following code succeed
            sys_user = new SystemUser(username, paswdToStoreInDB, "users");
            sys_user_group = new SystemUserGroup(username, "users");

            em.persist(sys_user);
            em.persist(sys_user_group);

        } catch (UnsupportedEncodingException | NoSuchAlgorithmException ex) {
            Logger.getLogger(UserService.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    //Returns the username of the logged in user
    public String getUsername() {
        String username = ejbContext.getCallerPrincipal().getName();
        return username;
    }

    public Double getBalance() {
        /**
         * TypedQuery<Double> query = em.createQuery("SELECT u.balance FROM
         * SystemUser AS u WHERE u.username = " + '"' + getUsername() + '"',
         * Double.class); Double result = query.getSingleResult();
         *
         */

        SystemUser user = (SystemUser) em.find(SystemUser.class, getUsername());
        return user.getBalance();
    }

    //TODO: turn into transaction
    //Handles sending money from sender to recipient
    public boolean sendPayment(String sender, String recipient, Double amount) {
        try {
            SystemUser senderObj;
            SystemUser recipientObj;

            senderObj = (SystemUser) em.find(SystemUser.class, sender);
            recipientObj = (SystemUser) em.find(SystemUser.class, recipient);
            System.out.println(senderObj.getUsername() + " " + recipientObj.getUsername());

            //Only go ahead if sender has enough money
            if (senderObj.getBalance() < amount) {
                System.out.println("Not enough money");
                return false;
            } else {
                senderObj.setBalance(senderObj.getBalance() - amount);
                System.out.println(senderObj.getBalance());
            }

            recipientObj.setBalance(recipientObj.getBalance() + amount);
            System.out.println(recipientObj.getBalance());

            Payment paymentToSend = new Payment(amount, sender, recipient, true);
            em.persist(paymentToSend);
            //em.merge(senderObj);
            //em.merge(recipientObj);
            //em.flush();
            return true;

        } catch (Exception err) {
            Logger.getLogger(UserService.class.getName()).log(Level.SEVERE, null, err);
            return false;
        }
    }

    //TODO: turn into transaction
    //Handles sending payment request
    public boolean sendPaymentRequest(String sender, String recipient, Double amount) {
        try {
            SystemUser senderObj;
            SystemUser recipientObj;

            senderObj = (SystemUser) em.find(SystemUser.class, sender);
            recipientObj = (SystemUser) em.find(SystemUser.class, recipient);
            System.out.println(senderObj.getUsername() + " " + recipientObj.getUsername());

            Payment paymentToSend = new Payment(amount, sender, recipient, false);
            em.persist(paymentToSend);
            return true;

        } catch (Exception err) {
            Logger.getLogger(UserService.class.getName()).log(Level.SEVERE, null, err);
            return false;
        }
    }

    //Retrieves all fulfilled payments (credits and debits) that a user has made
    public List<Payment> getFulfilledPayments() {
        List result = em.createNamedQuery("Payment.findAllFulfilledByName")
                .setParameter("user", getUsername())
                .getResultList();
        return result;
    }

    //Retrieves all pending payment requests that a user has made
    public List<Payment> getPendingPayments() {
        List result = em.createNamedQuery("Payment.findAllPendingByName")
                .setParameter("user", getUsername())
                .getResultList();
        return result;
    }

    //Retrieves all payment requests recieved by a user
    public List<Payment> getNotifPayments() {
        List result = em.createNamedQuery("Payment.findAllNotifsByName")
                .setParameter("user", getUsername())
                .getResultList();
        return result;
    }
}
