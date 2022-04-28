/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.webapps2022.ejb;

import com.webapps2022.entity.SystemUser;
import com.webapps2022.entity.SystemUserGroup;
import com.webapps2022.resources.Currency;
import com.webapps2022.thrift.DatetimeServer;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.annotation.Resource;
import javax.ejb.EJBContext;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;

@Startup
@Singleton
public class InitService {

    @PersistenceContext(unitName = "WebappsDBPU")
    EntityManager em;
    DatetimeServer server;
    @Resource
    EJBContext ejbContext;

    private boolean tableCreated;

    @PostConstruct
    public void init() {
        System.out.println("At startup: Opening Thrift server");
        //Start up thrift server
        server = new DatetimeServer();
        server.start();

        System.out.println("At startup: Initialising Database");
        //Register admin1 account when DB is initialized
        try {
            //End method if admin1 user already exists
            if (getAdmin1()) {
                System.out.println("admin1 exists");
            } else if (tableCreated) {
                System.out.println("admin1 not found. Registering...");
                SystemUser sys_user;
                SystemUserGroup sys_user_group;

                MessageDigest md = MessageDigest.getInstance("SHA-256");
                String passwd = "admin1";
                md.update(passwd.getBytes("UTF-8"));
                byte[] digest = md.digest();
                BigInteger bigInt = new BigInteger(1, digest);
                String paswdToStoreInDB = bigInt.toString(16);

                //Create default admin1 account
                sys_user = new SystemUser("admin1", paswdToStoreInDB, "admins", Currency.GBP, 0.0);
                sys_user.setBalance(0.0);
                sys_user_group = new SystemUserGroup("admin1", "admins");
                em.persist(sys_user);
                em.persist(sys_user_group);
                em.flush();
            }

        } catch (UnsupportedEncodingException | NoSuchAlgorithmException ex) {
            Logger.getLogger(UserService.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @PreDestroy
    public void closeThriftServer() {
        System.out.println("Closing Thrift server");
        server.StopServer();
    }

    //Attempts to find admin1
    public boolean getAdmin1() {
        try {
            SystemUser result = (SystemUser) em.createNamedQuery("SystemUser.findUserByName")
                    .setParameter("user", "admin1")
                    .getSingleResult();
            tableCreated = true;
            return result.getUsername().equals("admin1");
        } catch (NoResultException ex) {
            System.out.println("admin1 not found (inside method)");
            tableCreated = true;
            return false;
        } catch (PersistenceException ex) {
            System.out.println("Table not created yet");
            tableCreated = false;
            return false;
        }
    }

}
