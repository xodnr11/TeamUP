package com.example.TeamUP.Entity;

import lombok.Data;

import javax.persistence.*;

@Entity
@Data
public class UserInfo {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String oauth2id;

    @Column
    private String gender;

    @Column
    private String nickname;

    @Column
    private String email;

    @Column
    private String name;

    @Column
    private String phone;

    @Column
    private String birthday;

    @Column(nullable = false)
    @Enumerated(value = EnumType.STRING)
    private Role role;

    public UserInfo(String oauth2id, String gender, String nickname, String email, String name, String phone, String birthday, Role role) {
        this.oauth2id = oauth2id;
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
