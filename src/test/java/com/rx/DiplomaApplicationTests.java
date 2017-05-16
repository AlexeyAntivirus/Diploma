package com.rx;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@Ignore
@RunWith(SpringRunner.class)
@SpringBootTest
public class DiplomaApplicationTests {

	@Value("${app.storage.folder}")
    private String folder;

	@Value("${app.storage.allowed.filename}")
    private String allowedFilename;

	@Test
	public void contextLoads() {
	    Assert.assertNotNull(folder);
	    Assert.assertNotNull(allowedFilename);
    }

}
