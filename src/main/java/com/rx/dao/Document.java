package com.rx.dao;



import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.sql.Date;

@Entity
public class Document {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String documentFilename;

    @Enumerated(EnumType.STRING)
    private DocumentType documentType;

    private Date uploadingDate;

    protected Document() {
    }

    private Document(DocumentBuilder builder) {
        this.documentFilename = builder.documentFilename;
        this.uploadingDate = builder.uploadingDate;
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

    public Date getUploadingDate() {
        return uploadingDate;
    }

    public static DocumentBuilder builder() {
        return new DocumentBuilder();
    }

    public static class DocumentBuilder {

        private String documentFilename;

        private DocumentType documentType;

        private Date uploadingDate;

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

        public DocumentBuilder withUploadingDate(Date uploadingDate) {
            this.uploadingDate = uploadingDate;
            return this;
        }

        public Document build() {
            return new Document(this);
        }
    }
}