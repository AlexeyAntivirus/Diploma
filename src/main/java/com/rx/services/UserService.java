package com.rx.services;

import com.rx.dao.User;
import com.rx.dao.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User getUserById(Long id) {
        return userRepository.findOne(id);
    }

    public User getUserByLoginAndPassword(String login, String password) {
        return userRepository.findByLoginAndPassword(login, password);
    }

    public boolean existsByLogin(String login) {
        return userRepository.existsByLogin(login);
    }

    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    public Iterable<User> getAllUsers() {
        return userRepository.findAll();
    }

    public Long insertOrUpdateUser(User newUser) {
        return userRepository.save(newUser).getId();
    }

    public void deleteById(Long id) {
        userRepository.delete(id);
    }
}
