package smartthings.metrics.autoconfiguration.dropwizard;

import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.jvm.GarbageCollectorMetricSet;
import com.codahale.metrics.jvm.MemoryUsageGaugeSet;
import javax.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;

@ConditionalOnClass(value = {MemoryUsageGaugeSet.class, GarbageCollectorMetricSet.class})
public class DropwizardJvmAutoConfiguration {

	@Autowired
	private MetricRegistry metricRegistry;

	@PostConstruct
	private void registerMetrics() {
		metricRegistry.registerAll(new MemoryUsageGaugeSet());
		metricRegistry.registerAll(new GarbageCollectorMetricSet());
	}

}
