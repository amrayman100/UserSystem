package com.sumerge.program.dal;
import com.sumerge.program.exceptions.WrongFormatException;
import entities.User;
import entities.Group;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import javax.transaction.Transactional;
import com.sumerge.program.viewmodels.UserModel;
import com.fasterxml.jackson.databind.exc.UnrecognizedPropertyException;
import com.sumerge.program.exceptions.NotFoundException;
import org.apache.log4j.Logger;


public class UserRepo {


    public EntityManager entityManager = JPAUtil.getEntityManagerFactory().createEntityManager();
    static private AuditLogRepo AuditLogger = new AuditLogRepo();
    private static final Logger LOGGER = Logger.getLogger(UserRepo.class.getName());

    @Transactional(rollbackOn = Exception.class)
    public List<User> getAll(boolean admin){
        LOGGER.debug("Getting user list.");

        if(admin){
            try{
                entityManager.getTransaction().begin();
                TypedQuery<User> query =
                        entityManager.createNamedQuery("User.findAll", entities.User.class);
                List<entities.User> results = query.getResultList();
                entityManager.getTransaction().commit();
                return results;
            }
            catch(Exception e){
                entityManager.getTransaction().rollback();
                e.getMessage();
                List<User> empty = new ArrayList<User>();
                return empty;
            }

        }
        else {
            try{
                entityManager.getTransaction().begin();
                TypedQuery<User> query =
                        entityManager.createNamedQuery("User.findallBasic", entities.User.class);
                List<entities.User> results = query.getResultList();
                entityManager.getTransaction().commit();

                return results;

            }
            catch(Exception e){
                entityManager.getTransaction().rollback();
                e.getMessage();
                List<User> empty = new ArrayList<User>();
                return empty;
            }
        }

    }

