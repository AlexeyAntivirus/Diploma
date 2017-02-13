package com.rx;

import com.rx.data.ServiceResult;
import com.rx.services.FileStorageService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.*;
import org.mockito.internal.util.reflection.Whitebox;
import org.powermock.api.mockito.PowerMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.env.Environment;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpStatus;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.spi.FileSystemProvider;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Created by multi-view on 2/10/17.
 */

@RunWith(SpringRunner.class)
@SpringBootTest
public class FileStorageServiceTests {

    @InjectMocks
    @Autowired
    private FileStorageService fileStorageService;

    @Autowired
    private Environment environment;

    @Mock
    private Map<UUID, Path> database;

    @Mock
    private FileSystemProvider provider = PowerMockito.mock(FileSystemProvider.class);

    private MockMultipartFile mockMultipartFile = new MockMultipartFile(
            "mockMultipartFile", "text.txt", "text/plain", "This is a test".getBytes());

    private UUID uuid = UUID.randomUUID();

    private Path storageFolder;


    @Before
    public void init() {
       storageFolder = ((Path) Whitebox.getInternalState(
                this.fileStorageService, "storageFolder"))
                .resolve(mockMultipartFile.getOriginalFilename());
    }

    @Test
    public void shouldNormalInitialization() throws Exception {
        Path storageFolder = (Path)
                Whitebox.getInternalState(this.fileStorageService, "storageFolder");
        Map<UUID, Path> database = (Map<UUID, Path>)
                Whitebox.getInternalState(this.fileStorageService, "database");

        Assert.assertEquals(storageFolder, Paths.get(environment.getProperty("app.storage.folder")));
        Assert.assertNotEquals(database, null);
    }

    @Test
    public void shouldReturnNormalResultWhenCallingGetFromStorage() {
        this.database = new HashMap<UUID, Path>() {{
            put(uuid, storageFolder);
        }};
        Whitebox.setInternalState(this.fileStorageService,
                "database", this.database);

        FileSystemResource resource = new FileSystemResource(this.storageFolder.toFile());
        ServiceResult<FileSystemResource> result =
                fileStorageService.getFromStorage(this.uuid);
        Assert.assertEquals(result.getValue(), resource);
        Assert.assertEquals(result.getStatus(), HttpStatus.OK);
    }

    @Test
    public void shouldReturnNullWhenCallingGetFromStorage() {
        this.database = new HashMap<>();
        Whitebox.setInternalState(this.fileStorageService,
                "database", this.database);

        ServiceResult<FileSystemResource> result =
                fileStorageService.getFromStorage(this.uuid);
        Assert.assertEquals(result.getValue(), null);
        Assert.assertEquals(result.getStatus(), HttpStatus.NOT_FOUND);
    }

    @Test
    public void shouldReturnNormalResultWhenCallingSaveToStorage() throws Exception {
        Whitebox.setInternalState(this.fileStorageService,
                "provider", this.provider);

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        PowerMockito.when(provider.newOutputStream(storageFolder)).thenReturn(byteArrayOutputStream);

        ServiceResult<UUID> result = this.fileStorageService.saveToStorage(mockMultipartFile);

        Assert.assertNotEquals(result.getValue(), null);
        Assert.assertEquals(result.getStatus(), HttpStatus.OK);
    }

    @Test
    public void shouldReturnNullWhenFileNotSpecifiedWhenCallingSaveToStorage() {
        MockMultipartFile emptyMockMultipartFile = new MockMultipartFile("test.txt", (byte[]) null);

        ServiceResult<UUID> result = this.fileStorageService.saveToStorage(emptyMockMultipartFile);
        Assert.assertEquals(result.getValue(), null);
    }

    @Test
    public void shouldReturnNullWhenInternalErrorInServiceWhenCallingSaveToStorage() throws IOException {
        Whitebox.setInternalState(this.fileStorageService,
                "provider", this.provider);
        PowerMockito.when(provider.newOutputStream(storageFolder))
                .thenThrow(IOException.class);

        ServiceResult<UUID> result = this.fileStorageService.saveToStorage(mockMultipartFile);
        Assert.assertEquals(result.getValue(), null);
        Assert.assertEquals(result.getStatus(), HttpStatus.INTERNAL_SERVER_ERROR);
    }


}