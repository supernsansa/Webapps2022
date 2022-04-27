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
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.apache.thrift.transport.TServerSocket;
import org.apache.thrift.transport.TTransportException;

@Startup
@Singleton
public class InitService {

    @PersistenceContext(unitName = "WebappsDBPU")
    EntityManager em;
    DatetimeServer server;

    @PostConstruct
    public void init() {
        System.out.println("At startup: Opening Thrift server");
        //Start up thrift server
        server = new DatetimeServer();
        server.start();

        System.out.println("At startup: Initialising Datbase with admin1 account registered");
        //Register admin1 account when DB is initialized
        try {
            SystemUser sys_user;
            SystemUserGroup sys_user_group;

            //End method if admin1 user already exists
            if (em.find(SystemUser.class, "admin1") != null) {
                return;
            }

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

        } catch (UnsupportedEncodingException | NoSuchAlgorithmException ex) {
            Logger.getLogger(UserService.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @PreDestroy
    public void closeThriftServer() {
        System.out.println("Closing Thrift server");
        server.StopServer();
    }

}
