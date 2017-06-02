package com.rx;

import com.rx.dao.Discipline;
import com.rx.dao.Document;
import com.rx.dao.DocumentType;
import com.rx.dao.User;
import com.rx.dao.UserRole;
import com.rx.dao.repositories.DisciplineRepository;
import com.rx.dao.repositories.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;

import java.sql.Date;
import java.time.LocalDate;
import java.util.HashSet;

@SpringBootApplication
public class DiplomaApplication {

	public static void main(String[] args) {
		SpringApplication.run(DiplomaApplication.class, args);
	}

	@Bean
    @Profile("dev")
	public CommandLineRunner runner(UserRepository userRepository,
                                    DisciplineRepository disciplineRepository) {
		return args -> {
			Document document1 = Document.builder()
					.withDocumentFilename("Швець Н.В. Педагогічна нагрузка 2016-2017.xlsx")
					.withDocumentType(DocumentType.TEACHING_LOAD)
					.withUploadingDate(Date.valueOf(LocalDate.now()))
					.build();
			Document document3 = Document.builder()
					.withDocumentFilename("Мітрофанова Н.Ф. Педагогічна нагрузка 2016-2017.xlsx")
					.withDocumentType(DocumentType.TEACHING_LOAD)
					.withUploadingDate(Date.valueOf(LocalDate.now()))
					.build();
			Document document5 = Document.builder()
					.withDocumentFilename("ПтаППБ. Конспект лекцій 2013.docx")
					.withDocumentType(DocumentType.THEORETICAL_PART_LECTURE_NOTES)
					.withUploadingDate(Date.valueOf(LocalDate.now()))
					.build();
			Document document6 = Document.builder()
					.withDocumentFilename("ПтаППБ. Посібник до лабораторних робіт 2013.docx")
					.withDocumentType(DocumentType.LABORATORY_WORK_TUTORIALS)
					.withUploadingDate(Date.valueOf(LocalDate.now()))
					.build();
			Document document8 = Document.builder()
					.withDocumentFilename("КПП. Конспект лекцій 2013.docx")
					.withDocumentType(DocumentType.THEORETICAL_PART_LECTURE_NOTES)
					.withUploadingDate(Date.valueOf(LocalDate.now()))
					.build();
			Document document9 = Document.builder()
					.withDocumentFilename("КПП. Посібник до лабораторних робіт 2013.docx")
					.withDocumentType(DocumentType.LABORATORY_WORK_TUTORIALS)
					.withUploadingDate(Date.valueOf(LocalDate.now()))
					.build();
			Document document10 = Document.builder()
					.withDocumentFilename("КПП. Посібник до лабораторних робіт 2014.docx")
					.withDocumentType(DocumentType.LABORATORY_WORK_TUTORIALS)
					.withUploadingDate(Date.valueOf(LocalDate.now()))
					.build();
			Document document11 = Document.builder()
					.withDocumentFilename("КПП. Методичні матеріали для діагностики рівня засвоення програми модулів 2015.docx")
					.withDocumentType(DocumentType.PROGRAM_MODULES_ADOPTION_LEVEL_DIAGNOSIS_METHODOLOGICAL_MATERIALS)
					.withUploadingDate(Date.valueOf(LocalDate.now()))
					.build();

			Discipline discipline1 = Discipline.builder()
					.withName("Паралельне та Багатопоточне Програмування")
					.withCik(document5)
					.withCik(document6)
					.build();
			Discipline discipline2 = Discipline.builder()
					.withName("Крос-Платформне Програмування")
					.withCik(document8)
					.withCik(document9)
					.withCik(document10)
					.withCik(document11)
					.build();
			disciplineRepository.save(discipline1);
			disciplineRepository.save(discipline2);
			User user1 = User.builder()
					.withLogin("nshvec60")
					.withPassword("nshvec60")
					.withEmail("shvetsnatalya@rambler.ru")
					.withLastName("Швець")
					.withFirstName("Наталя")
					.withMiddleName("Василівна")
					.withUserRole(UserRole.METHODOLOGIST)
					.build();
			User user2 = User.builder()
					.withLogin("proziumod")
					.withPassword("proziumod")
					.withEmail("proziumod@gmail.com")
					.withLastName("Мітрофанова")
					.withFirstName("Наталя")
					.withMiddleName("Федорівна")
					.withUserRole(UserRole.ASSISTANT_LECTURER)
					.build();
			HashSet<Discipline> disciplines = new HashSet<Discipline>() {{
				add(discipline1);
				add(discipline2);
			}};
			user1.setTeachingLoads(new HashSet<Document>() {{
				add(document1);
			}});
			user1.setDisciplines(disciplines);
			user2.setTeachingLoads(new HashSet<Document>() {{
				add(document3);
			}});
			user2.setDisciplines(disciplines);

			userRepository.save(User.builder()
					.withLogin("admin")
					.withPassword("admin")
					.withEmail("blabla@gmail.com")
					.withPassword("14ph38")
					.withLastName("")
					.withFirstName("")
					.withMiddleName("")
					.withUserRole(UserRole.ADMINISTRATOR)
					.build());
			userRepository.save(user1);
			userRepository.save(user2);
			userRepository.save(User.builder()
					.withLogin("plotnikov")
					.withPassword("plotnikov")
					.withEmail("plotnikov@ukr.net")
					.withLastName("Плотников")
					.withFirstName("Валерій")
					.withMiddleName("Михайлович")
					.withUserRole(UserRole.HEAD_OF_DEPARTMENT)
					.build());
			userRepository.save(User.builder()
					.withLogin("popkovdn")
					.withPassword("popkovdn")
					.withEmail("popkovdn@ukr.net")
					.withLastName("Попков")
					.withFirstName("Денис")
					.withMiddleName("Миколайович")
					.withUserRole(UserRole.SENIOR_LECTURER)
					.build());
		};
	}
}
