package com.rx.services;


import com.rx.dao.User;
import com.rx.dao.repositories.UserRepository;
import com.rx.helpers.AuthenticatedUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


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
        User user = userRepository.findByUsername(username);

        if (user != null) {
            return AuthenticatedUser.user(user).build();
        } else {
            throw new UsernameNotFoundException("Невірний логін або пароль");
        }
    }
}