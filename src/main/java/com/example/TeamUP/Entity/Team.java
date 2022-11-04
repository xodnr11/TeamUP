package com.example.TeamUP.Entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@RequiredArgsConstructor
public class Team extends BaseTimeEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

//    @JsonBackReference
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.DETACH)
    @JoinColumn(name = "userInfo")
    private UserInfo userInfo;

    @JsonBackReference
    @OneToMany(mappedBy = "team", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<TeamMember> teamMember = new ArrayList<>();

    @JsonBackReference
    @OneToMany(mappedBy = "team", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Tag> tagList = new ArrayList<>();

    @JsonBackReference
    @OneToMany(mappedBy = "team", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<TeamRegister> teamRegisterList = new ArrayList<>();

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
