package com.rx.services;

import com.rx.dao.Discipline;
import com.rx.dao.Document;
import com.rx.dao.User;
import com.rx.dao.repositories.UserRepository;
import com.rx.dto.UserAddingResultDto;
import com.rx.dto.UserUpdatingResultDto;
import com.rx.dto.forms.AddUserFormDto;
import com.rx.dto.forms.FullUserFormDto;
import com.rx.dto.forms.UserFormDto;
import com.rx.helpers.AuthenticatedUser;
import com.rx.helpers.FileStorageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private UserRepository userRepository;

    private SimpleUserDetailsService simpleUserDetailsService;

    private FileStorageHelper fileStorageHelper;

    private BCryptPasswordEncoder bCryptPasswordEncoder;


    @Autowired
    public UserService(UserRepository userRepository,
                       BCryptPasswordEncoder bCryptPasswordEncoder,
                       FileStorageHelper fileStorageHelper,
                       SimpleUserDetailsService simpleUserDetailsService) {
        this.userRepository = userRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.simpleUserDetailsService = simpleUserDetailsService;
        this.fileStorageHelper = fileStorageHelper;
    }

    public User getUserById(Long id) {
        return userRepository.findOne(id);
    }

    public Iterable<User> getAllUsers() {
        return userRepository.findAll();
    }

    public UserAddingResultDto addUser(AddUserFormDto addUserFormDto) {
        String errorMessage = null;
        String errorField = null;
        Long userId = null;

        if (userRepository.existsByUsername(addUserFormDto.getUsername())) {
            errorField = "username";
            errorMessage = "username.isBusy";
        } else if (userRepository.existsByEmail(addUserFormDto.getEmail())) {
            errorField = "email";
            errorMessage = "email.isBusy";
        } else {
            User user = User.builder()
                    .withUsername(addUserFormDto.getUsername())
                    .withPassword(bCryptPasswordEncoder.encode(addUserFormDto.getPassword()))
                    .withEmail(addUserFormDto.getEmail())
                    .withLastName(addUserFormDto.getLastName())
                    .withFirstName(addUserFormDto.getFirstName())
                    .withMiddleName(addUserFormDto.getMiddleName())
                    .withUserRole(addUserFormDto.getUserRole())
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
            user.setPassword(!userFormDto.getCurrentPassword().isEmpty() ? bCryptPasswordEncoder.encode(userFormDto.getNewPassword()) : user.getPassword());
            user.setLastName(userFormDto.getLastName());
            user.setFirstName(userFormDto.getFirstName());
            user.setMiddleName(userFormDto.getMiddleName());
            User save = userRepository.save(user);
            UserDetails userDetails = simpleUserDetailsService.loadUserByUsername(save.getUsername());

            Authentication authentication = new UsernamePasswordAuthenticationToken(
                    userDetails, userDetails.getPassword(), userDetails.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(authentication);
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
        } else if (!fullUserFormDto.getUsername().equals(user.getUsername()) &&
                userRepository.existsByUsername(fullUserFormDto.getUsername())) {
            errorField = "username";
            errorMessage = "username.isBusy";
        } else {
            user.setUsername(fullUserFormDto.getUsername());
            user.setPassword(!fullUserFormDto.getPassword().isEmpty() ? bCryptPasswordEncoder.encode(fullUserFormDto.getPassword()) : user.getPassword());
            user.setEmail(fullUserFormDto.getEmail());
            user.setLastName(fullUserFormDto.getLastName());
            user.setFirstName(fullUserFormDto.getFirstName());
            user.setMiddleName(fullUserFormDto.getMiddleName());
            user.setUserRole(fullUserFormDto.getUserRole());

            User save = userRepository.save(user);
            Long userId = ((AuthenticatedUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getId();
            if (save.getId().equals(userId)) {
                UserDetails userDetails = simpleUserDetailsService.loadUserByUsername(save.getUsername());

                Authentication authentication = new UsernamePasswordAuthenticationToken(
                        userDetails, userDetails.getPassword(), userDetails.getAuthorities());
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
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

        for (Document document: one.getTeachingLoads()) {
            fileStorageHelper.deleteFile(document);
        }

        userRepository.delete(id);
    }

}
