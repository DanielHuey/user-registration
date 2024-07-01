package org.pahappa.systems.registrationapp.dao;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.pahappa.systems.registrationapp.config.SessionConfiguration;
import org.pahappa.systems.registrationapp.models.UserSkeleton;
import org.pahappa.systems.registrationapp.models.enums.Role;

import java.util.List;

public abstract class DaoSkeleton {
    protected String table;
    private final SessionFactory sessionFactory;

    protected DaoSkeleton() {
        this.sessionFactory = SessionConfiguration.getSessionFactory();
    }

    protected String getTable() {
        return table;
    }

    protected void setTable(String table) {
        this.table = table.strip();
    }

    protected SessionFactory getSessionFactory() {
        return sessionFactory;
    }
    
    protected void rollback(Transaction tx, Exception e) {
        assert tx != null;
        tx.rollback();
        System.out.println(e.getMessage());
    }
    
    public void add(UserSkeleton obj) {
        Transaction tx = null;
        try {
            Session session = sessionFactory.openSession();
            tx = session.beginTransaction();
            session.save(obj);
            tx.commit();
            session.close();
        } catch (Exception e) {
            rollback(tx,e);
        }
    }

    public UserSkeleton getByUsername(String username) {
        UserSkeleton object = null;
        try {
            Session session = sessionFactory.openSession();
            Query q = session.createQuery("FROM "+getTable()+" WHERE username = :username");
            q.setParameter("username", username);
            object = (UserSkeleton) q.uniqueResult();
            session.close();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return object;
    }

    /** @param softList True: A list of users with the deleted flag set to false. False: All users in the table */
    public <T> List<T> getAll(boolean softList) {
        List<T> objects = null;
        try {
            Session session = sessionFactory.openSession();
            Query query = session.createQuery("FROM "+ getTable() + (softList?" WHERE deleted = :deleted":""));
            if (softList) query.setParameter("deleted",false);
            objects = query.list();
            session.close();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return objects;
    }

    public void update(UserSkeleton object) {
        Transaction tx = null;
        try {
            Session session = sessionFactory.openSession();
            tx = session.beginTransaction();
            session.saveOrUpdate(object);
            tx.commit();
            session.close();
        } catch (Exception e) {
            rollback(tx,e);
        }
    }

    public void delete(UserSkeleton object, boolean softDelete) {
        Transaction tx = null;
        try {
            Session session = sessionFactory.openSession();
            tx = session.beginTransaction();
            if (softDelete) {
                object.setDeleted(true);
                object.setDeletedAt();
                session.saveOrUpdate(object);
            }
            else session.delete(object);
            tx.commit();
            session.close();
        } catch (Exception e) {
            rollback(tx,e);
        }
    }

    public void deleteAll(boolean softDelete) {
        Transaction tx = null;
        try {
            Session session = sessionFactory.openSession();
            tx = session.beginTransaction();
            session.createQuery("DELETE * FROM "+getTable()+" WHERE role = :role").setParameter("role", Role.Default).executeUpdate();
            tx.commit();
            session.close();
        } catch (Exception e) {
            rollback(tx,e);
        }
    }

    public UserSkeleton getByEmail(String email) {
        UserSkeleton object = null;
        try {
            Session session = sessionFactory.openSession();
            Query q = session.createQuery("FROM "+getTable()+" WHERE email = :email");
            q.setParameter("email", email);
            object = (UserSkeleton) q.uniqueResult();
            session.close();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return object;
    }
}
