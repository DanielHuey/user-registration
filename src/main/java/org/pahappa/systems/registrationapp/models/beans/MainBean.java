package org.pahappa.systems.registrationapp.models.beans;

import org.pahappa.systems.registrationapp.models.User;
import org.pahappa.systems.registrationapp.services.UserService;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import java.util.List;

@ManagedBean(name = "indexBean")
@SessionScoped
public class MainBean {
    private final UserService userService = new UserService();
    private List<User> users;

    public MainBean() {
        firstLaunch();
        refreshUsers();
    }

    private void firstLaunch() {
        if (userService.getListOfUsers(false).isEmpty()) {
            try {
                FacesContext.getCurrentInstance().getExternalContext().redirect("/pages/admin_setup");
            } catch (Exception ignore) {}
        }
    }

    public List<User> getUsers() {
        log("getting users");
        return users;
    }

    public void refreshUsers() {
        log("refreshing users");
        users = userService.getListOfUsers();
    }

    public void log(String s) {System.out.printf("[log]: %s%n",s);}
}
