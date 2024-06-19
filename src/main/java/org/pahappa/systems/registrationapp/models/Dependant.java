package org.pahappa.systems.registrationapp.models;

import org.pahappa.systems.registrationapp.models.enums.Gender;

import javax.persistence.*;
import java.text.DateFormat;
import java.text.FieldPosition;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

@Entity
public class Dependant extends UserSkeleton {
    @Id
    @Column(name = "user_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String username;
    private String firstname;
    private String lastname;
    private Date dateOfBirth;
    private Gender gender;
    @ManyToOne
    private User owner;

    @Override
    public Date getDateOfBirth() {
        return dateOfBirth;
    }

    @Override
    public void setDateOfBirth(Date dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    @Override
    public String getLastname() {
        return lastname;
    }

    @Override
    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    @Override
    public String getFirstname() {
        return firstname;
    }

    @Override
    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public long getId() {
        return id;
    }

    @Override
    public void setId(long id) {
        this.id = id;
    }

    public User getOwner() {
        return owner;
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }

    public Dependant() {}

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Dependant dependant = (Dependant) o;
        return gender == dependant.gender && getId() == dependant.getId() && Objects.equals(getUsername(), dependant.getUsername());
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), gender);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        sb.append("Dependant{name:").append(getFirstname()).append(" ").append(getLastname())
                .append(",username:").append(username)
                .append(",date_of_birth:").append(df.format(getDateOfBirth()))
                .append(",gender:").append(getGender()).append("}");
        return sb.toString();
    }
}
