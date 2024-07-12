package org.pahappa.systems.registrationapp.validators;

import org.pahappa.systems.registrationapp.services.ServiceSkeleton;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;

public class Name extends ServiceSkeleton implements Validator<String> {

    @Override
    public void validate(FacesContext facesContext, UIComponent uiComponent, String name) throws ValidatorException {
        try {
            validateName(name);
        } catch (Exception e) {
            FacesMessage msg = new FacesMessage(e.getMessage());
            msg.setSeverity(FacesMessage.SEVERITY_ERROR);
            throw new ValidatorException(msg);
        }
    }
}
