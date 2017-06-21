package com.rx.services;


import com.rx.dao.User;
import com.rx.dao.UserRole;
import com.rx.dao.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Set;

@Service
public class SimpleUserDetailsService implements UserDetailsService {

    private UserRepository userRepository;

    @Autowired
    public SimpleUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByLogin(username);

        if (user != null) {
            Set<GrantedAuthority> grantedAuthorities = new HashSet<>();
            for (UserRole role : UserRole.values()) {
                grantedAuthorities.add(new SimpleGrantedAuthority(role.toString()));
            }

            return new org.springframework.security.core.userdetails.User(user.getLogin(), user.getPassword(), grantedAuthorities);
        } else {
            throw new UsernameNotFoundException("Невірний логін або пароль");
        }
    }
}