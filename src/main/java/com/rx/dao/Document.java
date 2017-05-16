package com.rx.dao;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Document {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String documentFilename;

    @Enumerated(EnumType.STRING)
    private DocumentType documentType;

    protected Document() {
    }

    private Document(DocumentBuilder builder) {
        this.documentFilename = builder.documentFilename;
        this.documentType = builder.documentType;
    }

    public Long getId() {
        return id;
    }

    public String getDocumentFilename() {
        return documentFilename;
    }

    public DocumentType getDocumentType() {
        return documentType;
    }

    public static DocumentBuilder builder() {
        return new DocumentBuilder();
    }

    public static class DocumentBuilder {

        private String documentFilename;

        private DocumentType documentType;

        private DocumentBuilder() {
        }

        public DocumentBuilder withDocumentFilename(String documentFilename) {
            this.documentFilename = documentFilename;
            return this;
        }

        public DocumentBuilder withDocumentType(DocumentType documentType) {
            this.documentType = documentType;
            return this;
        }

        public Document build() {
            return new Document(this);
        }
    }
}