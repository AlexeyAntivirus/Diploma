package com.rx.services;

import com.rx.dao.Discipline;
import com.rx.dao.repositories.DisciplineRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

@Service
public class DisciplineService {

    private DisciplineRepository disciplineRepository;

    @Autowired
    public DisciplineService(DisciplineRepository disciplineRepository) {
        this.disciplineRepository = disciplineRepository;
    }

    public Discipline getDisciplineById(Long id) {
        return disciplineRepository.findOne(id);
    }

    public boolean existsByName(String name) {
        return disciplineRepository.existsByName(name);
    }

    public void deleteById(Long id) {
        disciplineRepository.delete(id);
    }

    public Iterable<Discipline> getAllDisciplines() {
        return disciplineRepository.findAll();
    }

    public Long insertOrUpdateDiscipline(Discipline discipline) {
        return disciplineRepository.save(discipline).getId();
    }
}
