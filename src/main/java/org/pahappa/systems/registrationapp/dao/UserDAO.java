package org.pahappa.systems.registrationapp.dao;

import org.hibernate.Transaction;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.pahappa.systems.registrationapp.models.Dependant;
import org.pahappa.systems.registrationapp.models.User;

public class UserDAO extends DaoSkeleton {


    public UserDAO() {
        this.setTable("User");
    }

    public void addUser(User user) {
        super.add(user);
    }

    public User getUserByUsername(String username) {
        return (User) this.getByUsername(username);
    }

    public List<User> getAllUsers() {
        return this.getAll();
    }
    
    public void updateUser(User user) {
        this.update(user);
    }

    public void deleteUser(User user) {
        this.delete(user);
    }

    public void deleteAllUsers() {
        this.deleteAll();
    }


    public void addDependants(User user, List<Dependant> dependants) {
        org.hibernate.Transaction tx = null;
        try {
            org.hibernate.Session session = getSessionFactory().openSession();
            tx = session.beginTransaction();
            user.addDependants(dependants);
            session.saveOrUpdate(user);
            tx.commit();
            session.close();
        } catch (Exception e) {
            assert tx != null;
            this.rollback(tx,e);
        }
    }
}