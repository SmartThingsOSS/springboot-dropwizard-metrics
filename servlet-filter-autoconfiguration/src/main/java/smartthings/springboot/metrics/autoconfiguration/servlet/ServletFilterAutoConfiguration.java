package smartthings.springboot.metrics.autoconfiguration.servlet;

import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.servlet.InstrumentedFilter;
import com.codahale.metrics.servlet.InstrumentedFilterContextListener;
import javax.servlet.Filter;
import org.springframework.boot.autoconfigure.condition.ConditionOutcome;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.SpringBootCondition;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.type.AnnotatedTypeMetadata;

/**
 * Automatically adds a {@link InstrumentedFilter} to the servlet configuration when one is
 * present on the classpath.
 */
@Configuration
@EnableConfigurationProperties(value = {ServletFilterProperties.class})
@ConditionalOnClass(value = {InstrumentedFilter.class, Filter.class})
@ConditionalOnBean(value = {MetricRegistry.class})
@Conditional(ServletFilterAutoConfiguration.ServletFilterSetCondition.class)
public class ServletFilterAutoConfiguration {

	@Bean
	public FilterRegistrationBean instrumentedFilter(ServletFilterProperties properties) {
		FilterRegistrationBean registrationBean = new FilterRegistrationBean();

		registrationBean.setFilter(new InstrumentedFilter());
		registrationBean.addInitParameter("name-prefix", properties.getNamePrefix());

		return registrationBean;
	}

	@Bean
	public InstrumentedFilterContextListener instrumentedFilterContextListener(
			final MetricRegistry metricRegistry) {
		// register the metric registry with the servlet context
		return new MetricRegistryContextListener(metricRegistry);
	}

	private static class MetricRegistryContextListener extends InstrumentedFilterContextListener {

		private final MetricRegistry metricRegistry;

		private MetricRegistryContextListener(MetricRegistry metricRegistry) {
			this.metricRegistry = metricRegistry;
		}

		@Override protected MetricRegistry getMetricRegistry() {
			return metricRegistry;
		}
	}

	static final class ServletFilterSetCondition extends SpringBootCondition {
		private static final String ENABLED_PROPERTY = "metrics.servlet.enabled";

		@Override public ConditionOutcome getMatchOutcome(ConditionContext context,
				AnnotatedTypeMetadata a) {

			boolean enabled = isTrue(context.getEnvironment().getProperty(ENABLED_PROPERTY, Boolean.class));

			return enabled ?
					ConditionOutcome.match() :
					ConditionOutcome.noMatch(ENABLED_PROPERTY + " is false or not set");
		}

		private static boolean isTrue(Boolean b) {
			return b != null && b;
		}
	}

}
