package com.sesc.studentportal;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;

@ContextConfiguration(classes = TestConfig.class)
@TestPropertySource(locations = "classpath:application.properties")
@DataJpaTest
class StudentPortalApplicationTests {

	@Test
	void contextLoads() {
	}

}
