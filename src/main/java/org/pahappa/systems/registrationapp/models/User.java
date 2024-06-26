package org.pahappa.systems.registrationapp.models;

import org.pahappa.systems.registrationapp.models.enums.Role;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import javax.persistence.*;

@Entity
public class User extends UserSkeleton {
    @Id
    @Column(name = "user_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String username;
    private String firstname;
    private String lastname;
    private Date dateOfBirth;
    private String email;
    private String password;
    private Role role;
    private boolean deleted;
    private Date deletedAt;
    @OneToMany(mappedBy = "owner")
    private List<Dependant> dependants;

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public User(){
        setRole(Role.Default);
        setDeleted(false);
    }

    @SuppressWarnings("unused")
    private User(long id, String username, String firstname, String lastname, Date dateOfBirth, Role role){
        setId(id);
        setUsername(username);
        setFirstname(firstname);
        setLastname(lastname);
        setDateOfBirth(dateOfBirth);
        setRole(role);
        setDeleted(false);
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
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

    public String getDateOfBirth() {
        return new SimpleDateFormat("dd/MM/yyyy").format(dateOfBirth);
    }

    public void setDateOfBirth(Date dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public List<Dependant> getDependants() {
        return dependants;
    }

    public void setDependants(List<Dependant> dependants) {
        this.dependants = dependants;
    }

    public void addDependant(Dependant dependant) {
        dependant.setOwner(this);
        this.dependants.add(dependant);
    }

    public void addDependants(List<Dependant> dependants) {
        for (Dependant dependant: dependants) addDependant(dependant);
    }

    public void removeDependant(Dependant dependant) {
        this.dependants.remove(dependant);
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public boolean isDeleted() {
        return deleted;
    }

    @Override
    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    @Override
    public Date getDeletedAt() {
        return deletedAt;
    }

    @Override
    public void setDeletedAt() {
        super.setDeletedAt();
    }

    @Override
    public String getEmail() {
        return email;
    }

    @Override
    public void setEmail(String email) {
        this.email = email;
    }

    public boolean passwordEquals(String otherPassword) {
        return password.equals(otherPassword);
    }

    @Override
    public String toString() {
        return username;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(username, user.username) &&
                Objects.equals(firstname, user.firstname) &&
                Objects.equals(lastname, user.lastname) &&
                Objects.equals(dateOfBirth, user.dateOfBirth) &&
                Objects.equals(dependants, user.dependants);
    }

    @Override
    public int hashCode() {
        return Objects.hash(username, firstname, lastname, dateOfBirth);
    }

}
