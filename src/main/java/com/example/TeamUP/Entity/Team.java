package com.example.TeamUP.Entity;

import lombok.Data;

import javax.persistence.*;

@Entity
@Data
public class Team extends BaseTimeEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne(cascade = CascadeType.REMOVE)
    private UserInfo userInfo;

    @Column(name = "title")
    private String title;

    @Column(name = "content")
    private String content;

    @Column(name = "category")
    private String category;

    @Column(name = "team_name")
    private String teamName;

    @Column(name = "team_present")
    private String teamPresent;

    @Column(name = "current_member")
    private int currentMember;

    @Column(name = "max_member")
    private int maxMember;
}
