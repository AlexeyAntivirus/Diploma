package com.rx.dao.repositories;


import com.rx.dao.User;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface UserRepository extends CrudRepository<User, Long> {
    boolean existsByLogin(String login);
    boolean existsByEmail(String email);
}
