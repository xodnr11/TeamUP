package com.example.TeamUP.Entity;

import lombok.Builder;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.util.Date;

@Entity
@Data
public class UserInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String username;

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

    @Builder
    public UserInfo(String username, char gender, String nickname, String email, String name, String phone, Date birthday, Role role) {
        this.username = username;
        this.gender = gender;
        this.nickname = nickname;
        this.email = email;
        this.name = name;
        this.phone = phone;
        this.birthday = birthday;
        this.role = role;
    }

    public UserInfo() {
    }

    /*
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Set<GrantedAuthority> roles = new HashSet<>();
        for (String role : authority.split("_")) {
            roles.add(new SimpleGrantedAuthority(role));
        }
        return roles;
    }

    @Override
    public String getPassword() {
        return null;
    }

    @Override
    public String getUsername() {
        return null;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    } //유저 아이디가 만료 되었는지

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }  //유저 아이디가 lock걸렸는지

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }  //비밀번호가 만료 되었는지

    @Override
    public boolean isEnabled() {
        return true;
    }  //계정이 활성화 되었는지*/
}
