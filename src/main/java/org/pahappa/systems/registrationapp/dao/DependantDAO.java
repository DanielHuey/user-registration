package org.pahappa.systems.registrationapp.dao;

import org.pahappa.systems.registrationapp.models.Dependant;
import org.pahappa.systems.registrationapp.models.User;

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
        List<Dependant> dependants = null;
        for (Object o: this.getAll()) {
            dependants.add((Dependant) o);
        }
        return dependants;
    }

    public List<Dependant> getAllUserDependants(User user) {
        List<Dependant> dependants = null;
        for (Object o: this.getAll()) {
            dependants.add((Dependant) o);
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
