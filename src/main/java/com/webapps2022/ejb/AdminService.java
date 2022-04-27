/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.webapps2022.ejb;

import com.webapps2022.entity.Payment;
import com.webapps2022.entity.SystemUser;
import com.webapps2022.entity.SystemUserGroup;
import com.webapps2022.resources.Currency;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.Resource;
import javax.annotation.security.RolesAllowed;
import javax.ejb.EJBContext;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import static javax.ejb.TransactionAttributeType.REQUIRED;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@RolesAllowed("admins")
@Stateless
@TransactionAttribute(REQUIRED)
public class AdminService {

    @PersistenceContext
    EntityManager em;
    @Resource
    EJBContext ejbContext;

    public AdminService() {
    }

    //Registers new admin
    public String registerAdmin(String username, String userpassword) {
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

            sys_user = new SystemUser(username, paswdToStoreInDB, "admins", Currency.GBP, 0.0);
            sys_user_group = new SystemUserGroup(username, "admins");

            em.persist(sys_user);
            em.persist(sys_user_group);
            em.flush();
            return "Success";
        } catch (UnsupportedEncodingException | NoSuchAlgorithmException ex) {
            Logger.getLogger(UserService.class.getName()).log(Level.SEVERE, null, ex);
            return "Error Occurred";
        }
    }

    //Returns list of all registered users (and admins) in the database
    public List<SystemUser> getAllUsers() {
        List result = em.createNamedQuery("SystemUser.findAllUsers")
                .getResultList();
        return result;
    }

    public EntityManager getEm() {
        return em;
    }

    public void setEm(EntityManager em) {
        this.em = em;
    }

    public EJBContext getEjbContext() {
        return ejbContext;
    }

    public void setEjbContext(EJBContext ejbContext) {
        this.ejbContext = ejbContext;
    }

    //Retrieves all fulfilled payments (credits and debits) in the database
    public List<Payment> getAllFulfilledPayments() {
        List result = em.createNamedQuery("Payment.findAllFulfilled")
                .getResultList();
        return result;
    }

    //Retrieves all fulfilled payments (credits and debits) that a user has made
    public List<Payment> getAllNotificationPayments() {
        List result = em.createNamedQuery("Payment.findAllPending")
                .getResultList();
        return result;
    }
}
