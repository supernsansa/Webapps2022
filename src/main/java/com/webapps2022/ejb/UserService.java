package com.webapps2022.ejb;

import com.webapps2022.entity.Payment;
import com.webapps2022.entity.SystemUser;
import com.webapps2022.entity.SystemUserGroup;
import com.webapps2022.resources.Currency;
import com.webapps2022.restservice.ConversionRestClient;
import com.webapps2022.thrift.DatetimeClient;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.Resource;
import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.ejb.EJBContext;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import static javax.ejb.TransactionAttributeType.*;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@PermitAll
@Stateless
@TransactionAttribute(REQUIRED)
public class UserService {

    @PersistenceContext
    EntityManager em;
    @Resource
    EJBContext ejbContext;

    public UserService() {
    }

    //Registers new user
    public String registerUser(String username, String userpassword, Currency usercurrency) {
        try {
            //First check if user already exists
            SystemUser existingObj = (SystemUser) em.find(SystemUser.class, username);
            if (existingObj != null) {
                return "A account with that username already exists";
            }

            SystemUser sys_user;
            SystemUserGroup sys_user_group;

            MessageDigest md = MessageDigest.getInstance("SHA-256");
            String passwd = userpassword;
            md.update(passwd.getBytes("UTF-8"));
            byte[] digest = md.digest();
            BigInteger bigInt = new BigInteger(1, digest);
            String paswdToStoreInDB = bigInt.toString(16);

            sys_user = new SystemUser(username, paswdToStoreInDB, "users", usercurrency);
            sys_user_group = new SystemUserGroup(username, "users");

            em.persist(sys_user);
            em.persist(sys_user_group);
            em.flush();
            return "Success";

        } catch (UnsupportedEncodingException | NoSuchAlgorithmException ex) {
            Logger.getLogger(UserService.class.getName()).log(Level.SEVERE, null, ex);
            return "Error Occured";
        }
    }

    //Returns the username of the logged in user
    public String getUsername() {
        String username = ejbContext.getCallerPrincipal().getName();
        return username;
    }

    //Returns balance of logged in user
    @RolesAllowed("users")
    public Double getBalance() {
        SystemUser user = (SystemUser) em.find(SystemUser.class, getUsername());
        return user.getBalance();
    }

    //Handles sending money from sender to recipient
    @RolesAllowed("users")
    public String sendPayment(String sender, String recipient, Double amount) {
        try {
            SystemUser senderObj;
            SystemUser recipientObj;

            senderObj = (SystemUser) em.find(SystemUser.class, sender);
            recipientObj = (SystemUser) em.find(SystemUser.class, recipient);
            //Check that recipient exists
            if (recipientObj == null) {
                return "Recipient not found, check field";
            } //Check that sender and recipient are not the same user
            else if (senderObj.getUsername().equals(recipientObj.getUsername())) {
                return "You cannot send money to yourself!";
            }
            System.out.println(senderObj.getUsername() + " " + recipientObj.getUsername());

            //Only go ahead if sender has enough money
            if (senderObj.getBalance() < amount) {
                return "Insufficient funds";
            } else {
                //Remove funds from sender
                senderObj.setBalance(senderObj.getBalance() - amount);
                System.out.println(senderObj.getBalance());
            }

            //Convert and add money to recipient's account balance
            Double amountToSend = ConversionRestClient.runConversionRestOperation(senderObj.getCurrency(), recipientObj.getCurrency(), amount);
            recipientObj.setBalance(recipientObj.getBalance() + amountToSend);
            System.out.println(recipientObj.getBalance());

            //Add payment to DB and relevant entities
            Payment paymentToSend = new Payment(senderObj.getCurrency(), amount, sender, recipient, true, currentDatetime());
            senderObj.getPayments().add(paymentToSend);
            recipientObj.getPayments().add(paymentToSend);
            em.persist(paymentToSend);
            em.flush();
            return "Success";

        } catch (IllegalArgumentException err) {
            Logger.getLogger(UserService.class.getName()).log(Level.SEVERE, null, err);
            return "Recipient not found, check field";
        }
    }

