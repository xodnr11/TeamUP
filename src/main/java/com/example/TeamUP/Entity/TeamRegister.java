package com.example.TeamUP.Entity;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
public class TeamRegister {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Team team;

    @ManyToOne
    private UserInfo userInfo;

    @Column(name = "content")
    private String content;
}
