package com.example.TeamUP.Entity;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
public class TeamRegister {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(cascade = CascadeType.REMOVE)
    private Team team;

    @ManyToOne(cascade = CascadeType.REMOVE)
    private UserInfo userInfo;

    @Column(name = "content")
    private String content;
}
