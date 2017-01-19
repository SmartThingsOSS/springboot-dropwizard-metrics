package smartthings.springboot.metrics.autoconfiguration.datadog;

import com.codahale.metrics.MetricRegistry;
import com.google.common.collect.Sets;
import java.util.EnumSet;
import org.coursera.metrics.datadog.DatadogReporter;
import org.coursera.metrics.datadog.model.DatadogGauge;
import org.coursera.metrics.datadog.transport.Transport;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.metrics.dropwizard.DropwizardMetricServices;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.ConfigurableWebApplicationContext;

import static org.assertj.core.api.Assertions.assertThat;
import static org.coursera.metrics.datadog.DatadogReporter.Expansion.COUNT;
import static org.coursera.metrics.datadog.DatadogReporter.Expansion.MAX;
import static org.coursera.metrics.datadog.DatadogReporter.Expansion.MEDIAN;
import static org.coursera.metrics.datadog.DatadogReporter.Expansion.MIN;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.timeout;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

/**
 * DatadogReporterIntegrationTest.
 */
@SpringBootTest(classes = TestServer.class)
@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@TestPropertySource(properties = {
		"metrics.datadog.enabled=true",
		"metrics.datadog.expansions=COUNT,MIN,MAX,MEDIAN",
		"metrics.datadog.includes=counter.one,counter.two,counter.test",
		"metrics.datadog.transport.type=http",
		"metrics.datadog.transport.api-key=test"
})
public class DatadogReporterIntegrationTest {

	@Autowired
	ConfigurableWebApplicationContext context;

	@MockBean
	Transport transport;

	MockMvc mockMvc;

	@Before
	public void init() {
		mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
	}

	@Test
	public void check_propertiesFromConfig() throws Exception {

		DatadogReporterProperties properties = context.getBean(DatadogReporterProperties.class);

		assertThat(properties.getIncludes())
				.isEqualTo(Sets.newHashSet("counter.one", "counter.two", "counter.test"));
		assertThat(properties.getExpansions())
				.isEqualTo(EnumSet.of(COUNT, MIN, MAX, MEDIAN));

	}

	@Test
	public void check_datadogReporterRegistered() throws Exception {

		assertThat(context.getBean(DatadogReporter.class)).isNotNull();

		// incrementing a counter should update update the metrics registry
		context.getBean(DropwizardMetricServices.class).increment("test");
		assertThat(context.getBean(MetricRegistry.class).getNames().toArray())
				.isEqualTo(new String[]{"counter.test"});

	}

	@Test
	public void whenMetricRecorded_thenTransportSentIsCalled() throws Exception {
		Transport.Request request = mock(Transport.Request.class);
		Mockito.when(transport.prepare()).thenReturn(request);

		context.getBean(DropwizardMetricServices.class).increment("test");

		verify(request, timeout(5000).times(1)).send();

		ArgumentCaptor<DatadogGauge> arg = ArgumentCaptor.forClass(DatadogGauge.class);
		verify(request, times(1)).addGauge(arg.capture());
		assertThat(arg.getValue().getMetric()).isEqualTo("counter.test");
	}

}
