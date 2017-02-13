package com.rx;

import com.rx.controllers.FileUploadController;
import com.rx.data.ServiceResult;
import com.rx.services.FileStorageService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.UUID;

/**
 * Created by multi-view on 2/10/17.
 */


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
        ServiceResult<UUID> result = new ServiceResult<>(
                UUID.randomUUID(), HttpStatus.OK);

        Mockito.when(mockFileStorageService.saveToStorage(file)).thenReturn(result);
        this.mvc.perform(builder)
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void shouldBadRequestWhenFileIsNotSpecified() throws Exception {
        ServiceResult<UUID> result = new ServiceResult<>(
                null, HttpStatus.BAD_REQUEST);

        Mockito.when(mockFileStorageService.saveToStorage(file)).thenReturn(result);
        this.mvc.perform(builder)
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    public void shouldInternalServerErrorWhenIOExceptionIsOccur() throws Exception {
        ServiceResult<UUID> result = new ServiceResult<>(
                null, HttpStatus.INTERNAL_SERVER_ERROR);

        Mockito.when(mockFileStorageService.saveToStorage(file)).thenReturn(result);
        this.mvc.perform(builder)
                .andExpect(MockMvcResultMatchers.status().isInternalServerError());
    }
}
