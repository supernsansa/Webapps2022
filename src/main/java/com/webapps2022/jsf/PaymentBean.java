/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSF/JSFManagedBean.java to edit this template
 */
package com.webapps2022.jsf;

import com.webapps2022.ejb.UserService;
import com.webapps2022.resources.Currency;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

@Named
@RequestScoped
public class PaymentBean {

    @EJB
    UserService usrSrv;
    String sender;
    String recipient;
    Double amount;

    public PaymentBean() {
    }

    public void sendPayment() throws IOException {
        sender = usrSrv.getUsername();
        FacesContext context = FacesContext.getCurrentInstance();
        String result = usrSrv.sendPayment(sender, recipient, amount);
        if (result.equals("Success")) {
            context.getExternalContext().redirect("user.xhtml");
        } else {
            context.addMessage(null, new FacesMessage(result));
        }
    }

    public void sendPaymentRequest() throws IOException {
        recipient = usrSrv.getUsername();
        FacesContext context = FacesContext.getCurrentInstance();
        String result = usrSrv.sendPaymentRequest(sender, recipient, amount);
        if (result.equals("Success")) {
            context.getExternalContext().redirect("user.xhtml");
        } else {
            context.addMessage(null, new FacesMessage(result));
        }
    }

    public UserService getUsrSrv() {
        return usrSrv;
    }

    public void setUsrSrv(UserService usrSrv) {
        this.usrSrv = usrSrv;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getRecipient() {
        return recipient;
    }

    public void setRecipient(String recipient) {
        this.recipient = recipient;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

}
