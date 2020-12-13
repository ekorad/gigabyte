package com.gigabyte.application.services;

import java.util.stream.Collectors;

import com.gigabyte.application.models.UserPermission;
import com.gigabyte.application.models.WebUser;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class WebUserDetailsService implements UserDetailsService {

    @Autowired
    private WebUserService userService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        WebUser user = userService.getUserByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException("User with username \"" + username + "\" could not be found");
        }
        return User.builder().username(user.getUsername())
            .password(user.getPassword())
            .authorities(user.getRole().getPermissions().stream()
                .map(UserPermission::getName).map(SimpleGrantedAuthority::new).collect(Collectors.toList()))
            .build();
    }
    
}
