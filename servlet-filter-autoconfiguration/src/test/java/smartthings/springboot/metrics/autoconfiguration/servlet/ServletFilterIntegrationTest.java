package smartthings.springboot.metrics.autoconfiguration.servlet;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.web.context.ConfigurableWebApplicationContext;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * ServletFilterIntegrationTest.
 */
@SpringBootTest(classes = TestServer.class)
@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@TestPropertySource(properties = {
		"metrics.servlet.enabled=true",
		"metrics.datadog.enabled=true",
		"metrics.datadog.expansions=COUNT,MIN,MAX,MEDIAN",
		"metrics.datadog.includes=counter.one,counter.two,counter.test",
		"metrics.datadog.transport.type=http",
		"metrics.datadog.transport.api-key=test"
})
@EnableConfigurationProperties(ServletFilterProperties.class)
public class ServletFilterIntegrationTest {

	@Autowired
	ConfigurableWebApplicationContext context;

	@Test
	public void check_propertiesFromConfig() throws Exception {

		ServletFilterProperties properties = context.getBean(ServletFilterProperties.class);

		assertThat(properties.getNamePrefix()).isEqualTo("servlet");

	}
}
