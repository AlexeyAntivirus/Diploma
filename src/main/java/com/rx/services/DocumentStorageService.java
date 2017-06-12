package com.rx.services;

import com.rx.controllers.exceptions.FileDownloadNotFoundException;
import com.rx.dao.Discipline;
import com.rx.dao.Document;
import com.rx.dao.DocumentType;
import com.rx.dao.User;
import com.rx.dao.repositories.DocumentRepository;
import com.rx.dto.FileDownloadResultDto;
import com.rx.dto.DocumentUploadResultDto;
import com.rx.helpers.FileStorageHelper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.sql.Date;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static com.rx.dto.FileDownloadResultDto.FileDownloadResultDtoBuilder;

@Service
public class DocumentStorageService {

    private static final Logger LOGGER = LogManager.getLogger(DocumentStorageService.class);

    private DocumentRepository documentRepository;
    private FileStorageHelper fileStorageHelper;

    @Autowired
    public DocumentStorageService(DocumentRepository documentRepository,
                                  FileStorageHelper fileStorageHelper) {
        this.documentRepository = documentRepository;
        this.fileStorageHelper = fileStorageHelper;
    }

    public DocumentUploadResultDto saveTeachingLoadInStorage(User user, MultipartFile file, Date uploadingDate) {
        Document document = this.saveDocumentInStorage(file, DocumentType.TEACHING_LOAD, uploadingDate);

        user.getTeachingLoads().add(document);

        return DocumentUploadResultDto.builder()
                .withFileId(document.getId())
                .build();
    }

    public DocumentUploadResultDto saveCurriculumInStorage(Discipline discipline, MultipartFile file, DocumentType documentType, Date uploadingDate) {
        Document document = this.saveDocumentInStorage(file, documentType, uploadingDate);

        discipline.getCurriculums().add(document);

        return DocumentUploadResultDto.builder()
                .withFileId(document.getId())
                .build();
    }

    public DocumentUploadResultDto saveNormativeActInStorage(MultipartFile file, Date uploadingDate) {
        Document document = this.saveDocumentInStorage(file, DocumentType.NORMATIVE_ACT, uploadingDate);

        return DocumentUploadResultDto.builder()
                .withFileId(document.getId())
                .build();
    }

    public DocumentUploadResultDto saveSyllabusInStorage(MultipartFile file, Date uploadingDate) {
        Document document = this.saveDocumentInStorage(file, DocumentType.SYLLABUS, uploadingDate);

        return DocumentUploadResultDto.builder()
                .withFileId(document.getId())
                .build();
    }

    public FileDownloadResultDto getDocumentFromStorageById(Long documentId) {
        if (documentId == null) {
            throw new FileDownloadNotFoundException("documentId пропущений!");
        }

        Document document = this.documentRepository.findOne(documentId);

        if (document == null) {
            throw new FileDownloadNotFoundException("Документ з id=" + documentId + "не було знайдено");
        }

        File file = fileStorageHelper.getFileByName(document.getDocumentFilename());
        FileSystemResource fileSystemResource = new FileSystemResource(file);

        return new FileDownloadResultDtoBuilder().withFileResource(fileSystemResource).build();
    }

    public List<Document> getAllSyllabuses() {
        Iterable<Document> allDocuments = documentRepository.findAll();

        return StreamSupport.stream(allDocuments.spliterator(), false)
                .filter(document -> document.getDocumentType() == DocumentType.SYLLABUS)
                .collect(Collectors.toList());
    }

    public List<Document> getAllNormativeActs() {
        Iterable<Document> allDocuments = documentRepository.findAll();

        return StreamSupport.stream(allDocuments.spliterator(), false)
                .filter(document -> document.getDocumentType() == DocumentType.NORMATIVE_ACT)
                .collect(Collectors.toList());
    }

    private Document saveDocumentInStorage(MultipartFile file, DocumentType documentType, Date uploadingDate) {
        String uploadedFilename = fileStorageHelper.saveNewFile(file);
        Document document = Document.builder()
                .withDocumentFilename(uploadedFilename)
                .withDocumentType(documentType)
                .withUploadingDate(uploadingDate)
                .build();

        return documentRepository.save(document);
    }
}
