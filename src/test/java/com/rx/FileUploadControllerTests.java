package com.rx;

import com.rx.controllers.FileUploadController;
import com.rx.dto.FileUploadResultDto;
import com.rx.services.FileStorageService;
import com.rx.validators.FileUploadFormDtoValidator;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.UUID;
import java.util.regex.Pattern;

import static com.rx.dto.FileUploadResultDto.FileUploadResultDtoBuilder;

@RunWith(SpringRunner.class)
@SpringBootTest
public class FileUploadControllerTests {

    @Value("app.storage.allowed.filename")
    String allowedFilenamePattern;

    private MockMultipartFile file = new MockMultipartFile(
            "file", "text.txt", "text/plain", "This is a test".getBytes());

    private FileStorageService mockFileStorageService =
            Mockito.mock(FileStorageService.class);

    private MockMvc mvc = MockMvcBuilders.standaloneSetup(
            new FileUploadController(mockFileStorageService, new FileUploadFormDtoValidator(allowedFilenamePattern)))
            .build();

    private RequestBuilder builder = MockMvcRequestBuilders.fileUpload("/upload").file(file);

    @Test
    public void shouldOkWhenUploadingFile() throws Exception {
        FileUploadResultDto result = new FileUploadResultDtoBuilder()
                .withFileUUID(UUID.randomUUID()).build();

        Mockito.when(mockFileStorageService.saveToStorage(file)).thenReturn(result);
        this.mvc.perform(builder)
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void shouldBadRequestWhenFileIsNotSpecified() throws Exception {
        FileUploadResultDto result = new FileUploadResultDtoBuilder().build();

        Mockito.when(mockFileStorageService.saveToStorage(file)).thenReturn(result);
        this.mvc.perform(builder)
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    public void shouldInternalServerErrorWhenIOExceptionIsOccur() throws Exception {
        FileUploadResultDto result = new FileUploadResultDtoBuilder().build();

        Mockito.when(mockFileStorageService.saveToStorage(file)).thenReturn(result);
        this.mvc.perform(builder)
                .andExpect(MockMvcResultMatchers.status().isInternalServerError());
    }
}