    //Handles sending payment request
    @RolesAllowed("users")
    public String sendPaymentRequest(String sender, String recipient, Double amount) {
        try {
            SystemUser senderObj;
            SystemUser recipientObj;

            senderObj = (SystemUser) em.find(SystemUser.class, sender);
            recipientObj = (SystemUser) em.find(SystemUser.class, recipient);
            //Check that recipient exists
            if (senderObj == null) {
                return "Payer not found, check field";
            } //Check that sender and recipient are not the same user
            else if (senderObj.getUsername().equals(recipientObj.getUsername())) {
                return "You cannot request money from yourself!";
            }
            System.out.println(senderObj.getUsername() + " " + recipientObj.getUsername());
            //Convert amount
            Double amountToSend = ConversionRestClient.runConversionRestOperation(recipientObj.getCurrency(), senderObj.getCurrency(), amount);
            //Persist payment record in db
            Payment paymentToSend = new Payment(senderObj.getCurrency(), amountToSend, sender, recipient, false, currentDatetime());
            em.persist(paymentToSend);
            em.flush();
            return "Success";

        } catch (IllegalArgumentException err) {
            Logger.getLogger(UserService.class.getName()).log(Level.SEVERE, null, err);
            return "Recipient not found, check field";
        }
    }

    //Handles accepting payment request
    @RolesAllowed("users")
    public String acceptPaymentRequest(Long paymentId) {
        try {
            SystemUser senderObj;
            SystemUser recipientObj;
            Payment paymentObj;
            Double amount;

            //Find all relevant entities
            paymentObj = (Payment) em.find(Payment.class, paymentId);
            senderObj = (SystemUser) em.find(SystemUser.class, paymentObj.getSender());
            recipientObj = (SystemUser) em.find(SystemUser.class, paymentObj.getRecipient());
            System.out.println(senderObj.getUsername() + " " + recipientObj.getUsername());
            amount = paymentObj.getAmount();

            //Only go ahead if sender has enough money
            //Convert amount
            Double amountToRecieve = ConversionRestClient.runConversionRestOperation(paymentObj.getCurrency(), recipientObj.getCurrency(), amount);
            if (senderObj.getBalance() < amount) {
                return "Insufficient funds";
            } else {
                //Remove amount from sender's account
                senderObj.setBalance(senderObj.getBalance() - amount);
                System.out.println(senderObj.getBalance());
            }

            //Put amount into recipient's account
            recipientObj.setBalance(recipientObj.getBalance() + amountToRecieve);
            System.out.println(recipientObj.getBalance());

            //Change fulfilled variable in paymentObj
            paymentObj.setFulfilled(Boolean.TRUE);
            //Update datetime variable in paymentObj
            //TODO THRIFT timestamp implementation
            paymentObj.setDateTime(currentDatetime());
            em.flush();

            return "Success";

        } catch (Exception err) {
            Logger.getLogger(UserService.class.getName()).log(Level.SEVERE, null, err);
            return "Error occurred";
        }
    }

    //Handles rejecting payment request
    @RolesAllowed("users")
    public boolean rejectPaymentRequest(Long paymentId) {
        try {
            Payment paymentObj;

            //Find payment entity
            paymentObj = (Payment) em.find(Payment.class, paymentId);

            //Remove it from database
            em.remove(paymentObj);
            em.flush();
            return true;

        } catch (Exception err) {
            Logger.getLogger(UserService.class.getName()).log(Level.SEVERE, null, err);
            return false;
        }
    }

    //Retrieves all fulfilled payments (credits and debits) that a user has made
    @RolesAllowed("users")
    public List<Payment> getFulfilledPayments() {
        List result = em.createNamedQuery("Payment.findAllFulfilledByName")
                .setParameter("user", getUsername())
                .getResultList();
        return result;
    }

    //Retrieves all pending payment requests that a user has made
    @RolesAllowed("users")
    public List<Payment> getPendingPayments() {
        List result = em.createNamedQuery("Payment.findAllPendingByName")
                .setParameter("user", getUsername())
                .getResultList();
        return result;
    }

    //Retrieves all payment requests recieved by a user
    @RolesAllowed("users")
    public List<Payment> getNotifPayments() {
        List result = em.createNamedQuery("Payment.findAllNotifsByName")
                .setParameter("user", getUsername())
                .getResultList();
        return result;
    }

    //Returns appropriate currency symbol depending on user's chosen currency
    @RolesAllowed("users")
    public Character getCurrencySymbol() {
        SystemUser userObj = (SystemUser) em.find(SystemUser.class, getUsername());
        if (userObj.getCurrency() == Currency.GBP) {
            return '£';
        } else if (userObj.getCurrency() == Currency.EUR) {
            return '€';
        } else if (userObj.getCurrency() == Currency.USD) {
            return '$';
            //If no case matches for some reason, return N
        } else {
            return 'N';
        }
    }

    //Fetches current timestamp from thrift server
    public String currentDatetime() {
        DatetimeClient client = new DatetimeClient();
        String datetime = client.fetchDatetime();
        return datetime;
    }
}
