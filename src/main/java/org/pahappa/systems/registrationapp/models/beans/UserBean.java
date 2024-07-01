package org.pahappa.systems.registrationapp.models.beans;

import org.pahappa.systems.registrationapp.models.Dependant;
import org.pahappa.systems.registrationapp.models.User;
import org.pahappa.systems.registrationapp.models.enums.Gender;
import org.pahappa.systems.registrationapp.models.enums.Role;
import org.pahappa.systems.registrationapp.services.DependantService;
import org.pahappa.systems.registrationapp.services.UserService;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import static org.pahappa.systems.registrationapp.models.beans.AuthBean.*;
import static org.pahappa.systems.registrationapp.models.beans.DependantBean.loadDependants;
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
    private static final UserService userService = new UserService();
    private static final DependantService dependantService = new DependantService();
    private static User userToEdit;
    private String oldUsername = "";
    private String oldPassword = "";
    private String oldUserPassword;

    public UserBean() {
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

    public static User getUserToEdit() {
        return userToEdit;
    }

    public void setUserToEdit(User user) {
        DependantBean.loadDependants(user);
        userToEdit = user;
    }

    public static List<User> getUsers() {
        List<User> users = userService.getListOfUsers(false);
        for (User u:users) loadDependants(u);
        return users;
    }

    public void loadSelfDependants() {
        loadDependants(getSessionUser());
    }

    public void addDependantToSelf() {

    }

    public List<Dependant> filterDependantsOfSelf(Gender gender) {
        List<Dependant> filters = new ArrayList<>();
        loadSelfDependants();
        for (Dependant d:getSessionUser().getDependants()) {
            if (d.getGender() == gender) {
                filters.add(d);
            }
        }
        return filters;
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
        container(() -> {
            userService.deleteUser(username);
            refreshUsers();
            redirect("/pages/admin/dashboard");
        });
    }
    public static void deleteAll() {
        userService.deleteAllUsers();
    }

    public void edit(String username) {
        resetValues();
        if (container(() -> userService.validateUsername(username))) {
            container(() -> {
                setUserToEdit(userService.getUserByUsername(username));
                oldUsername = getUserToEdit().getUsername();
                redirect("/pages/user/settings");
            });
        }
    }
    public void addDependantPage(String username) {
        container(() -> {
            DependantBean.setDependantOwner(userService.getUserByUsername(username));
            redirect("/pages/dependant/add");
        });
    }

    public void setupSettings() {
        container(() -> {
            oldUsername = getSessionUser().getUsername();
            oldUserPassword = getSessionUser().getPassword();
            setUserToEdit(getSessionUser());
            router("settings");
            redirect("/pages/user/settings");
        });
    }
    public void adminOrSelf(String username) {
        if (!(isCurrentUserAdmin() && getSessionUser().getUsername().equals(username))) {
            setUserToEdit(getSessionUser());
            goHomeIfAuthenticated();
        }
    }

    public void saveDetails() {
        container(() -> {
            if (isCurrentUserAdmin() || Objects.equals(hexHashString(oldPassword), oldUserPassword)){
                if (!(username==null||username.isEmpty())) userToEdit.setUsername(userService.validateUsername(username));
                if (!(firstname==null||firstname.isEmpty())) userToEdit.setFirstname(userService.validateName(firstname));
                if (!(lastname==null||lastname.isEmpty())) userToEdit.setLastname(userService.validateName(lastname));
                if (dateOfBirth != null) userToEdit.setDateOfBirth(userService.validateDateOfBirth(dateOfBirth));
                if (!(email==null||email.isEmpty())) userToEdit.setEmail(email);
                if (!(password==null||password.isEmpty())) userToEdit.setPassword(hexHashString(password));
                userService.updateDetailsOfUser(oldUsername,userToEdit);
            }
        });
    }
     public void resetValues() {
        setUsername(null);
        setFirstname(null);
        setLastname(null);
        setEmail(null);
        setDateOfBirth(null);
        setPassword(null);
     }

    public String getOldPassword() {
        return oldPassword;
    }

    public void setOldPassword(String oldPassword) {
        this.oldPassword = oldPassword;
    }
}


