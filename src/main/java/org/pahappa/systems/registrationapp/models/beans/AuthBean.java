package org.pahappa.systems.registrationapp.models.beans;

import org.pahappa.systems.registrationapp.models.User;
import org.pahappa.systems.registrationapp.models.enums.Role;
import org.pahappa.systems.registrationapp.services.UserService;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import java.io.Serializable;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import static org.pahappa.systems.registrationapp.models.beans.MainBean.*;

@ManagedBean(name = "authBean")
@SessionScoped
public class AuthBean implements Serializable {
    private String identity;
    private String password;
    private final UserService userService;
    private static User sessionUser;

    public static User getSessionUser() {
        return sessionUser;
    }

    public void setSessionUser(User user) {
        sessionUser = user;
    }

    public AuthBean() {
        userService = new UserService();
    }

    public static byte[] shaHash(String input) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("SHA-512");
        return md.digest(input.getBytes(StandardCharsets.UTF_8));
    }

    public static String hexHashString(String input) {
        try {
            BigInteger num = new BigInteger(1,shaHash(input));
            return num.toString(16);
        } catch (Exception ignored) {}
        return null;
    }

    public String getIdentity() {
        return identity;
    }

    public void setIdentity(String identity) {
        this.identity = identity;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void login() {
        container(() -> {
            boolean isEmail;
            if (identity.matches("^[a-zA-Z0-9_.]*@[a-z]*\\.[a-z]*$"))
                isEmail = true;
            else if (identity.matches("^[a-zA-Z0-9][a-zA-Z0-9_.]*$"))
                isEmail = false;
            else throw new Exception("The provided identity doesn't match a Username or Email");
            authenticate(identity,password,isEmail);
        });
    }

    private void authenticate(String identity, String password, boolean isEmail) throws Exception {
        User user;
        if (isEmail)
            user = userService.getUserByEmail(identity);
        else
            user = userService.getUserByUsername(identity);
        if (user==null) throw new Exception("There is no user with the provided "+ (isEmail?"email":"username"));
        password = hexHashString(password);
        if (user.passwordEquals(password)) {
            setSessionUser(user);
            getHomePage();
        } else throw new Exception("The password provided is Incorrect. Try Again");
    }

    private static void getHomePage() {
        container(() -> {
            if (isCurrentUserAdmin())
                redirect("/pages/admin/dashboard");
            else {
                new UserBean().setupSettings();
                redirect("/pages/user/settings");
            }
        });
    }

    public void logout() {
        setSessionUser(null);
        container(() -> redirect("/pages/login"));
    }

    public void restrictToAuthenticated() {
        if (getSessionUser() == null)
            container(() -> redirect("/pages/login"));
    }

    public static void goHomeIfAuthenticated() {
        if (getSessionUser() != null)
            getHomePage();
    }

    public static boolean isCurrentUserAdmin() {
        return getSessionUser().getRole() == Role.Admin;
    }
}
