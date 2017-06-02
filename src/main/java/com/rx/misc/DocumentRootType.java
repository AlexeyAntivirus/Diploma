package com.rx.misc;


import com.rx.dao.DocumentType;

public enum DocumentRootType {
    SYLLABUS,
    NORMATIVE_ACT,
    TEACHING_LOAD,

    TRAINING_PROGRAM,
    WORK_PROGRAM,
    THEORETICAL_PART,
    PRACTICAL_LESSON,
    LABORATORY_WORK,
    COURSE_WORK,
    DIPLOMA_WORK,
    COMPLEX_TEST_METHODOLOGICAL_MATERIALS,
    PROGRAM_MODULES_ADOPTION_LEVEL_DIAGNOSIS_METHODOLOGICAL_MATERIALS,
    INTELLIGENT_COMPUTER_SIMULATORS,
    VIRTUAL_LABORATORY_AND_PRACTICAL_WORK,
    EXTRAMULAR_STUDIES,
    PRACTICE;

    public boolean isCurriculum() {
        return this != DocumentRootType.SYLLABUS &&
                this != DocumentRootType.NORMATIVE_ACT &&
                this != DocumentRootType.TEACHING_LOAD;
    }

    @Override
    public String toString() {
        switch (this) {
            case SYLLABUS:
                return "document.type.syllabus";
            case NORMATIVE_ACT:
                return "document.type.normative-act";
            case TEACHING_LOAD:
                return "document.type.teaching-load";
            case TRAINING_PROGRAM:
                return "document.type.training-program";
            case WORK_PROGRAM:
                return "document.type.work-program";
            case THEORETICAL_PART:
                return "document.type.theoretical-part";
            case PRACTICAL_LESSON:
                return "document.type.practical-lessons";
            case LABORATORY_WORK:
                return "document.type.laboratory-lessons";
            case COURSE_WORK:
                return "document.type.course-works";
            case DIPLOMA_WORK:
                return "document.type.diploma-works";
            case COMPLEX_TEST_METHODOLOGICAL_MATERIALS:
                return "document.type.complex-test-methodical-materials";
            case PROGRAM_MODULES_ADOPTION_LEVEL_DIAGNOSIS_METHODOLOGICAL_MATERIALS:
                return "document.type.program-modules-adoption-level-diagnosis-methodical-materials";
            case INTELLIGENT_COMPUTER_SIMULATORS:
                return "document.type.intelligent-computer-simulators";
            case VIRTUAL_LABORATORY_AND_PRACTICAL_WORK:
                return "document.type.virtual-laboratory-and-practical-work";
            case EXTRAMULAR_STUDIES:
                return "document.type.extramular-studies";
            case PRACTICE:
                return "document.type.practice";
            default:
                throw new IllegalStateException();
        }
    }

    public static DocumentRootType resolve(DocumentType documentType) {
        switch (documentType) {
            case SYLLABUS:
                return DocumentRootType.SYLLABUS;
            case NORMATIVE_ACT:
                return DocumentRootType.NORMATIVE_ACT;
            case TEACHING_LOAD:
                return DocumentRootType.TEACHING_LOAD;
            case TRAINING_PROGRAM:
                return DocumentRootType.TRAINING_PROGRAM;
            case WORK_PROGRAM:
                return DocumentRootType.WORK_PROGRAM;
            case THEORETICAL_PART_TEXTBOOKS:
            case THEORETICAL_PART_LECTURE_NOTES:
            case THEORETICAL_PART_TUTORIALS:
                return DocumentRootType.THEORETICAL_PART;
            case PRACTICAL_LESSON_GUIDELINES:
            case PRACTICAL_LESSON_TUTORIALS:
                return DocumentRootType.PRACTICAL_LESSON;
            case LABORATORY_WORK_GUIDELINES:
            case LABORATORY_WORK_TUTORIALS:
                return DocumentRootType.LABORATORY_WORK;
            case COURSE_WORK_GUIDELINES:
            case COURSE_WORK_TUTORIALS:
                return DocumentRootType.COURSE_WORK;
            case DIPLOMA_WORK_GUIDELINES:
            case DIPLOMA_WORK_TUTORIALS:
                return DocumentRootType.DIPLOMA_WORK;
            case COMPLEX_TEST_METHODOLOGICAL_MATERIALS:
                return DocumentRootType.COMPLEX_TEST_METHODOLOGICAL_MATERIALS;
            case PROGRAM_MODULES_ADOPTION_LEVEL_DIAGNOSIS_METHODOLOGICAL_MATERIALS:
                return DocumentRootType.PROGRAM_MODULES_ADOPTION_LEVEL_DIAGNOSIS_METHODOLOGICAL_MATERIALS;
            case INTELLIGENT_COMPUTER_SIMULATORS:
                return DocumentRootType.INTELLIGENT_COMPUTER_SIMULATORS;
            case VIRTUAL_LABORATORY_AND_PRACTICAL_WORK:
                return DocumentRootType.VIRTUAL_LABORATORY_AND_PRACTICAL_WORK;
            case EXTRAMULAR_STUDIES_INDEPENDENT_WORK_GUIDELINES:
            case EXTRAMULAR_STUDIES_INDEPENDENT_WORK_TUTORIALS:
            case EXTRAMULAR_STUDIES_MULTIMEDIA_LECTURES:
                return DocumentRootType.EXTRAMULAR_STUDIES;
            case PRACTICE_GUIDELINES:
            case PRACTICE_TUTORIALS:
                return DocumentRootType.PRACTICE;
            default:
                throw new IllegalArgumentException("documentType");
        }
    }
}

