package smartthings.metrics.autoconfiguration.datadog;

import com.codahale.metrics.Metric;
import com.codahale.metrics.MetricFilter;
import com.codahale.metrics.MetricRegistry;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;
import org.coursera.metrics.datadog.DatadogReporter;
import org.coursera.metrics.datadog.DefaultMetricNameFormatter;
import org.coursera.metrics.datadog.MetricNameFormatter;
import org.coursera.metrics.datadog.transport.HttpTransport;
import org.coursera.metrics.datadog.transport.Transport;
import org.coursera.metrics.datadog.transport.UdpTransport;
import org.springframework.boot.autoconfigure.condition.ConditionOutcome;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.SpringBootCondition;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.type.AnnotatedTypeMetadata;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

@Configuration
@EnableConfigurationProperties(value = {DatadogReporterProperties.class, DatadogTransportProperties.class})
@Conditional(DatadogReporterAutoConfiguration.DatadogSetCondition.class)
public class DatadogReporterAutoConfiguration {

	@Bean
	@ConditionalOnMissingBean
	public MetricNameFormatter metricNameFormatter() {
		return new DefaultMetricNameFormatter();
	}

	@Bean
	@ConditionalOnMissingBean
	public DatadogReporter datadogReporter(
			MetricRegistry metricRegistry,
			Transport transport,
			MetricNameFormatter metricNameFormatter,
			DatadogReporterProperties properties) throws IOException {

		DatadogReporter.Builder builder = DatadogReporter.forRegistry(metricRegistry)
				.withTransport(transport)
				.withExpansions(properties.getExpansions())
				.withMetricNameFormatter(metricNameFormatter)
				.withTags(new ArrayList<>(properties.getTags()))
				.filter(getMetricFilter(properties.getIncludes(), properties.getExcludes()));

		builder = (properties.isEc2Host()) ? builder.withEC2Host() : builder;
		builder = (properties.getHost() != null) ? builder.withHost(properties.getHost()) : builder;
		builder = (properties.getPrefix() != null) ? builder.withPrefix(properties.getPrefix()) : builder;

		DatadogReporter reporter = builder.build();

		reporter.start(properties.getInterval(), TimeUnit.SECONDS);

		return reporter;
	}

	@Bean
	@ConditionalOnMissingBean
	public Transport transport(DatadogTransportProperties properties) {
		Transport transport;
		if ("http".equals(properties.getType())) {

			Assert.hasLength(properties.getApiKey(), "Datadog API key is required for HTTP transport");

			transport = new HttpTransport.Builder()
					.withApiKey(properties.getApiKey())
					.withConnectTimeout(properties.getConnectionTimeout())
					.withSocketTimeout(properties.getSocketTimeout())
					.build();
		} else {
			Assert.isTrue(properties.getStatsdPort() > 0, "Datadog StatsD port is required for UDP transport");
			Assert.hasLength(properties.getStatsdHost(), "Datadog StatsD host is required for UDP transport");

			UdpTransport.Builder builder = new UdpTransport.Builder()
					.withPort(properties.getStatsdPort())
					.withPrefix(properties.getPrefix())
					.withStatsdHost(properties.getStatsdHost());

			builder = (properties.getStatsdPrefix() != null) ? builder.withPrefix(properties.getStatsdPrefix()) : builder;

			transport = builder.build();
		}

		return transport;
	}

	private static MetricFilter getMetricFilter(final Set<String> includes, Set<String> excludes) {

		final List<Pattern> includePatterns = new LinkedList<>();
		final List<Pattern> excludePatterns = new LinkedList<>();

		for (String include : includes) {
			includePatterns.add(Pattern.compile(include));
		}

		for (String exclude : excludes) {
			excludePatterns.add(Pattern.compile(exclude));
		}

		return new MetricFilter() {
			@Override public boolean matches(String name, Metric metric) {

				if (includePatterns.size() > 0) {
					for (Pattern pattern : includePatterns) {
						if (pattern.matcher(name).matches()) return true;
					}

					return false;
				} else {
					for (Pattern pattern : excludePatterns) {
						if (pattern.matcher(name).matches()) return false;
					}

					return true;
				}
			}

			@Override public String toString() {
				return "[MetricFilter regexIncludes=" + StringUtils.collectionToDelimitedString(includes, "," +
						" regexExcludes=" + StringUtils.collectionToDelimitedString(excludes, ","));
			}
		};
	}

  static final class DatadogSetCondition extends SpringBootCondition {
		private static final String ENABLED_PROPERTY = "metrics.datadog.enabled";

    @Override public ConditionOutcome getMatchOutcome(ConditionContext context,
        AnnotatedTypeMetadata a) {

      boolean enabled = isTrue(context.getEnvironment().getProperty(ENABLED_PROPERTY, Boolean.class));

      return enabled ?
          ConditionOutcome.match() :
					ConditionOutcome.noMatch(ENABLED_PROPERTY + " is false or not set");
    }

    private static boolean isTrue(Boolean b) {
    	return b == null ? false : b;
		}
  }
}
