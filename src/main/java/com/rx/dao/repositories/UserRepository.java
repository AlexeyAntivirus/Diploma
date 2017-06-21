package com.rx.dao.repositories;


import com.rx.dao.User;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<User, Long> {
    User findByUsername(String username);

    boolean existsByUsername(String username);
    boolean existsByEmail(String email);

}
