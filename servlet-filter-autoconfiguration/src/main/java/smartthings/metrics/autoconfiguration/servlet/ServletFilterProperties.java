package smartthings.metrics.autoconfiguration.servlet;

import org.springframework.boot.context.properties.ConfigurationProperties;

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
