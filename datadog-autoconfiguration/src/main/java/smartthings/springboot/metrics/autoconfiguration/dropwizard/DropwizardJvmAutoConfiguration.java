package smartthings.springboot.metrics.autoconfiguration.dropwizard;

import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.jvm.GarbageCollectorMetricSet;
import com.codahale.metrics.jvm.MemoryUsageGaugeSet;
import javax.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;

/**
 * Automatically adds {@link MemoryUsageGaugeSet} and {@link GarbageCollectorMetricSet} to
 * the Datadog {@link MetricRegistry} when these classes are present on the classpath.
 */
@ConditionalOnClass(value = {
		MemoryUsageGaugeSet.class, GarbageCollectorMetricSet.class
})
@ConditionalOnBean(MetricRegistry.class)
public class DropwizardJvmAutoConfiguration {

	@Autowired
	private MetricRegistry metricRegistry;

	@PostConstruct
	private void registerMetrics() {
		metricRegistry.registerAll(new MemoryUsageGaugeSet());
		metricRegistry.registerAll(new GarbageCollectorMetricSet());
	}

}
