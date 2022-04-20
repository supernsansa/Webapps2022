/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSF/JSFManagedBean.java to edit this template
 */
package com.webapps2022.jsf;

import com.webapps2022.ejb.UserService;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.enterprise.context.RequestScoped;

@Named
@RequestScoped
public class UserBean {

    @EJB
    UserService usrSrv;

    public UserBean() {
    }

    public String getActiveUsername() {
        return usrSrv.getUsername();
    }

    public Double getActiveBalance() {
        return usrSrv.getBalance();
    }

    public UserService getUsrSrv() {
        return usrSrv;
    }

    public void setUsrSrv(UserService usrSrv) {
        this.usrSrv = usrSrv;
    }
}
