package org.pahappa.systems.registrationapp.models.beans;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import java.io.Serializable;

@ManagedBean(name = "Const")
@SessionScoped
public class ConstantsBean implements Serializable {
    public String getEmailRgx() {
        return "^[a-zA-Z0-9_.]*@[a-zA-Z0-9]*\\.[a-z]*$";
    }
    public String getPassRgx() {
        return "";
    }
    public String getUnameRgx() {
        return "^[a-zA-Z0-9][a-zA-Z0-9_.]*$";
    }
    public String getNamesRgx() {return "^[a-zA-Z]*$";}
}
