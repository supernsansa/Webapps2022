/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSF/JSFManagedBean.java to edit this template
 */
package com.webapps2022.jsf;

import com.webapps2022.ejb.UserService;
import com.webapps2022.entity.Payment;
import java.util.List;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.enterprise.context.Dependent;
import javax.enterprise.context.RequestScoped;

@Named
@RequestScoped
public class ViewPaymentsBean {

    @EJB
    UserService usrSrv;
    List<Payment> fulfilledPayments;
    List<Payment> pendingPayments;
    List<Payment> notificationPayments;

    public ViewPaymentsBean() {
    }

    public List<Payment> getFulfilledPayments() {
        fulfilledPayments = usrSrv.getFulfilledPayments();
        return fulfilledPayments;
    }

    public List<Payment> getPendingPayments() {
        pendingPayments = usrSrv.getPendingPayments();
        return pendingPayments;
    }

    public List<Payment> getNotificaitonPayments() {
        notificationPayments = usrSrv.getNotifPayments();
        return notificationPayments;
    }

}
