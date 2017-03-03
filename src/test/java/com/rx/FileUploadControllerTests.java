package com.rx;

import com.rx.controllers.FileUploadController;
import com.rx.dto.FileUploadResultDto;
import com.rx.services.FileStorageService;
import com.rx.validators.FileUploadFormDtoValidator;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.BDDMockito;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

import java.util.UUID;

import static com.rx.dto.FileUploadResultDto.FileUploadResultDtoBuilder;

@RunWith(SpringRunner.class)
@SpringBootTest
public class FileUploadControllerTests {

    @Value("${app.storage.allowed.filename}")
    String allowedFilenamePattern;

    private MockMultipartFile file = new MockMultipartFile(
            "multipartFile", "text.txt", "text/plain", "This is a testHandleUploadWhenFileUploaded".getBytes());

    private FileStorageService mockFileStorageService =
            Mockito.mock(FileStorageService.class);

    private MockMvc mvc;

    @Before
    public void init() {
        InternalResourceViewResolver viewResolver = new InternalResourceViewResolver();
        viewResolver.setPrefix("/WEB-INF/templates");
        viewResolver.setSuffix(".html");
        FileUploadController controller = new FileUploadController(mockFileStorageService,
                new FileUploadFormDtoValidator(allowedFilenamePattern));
        mvc = MockMvcBuilders.standaloneSetup(controller)
                .setViewResolvers(viewResolver)
                .build();
    }

    @Test
    public void testHandleUploadWhenFileUploaded() throws Exception {
        FileUploadResultDto result = new FileUploadResultDtoBuilder()
                .withFileUUID(UUID.randomUUID()).build();

        Mockito.when(mockFileStorageService.saveToStorage(file)).thenReturn(result);

        mvc.perform(MockMvcRequestBuilders.fileUpload("/upload").file(file))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("upload-result"));

        BDDMockito.then(mockFileStorageService).should().saveToStorage(file);
    }

    @Test
    public void testHandleUploadWhenFileNotMatched() throws Exception {
        mvc.perform(MockMvcRequestBuilders.fileUpload("/upload").file(
                new MockMultipartFile("multipartFile", "test.sh", "text/plain", "echo \"Hello!\"".getBytes())))
                .andExpect(MockMvcResultMatchers.model().hasErrors())
                .andExpect(MockMvcResultMatchers.view().name("upload"));
    }

    @Test
    public void testHandleUploadWhenFileIsEmpty() throws Exception {
        FileUploadResultDto result = new FileUploadResultDtoBuilder()
                .withFileUUID(UUID.randomUUID()).build();

        Mockito.when(mockFileStorageService.saveToStorage(file)).thenReturn(result);

        mvc.perform(MockMvcRequestBuilders.fileUpload("/upload").file(
                new MockMultipartFile("multipartFile", "test.sh", "text/plain", (byte[]) null)))
                .andExpect(MockMvcResultMatchers.model().hasErrors())
                .andExpect(MockMvcResultMatchers.view().name("upload"));

    }
}
