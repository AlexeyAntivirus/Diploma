package com.rx.services;

import com.rx.dao.Discipline;
import com.rx.dao.Document;
import com.rx.dao.DocumentType;
import com.rx.dao.repositories.DisciplineRepository;
import com.rx.dto.CurriculumStateDto;
import com.rx.misc.DocumentRootType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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

    public List<CurriculumStateDto> getCurriculumsStateOfAllDisciplines() {
        List<CurriculumStateDto> states = new ArrayList<>();

        for (Discipline discipline: getAllDisciplines()) {
            Map<DocumentType, Boolean> curriculumsState = new HashMap<>();

            for (DocumentType documentType: DocumentType.values()) {
                if (DocumentRootType.resolve(documentType).isCurriculum()) {
                    curriculumsState.put(documentType, false);
                }
            }

            for (Document curriculum: discipline.getCurriculums()) {
                LocalDate uploadingDate = curriculum.getUploadingDate()
                        .toLocalDate()
                        .plusYears(5);
                LocalDate now = LocalDate.now();
                if (!uploadingDate.isBefore(now)) {
                    curriculumsState.replace(curriculum.getDocumentType(), true);
                }
            }

            states.add(CurriculumStateDto.builder()
                    .withDisciplineId(discipline.getId())
                    .withDisciplineName(discipline.getName())
                    .withCurriculumState(curriculumsState)
                    .build());
        }

        return states;
    }
}
