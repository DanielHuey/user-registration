package org.pahappa.systems.registrationapp.services;

import org.pahappa.systems.registrationapp.dao.DependantDAO;
import org.pahappa.systems.registrationapp.exception.UsernameException;
import org.pahappa.systems.registrationapp.models.Dependant;

public class DependantService extends UserService{
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

    @Override
    public boolean usernameExists(String username) throws UsernameException {
        return getDependantByUsername(username) != null;
    }
}
