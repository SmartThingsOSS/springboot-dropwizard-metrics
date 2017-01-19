package smartthings.springboot.metrics.autoconfiguration.servlet;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Configuration properties for {@link ServletFilterAutoConfiguration}.
 */
@ConfigurationProperties(prefix = "metrics.servlet")
public class ServletFilterProperties {

	private String namePrefix = "instrumented";

	public String getNamePrefix() {
		return this.namePrefix;
	}

	public void setNamePrefix(String namePrefix) {
		this.namePrefix = namePrefix;
	}

}
