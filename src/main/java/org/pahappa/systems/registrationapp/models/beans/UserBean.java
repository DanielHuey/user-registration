package org.pahappa.systems.registrationapp.models.beans;

import org.pahappa.systems.registrationapp.models.User;
import org.pahappa.systems.registrationapp.services.UserService;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import java.io.Serializable;
import java.util.Date;

@ManagedBean(name = "userBean")
@SessionScoped
public class UserBean implements Serializable {
    private String username;
    private String firstname;
    private String lastname;
    private Date dateOfBirth;
    private UserService userv;

    public UserBean() {
        userv = new UserService();
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

    public void register() {
        User user = new User();
        user.setUsername(username);
        user.setFirstname(firstname);
        user.setLastname(lastname);
        user.setDateOfBirth(dateOfBirth);
        FacesContext ctx = FacesContext.getCurrentInstance();
        if (container(() -> userv.registerUser(user))) {
            ctx.addMessage(null, new FacesMessage("Successful Registration"));
            container(() -> ctx.getExternalContext().redirect("/pages/index")); //an external context can redirect
        }
    }

    public String delete(String username) {
        container(() -> userv.deleteUser(username));
        return "";
    }

    public void edit(String username) {
        if (container(() -> userv.validateUsername(username))) {
            final User[] user = new User[1];
            final Date[] date = new Date[1];
            container(() -> user[0] = userv.getUserByUsername(username));
            container(() -> date[0] = userv.validateDateOfBirth(user[0].getDateOfBirth()));
            String url = "user/view.xhtml?u=" + username + "&f=" + user[0].getFirstname() +
                    "&l=" + user[0].getLastname() +
                    "&d=" + date[0];
            try {
                FacesContext.getCurrentInstance().getExternalContext().redirect(url);
            } catch (Exception ignored) {}
        }
    }

    @FunctionalInterface
    interface Ex {void run() throws Exception;}
    boolean container(Ex ex) {
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


