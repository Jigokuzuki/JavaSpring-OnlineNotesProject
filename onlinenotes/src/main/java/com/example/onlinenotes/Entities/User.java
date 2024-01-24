package com.example.onlinenotes.Entities;

import jakarta.annotation.Nonnull;
import jakarta.persistence.*;

@Entity
@Table(name = "Users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int Id;

    public void setId(int Id) {
        this.Id = Id;
    }

    public int getId() {
        return Id;
    }

    @Column(name = "FirstName")
    @Nonnull
    private String FirstName;

    public void setFirstName(String FirstName) {
        this.FirstName = FirstName;
    }

    public String getFirstName() {
        return FirstName;
    }

    @Column(name = "Surname")
    @Nonnull
    private String Surname;

    public void setSurname(String Surname) {
        this.Surname = Surname;
    }

    public String getSurname() {
        return Surname;
    }

    @Column(name = "Email")
    @Nonnull
    private String Email;

    public void setEmail(String Email) {
        this.Email = Email;
    }

    public String getEmail() {
        return Email;
    }

    @Column(name = "Password")
    @Nonnull
    private String Password;

    public void setPassword(String Password) {
        this.Password = Password;
    }

    public String getPassword() {
        return Password;
    }

}
