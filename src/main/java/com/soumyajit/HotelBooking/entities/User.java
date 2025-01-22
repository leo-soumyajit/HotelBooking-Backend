package com.soumyajit.HotelBooking.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.soumyajit.HotelBooking.entities.Enums.Roles;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.apachecommons.CommonsLog;

import java.util.Set;

@Entity
@Getter
@Setter
@Table(name = "app_user")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ElementCollection(fetch = FetchType.EAGER) // to save the roles in a separate db table
    @Enumerated(EnumType.STRING)
    private Set<Roles> roles;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false,unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @OneToMany(mappedBy = "user")
    @JsonIgnore()
    private Set<Guest> guests;
}
