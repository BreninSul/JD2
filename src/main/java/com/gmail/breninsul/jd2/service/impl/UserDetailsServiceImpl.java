package com.gmail.breninsul.jd2.service.impl;

import com.gmail.breninsul.jd2.pojo.User;
import com.gmail.breninsul.jd2.service.UserService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Log4j2
@Service
@Qualifier("custom")
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UserService userService;


    @Override
    public UserDetails loadUserByUsername(String name) throws UsernameNotFoundException {
        User user = userService.getUserByName(name);
        if (user == null) {
            throw new  UsernameNotFoundException("There is no user with name = "+name);
        }
        Set<GrantedAuthority> authorities = new HashSet();
        log.info("Checking user " + user.toString() + " authorities " + authorities);
        authorities.add(new SimpleGrantedAuthority(user.getRole()));
        UserDetails userDetails =
                new org.springframework.security.core.userdetails.User(user.getName(),
                        user.getPass(),user.isEnabled(),true,true,true,
                        authorities);
        return userDetails;
    }

    public enum UserRoleEnum {
        ROLE_ADMIN,
        ROLE_USER,
        ROLE_ANONYMOUS;

        UserRoleEnum() {
        }

    }
}

