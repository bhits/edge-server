package gov.samhsa.mhc.edgeserver;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.WebIntegrationTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = EdgeServerApplication.class)
@WebIntegrationTest({"server.port=0", "management.port=0"})
@ActiveProfiles("eureka-ssl")
public class EdgeServerApplicationTests {

	@Test
	public void contextLoads() {
	}

}
