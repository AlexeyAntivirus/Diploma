package com.rx.helpers;


import com.rx.dao.User;
import com.rx.dao.UserRole;
import com.rx.dao.repositories.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class ProdDataAccessObjectCommandLineRunner implements CommandLineRunner {

    private final UserRepository userRepository;

    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public ProdDataAccessObjectCommandLineRunner(UserRepository userRepository, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.userRepository = userRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }


    @Override
    public void run(String... strings) throws Exception {
        User user = User.builder()
                .withUsername("administrator")
                .withEmail("blabla@gmail.com")
                .withPassword(bCryptPasswordEncoder.encode("14ph38th2796a"))
                .withLastName("Admin")
                .withFirstName("Admin")
                .withMiddleName("Admin")
                .withUserRole(UserRole.ADMINISTRATOR)
                .build();
        if (!userRepository.existsByUsername(user.getUsername())) {
            userRepository.save(user);
        }
    }
}
