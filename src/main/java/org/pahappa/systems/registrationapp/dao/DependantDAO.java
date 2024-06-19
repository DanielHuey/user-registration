package org.pahappa.systems.registrationapp.dao;

import org.pahappa.systems.registrationapp.models.Dependant;
import org.pahappa.systems.registrationapp.models.User;

import java.util.ArrayList;
import java.util.List;

public class DependantDAO extends DaoSkeleton {
    public DependantDAO() {
        this.setTable("Dependant");
    }

    public void addDependant(Dependant dependant) {
        super.add(dependant);
    }

    public Dependant getDependantByUsername(String username) {
        return (Dependant) this.getByUsername(username);
    }

    public List<Dependant> getAllDependants() {
        return this.getAll();
    }

    public List<Dependant> getAllUserDependants(User user) {
        List<Dependant> dependants = new ArrayList<>();
        org.hibernate.Transaction tx = null;
        try {
            org.hibernate.Session session = getSessionFactory().openSession();
            tx = session.beginTransaction();
            org.hibernate.Query q = session.createQuery("FROM "+getTable()+" where owner = :owner");
            q.setParameter("owner", user);
            dependants = q.list();
            tx.commit();
            session.close();
        } catch (Exception e) {
            System.out.println(e.getMessage());
            assert tx != null;
            tx.rollback();
        }
        return dependants;
    }

    public void updateDependant(Dependant dependant) {
        this.update(dependant);
    }

    public void deleteDependant(Dependant dependant) {
        this.delete(dependant);
    }

    public void deleteAllDependants() {
        this.deleteAll();
    }
}