package smartthings.metrics.autoconfiguration.datadog;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

@SpringBootApplication
public class TestServer {

	public static void main(String[] args) {
		new SpringApplicationBuilder(TestServer.class)
				.properties("spring.config.name=test-server")
				.run(args);
	}

}
