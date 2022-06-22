package com.example.TeamUP.Entity;

import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Entity
@Data
public class UserInfo implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

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
    private Date birthday;

    @Column
    private String authority;


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
    }  //계정이 활성화 되었는지
}
