package com.rx;

import com.rx.controllers.exceptions.FileDownloadNotFoundException;
import com.rx.controllers.exceptions.FileUploadIOException;
import com.rx.controllers.exceptions.FileUploadInvalidPathException;
import com.rx.helpers.FileStorageHelper;
import com.rx.services.DocumentStorageService;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.junit.runner.RunWith;
import org.mockito.internal.util.reflection.Whitebox;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;

@Ignore
@RunWith(SpringRunner.class)
public class FileStorageServiceTests {

    @Rule
    public TemporaryFolder testFolder = new TemporaryFolder();

    @MockBean
    private FileStorageHelper fileStorageHelper;

    private MockMultipartFile mockMultipartFile;
    private DocumentStorageService documentStorageService;

    @Before
    public void init() {
        mockMultipartFile = new MockMultipartFile("file", "text.txt", "text/plain", "This is a testHandleUploadWhenFileUploaded".getBytes());
        //fileStorageService = new FileStorageService(fileStorageHelper);
    }

    @Test(expected = FileUploadIOException.class)
    public void testSaveOnStorageWhenIOOccurs() {
        given(fileStorageHelper.saveNewFile(any(MultipartFile.class))).willThrow(FileUploadIOException.class);

        //fileStorageService.saveFileInStorage(mockMultipartFile);
    }

    @Test(expected = FileUploadInvalidPathException.class)
    public void testSaveOnStorageWhenInvalidPath() {
        given(fileStorageHelper.saveNewFile(any(MultipartFile.class))).willThrow(FileUploadInvalidPathException.class);

        //fileStorageService.saveFileInStorage(mockMultipartFile);
    }

    @Test
    public void testSuccessSaveFile() {
        String testPath = "test_path";

        given(fileStorageHelper.saveNewFile(any(MultipartFile.class))).willReturn(testPath);

        /*DocumentUploadResultDto result = fileStorageService.saveFileInStorage(mockMultipartFile);

        Assert.assertNotNull(result.getDocumentId());
        Assert.assertEquals(testPath, ((Map<UUID, String>) Whitebox.getInternalState(fileStorageService, "database")).get(result.getDocumentId()));
 */   }

    @Test(expected = FileDownloadNotFoundException.class)
    public void testGetFileWhenUuidIsNull() {
        documentStorageService.getDocumentFromStorageById(null);
    }

    @Test(expected = FileDownloadNotFoundException.class)
    public void testGetFileWhenUuidIsNotInDb() {
        //fileStorageService.getDocumentFromStorageById(UUID.randomUUID());
    }

    @Test
    public void testGetFileFromStorage() throws IOException {
        String filename = "file.txt";
        File tempFile = testFolder.newFile(filename);
        UUID uuid = UUID.randomUUID();
        ConcurrentHashMap<UUID, String> hashMap = new ConcurrentHashMap<>();

        hashMap.put(uuid, filename);

        Whitebox.setInternalState(documentStorageService, "database", hashMap);
        given(fileStorageHelper.getFileByName(anyString())).willReturn(tempFile);

        /*FileDownloadResultDto fileFromStorage = fileStorageService.getDocumentFromStorageById(uuid);

        Assert.assertEquals(fileFromStorage.getFileResource().getFile(), tempFile);*/
    }
}