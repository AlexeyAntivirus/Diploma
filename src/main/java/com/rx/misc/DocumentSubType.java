package com.rx.misc;


import com.rx.dao.DocumentType;

public enum DocumentSubType {
    TEXT_BOOK,
    TUTORIALS,
    LECTURE_NOTES,
    GUIDELINES,
    MULTIMEDIA_LECTURES,
    INDEPENDENT_WORK_TUTORIALS,
    INDEPENDENT_WORK_GUIDELINES,
    PRACTICE_TUTORIALS,
    PRACTICE_GUIDELINES,
    NO_SUB_TYPE;

    @Override
    public String toString() {
        switch (this) {
            case TEXT_BOOK:
                return "document.sub-type.text-book";
            case TUTORIALS:
                return "document.sub-type.tutorials";
            case LECTURE_NOTES:
                return "document.sub-type.lecture-notes";
            case GUIDELINES:
                return "document.sub-type.guidelines";
            case MULTIMEDIA_LECTURES:
                return "document.sub-type.multimedia-lectures";
            case INDEPENDENT_WORK_TUTORIALS:
                return "document.sub-type.independent-work-tutorials";
            case INDEPENDENT_WORK_GUIDELINES:
                return "document.sub-type.independent-work-guidelines";
            case PRACTICE_TUTORIALS:
                return "document.sub-type.practice-tutorials";
            case PRACTICE_GUIDELINES:
                return "document.sub-type.practice-guidelines";
            case NO_SUB_TYPE:
                return "document.sub-type.no-sub-type";
            default: throw new IllegalStateException("documentType");
        }
    }

    public static DocumentSubType resolve(DocumentType documentType) {
        switch (documentType) {
            case SYLLABUS:
            case NORMATIVE_ACT:
            case TEACHING_LOAD:
            case TRAINING_PROGRAM:
            case WORK_PROGRAM:
            case COMPLEX_TEST_METHODOLOGICAL_MATERIALS:
            case PROGRAM_MODULES_ADOPTION_LEVEL_DIAGNOSIS_METHODOLOGICAL_MATERIALS:
            case INTELLIGENT_COMPUTER_SIMULATORS:
            case VIRTUAL_LABORATORY_AND_PRACTICAL_WORK:
                return NO_SUB_TYPE;
            case THEORETICAL_PART_TEXTBOOKS:
                return TEXT_BOOK;
            case THEORETICAL_PART_LECTURE_NOTES:
                return LECTURE_NOTES;
            case THEORETICAL_PART_TUTORIALS:
            case LABORATORY_WORK_TUTORIALS:
            case PRACTICAL_LESSON_TUTORIALS:
            case COURSE_WORK_TUTORIALS:
            case DIPLOMA_WORK_TUTORIALS:
                return TUTORIALS;
            case LABORATORY_WORK_GUIDELINES:
            case PRACTICAL_LESSON_GUIDELINES:
            case COURSE_WORK_GUIDELINES:
            case DIPLOMA_WORK_GUIDELINES:
                return GUIDELINES;
            case EXTRAMULAR_STUDIES_INDEPENDENT_WORK_TUTORIALS:
                return INDEPENDENT_WORK_TUTORIALS;
            case EXTRAMULAR_STUDIES_INDEPENDENT_WORK_GUIDELINES:
                return INDEPENDENT_WORK_GUIDELINES;
            case EXTRAMULAR_STUDIES_MULTIMEDIA_LECTURES:
                return MULTIMEDIA_LECTURES;
            case PRACTICE_GUIDELINES:
                return PRACTICE_GUIDELINES;
            case PRACTICE_TUTORIALS:
                return PRACTICE_TUTORIALS;
            default: throw new IllegalArgumentException("documentType");
        }
    }

    public static DocumentSubType[] resolveSubTypes(DocumentRootType documentType) {
        switch (documentType) {
            case SYLLABUS:
            case NORMATIVE_ACT:
            case TEACHING_LOAD:
            case TRAINING_PROGRAM:
            case WORK_PROGRAM:
            case COMPLEX_TEST_METHODOLOGICAL_MATERIALS:
            case PROGRAM_MODULES_ADOPTION_LEVEL_DIAGNOSIS_METHODOLOGICAL_MATERIALS:
            case INTELLIGENT_COMPUTER_SIMULATORS:
            case VIRTUAL_LABORATORY_AND_PRACTICAL_WORK:
                return new DocumentSubType[]{NO_SUB_TYPE};
            case THEORETICAL_PART:
                return new DocumentSubType[]{TEXT_BOOK, TUTORIALS, LECTURE_NOTES};
            case PRACTICAL_LESSON:
            case LABORATORY_WORK:
            case COURSE_WORK:
            case DIPLOMA_WORK:
                return new DocumentSubType[]{TUTORIALS, GUIDELINES};
            case EXTRAMULAR_STUDIES:
                return new DocumentSubType[]{MULTIMEDIA_LECTURES, INDEPENDENT_WORK_GUIDELINES, INDEPENDENT_WORK_TUTORIALS};
            case PRACTICE:
                return new DocumentSubType[]{PRACTICE_TUTORIALS, PRACTICE_GUIDELINES};
            default:
                throw new IllegalArgumentException("documentType");
        }
    }
}
