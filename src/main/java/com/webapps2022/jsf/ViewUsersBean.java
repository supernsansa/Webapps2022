/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.webapps2022.jsf;

//Handles viewing all users in the database
import com.webapps2022.ejb.AdminService;
import com.webapps2022.entity.SystemUser;
import java.util.List;
import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.inject.Named;

@Named
@RequestScoped
public class ViewUsersBean {

    @EJB
    AdminService adminSrv;
    List<SystemUser> allSystemUsers;

    public ViewUsersBean() {
    }

    public AdminService getAdminSrv() {
        return adminSrv;
    }

    public void setAdminSrv(AdminService adminSrv) {
        this.adminSrv = adminSrv;
    }

    public List<SystemUser> getAllSystemUsers() {
        allSystemUsers = adminSrv.getAllUsers();
        return allSystemUsers;
    }

}
