[![CircleCI](https://circleci.com/gh/SmartThingsOSS/springboot-dropwizard-metrics.svg?style=svg)](https://circleci.com/gh/SmartThingsOSS/springboot-dropwizard-metrics)
[![Download](https://api.bintray.com/packages/smartthingsoss/maven/smartthings.springboot-dropwizard-metrics/images/download.svg) ](https://bintray.com/smartthingsoss/maven/smartthings.springboot-dropwizard-metrics/_latestVersion)


# springboot-dropwizard-metrics
Spring Boot auto configuration libraries for adding Dropwizard metrics
collection and reporting to Spring Boot projects.

## Reporters

### Datadog

Configures Coursera Dropwizard Datadog reporter using Spring Boot 
configuration.

#### Configuration
```yaml
metrics:
	datadog:
  	interval: 1 # interval in which metrics are reported
  	host:
  	ec2-host: false
  	prefix: my-app # Datadog metric prefix
  	expansions: # Datadog expansion enum list (use ALL for all expansions)
  	tags: # Datadog tag list
  	includes: # regex list of metrics to include.
  	excludes: # regex list of metric names to exclude.
  	transport:
  		type: # on of 'http' or 'statsd'
  		# HTTP
  		api-key: # Datadog API key
  		connection-timeout: # connection timeout in milliseconds
  		socket-timeout: # socket timeout in milliseconds
  		# Statsd
  		statsd-port: 
  		statsd-host:
  		statsd-prefix:
  		prefix:
  
```

## Collection

### InstrumentedFilter

Configures the Dropwizard InstrumentedFilter and adds it to 
Spring Boot as a FilterRegistrationBean. 

#### Configuration
```yaml
metrics:
	servlet:
		name-prefix: 'instrumented' # metric prefix
```

## Building from source

```bash
$ ./gradlew check
```

## Artifacts

### Library Releases

Releases are uploaded to [Bintray](https://dl.bintray.com/smartthingsoss/maven/smartthings)

### Library Snapshots

Snapshot are uploaded to [SmartThings Artifactory](https://smartthings.artifactoryonline.com/smartthings/libs-snapshot-local)

