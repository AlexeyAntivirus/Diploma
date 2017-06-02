package com.rx.dto;


import com.rx.dao.DocumentType;

import java.util.Map;

public class CurriculumStateDto {

    private Long disciplineId;

    private String disciplineName;

    private Map<DocumentType, Boolean> curriculumState;

    protected CurriculumStateDto(CurriculumStateDtoBuilder builder) {
        this.disciplineId = builder.disciplineId;

        this.disciplineName = builder.disciplineName;
        this.curriculumState = builder.curriculumState;
    }

    public Long getDisciplineId() {
        return disciplineId;
    }

    public String getDisciplineName() {
        return disciplineName;
    }

    public Map<DocumentType, Boolean> getCurriculumState() {
        return curriculumState;
    }

    public static CurriculumStateDtoBuilder builder() {
        return new CurriculumStateDtoBuilder();
    }

    public static class CurriculumStateDtoBuilder {

        private Long disciplineId;

        private String disciplineName;

        private Map<DocumentType, Boolean> curriculumState;

        private CurriculumStateDtoBuilder() {
        }

        public CurriculumStateDtoBuilder withDisciplineId(Long disciplineId) {
            this.disciplineId = disciplineId;
            return this;
        }

        public CurriculumStateDtoBuilder withDisciplineName(String disciplineName) {
            this.disciplineName = disciplineName;
            return this;
        }

        public CurriculumStateDtoBuilder withCurriculumState(Map<DocumentType, Boolean> curriculumState) {
            this.curriculumState = curriculumState;
            return this;
        }

        public CurriculumStateDto build() {
            return new CurriculumStateDto(this);
        }
    }
}
