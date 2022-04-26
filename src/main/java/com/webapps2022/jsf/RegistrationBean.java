package com.webapps2022.jsf;

import com.webapps2022.ejb.AdminService;
import com.webapps2022.ejb.UserService;
import com.webapps2022.resources.Currency;
import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.inject.Named;

@Named
@RequestScoped
public class RegistrationBean {

    @EJB
    UserService usrSrv;
    @EJB
    AdminService adminSrv;

    String username;
    String userpassword;
    Currency usercurrency;

    public RegistrationBean() {

    }

    //Register normal user
    public String register() {
        usrSrv.registerUser(username, userpassword, usercurrency);
        return "index";
    }

    //Register an admin
    public String registerAdmin() {
        adminSrv.registerAdmin(username, userpassword);
        return "admin";
    }

    public UserService getUsrSrv() {
        return usrSrv;
    }

    public void setUsrSrv(UserService usrSrv) {
        this.usrSrv = usrSrv;
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

    public AdminService getAdminSrv() {
        return adminSrv;
    }

    public void setAdminSrv(AdminService adminSrv) {
        this.adminSrv = adminSrv;
    }

    public Currency getUsercurrency() {
        return usercurrency;
    }

    public void setUsercurrency(Currency usercurrency) {
        this.usercurrency = usercurrency;
    }

    //Returns Currency enum as an array
    public Currency[] getCurrencies() {
        return Currency.values();
    }

}
