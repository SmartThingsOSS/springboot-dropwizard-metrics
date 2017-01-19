package smartthings.springboot.metrics.autoconfiguration.datadog;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

/**
 * SpringBootApplication used for integration tests.
 */
@SpringBootApplication
public class TestServer {

	public static void main(String[] args) {
		new SpringApplicationBuilder(TestServer.class)
				.properties("spring.config.name=test-server")
				.run(args);
	}

}
