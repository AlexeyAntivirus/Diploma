package com.rx.services;

import com.rx.dao.Discipline;
import com.rx.dao.Document;
import com.rx.dao.User;
import com.rx.dao.repositories.UserRepository;
import com.rx.dto.UserAddingResultDto;
import com.rx.dto.UserUpdatingResultDto;
import com.rx.dto.forms.FullUserFormDto;
import com.rx.dto.forms.UserFormDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private UserRepository userRepository;

    private BCryptPasswordEncoder bCryptPasswordEncoder;


    @Autowired
    public UserService(UserRepository userRepository,
                       BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.userRepository = userRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    public User getUserById(Long id) {
        return userRepository.findOne(id);
    }

    public User getUserByLoginAndPassword(String login, String password) {
        User user = userRepository.findByUsername(login);

        if (!bCryptPasswordEncoder.matches(password, user.getPassword())) {
            user = null;
        }

        return user;
    }

    public Iterable<User> getAllUsers() {
        return userRepository.findAll();
    }

    public UserAddingResultDto addUser(FullUserFormDto fullUserFormDto) {
        String errorMessage = null;
        String errorField = null;
        Long userId = null;

        if (userRepository.existsByUsername(fullUserFormDto.getLogin())) {
            errorField = "login";
            errorMessage = "login.isBusy";
        } else if (userRepository.existsByEmail(fullUserFormDto.getEmail())) {
            errorField = "email";
            errorMessage = "email.isBusy";
        } else {
            User user = User.builder()
                    .withUsername(fullUserFormDto.getLogin())
                    .withPassword(bCryptPasswordEncoder.encode(fullUserFormDto.getPassword()))
                    .withEmail(fullUserFormDto.getEmail())
                    .withLastName(fullUserFormDto.getLastName())
                    .withFirstName(fullUserFormDto.getFirstName())
                    .withMiddleName(fullUserFormDto.getMiddleName())
                    .withUserRole(fullUserFormDto.getUserRole())
                    .build();
            userId = userRepository.save(user).getId();
        }

        return UserAddingResultDto.builder()
                .withErrorField(errorField)
                .withErrorMessage(errorMessage)
                .withUserId(userId)
                .build();
    }

    public UserUpdatingResultDto updateUser(Long id, UserFormDto userFormDto) {
        User user = this.getUserById(id);
        String errorMessage = null;
        String errorField = null;

        if (!userFormDto.getEmail().equals(user.getEmail()) &&
                userRepository.existsByEmail(userFormDto.getEmail())) {
            errorField = "email";
            errorMessage = "email.isBusy";
        } else {
            user.setEmail(userFormDto.getEmail());
            user.setPassword(bCryptPasswordEncoder.encode(userFormDto.getPassword()));
            user.setLastName(userFormDto.getLastName());
            user.setFirstName(userFormDto.getFirstName());
            user.setMiddleName(userFormDto.getMiddleName());
            userRepository.save(user);
        }

        return UserUpdatingResultDto.builder()
                .withErrorField(errorField)
                .withErrorMessage(errorMessage)
                .isUpdated(errorField == null)
                .build();
    }

    public UserUpdatingResultDto updateUserFully(Long id, FullUserFormDto fullUserFormDto) {
        User user = this.getUserById(id);
        String errorMessage = null;

        String errorField = null;
        if (!fullUserFormDto.getEmail().equals(user.getEmail()) &&
                userRepository.existsByEmail(fullUserFormDto.getEmail())) {
            errorField = "email";
            errorMessage = "email.isBusy";
        } else if (!fullUserFormDto.getLogin().equals(user.getUsername()) &&
                userRepository.existsByUsername(fullUserFormDto.getLogin())) {
            errorField = "login";
            errorMessage = "login.isBusy";
        } else {
            user.setUsername(fullUserFormDto.getLogin());
            user.setPassword(bCryptPasswordEncoder.encode(fullUserFormDto.getPassword()));
            user.setEmail(fullUserFormDto.getEmail());
            user.setLastName(fullUserFormDto.getLastName());
            user.setFirstName(fullUserFormDto.getFirstName());
            user.setMiddleName(fullUserFormDto.getMiddleName());
            user.setUserRole(fullUserFormDto.getUserRole());
            userRepository.save(user);
        }

        return UserUpdatingResultDto.builder()
                .withErrorField(errorField)
                .withErrorMessage(errorMessage)
                .isUpdated(errorField == null)
                .build();
    }

    public void updateUserTeachingLoad(User user, Document teachingLoad) {

        user.getTeachingLoads().add(teachingLoad);

        this.userRepository.save(user);
    }

    public void deleteById(Long id) {
        User one = userRepository.findOne(id);
        for (Discipline discipline : one.getDisciplines()) {
            discipline.getUsers().remove(one);
        }
        userRepository.delete(id);
    }

}
