package org.ream.power.config;

import org.ream.power.endpoints.TedMessageEndpoint;
import org.ream.power.sources.TedPacketSourceAdapter;
import org.ream.power.targets.TedPacketInfluxAdapter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.IntegrationFlows;
import org.springframework.integration.dsl.Pollers;

@Configuration
public class AppConfig {
    
	@Bean
	public IntegrationFlow integrationFlow(TedMessageEndpoint endpoint) {
		return IntegrationFlows.from(tedSource(), c -> c.poller(Pollers.fixedRate(10000)))
				.channel(inputChannel()).handle(endpoint)
				.channel(outputChannel()).handle(influxTarget(), "writeMessage")
//				.channel(outputChannel()).handle(logTarget(), "writeMessage")
				.get();
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

	// @Bean
	// public TedPacketTargetAdapter logTarget() {
	// 	return new TedPacketTargetAdapter();
	// }
	
	@Bean
	public TedPacketInfluxAdapter influxTarget() {
		return new TedPacketInfluxAdapter();
	}
}