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

import static org.pahappa.systems.registrationapp.models.beans.AuthBean.*;
import static org.pahappa.systems.registrationapp.models.beans.MainBean.*;

@ManagedBean(name = "userBean")
@SessionScoped
public class UserBean implements Serializable {
    private String username;
    private String firstname;
    private String lastname;
    private Date dateOfBirth;
    private String email;
    private String password;
    private Role role;
    private boolean deleted;
    private Date deletedAt;
    private final UserService userService;
    private static User userToEdit;

    public UserBean() {
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

    public Date getDeletedAt() {
        return deletedAt;
    }

    public void setDeletedAt(Date deletedAt) {
        this.deletedAt = deletedAt;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public User getUserToEdit() {
        return userToEdit;
    }

    public void setUserToEdit(User user) {
        userToEdit = user;
    }

    public void register() {
        User user = new User();
        user.setUsername(username);
        user.setFirstname(firstname);
        user.setLastname(lastname);
        user.setDateOfBirth(dateOfBirth);
        user.setEmail(email);
        user.setPassword(hexHashString(password));
        FacesContext ctx = FacesContext.getCurrentInstance();
        if (container(() -> userService.registerUser(user))) {
            ctx.addMessage(null, new FacesMessage("Successful Registration"));
            container(() -> redirect("/pages/admin/dashboard")); //an external context can redirect
        }
    }

    public void delete(String username) {
        container(() -> userService.deleteUser(username));

        try {
            redirect("");
        }
        catch (Exception ignore) {}
    }

    public void edit(String username) {
        if (container(() -> userService.validateUsername(username))) {
            container(() -> {
                setUserToEdit(userService.getUserByUsername(username));
                redirect("/pages/user/settings");
            });
        }
    }

    public void saveDetails() {
        container(() -> {
            userToEdit.setUsername(username);
            userToEdit.setFirstname(firstname);
            userToEdit.setLastname(lastname);
            userToEdit.setDateOfBirth(dateOfBirth);
            userToEdit.setEmail(email);
            userToEdit.setPassword(hexHashString(password));
//            userService.u
        });
    }
}


