package com.codecool.peermentoringbackend.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.validator.routines.EmailValidator;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class UserEntity {

    @Id
    @GeneratedValue
    private Long id;

    @Pattern(regexp="^[A-Za-z]*$",message = "Invalid Input")
    @Size(min = 2, max = 20, message = "First name can't be longer than 20 characters")
    @Column
    private String firstName;

    @Pattern(regexp="^[A-Za-z]*$",message = "Invalid Input")
    @Size(min = 2, max = 20, message = "Last name can't be longer than 20 characters")
    @Column
    private String lastName;

    @Size(min = 2, max = 20, message = "username can't be longer than 20 characters")
    @Column(unique=true)
    private String username;

    @Column( nullable = false)
    @Email
    @NotBlank
    private String email;

    @Size(min = 8, max = 20, message = "Password must be between 8 and 20 characters")
    @Column
    private String password;

    @Column
    private LocalDateTime registrationDate;

    @ElementCollection(fetch = FetchType.EAGER)
    @Builder.Default
    private List<String> roles = new ArrayList<>();
}
