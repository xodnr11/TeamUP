package com.example.TeamUP.Entity;

import lombok.Data;

import javax.persistence.*;

@Entity
@Data
public class TeamMember {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(cascade = CascadeType.REMOVE)
    private Team team;

    @ManyToOne(cascade = CascadeType.REMOVE)
    private UserInfo userInfo;

    @Column(name = "role")
    private String role;
}
