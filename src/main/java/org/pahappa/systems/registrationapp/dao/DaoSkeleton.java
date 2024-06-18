package org.pahappa.systems.registrationapp.dao;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.pahappa.systems.registrationapp.config.SessionConfiguration;

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
    
    public void add(Object obj) {
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

    public Object getByUsername(String username) {
        Object object = null;
        try {
            Session session = sessionFactory.openSession();
            Query q = session.createQuery("FROM "+getTable()+" WHERE username = :username");
            q.setParameter("username", username);
            object = q.uniqueResult();
            session.close();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return object;
    }

    public <T> List<T> getAll() {
        List<T> objects = null;
        try {
            Session session = sessionFactory.openSession();
            objects = session.createQuery("FROM "+getTable()).list();
            session.close();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return objects;
    }

    public void update(Object object) {
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

    public void delete(Object object) {
        Transaction tx = null;
        try {
            Session session = sessionFactory.openSession();
            tx = session.beginTransaction();
            session.delete(object);
            tx.commit();
            session.close();
        } catch (Exception e) {
            rollback(tx,e);
        }
    }

    public void deleteAll() {
        Transaction tx = null;
        try {
            Session session = sessionFactory.openSession();
            tx = session.beginTransaction();
            session.createQuery("DELETE * FROM "+getTable());
            tx.commit();
            session.close();
        } catch (Exception e) {
            rollback(tx,e);
        }
    }
}
