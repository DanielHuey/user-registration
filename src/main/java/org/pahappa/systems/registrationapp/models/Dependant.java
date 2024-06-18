package org.pahappa.systems.registrationapp.models;

import org.pahappa.systems.registrationapp.models.enums.Gender;

import javax.persistence.*;
import java.util.Objects;

@Entity
public class Dependant extends UserSkeleton {
    @Id
    @Column(name = "dependant_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private Gender gender;
    @ManyToOne
    private User owner;

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
        return "Dependant{" +
                "gender=" + gender +
                ",username=" + getUsername() +
                '}';
    }
}
