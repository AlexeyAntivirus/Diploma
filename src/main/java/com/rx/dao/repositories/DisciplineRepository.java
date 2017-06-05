package com.rx.dao.repositories;


import com.rx.dao.Discipline;
import com.rx.dao.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;


public interface DisciplineRepository extends JpaRepository<Discipline, Long> {
    boolean existsByName(String name);

    @Modifying
    @Transactional
    @Query("UPDATE Discipline d SET d.name = :name, d.users = :users WHERE d.id = :id")
    int updateDiscipline(@Param("id") Long id,
                         @Param("name") String name,
                         @Param("users") Set<User> users);
}
