package smartthings.springboot.metrics.autoconfiguration.servlet;

import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.servlet.InstrumentedFilter;
import com.codahale.metrics.servlet.InstrumentedFilterContextListener;
import javax.servlet.Filter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Automatically adds a {@link InstrumentedFilter} to the servlet configuration when one is
 * present on the classpath.
 */
@Configuration
@ConditionalOnClass(value = {InstrumentedFilter.class, Filter.class})
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

}
