/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.webapps2022.ejb;

import com.webapps2022.entity.SystemUser;
import java.util.List;
import javax.annotation.Resource;
import javax.ejb.EJBContext;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import static javax.ejb.TransactionAttributeType.REQUIRED;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Stateless
@TransactionAttribute(REQUIRED)
public class AdminService {

    @PersistenceContext
    EntityManager em;
    @Resource
    EJBContext ejbContext;

    public AdminService() {
    }

    public List<SystemUser> getAllUsers() {
        List result = em.createNamedQuery("SystemUser.findAllUsers")
                .getResultList();
        return result;
    }

    public EntityManager getEm() {
        return em;
    }

    public void setEm(EntityManager em) {
        this.em = em;
    }

    public EJBContext getEjbContext() {
        return ejbContext;
    }

    public void setEjbContext(EJBContext ejbContext) {
        this.ejbContext = ejbContext;
    }
}
