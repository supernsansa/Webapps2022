package com.webapps2022.jsf;

import com.webapps2022.ejb.UserService;
import java.io.IOException;
import java.io.Serializable;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

@Named
@RequestScoped
public class LoginBean implements Serializable {

    @EJB
    UserService usrSrv;

    public void logout() {
        FacesContext context = FacesContext.getCurrentInstance();
        HttpServletRequest request = (HttpServletRequest) context.getExternalContext().getRequest();
        try {
            //this method will disassociate the principal from the session (effectively logging him/her out)
            //Then, the user will be redirected to the index if logout is successful
            request.logout();
            context.getExternalContext().redirect("/Webapps2022/");
        } catch (ServletException e) {
            context.addMessage(null, new FacesMessage("Logout failed."));
        } catch (IOException ex) {
            Logger.getLogger(LoginBean.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public String getActiveUsername() {
        return usrSrv.getUsername();
    }

    public Double getActiveBalance() {
        return usrSrv.getBalance();
    }
}
