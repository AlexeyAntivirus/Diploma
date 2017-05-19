package com.rx.dao.repositories;


import com.rx.dao.Discipline;
import org.springframework.data.repository.CrudRepository;

import java.util.Set;

public interface DisciplineRepository extends CrudRepository<Discipline, Long> {
    boolean existsByName(String name);
}
