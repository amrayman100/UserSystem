package com.sumerge.program.DAL;

import Entities.AuditLog;
import Entities.User;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.Principal;
import java.sql.Timestamp;
import java.util.List;
import javax.annotation.security.DeclareRoles;
import javax.annotation.security.RolesAllowed;
import javax.transaction.Transactional;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.SecurityContext;
public class AuditLogRepo {
    EntityManager entityManager = JPAUtil.getEntityManagerFactory().createEntityManager();

    public void addLog (Object o , String author , String name ) {
        ObjectMapper om = new ObjectMapper();
        AuditLog log = new AuditLog();

        try {
            log.setEntity(om.writeValueAsString(o));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        log.setAuthor(author);
        log.setName(name);
        log.setLogtime(new Timestamp(System.currentTimeMillis()));
        EntityTransaction entityTransaction = entityManager.getTransaction();
        entityTransaction.begin();
        entityManager.persist(log);
        entityManager.getTransaction().commit();


    }
}
