package smartthings.metrics.autoconfiguration.datadog;

import org.coursera.metrics.datadog.transport.HttpTransport;
import org.coursera.metrics.datadog.transport.Transport;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.web.context.ConfigurableWebApplicationContext;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(classes = TestServer.class)
@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@TestPropertySource(properties = {
		"metrics.datadog.enabled=true",
		"metrics.datadog.transport.type=http",
		"metrics.datadog.transport.api-key=test"
})
public class DatadogTransportIntegrationTest {

	@Autowired
	ConfigurableWebApplicationContext context;

	@Test
	public void check_transportConfig() throws Exception {
		DatadogTransportProperties properties = context.getBean(DatadogTransportProperties.class);

		assertThat(properties.getType()).isEqualTo("http");
		assertThat(properties.getApiKey()).isEqualTo("test");
	}

	@Test
	public void check_transportRegistered() throws Exception {
		Transport transport = context.getBean(Transport.class);
		assertThat(transport).isNotNull();
		assertThat(transport).isInstanceOf(HttpTransport.class);

		DatadogTransportProperties properties = context.getBean(DatadogTransportProperties.class);

		assertThat(properties.getApiKey()).isEqualTo("test");

	}

}
