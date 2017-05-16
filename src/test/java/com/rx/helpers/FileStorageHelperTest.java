package com.rx.helpers;

import com.rx.controllers.exceptions.FileDownloadNotFoundException;
import com.rx.controllers.exceptions.FileUploadIOException;
import com.rx.controllers.exceptions.FileUploadInvalidPathException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.junit.runner.RunWith;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.nio.file.Paths;

@Ignore
@RunWith(SpringRunner.class)
public class FileStorageHelperTest {

    @Rule
    public TemporaryFolder testFolder = new TemporaryFolder();

    private FileStorageHelper fileStorageHelper;
    private MockMultipartFile mockMultipartFile;

    @Before
    public void setUp() throws Exception {
        fileStorageHelper = new FileStorageHelper();
        mockMultipartFile = new MockMultipartFile("file", "text.txt", "text/plain", "This is a testHandleUploadWhenFileUploaded".getBytes());
        Path testFolder = Paths.get(this.testFolder.getRoot().toURI());

        fileStorageHelper.setFileStoragePath(testFolder);
    }

    @Test(expected = FileUploadInvalidPathException.class)
    public void saveNewFileWhenPathInvalid() throws Exception {
        MockMultipartFile mockMultipartFile = new MockMultipartFile("file", "../../../../../../text.sh", "text/plain", "echo `pwd`".getBytes());

        fileStorageHelper.saveNewFile(mockMultipartFile);
    }

    @Test(expected = FileUploadIOException.class)
    public void saveNewFileWhenIOException() throws Exception {
        fileStorageHelper.saveNewFile(new MultipartFile() {
            @Override
            public String getName() {
                return null;
            }

            @Override
            public String getOriginalFilename() {
                return "testFile.txt";
            }

            @Override
            public String getContentType() {
                return null;
            }

            @Override
            public boolean isEmpty() {
                return false;
            }

            @Override
            public long getSize() {
                return 0;
            }

            @Override
            public byte[] getBytes() throws IOException {
                throw new IOException();
            }

            @Override
            public InputStream getInputStream() throws IOException {
                return null;
            }

            @Override
            public void transferTo(File file) throws IOException, IllegalStateException {

            }
        });
    }

    @Test
    public void saveNewFile() throws Exception {
        String newFilePath = fileStorageHelper.saveNewFile(mockMultipartFile);

        Assert.assertEquals(newFilePath, mockMultipartFile.getOriginalFilename());
    }

    @Test(expected = FileDownloadNotFoundException.class)
    public void getFileByNameWhenInvalidFilename() throws Exception {
        fileStorageHelper.getFileByName("/../../../../../../../");
    }

    @Test(expected = FileDownloadNotFoundException.class)
    public void getFileByNameWhenNotFile() throws Exception {
        fileStorageHelper.setRunningTests(false);

        fileStorageHelper.getFileByName("./");
    }

    @Test
    public void getFileByName() throws Exception {
        String filename = "testFile.txt";
        File file = testFolder.newFile(filename);

        File fileFromStorage = fileStorageHelper.getFileByName(filename);

        Assert.assertEquals(file, fileFromStorage);
    }
}