package com.example.TeamUP.Entity;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@Getter
@Setter
public class UserInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToMany(mappedBy = "userInfo",fetch = FetchType.LAZY)
    private List<Team> team;

    @Column(unique = true, nullable = false)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column
    private char gender;

    @Column
    private String nickname;

    @Column
    private String email;

    @Column
    private String name;

    @Column
    private String phone;

    @Column
    @DateTimeFormat(pattern = "yyyy-MM-dd")     //Date 형식 지정
    private Date birthday;

    @Column(nullable = false)
    @Enumerated(value = EnumType.STRING)
    private Role role;


    @Column
    private String refreshtoken;

    @Builder
    public UserInfo(Long id, List<Team> team, String username, String password, char gender, String nickname, String email, String name, String phone, Date birthday, Role role, String refreshtoken) {
        this.id = id;
        this.team = team;
        this.username = username;
        this.password = password;
        this.gender = gender;
        this.nickname = nickname;
        this.email = email;
        this.name = name;
        this.phone = phone;
        this.birthday = birthday;
        this.role = role;
        this.refreshtoken = refreshtoken;
    }

    public UserInfo() {
    }
}