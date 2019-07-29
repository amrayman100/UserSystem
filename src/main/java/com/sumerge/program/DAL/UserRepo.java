package com.sumerge.program.DAL;
import Entities.User;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import java.util.List;

public class UserRepo {
    EntityManager entityManager = JPAUtil.getEntityManagerFactory().createEntityManager();

    public List<User> getAll(){
        entityManager.getTransaction().begin();
        TypedQuery<User> query =
                entityManager.createNamedQuery("User.findAll", Entities.User.class);
        List<Entities.User> results = query.getResultList();
        entityManager.getTransaction().commit();
        entityManager.close();
        return results;
    }

    public User findByUsername(String username){
        Query query = entityManager.createQuery("SELECT u FROM User u WHERE u.username=:username").setParameter("username",username);
        User u = (User)query.getSingleResult();
        return u;

    }

    public void addUser(User u){
        entityManager = JPAUtil.getEntityManagerFactory().createEntityManager();
        EntityTransaction entityTransaction = entityManager.getTransaction();
        entityTransaction.begin();
        entityManager.persist(u);
        entityManager.getTransaction().commit();
        entityManager.close();

    }

    


}
