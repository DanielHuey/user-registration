package org.pahappa.systems.registrationapp.beans;

import org.pahappa.systems.registrationapp.models.User;
import org.pahappa.systems.registrationapp.models.enums.Role;
import org.pahappa.systems.registrationapp.services.UserService;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import java.io.Serializable;
import java.util.Date;

import static org.pahappa.systems.registrationapp.beans.MainBean.*;
import static org.pahappa.systems.registrationapp.beans.AuthBean.*;
import static org.pahappa.systems.registrationapp.beans.UserBean.getUsers;

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

    private User makeAdmin() {
        User admin = new User();
        admin.setUsername(username);
        admin.setFirstname(firstname);
        admin.setLastname(lastname);
        admin.setEmail(email);
        admin.setPassword(AuthBean.hexHashString(password));
        admin.setDateOfBirth(dateOfBirth);
        admin.setRole(Role.Admin);
        return admin;
    }
    public void register() {
        FacesContext ctx = FacesContext.getCurrentInstance();
        container(() -> {
            userService.registerUser(makeAdmin());
            ctx.addMessage(null, new FacesMessage("Successful Administrator Registration"));
        });
    }
    public void register(final boolean autoLogin) {
        FacesContext ctx = FacesContext.getCurrentInstance();
        container(() -> {
            User admin = makeAdmin();
            userService.registerUser(admin);
            ctx.addMessage(null, new FacesMessage("Successful Administrator Registration"));
            if (autoLogin) {
                new AuthBean().setSessionUser(admin);
                redirect("/pages/admin/dashboard");
            } else {
                redirect("/pages/login");
            }
        });
    }

    public void restrictToAdmin() {
        if (getSessionUser().getRole() != Role.Admin)
            container(()-> redirect("/pages/user/settings"));
    }

    public void restrictIfUsersExist() {
        if (!getUsers().isEmpty()) restrictToAdmin();
    }
}


