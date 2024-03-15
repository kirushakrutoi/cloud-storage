package ru.kirill.CloudStorage.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "users")
public class User {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "email", unique = true)
    @NotEmpty(message = "The password cannot be empty")
    @Email(message = "Not valid email")
    private String email;

    @Column(name = "username")
    @NotEmpty(message = "The username cannot be empty")
    @Size(min = 2, max = 30, message = "Not valid username")
    private String username;

    @Column(name = "password")
    @NotEmpty(message = "The password cannot be empty")
    private String password;

    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }
}
