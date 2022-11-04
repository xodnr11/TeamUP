package com.example.TeamUP.Entity;

import lombok.Builder;
import lombok.Data;

import javax.persistence.*;

@Data
@Entity
public class TeamRegister extends BaseTimeEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(cascade = CascadeType.REMOVE)
    private Team team;

    @ManyToOne(cascade = CascadeType.DETACH)
    private UserInfo userInfo;

    @Column(name = "content")
    private String content;

    @Builder
    public TeamRegister(Long id, Team team, UserInfo userInfo, String content) {
        this.id = id;
        this.team = team;
        this.userInfo = userInfo;
        this.content = content;
    }

    public TeamRegister() {

    }
}
