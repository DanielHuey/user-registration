package org.pahappa.systems.registrationapp.models.beans;

import org.pahappa.systems.registrationapp.models.User;
import org.pahappa.systems.registrationapp.models.enums.Role;
import org.pahappa.systems.registrationapp.services.UserService;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import java.io.Serializable;
import java.util.Date;

import static org.pahappa.systems.registrationapp.models.beans.MainBean.*;
import static org.pahappa.systems.registrationapp.models.beans.AuthBean.*;

@ManagedBean(name = "adminBean")
@SessionScoped
public class AdminBean implements Serializable {
    private String username;
    private String firstname;
    private String lastname;
    private Date dateOfBirth;
    private String email;
    private String password;
    private Role role;
    private boolean deleted;
    private final UserService userService;

    public AdminBean() {
        userService = new UserService();
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public Date getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(Date dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    public void register() {
        User admin = new User();
        admin.setUsername(username);
        admin.setFirstname(firstname);
        admin.setLastname(lastname);
        admin.setEmail(email);
        admin.setPassword(password);
        admin.setDateOfBirth(dateOfBirth);
        admin.setRole(Role.Admin);
        FacesContext ctx = FacesContext.getCurrentInstance();
        if (container(() -> userService.registerUser(admin))) {
            ctx.addMessage(null, new FacesMessage("Successful Administrator Registration"));
            AuthBean ab = new AuthBean();
            ab.setSessionUser(admin);
            container(() -> redirect("/pages/admin/dashboard")); //an external context can redirect
        }
    }

    public void restrictToAdmin() {
        if (getSessionUser().getRole() != Role.Admin)
            container(()-> redirect("/pages/user/view"));
    }
}


