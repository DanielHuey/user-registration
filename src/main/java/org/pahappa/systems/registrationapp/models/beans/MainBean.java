package org.pahappa.systems.registrationapp.models.beans;

import org.pahappa.systems.registrationapp.models.User;
import org.pahappa.systems.registrationapp.services.UserService;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import java.io.IOException;
import java.io.Serializable;
import java.util.List;

@ManagedBean(name = "indexBean")
@SessionScoped
public class MainBean implements Serializable {
    private final UserService userService = new UserService();
    private List<User> users;

    public MainBean() {
        firstLaunch();
        refreshUsers();
    }

    private void firstLaunch() {
        // is it the first launch?
        if (userService.getListOfUsers(false).isEmpty())
            container(() -> redirect("/pages/admin/admin_setup"));
        // else go to log in
        else container(() -> redirect("/pages/login"));
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

    public static void redirect(String location) throws IOException {
        FacesContext.getCurrentInstance().getExternalContext().redirect("/registration-app"+location+".xhtml");
    }
    @FunctionalInterface
    interface Ex {void run() throws Exception;}
    public static boolean container(Ex ex) {
        try {
            ex.run();
            return true;
        } catch (Exception e) {
            FacesMessage msg = new FacesMessage(e.getMessage());
            msg.setSeverity(FacesMessage.SEVERITY_ERROR);
            FacesContext.getCurrentInstance().addMessage(null,msg);
            return false;
        }
    }
}
