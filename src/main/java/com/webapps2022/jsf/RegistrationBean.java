package com.webapps2022.jsf;

import com.webapps2022.ejb.AdminService;
import com.webapps2022.ejb.UserService;
import com.webapps2022.resources.Currency;
import java.io.IOException;
import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;

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
    public void register() throws IOException {
        FacesContext context = FacesContext.getCurrentInstance();
        String result = usrSrv.registerUser(username, userpassword, usercurrency);
        if (!result.equals("Success")) {
            context.addMessage(null, new FacesMessage(result));
            return;
        } //return "index";
        else {
            context.getExternalContext().redirect("/webapps2022/");
        }
    }

    //Register an admin
    public void registerAdmin() throws IOException {
        FacesContext context = FacesContext.getCurrentInstance();
        String result = adminSrv.registerAdmin(username, userpassword);
        if (!result.equals("Success")) {
            context.addMessage(null, new FacesMessage(result));
            return;
        } //return "admin";
        else {
            context.getExternalContext().redirect("/webapps2022/faces/admins/admin.xhtml");
        }
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
