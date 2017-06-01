package com.rx.services;

import com.rx.dao.Discipline;
import com.rx.dao.Document;
import com.rx.dao.DocumentType;
import com.rx.dao.repositories.DisciplineRepository;
import com.rx.misc.DocumentRootType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.HashMap;
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

    public Map<Discipline, Boolean[]> getCurriculumsStateOfAllDisciplines() {
        HashMap<Discipline, Boolean[]> curriculumsStateOfAllDisciplines = new HashMap<>();

        Iterable<Discipline> allDisciplines = getAllDisciplines();
        for (Discipline discipline : allDisciplines) {
            Set<Document> actualCurriculums = discipline.getCurriculums().stream()
                    .filter(document -> {
                        LocalDate uploadingDate = document.getUploadingDate()
                                .toLocalDate()
                                .plusYears(5);
                        LocalDate now = LocalDate.now();
                        return !uploadingDate.isBefore(now);
                    })
                    .collect(Collectors.toSet());
            DocumentType[] allDocumentTypes = DocumentType.values();
            Boolean[] state = Stream.of(allDocumentTypes)
                    .filter(documentType -> DocumentRootType.resolve(documentType).isCurriculum())
                    .map(documentType -> actualCurriculums.stream()
                            .anyMatch(document -> document.getDocumentType() == documentType))
                    .toArray(Boolean[]::new);
            curriculumsStateOfAllDisciplines.put(discipline, state);
        }

        return curriculumsStateOfAllDisciplines;
    }
}
