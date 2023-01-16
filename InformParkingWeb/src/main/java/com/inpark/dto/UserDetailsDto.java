package com.inpark.dto;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.*;

public class UserDetailsDto implements UserDetails {
    // 안만들어도 상관없지만 Warning이 발생함
    private static final long serialVersionUID = 1L;

    private String username; // ID
    private String password; // PW
    private String username2;
    private String phone;
    private String carNum;
    private List<GrantedAuthority> authorities;

    public String getUsername2() {
        return username2;
    }

    public void setUsername2(String username2) {
        this.username2 = username2;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getCarNum() {
        return carNum;
    }

    public void setCarNum(String carNum) {
        this.carNum = carNum;
    }


    // setter
    public void setUsername(String username) {
        this.username = username;
    }

    // setter
    public void setPassword(String password) {
        this.password = password;
    }

    // setter
    public void setAuthorities(List<String> authList) {

        List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();

        for (int i = 0; i < authList.size(); i++) {
            authorities.add(new SimpleGrantedAuthority(authList.get(i)));
        }

        this.authorities = authorities;
    }

    @Override
    // ID
    public String getUsername() {

        return username;
    }

    @Override
    // PW
    public String getPassword() {

        return password;
    }

    @Override
    // 권한
    public Collection<? extends GrantedAuthority> getAuthorities() {

        return authorities;
    }

    @Override
    // 계정이 만료 되지 않았는가?
    public boolean isAccountNonExpired() {

        return true;
    }

    @Override
    // 계정이 잠기지 않았는가?
    public boolean isAccountNonLocked() {

        return true;
    }

    @Override
    // 패스워드가 만료되지 않았는가?
    public boolean isCredentialsNonExpired() {

        return true;
    }

    @Override
    // 계정이 활성화 되었는가?
    public boolean isEnabled() {

        return true;
    }
}
