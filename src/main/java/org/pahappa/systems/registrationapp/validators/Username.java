package org.pahappa.systems.registrationapp.validators;

import org.pahappa.systems.registrationapp.services.UserService;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;

public class Username implements Validator{

    @Override
    public void validate(FacesContext facesContext, UIComponent uiComponent, Object o) throws ValidatorException {
        try {
            if (new UserService().usernameExists((String) o)) throw new Exception("This username already exists in the system");
        } catch (Exception e) {
            FacesMessage msg = new FacesMessage(e.getMessage());
            msg.setSeverity(FacesMessage.SEVERITY_ERROR);
            throw new ValidatorException(msg);
        }
    }
}
