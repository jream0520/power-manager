package org.ream.power;

import org.ream.power.endpoints.TedMessageEndpoint;
import org.ream.power.service.ServiceProperties;
import org.ream.power.sources.FakeTedPacketSourceAdapter;
import org.ream.power.sources.TedPacketSourceAdapter;
import org.ream.power.targets.TedPacketInfluxAdapter;
import org.ream.power.targets.TedPacketTargetAdapter;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.config.EnableIntegration;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.IntegrationFlows;
import org.springframework.integration.dsl.Pollers;

@Configuration
@SpringBootApplication
@EnableIntegration
@EnableConfigurationProperties(ServiceProperties.class)
public class PowerManagerApplication {

	@Bean
	public IntegrationFlow integrationFlow(TedMessageEndpoint endpoint) {
		return IntegrationFlows.from(tedSource(), c -> c.poller(Pollers.fixedRate(100)))
				.channel(inputChannel()).handle(endpoint)
				.channel(outputChannel()).handle(influxTarget(), "writeMessage")
				.get();
	}
		
	public static void main(String[] args) {
		ConfigurableApplicationContext ctx = SpringApplication.run(PowerManagerApplication.class, args);
	}
	
	@Bean
	public TedPacketSourceAdapter tedSource() {
		TedPacketSourceAdapter source = new TedPacketSourceAdapter("ttyUSB0");
//		FakeTedPacketSourceAdapter source = new FakeTedPacketSourceAdapter("ttyUSB0");
		return source;
	}

	@Bean
	public DirectChannel inputChannel() {
		return new DirectChannel();
	}

	@Bean
	public DirectChannel outputChannel() {
		return new DirectChannel();
	}

//	@Bean
//	public TedPacketTargetAdapter logTarget() {
//		return new TedPacketTargetAdapter();
//	}
	
	@Bean
	public TedPacketInfluxAdapter influxTarget() {
		return new TedPacketInfluxAdapter();
	}

}