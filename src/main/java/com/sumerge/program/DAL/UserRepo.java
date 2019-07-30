package com.sumerge.program.DAL;
import Entities.User;
import Entities.Group;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.Principal;
import java.util.List;
import javax.annotation.security.DeclareRoles;
import javax.annotation.security.RolesAllowed;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.SecurityContext;


public class UserRepo {
    EntityManager entityManager = JPAUtil.getEntityManagerFactory().createEntityManager();
    public List<User> getAll(boolean admin){

        if(admin){
            entityManager.getTransaction().begin();
            TypedQuery<User> query =
                    entityManager.createNamedQuery("User.findAll", Entities.User.class);
            List<Entities.User> results = query.getResultList();
            entityManager.getTransaction().commit();

            return results;
        }
        else {
            entityManager.getTransaction().begin();
            TypedQuery<User> query =
                    entityManager.createNamedQuery("User.findallBasic", Entities.User.class);
            List<Entities.User> results = query.getResultList();
            entityManager.getTransaction().commit();

            return results;
        }

    }


    public User findByUsername(String username , boolean admin){
        if(admin){
            entityManager.getTransaction().begin();
            Query query = entityManager.createQuery("SELECT u FROM User u WHERE u.username=:username").setParameter("username",username);
            User u = (User)query.getSingleResult();
            entityManager.getTransaction().commit();
            return u;
        }
        else{
            entityManager.getTransaction().begin();
            Query query = entityManager.createQuery("SELECT u.name , u.username , u.email , u.phonenum FROM User u WHERE u.username=:username and u.del = false").setParameter("username",username);
            User u = (User)query.getSingleResult();
            entityManager.getTransaction().commit();
            return u;
        }


    }

    public  String sha256(String input) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        MessageDigest md5 = MessageDigest.getInstance("SHA-256");
        byte[] digest = md5.digest(input.getBytes("UTF-8"));
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < digest.length; ++i) {
            sb.append(Integer.toHexString((digest[i] & 0xFF) | 0x100).substring(1, 3));
        }
        return sb.toString();
    }

    public void addUser (User u) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        entityManager = JPAUtil.getEntityManagerFactory().createEntityManager();
        EntityTransaction entityTransaction = entityManager.getTransaction();
        entityTransaction.begin();
        u.setPassword(sha256(u.getPassword()));
        entityManager.persist(u);
        entityManager.getTransaction().commit();


    }

    public void deleteUser(String username){
        entityManager.getTransaction().begin();
        Query query = entityManager.createQuery("SELECT u FROM User u WHERE u.username=:username").setParameter("username",username);
        User u = (User)query.getSingleResult();
        u.setDel(true);
        entityManager.getTransaction().commit();

    }

    public void undoDeleteUser(String username){
        entityManager.getTransaction().begin();
        Query query = entityManager.createQuery("SELECT u FROM User u WHERE u.username=:username").setParameter("username",username);
        User u = (User)query.getSingleResult();
        u.setDel(false);
        entityManager.getTransaction().commit();


    }

    public void updateUserName(String username,String newusername){

        entityManager.getTransaction().begin();
        Query query = entityManager.createQuery("SELECT u FROM User u WHERE u.username=:username").setParameter("username",username);
        User u = (User)query.getSingleResult();
        u.setUsername(newusername);
        u.setUsername(newusername);
        entityManager.getTransaction().commit();

    }

    public void updateUserEmail(String username , String newemail){
        entityManager.getTransaction().begin();
        Query query = entityManager.createQuery("SELECT u FROM User u WHERE u.username=:username").setParameter("username",username);
        User u = (User)query.getSingleResult();
        u.setEmail(newemail);
        entityManager.getTransaction().commit();

    }

    public void updateUserPhone(String username , String newphone){
        entityManager.getTransaction().begin();
        Query query = entityManager.createQuery("SELECT u FROM User u WHERE u.username=:username").setParameter("username",username);
        User u = (User)query.getSingleResult();
        u.setPhonenum(newphone);
        entityManager.getTransaction().commit();

    }

    public void updateUserPassword (String username , String newpass)throws NoSuchAlgorithmException, UnsupportedEncodingException{
        entityManager.getTransaction().begin();
        Query query = entityManager.createQuery("SELECT u FROM User u WHERE u.username=:username").setParameter("username",username);
        User u = (User)query.getSingleResult();
        u.setPassword(sha256(newpass));
        entityManager.getTransaction().commit();

    }

    public void addtoGroup(String username , int groupid){
        entityManager.getTransaction().begin();
        Query query = entityManager.createQuery("SELECT u FROM User u WHERE u.username=:username").setParameter("username",username);
        User u = (User)query.getSingleResult();
        Group group = entityManager.find(Group.class, groupid);
        List<Group> usergroups = u.getGroups();
        usergroups.add(group);
        entityManager.getTransaction().commit();

    }

    public void removefromGroup(String username , int groupid){
        entityManager.getTransaction().begin();
        Query query = entityManager.createQuery("SELECT u FROM User u WHERE u.username=:username").setParameter("username",username);
        User u = (User)query.getSingleResult();
        Group group = entityManager.find(Group.class, groupid);
        List<Group> usergroups = u.getGroups();
        if(usergroups.contains(group))  usergroups.remove(group);
        entityManager.getTransaction().commit();

    }







}
