package org.pahappa.systems.registrationapp.services;

import org.pahappa.systems.registrationapp.dao.DependantDAO;
import org.pahappa.systems.registrationapp.exception.UsernameException;
import org.pahappa.systems.registrationapp.models.Dependant;
import org.pahappa.systems.registrationapp.models.User;

import java.util.List;

public class DependantService extends ServiceSkeleton {
    private final DependantDAO dependantDAO;
    public DependantService() {
        dependantDAO = new DependantDAO();
    }

    public void registerDependant(Dependant dependant) {
        dependantDAO.addDependant(dependant);
    }

    public Dependant getDependantByUsername(String username) throws UsernameException {
        validateUsername(username);
        return dependantDAO.getDependantByUsername(username);
    }

    public List<Dependant> getDependantsOfUser(User user) { return dependantDAO.getAllUserDependants(user); }

    public boolean usernameExists(String username) throws UsernameException {
        return getDependantByUsername(username) != null;
    }

    public void deleteDependant(Dependant dependant) {
        deleteDependant(dependant, true);
    }
    public void deleteDependant(Dependant dependant, boolean softDelete) {
        dependantDAO.deleteDependant(dependant, softDelete);
    }
}
