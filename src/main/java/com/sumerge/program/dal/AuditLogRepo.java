package com.sumerge.program.dal;

import entities.AuditLog;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.persistence.*;
import java.sql.Timestamp;

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
