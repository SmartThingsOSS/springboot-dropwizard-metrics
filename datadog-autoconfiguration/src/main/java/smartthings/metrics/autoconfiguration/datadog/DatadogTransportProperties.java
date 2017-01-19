package smartthings.metrics.autoconfiguration.datadog;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("metrics.datadog.transport")
public class DatadogTransportProperties {

	private String type = "http"; // http or udp

	// HTTP properties
	private String apiKey;
	private int connectionTimeout = 2000;
	private int socketTimeout = 2000;

	// Statsd properties
	private int statsdPort;
	private String statsdHost;
	private String statsdPrefix;
	private String prefix;


	public void setApiKey(String apiKey) {
		this.apiKey = apiKey;
	}

	public String getApiKey() {
		return this.apiKey;
	}

	public void setConnectionTimeout(int value) {
		this.connectionTimeout = value;
	}

	public int getConnectionTimeout() {
		return this.connectionTimeout;
	}

	public void setSocketTimeout(int value) {
		this.socketTimeout = value;
	}

	public int getSocketTimeout() {
		return this.socketTimeout;
	}



	public String getType() {
		return this.type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public int getStatsdPort() {
		return this.statsdPort;
	}

	public void setStatsdPort(int port) {
		this.statsdPort = port;
	}

	public String getStatsdHost() {
		return this.statsdHost;
	}

	public void setStatsdHost(String host) {
		this.statsdHost = host;
	}

	public String getStatsdPrefix() {
		return this.statsdPrefix;
	}

	public void setStatsdPrefix(String prefix) {
		this.statsdPrefix = prefix;
	}

	public String getPrefix() {
		return this.prefix;
	}

	public void setPrefix(String prefix) {
		this.prefix = prefix;
	}

}
