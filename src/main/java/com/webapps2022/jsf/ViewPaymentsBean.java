/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSF/JSFManagedBean.java to edit this template
 */
package com.webapps2022.jsf;

import com.webapps2022.ejb.AdminService;
import com.webapps2022.ejb.UserService;
import com.webapps2022.entity.Payment;
import java.io.IOException;
import java.util.List;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

@Named
@RequestScoped
public class ViewPaymentsBean {

    @EJB
    UserService usrSrv;
    @EJB
    AdminService adminSrv;
    List<Payment> fulfilledPayments;
    List<Payment> pendingPayments;
    List<Payment> notificationPayments;
    List<Payment> allFulfilledPayments;
    List<Payment> allNotificationPayments;
    Long paymentId;

    public ViewPaymentsBean() {
    }

    public UserService getUsrSrv() {
        return usrSrv;
    }

    public void setUsrSrv(UserService usrSrv) {
        this.usrSrv = usrSrv;
    }

    public Long getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(Long paymentId) {
        this.paymentId = paymentId;
    }

    public List<Payment> getFulfilledPayments() {
        fulfilledPayments = usrSrv.getFulfilledPayments();
        return fulfilledPayments;
    }

    public List<Payment> getPendingPayments() {
        pendingPayments = usrSrv.getPendingPayments();
        return pendingPayments;
    }

    public List<Payment> getNotificationPayments() {
        notificationPayments = usrSrv.getNotifPayments();
        return notificationPayments;
    }

    public List<Payment> getAllFulfilledPayments() {
        allFulfilledPayments = adminSrv.getAllFulfilledPayments();
        return allFulfilledPayments;
    }

    public List<Payment> getAllNotificationPayments() {
        allNotificationPayments = adminSrv.getAllNotificationPayments();
        return allNotificationPayments;
    }

    public AdminService getAdminSrv() {
        return adminSrv;
    }

    //Instructs user service EJB to execute payment request
    public void acceptPaymentRequest() throws IOException {
        FacesContext context = FacesContext.getCurrentInstance();
        String result = usrSrv.acceptPaymentRequest(paymentId);
        if (result.equals("Success")) {
            context.getExternalContext().redirect("user.xhtml");
        } else {
            context.addMessage(null, new FacesMessage(result));
        }
    }

    //Instructs user service EJB to cancel payment request     
    public void rejectPaymentRequest() throws IOException {
        FacesContext context = FacesContext.getCurrentInstance();
        if (usrSrv.rejectPaymentRequest(paymentId) == true) {
            context.getExternalContext().redirect("user.xhtml");
        } else {
            context.addMessage(null, new FacesMessage("Error occurred"));
        }
    }
}
