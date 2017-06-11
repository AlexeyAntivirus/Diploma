package com.rx.services;

import com.rx.dao.Discipline;
import com.rx.dao.Document;
import com.rx.dao.DocumentType;
import com.rx.dao.User;
import com.rx.dao.repositories.DisciplineRepository;
import com.rx.dto.CurriculumStateDto;
import com.rx.dto.DisciplineAddingResultDto;
import com.rx.dto.forms.AddDisciplineFormDto;
import com.rx.dto.forms.FullDisciplineFormDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@Transactional
public class DisciplineService {

    private DisciplineRepository disciplineRepository;
    private UserService userService;

    @Autowired
    public DisciplineService(DisciplineRepository disciplineRepository, UserService userService) {
        this.disciplineRepository = disciplineRepository;
        this.userService = userService;
    }

    public Discipline getDisciplineById(Long id) {
        return disciplineRepository.findOne(id);
    }

    public boolean existsByName(String name) {
        return disciplineRepository.existsByName(name);
    }

    public void deleteById(Long id) {
        Discipline one = disciplineRepository.findOne(id);
        for (User user: one.getUsers()) {
            user.getDisciplines().remove(one);
        }
        disciplineRepository.delete(id);
    }

    public Iterable<Discipline> getTeacherDisciplines(Long userId) {
        return disciplineRepository.findByUsersId(userId);
    }

    public Iterable<Discipline> getAllDisciplines() {
        return disciplineRepository.findAll();
    }

    public DisciplineAddingResultDto addDiscipline(AddDisciplineFormDto addDisciplineFormDto) {
        String errorField = null;
        String errorMessage = null;
        Long disciplineId = null;

        if (disciplineRepository.existsByName(addDisciplineFormDto.getDisciplineName())) {
            errorField = "disciplineName";
            errorMessage = "discipline.isPresent";
        } else {
            disciplineId = disciplineRepository.save(Discipline.builder()
                    .withName(addDisciplineFormDto.getDisciplineName())
                    .build()).getId();
        }
        return DisciplineAddingResultDto.builder()
                .withDisciplineId(disciplineId)
                .withErrorField(errorField)
                .withErrorMessage(errorMessage)
                .build();
    }


    public Discipline updateDiscipline(Long id, FullDisciplineFormDto fullDisciplineFormDto) {
        Discipline discipline = disciplineRepository.findOne(id);

        discipline.setName(fullDisciplineFormDto.getName());

        Long userId = fullDisciplineFormDto.getTeacherId();
        if (userId != null) {
            User user = userService.getUserById(fullDisciplineFormDto.getTeacherId());
            user.setDisciplines(new HashSet<Discipline>() {{
                add(discipline);
                addAll(user.getDisciplines());
            }});

            Set<User> users = discipline.getUsers();
            users.add(user);

            discipline.setUsers(users);

            return disciplineRepository.save(discipline);
        }

        return discipline;
    }

    public List<CurriculumStateDto> getCurriculumsStateOfAllDisciplines() {
        List<CurriculumStateDto> states = new ArrayList<>();

        for (Discipline discipline : getAllDisciplines()) {
            Map<DocumentType, Boolean> curriculumsState = new HashMap<>();

            List<DocumentType> allDocumentTypes = Stream.of(DocumentType.values()).collect(Collectors.toList());

            for (Document curriculum : discipline.getCurriculums()) {
                LocalDate uploadingDate = curriculum.getUploadingDate()
                        .toLocalDate()
                        .plusYears(5);
                LocalDate now = LocalDate.now();
                DocumentType documentType = curriculum.getDocumentType();

                if (!uploadingDate.isBefore(now)) {
                    curriculumsState.put(documentType, true);
                    allDocumentTypes.remove(documentType);
                }
            }

            curriculumsState.putAll(allDocumentTypes.stream()
                    .collect(Collectors.toMap(keyMapper -> keyMapper, valueMapper -> false)));

            states.add(CurriculumStateDto.builder()
                    .withDisciplineId(discipline.getId())
                    .withDisciplineName(discipline.getName())
                    .withCurriculumState(curriculumsState)
                    .build());
        }

        return states;
    }

    public void detachUserFromDiscipline(Long disciplineId, Long userId) {
        Discipline discipline = disciplineRepository.findOne(disciplineId);
        User user = userService.getUserById(userId);

        discipline.getUsers().remove(user);
        user.getDisciplines().remove(discipline);
    }
}
