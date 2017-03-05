package com.rx;

import com.rx.controllers.exceptions.FileDownloadNotFoundException;
import com.rx.controllers.exceptions.FileUploadIOException;
import com.rx.controllers.exceptions.FileUploadInvalidPathException;
import com.rx.dto.FileDownloadResultDto;
import com.rx.dto.FileUploadResultDto;
import com.rx.services.FileStorageService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.internal.util.reflection.Whitebox;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.springframework.beans.factory.BeanCreationException;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mock.web.MockMultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@RunWith(PowerMockRunner.class)
@PrepareForTest(FileStorageService.class)
public class FileStorageServiceTests {

    private String originalFileStorageFolder = "file_storage";

    private MockMultipartFile file = new MockMultipartFile(
            "file", "text.txt", "text/plain", "This is a testHandleUploadWhenFileUploaded".getBytes());

    private FileStorageService service;

    @Before
    public void init() {
        this.service = new FileStorageService(originalFileStorageFolder);
    }

    @Test
    public void testConstructorInNormalInitialization() {

        FileStorageService service = new FileStorageService(originalFileStorageFolder);
        Map<UUID, String> database = (Map<UUID, String>) Whitebox.getInternalState(service, "database");
        Path fileStorageFolder = (Path) Whitebox.getInternalState(service, "fileStoragePath");

        Assert.assertEquals(fileStorageFolder.toString(), originalFileStorageFolder);
        Assert.assertNotNull(database);
    }

    @Test(expected = BeanCreationException.class)
    public void testConstructorInThrowingException() throws IOException {
        Path storageFolder = Paths.get(originalFileStorageFolder);

        PowerMockito.mockStatic(Files.class);
        PowerMockito.when(Files.notExists(storageFolder)).thenReturn(true);
        PowerMockito.when(Files.createDirectory(storageFolder)).thenThrow(IOException.class);

        new FileStorageService(originalFileStorageFolder);
    }

    @Test(expected = FileUploadIOException.class)
    public void testSaveOnStorageWhenInternalErrorOccurs() throws IOException {
        Path filePath = Paths.get(originalFileStorageFolder).resolve(file.getOriginalFilename());

        PowerMockito.mockStatic(Files.class);
        PowerMockito.when(Files.write(filePath, file.getBytes())).thenThrow(IOException.class);

        FileUploadResultDto result = this.service.saveToStorage(file);

        Assert.assertNull(result.getFileUUID());
    }

    @Test(expected = FileUploadInvalidPathException.class)
    public void testSaveOnStorageWhenDesiredPathIsNotStartsWithStorageFolderPath() {
        FileStorageService service = new FileStorageService("");

        FileUploadResultDto result = service.saveToStorage(file);

        Assert.assertNull(result.getFileUUID());
    }

    @Test
    public void testSaveToStorageWhenFileExistsAndNotEmpty() throws Exception {
        Path filePath = Paths.get(originalFileStorageFolder).resolve(file.getOriginalFilename());

        FileUploadResultDto result = this.service.saveToStorage(file);
        PowerMockito.mockStatic(Files.class);
        PowerMockito.when(Files.write(filePath, file.getBytes())).thenReturn(filePath);

        Assert.assertNotNull(result.getFileUUID());
    }

    @Test
    public void testGetFromStorageWhenFileNotFound() {
        Whitebox.setInternalState(this.service, "database", new HashMap<>());

        UUID uuid = UUID.randomUUID();
        FileDownloadResultDto result = this.service.getFromStorage(uuid);

        Assert.assertEquals(result.getFileResource(), null);
    }

    @Test
    public void testGetFromStorageWhenFileFound() {
        UUID uuid = UUID.randomUUID();

        Whitebox.setInternalState(this.service, "database",
                new HashMap<UUID, String>() {{
                    put(uuid, file.getOriginalFilename());
                }});

        FileSystemResource resource = new FileSystemResource(
                Paths.get(originalFileStorageFolder, file.getOriginalFilename()).toFile());
        FileDownloadResultDto result = service.getFromStorage(uuid);
        Assert.assertEquals(result.getFileResource(), resource);
    }

    @Test(expected = FileDownloadNotFoundException.class)
    public void testGetFromStorageWhenDesiredFilePathNotStartsWithStorageFolderPath() {
        UUID uuid = UUID.randomUUID();

        Whitebox.setInternalState(this.service, "database",
                new HashMap<UUID, String>() {{
                    put(uuid, file.getOriginalFilename());
                }});
        Whitebox.setInternalState(this.service, "fileStoragePath", Paths.get(""));

        service.getFromStorage(uuid);
    }
}