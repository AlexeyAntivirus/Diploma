package com.rx;

import com.rx.controllers.FileDownloadController;
import com.rx.services.FileStorageService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.FileSystemResource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.io.ByteArrayInputStream;
import java.util.UUID;

import static com.rx.dto.FileDownloadResultDto.FileDownloadResultDtoBuilder;


@RunWith(SpringRunner.class)
@SpringBootTest
public class FileDownloadControllerTests {

    private FileSystemResource mockedResource = Mockito.mock(FileSystemResource.class);

    private FileStorageService mockFileStorageService =
            Mockito.mock(FileStorageService.class);

    private MockMvc mvc = MockMvcBuilders.standaloneSetup(
            new FileDownloadController(mockFileStorageService))
            .build();

    @Test
    public void shouldOkWhenDownloading() throws Exception {
        UUID randomUUID = UUID.randomUUID();

        Mockito.when(mockedResource.getInputStream())
                .thenReturn(new ByteArrayInputStream("This is a test".getBytes()));
        Mockito.when(mockFileStorageService.getFromStorage(randomUUID))
                .thenReturn(new FileDownloadResultDtoBuilder()
                        .withFileResource(mockedResource)
                        .build());

        this.mvc.perform(MockMvcRequestBuilders.get(
                "/download?fileUUID=" + randomUUID))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void should404WhenFileNotFound() throws Exception {
        UUID randomUUID = UUID.randomUUID();

        Mockito.when(mockedResource.getInputStream()).thenReturn(
                new ByteArrayInputStream("This is a test".getBytes()));
        Mockito.when(mockFileStorageService.getFromStorage(randomUUID))
                .thenReturn(new FileDownloadResultDtoBuilder()
                        .withFileResource(mockedResource)
                        .build());

        this.mvc.perform(MockMvcRequestBuilders.get(
                "/download?fileUUID=" + randomUUID))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

}
