package com.rx.services;

import com.rx.controllers.exceptions.FileDownloadNotFoundException;
import com.rx.dao.Document;
import com.rx.dao.DocumentType;
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

import static com.rx.dto.FileDownloadResultDto.FileDownloadResultDtoBuilder;
import static com.rx.dto.DocumentUploadResultDto.FileUploadResultDtoBuilder;

@Service
public class DocumentStorageService {

    private static final Logger LOGGER = LogManager.getLogger(DocumentStorageService.class);

    private DocumentRepository documentRepository;
    private FileStorageHelper fileStorageHelper;

    @Autowired
    public DocumentStorageService(DocumentRepository documentRepository, FileStorageHelper fileStorageHelper) {
        this.documentRepository = documentRepository;
        this.fileStorageHelper = fileStorageHelper;
    }

    public DocumentUploadResultDto saveFileInStorage(MultipartFile file, DocumentType documentType, Date uploadingDate) {
        String uploadedFilename = fileStorageHelper.saveNewFile(file);
        Document document = Document.builder()
                .withDocumentFilename(uploadedFilename)
                .withDocumentType(documentType)
                .withUploadingDate(uploadingDate)
                .build();

        Long fileId = documentRepository.save(document).getId(); //метод save возращает тот же объект что мы добавляем

        return new FileUploadResultDtoBuilder().withFileId(fileId).build();
    }

    public FileDownloadResultDto getFileFromStorageById(Long fileId) {
        if (fileId == null) {
            throw new FileDownloadNotFoundException("fileId is missing!");
        }

        Document document = this.documentRepository.findOne(fileId);

        if (document == null) {
            throw new FileDownloadNotFoundException("No file was found by fileId. fileId=" + fileId);
        }

        File file = fileStorageHelper.getFileByName(document.getDocumentFilename());
        FileSystemResource fileSystemResource = new FileSystemResource(file);

        return new FileDownloadResultDtoBuilder().withFileResource(fileSystemResource).build();
    }
}
