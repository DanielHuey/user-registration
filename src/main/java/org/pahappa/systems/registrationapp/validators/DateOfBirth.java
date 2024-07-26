package org.pahappa.systems.registrationapp.validators;

import org.pahappa.systems.registrationapp.services.ServiceSkeleton;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;
import java.util.Date;

public class DateOfBirth extends ServiceSkeleton implements Validator<Date> {

    @Override
    public void validate(FacesContext facesContext, UIComponent uiComponent, Date date) throws ValidatorException {
        try {
            validateDateOfBirth(date);
        } catch (Exception e) {
            FacesMessage msg = new FacesMessage(e.getMessage());
            msg.setSeverity(FacesMessage.SEVERITY_ERROR);
            throw new ValidatorException(msg);
        }
    }
}
