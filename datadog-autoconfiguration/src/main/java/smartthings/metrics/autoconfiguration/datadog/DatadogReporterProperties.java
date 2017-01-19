package smartthings.metrics.autoconfiguration.datadog;

import java.util.Collections;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;
import org.coursera.metrics.datadog.DatadogReporter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("metrics.datadog")
final public class DatadogReporterProperties {

	private int interval = 1;

	private String host;

	private boolean ec2Host = false;

	private String prefix;

	private EnumSet<DatadogReporter.Expansion> expansions = DatadogReporter.Expansion.ALL;

  private Set<String> tags = Collections.emptySet();

  private Set<String> includes = Collections.emptySet();

  private Set<String> excludes = Collections.emptySet();

  public EnumSet<DatadogReporter.Expansion> getExpansions() {
  	return this.expansions;
	}

	public void setExpansions(EnumSet<DatadogReporter.Expansion> expansions) {
  	this.expansions = expansions;
	}

  public int getInterval() {
  	return interval;
	}

	public void setInterval(int value) {
  	this.interval = value;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public String getHost() {
		return this.host;
	}

	public boolean isEc2Host() {
		return this.ec2Host;
	}

	public void setEc2Host(boolean ec2Host) {
		this.ec2Host = ec2Host;
	}

	public String getPrefix() {
		return this.prefix;
	}

	public void setPrefix(String prefix) {
		this.prefix = prefix;
	}

	public Set<String> getIncludes() {
  	return this.includes;
	}

	public void setIncludes(Set<String> includes) {
  	this.includes = includes;
	}

	public Set<String> getExcludes() {
  	return this.excludes;
	}

	public void setExcludes(Set<String> excludes) {
  	this.excludes = excludes;
	}


	public Set<String> getTags() {
  	return this.tags;
	}

	public void setTags(Set<String> tags) {
  	this.tags = tags;
	}

}
