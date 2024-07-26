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

    public List<Dependant> getAllDependants(boolean softList) {
        return this.getAll(softList);
    }
    public List<Dependant> getAllUserDependants(User user) {return getAllUserDependants(user,true);}
    public List<Dependant> getAllUserDependants(User user,boolean softList) {
        List<Dependant> dependants = new ArrayList<>();
        org.hibernate.Transaction tx = null;
        try {
            org.hibernate.Session session = getSessionFactory().openSession();
            tx = session.beginTransaction();
            org.hibernate.Query q = session.createQuery("FROM "+getTable()+" where owner = :owner" + (softList ? " AND deleted = :deleted":""));
            q.setParameter("owner", user);
            if (softList) q.setParameter("deleted", false);
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

    public void deleteDependant(Dependant dependant, boolean softDelete) {
        this.delete(dependant, softDelete);
    }

    public void deleteAllDependants(boolean softDelete) {
        this.deleteAll(softDelete);
    }
}
