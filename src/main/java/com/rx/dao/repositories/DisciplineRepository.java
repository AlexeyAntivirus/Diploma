package com.rx.dao.repositories;


import com.rx.dao.Discipline;
import org.springframework.data.repository.CrudRepository;


public interface DisciplineRepository extends CrudRepository<Discipline, Long> {
    boolean existsByName(String name);
}