    @Transactional
    public Object findByUsername(String username , boolean admin) throws NotFoundException {
        LOGGER.debug("Finding by username");

        Object u = null;
        if(admin){
            try{
                entityManager.getTransaction().begin();
                Query query = entityManager.createQuery("SELECT u FROM User u WHERE u.username=:username").setParameter("username",username);
                u = (User)query.getSingleResult();
                entityManager.getTransaction().commit();
                return u;
            }
            catch(Exception e){
                entityManager.getTransaction().rollback();
                e.getMessage();
                LOGGER.debug("User not found");
                return u;
            }


        }
        else{
            try{
                entityManager.getTransaction().begin();
                Query query = entityManager.createQuery("SELECT u.name , u.username , u.email , u.phonenum FROM User u WHERE u.username=:username and u.del = false").setParameter("username",username);
                u = query.getSingleResult();
                //UserModel f = (UserModel) u;
                entityManager.getTransaction().commit();
                return u;
            }
            catch(Exception e){
                entityManager.getTransaction().rollback();

                e.getMessage();
                LOGGER.debug("User not found");
                return u;
            }

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

    public void addUser (UserModel u , String username) throws NotFoundException, WrongFormatException, UnsupportedEncodingException, NoSuchAlgorithmException {
        LOGGER.debug("Add User");
            User o = null;
            entityManager.getTransaction().begin();

           if(u.getEmail().equalsIgnoreCase("")||u.getName().equalsIgnoreCase("")||u.getNumber().equalsIgnoreCase("")||u.getPassword().equalsIgnoreCase("")||u.getRole().equalsIgnoreCase("")||u.getUsername().equalsIgnoreCase("")){
                throw new WrongFormatException("Missing Parameter");
            }

            try{
              Query query = entityManager.createQuery("SELECT u FROM User u WHERE u.username=:username").setParameter("username",u.getUsername());
              o = (User)query.getSingleResult();

            }
            catch(Exception e){
                LOGGER.debug("User with this username already exists");
            }

            if(o!=null){
                if (o.getUsername().equalsIgnoreCase(u.getUsername())) {
                    entityManager.getTransaction().rollback();
                    throw new WrongFormatException("A User with this username exists");
                }
            }

            User newUser = new User(u.getName(),u.getUsername(),u.getPassword(),u.getRole(),u.getEmail(),u.getNumber());
            newUser.setPassword(sha256(newUser.getPassword()));
            entityManager.persist(newUser);
            AuditLogger.addLog(newUser,username,"Add User");
            entityManager.getTransaction().commit();

    }
    @Transactional(rollbackOn = Exception.class)
    public void deleteUser(String username) throws NotFoundException {
        LOGGER.debug("Delete User");
        User u = null;
        try{
            entityManager.getTransaction().begin();
            Query query = entityManager.createQuery("SELECT u FROM User u WHERE u.username=:username").setParameter("username",username);
            u = (User)query.getSingleResult();
            AuditLogger.addLog(u,username,"Delete User");
            u.setDel(true);
            entityManager.getTransaction().commit();
        }
        catch(Exception e){
            entityManager.getTransaction().rollback();

            e.getMessage();

            if(u==null){
                LOGGER.debug("User not found");
                throw new NotFoundException("User with the specified username can not be found");
            }
        }

    }
    @Transactional(rollbackOn = Exception.class)
    public void undoDeleteUser(String username) throws NotFoundException {
        LOGGER.debug("Undo Delete User");
        User u = null;
        try{
            entityManager.getTransaction().begin();
            Query query = entityManager.createQuery("SELECT u FROM User u WHERE u.username=:username").setParameter("username",username);
            u = (User)query.getSingleResult();
            AuditLogger.addLog(u,username,"Undo Delete User");
            u.setDel(false);
            entityManager.getTransaction().commit();

        }
        catch(Exception e){
            entityManager.getTransaction().rollback();

            e.getMessage();

            if(u==null){
                LOGGER.debug("User not found");
                throw new NotFoundException("User with the specified username can not be found");
            }

        }


    }
    @Transactional(rollbackOn = Exception.class)
    public void updateUserName(String username,String newusername , String author) throws NotFoundException {
        LOGGER.debug("Update Username");
        User u = null;
        try{
            entityManager.getTransaction().begin();
            Query query = entityManager.createQuery("SELECT u FROM User u WHERE u.username=:username").setParameter("username",username);
            u = (User)query.getSingleResult();
            u.setUsername(newusername);
            u.setUsername(newusername);
            AuditLogger.addLog(u,author,"Update User name");
            entityManager.getTransaction().commit();

        }
        catch(Exception e){
            entityManager.getTransaction().rollback();

            e.getMessage();
            if(u==null){
                throw new NotFoundException("User with the specified username can not be found");
            }


        }



    }
    @Transactional(rollbackOn = Exception.class)
    public void updateUserEmail(String username , String newemail , String author) throws NotFoundException {
        User u = null;
        try{
            entityManager.getTransaction().begin();
            Query query = entityManager.createQuery("SELECT u FROM User u WHERE u.username=:username").setParameter("username",username);
            u = (User)query.getSingleResult();
            u.setEmail(newemail);
            AuditLogger.addLog(u,author,"Update Email");
            entityManager.getTransaction().commit();
        }
        catch(Exception e){
            entityManager.getTransaction().rollback();
            e.getMessage();
            if(u==null){
                LOGGER.debug("User not found");
                throw new NotFoundException("User with the specified username can not be found");
            }

        }



    }
    @Transactional(rollbackOn = Exception.class)
    public void updateUserPhone(String username , String newphone , String author) throws NotFoundException {
        User u = null;
        try{
            entityManager.getTransaction().begin();
            Query query = entityManager.createQuery("SELECT u FROM User u WHERE u.username=:username").setParameter("username",username);
            u = (User)query.getSingleResult();
            u.setPhonenum(newphone);
            AuditLogger.addLog(u,author,"Update Phone number");
            entityManager.getTransaction().commit();
        }
        catch(Exception e){
            entityManager.getTransaction().rollback();
            if(u==null){
                LOGGER.debug("User not found");
                throw new NotFoundException("User with the specified username can not be found");
            }

        }

    }
    @Transactional(rollbackOn = Exception.class)
    public void updateUserPassword (String username , String newpass , String author) throws NoSuchAlgorithmException, UnsupportedEncodingException, NotFoundException {
        User u = null;
        try{
            entityManager.getTransaction().begin();
            Query query = entityManager.createQuery("SELECT u FROM User u WHERE u.username=:username").setParameter("username",username);
            u = (User)query.getSingleResult();
            u.setPassword(sha256(newpass));
            AuditLogger.addLog(u,author,"Update Password");
            entityManager.getTransaction().commit();
        }
        catch(Exception e){
            entityManager.getTransaction().rollback();
            if(u==null){
                LOGGER.debug("User not found");
                throw new NotFoundException("User with the specified username can not be found");
            }


        }

    }
    @Transactional(rollbackOn = Exception.class)
    public void addtoGroup(String username , int groupid , String author) throws NotFoundException {
        /*User u = null;
        try{
            entityManager.getTransaction().begin();
            Query query = entityManager.createQuery("SELECT u FROM User u WHERE u.username=:username").setParameter("username",username);
            u = (User)query.getSingleResult();
            Group group = entityManager.find(Group.class, groupid);
            List<Group> usergroups = u.getGroups();
            usergroups.add(group);
            AuditLogger.addLog(u,author,"Added to group");
            entityManager.getTransaction().commit();
        }
        catch(Exception e){
            entityManager.getTransaction().rollback();
            if(u==null){
                LOGGER.debug("User not found");
                throw new NotFoundException("User with the specified username can not be found");
            }


        }*/

        entityManager.getTransaction().begin();
        Query query = entityManager.createQuery("SELECT u FROM User u WHERE u.username=:username").setParameter("username",username);
        User u = (User)query.getSingleResult();
        Group group = entityManager.find(Group.class, groupid);
        List<Group> usergroups = u.getGroups();
        usergroups.add(group);
        AuditLogger.addLog(u,author,"Added to group");
        entityManager.getTransaction().commit();


    }
    @Transactional(rollbackOn = Exception.class)
    public void removefromGroup(String username , int groupid , String author) throws NotFoundException {
        User u = null;

        try{
            entityManager.getTransaction().begin();
            Query query = entityManager.createQuery("SELECT u FROM User u WHERE u.username=:username").setParameter("username",username);
             u = (User)query.getSingleResult();
            Group group = entityManager.find(Group.class, groupid);
            List<Group> usergroups = u.getGroups();
            if(usergroups.contains(group))  usergroups.remove(group);
            AuditLogger.addLog(u,author,"Remove from group");
            entityManager.getTransaction().commit();
        }
        catch(Exception e){
            entityManager.getTransaction().rollback();
            if(u==null){

                throw new NotFoundException("User with the specified username can not be found");
            }


        }


    }



}
