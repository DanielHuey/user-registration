package org.pahappa.systems.registrationapp.models;

import org.pahappa.systems.registrationapp.models.enums.Gender;

import java.util.Objects;

public class Dependant extends User {
    Gender gender;

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
        return gender == dependant.gender;
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
