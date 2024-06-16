package org.pahappa.systems.registrationapp.dao;

import org.hibernate.Transaction;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.pahappa.systems.registrationapp.config.SessionConfiguration;
import org.pahappa.systems.registrationapp.models.User;

public class UserDAO {
    private final SessionFactory sessionFactory;

    public UserDAO() {
        this.sessionFactory = SessionConfiguration.getSessionFactory();
    }

    //add
    //get (1, all)
    //update
    //delete (1, all)

    public void addUser(User user) {
        Transaction tx = null;
        try {
            Session session = sessionFactory.openSession();
            tx = session.beginTransaction();
            session.save(user);
            tx.commit();
            session.close();
        } catch (Exception e) {
            rollback(tx,e);
        }
    }

    public User getUserByUsername(String username) {
        User user = null;
        try {
            Session session = sessionFactory.openSession();
            Query q = session.createQuery("FROM User WHERE username = :username");
            q.setParameter("username", username);
            user = (User) q.uniqueResult();
            session.close();
        } catch (Exception e) {
            System.out.println(e);
        }
        return user;
    }

    public List<User> getAllUsers() {
        List users = null;
        try {
            Session session = sessionFactory.openSession();
            users = session.createQuery("FROM User").list();
            session.close();
        } catch (Exception e) {
            System.out.println(e);
        }
        return users;
    }
    
    public void updateUser(User user) {
        Transaction tx = null;
        try {
            Session session = sessionFactory.openSession();
            tx = session.beginTransaction();
            session.saveOrUpdate(user);
            tx.commit();
            session.close();
        } catch (Exception e) {
            rollback(tx,e);
        }
    }

    public void deleteUser(User user) {
        Transaction tx = null;
        try {
            Session session = sessionFactory.openSession();
            tx = session.beginTransaction();
            session.delete(user);
            tx.commit();
            session.close();
        } catch (Exception e) {
            rollback(tx,e);
        }
    }

    public void deleteAllUsers() {
        Transaction tx = null;
        try {
            Session session = sessionFactory.openSession();
            tx = session.beginTransaction();
            session.createQuery("DELETE * FROM User");
            tx.commit();
            session.close();
        } catch (Exception e) {
            rollback(tx,e);
        }
    }

    private void rollback(Transaction tx, Exception e) {
        assert tx != null;
        tx.rollback();
        System.out.println(e.getMessage());
    }

}