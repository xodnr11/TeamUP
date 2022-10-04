package com.example.TeamUP.Entity;

import lombok.Data;

import javax.persistence.*;

@Entity
@Data
@Table(name = "teammember")
public class TeamMember {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Team team;

    @ManyToOne
    private UserInfo userInfo;

    @Column(name = "role")
    private String role;
}
