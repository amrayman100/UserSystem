package com.sumerge.program.dal;
import com.sumerge.program.exceptions.NotFoundException;
import com.sumerge.program.exceptions.WrongFormatException;
import entities.Group;
import entities.User;
import com.sumerge.program.viewmodels.GroupModel;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import java.util.ArrayList;
import java.util.List;

public class GroupRepo {
    public EntityManager entityManager = JPAUtil.getEntityManagerFactory().createEntityManager();
    static private AuditLogRepo AuditLogger = new AuditLogRepo();
    public List<Group> getAll(){


        try{
            entityManager.getTransaction().begin();
            TypedQuery<Group> query =
                    entityManager.createNamedQuery("Group.findAll", entities.Group.class);
            List<Group> results = query.getResultList();
            entityManager.getTransaction().commit();
            return results;

        }
        catch(Exception e){
            entityManager.getTransaction().rollback();
            e.getMessage();
            List<Group> empty = new ArrayList<Group>();
            return empty;
        }

    }

    public Object findByName(String groupName , boolean admin){
        Object u = null;
        try{
            entityManager.getTransaction().begin();
            Query query = entityManager.createQuery("SELECT u FROM Group u WHERE u.name=:groupname").setParameter("groupname",groupName);
            u = (Group) query.getSingleResult();
            entityManager.getTransaction().commit();
            return u;

        }
        catch(Exception e){
            entityManager.getTransaction().rollback();
            e.getMessage();

            return u;
        }

    }

    public void addGroup (GroupModel g , String username) throws WrongFormatException {
        if(g.getName().isEmpty()&&g.getDescription().isEmpty()){
            throw new WrongFormatException("Wrong Format");
        }
        Group newGroup = new Group(g.getName(),g.getDescription());
        entityManager = JPAUtil.getEntityManagerFactory().createEntityManager();
        EntityTransaction entityTransaction = entityManager.getTransaction();
        entityTransaction.begin();
        entityManager.persist(newGroup);
        AuditLogger.addLog(newGroup,username,"Add Group");
        entityManager.getTransaction().commit();

    }

    public void deleteGroup(int id , String username){
        entityManager = JPAUtil.getEntityManagerFactory().createEntityManager();
        entityManager.getTransaction().begin();
        Group em = entityManager.find(Group.class, id);
        AuditLogger.addLog(em,username,"Delete Group");
        entityManager.remove(em);
        entityManager.getTransaction().commit();
    }

    public void updateGroupName(int id , String newName , String username) throws NotFoundException {
        try {
            entityManager.getTransaction().begin();
            Group em = entityManager.find(Group.class, id);
            if (em == null) {
                throw new NotFoundException("Group can not be found");
            }
            AuditLogger.addLog(em, username, "Update Name");
            em.setName(newName);
            entityManager.getTransaction().commit();
        } catch (Exception e) {
            entityManager.getTransaction().rollback();

            e.getMessage();

        }

    }

    public void updateGroupDesc(int id , String newDesc , String username) throws NotFoundException {

        try {
            entityManager.getTransaction().begin();
            Group em = entityManager.find(Group.class, id);
            if(em==null){
                throw new NotFoundException("Group can not be found");
            }
            AuditLogger.addLog(em,username,"Update Desc");
            em.setDesc(newDesc);
            entityManager.getTransaction().commit();

        } catch (Exception e) {
            entityManager.getTransaction().rollback();

            e.getMessage();

        }

    }

}
