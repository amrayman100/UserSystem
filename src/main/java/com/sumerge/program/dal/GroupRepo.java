package com.sumerge.program.dal;
import entities.Group;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import java.util.List;

public class GroupRepo {
    EntityManager entityManager = JPAUtil.getEntityManagerFactory().createEntityManager();

    public List<Group> getAll(){

            entityManager.getTransaction().begin();
            TypedQuery<Group> query =
                    entityManager.createNamedQuery("Group.findAll", entities.Group.class);
            List<entities.Group> results = query.getResultList();
            entityManager.getTransaction().commit();
            return results;

    }

    public Group findByName(String groupname , boolean admin){
        entityManager.getTransaction().begin();
        Query query = entityManager.createQuery("SELECT u FROM Group u WHERE u.name=:groupname").setParameter("groupname",groupname);
        Group u = (Group) query.getSingleResult();
        entityManager.getTransaction().commit();
        return u;

    }

    public void addGroup (Group g)  {
        entityManager = JPAUtil.getEntityManagerFactory().createEntityManager();
        EntityTransaction entityTransaction = entityManager.getTransaction();
        entityTransaction.begin();
        entityManager.persist(g);
        entityManager.getTransaction().commit();

    }

    public void deleteGroup(int id){
        entityManager = JPAUtil.getEntityManagerFactory().createEntityManager();
        entityManager.getTransaction().begin();
        Group em = entityManager.find(Group.class, id);
        entityManager.remove(em);
        entityManager.getTransaction().commit();
    }

    public void updateGroupName(int id , String newname){
        entityManager.getTransaction().begin();
        Group em = entityManager.find(Group.class, id);
        em.setName(newname);
        entityManager.getTransaction().commit();

    }

    public void updateGroupDesc(int id , String newdesc){
        entityManager.getTransaction().begin();
        Group em = entityManager.find(Group.class, id);
        em.setDesc(newdesc);
        entityManager.getTransaction().commit();

    }

}
