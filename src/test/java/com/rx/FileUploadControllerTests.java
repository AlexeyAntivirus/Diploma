package com.rx;

import com.rx.controllers.FileUploadController;
import com.rx.dto.FileUploadResultDto;
import com.rx.dto.FileUploadStatus;
import com.rx.services.FileStorageService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.UUID;

import static com.rx.dto.FileUploadResultDto.FileUploadResultDtoBuilder;

@RunWith(SpringRunner.class)
@SpringBootTest
public class FileUploadControllerTests {

    private MockMultipartFile file = new MockMultipartFile(
            "file", "text.txt", "text/plain", "This is a test".getBytes());

    private FileStorageService mockFileStorageService =
            Mockito.mock(FileStorageService.class);

    private MockMvc mvc = MockMvcBuilders.standaloneSetup(
            new FileUploadController(mockFileStorageService))
            .build();

    private RequestBuilder builder = MockMvcRequestBuilders.fileUpload("/upload").file(file);

    @Test
    public void shouldOkWhenUploadingFile() throws Exception {
        FileUploadResultDto result = new FileUploadResultDtoBuilder()
                .setUploadedFileUUID(UUID.randomUUID())
                .setFileUploadStatus(FileUploadStatus.FILE_UPLOADED).build();

        Mockito.when(mockFileStorageService.saveToStorage(file)).thenReturn(result);
        this.mvc.perform(builder)
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void shouldBadRequestWhenFileIsNotSpecified() throws Exception {
        FileUploadResultDto result = new FileUploadResultDtoBuilder()
                .setFileUploadStatus(FileUploadStatus.EMPTY_OR_NULL_FILE).build();

        Mockito.when(mockFileStorageService.saveToStorage(file)).thenReturn(result);
        this.mvc.perform(builder)
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    public void shouldInternalServerErrorWhenIOExceptionIsOccur() throws Exception {
        FileUploadResultDto result = new FileUploadResultDtoBuilder()
                .setFileUploadStatus(FileUploadStatus.INTERNAL_ERROR).build();

        Mockito.when(mockFileStorageService.saveToStorage(file)).thenReturn(result);
        this.mvc.perform(builder)
                .andExpect(MockMvcResultMatchers.status().isInternalServerError());
    }
}
