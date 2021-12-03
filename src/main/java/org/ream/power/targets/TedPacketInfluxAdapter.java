package org.ream.power.targets;

import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.influxdb.client.InfluxDBClient;
import com.influxdb.client.InfluxDBClientFactory;
import com.influxdb.client.WriteApiBlocking;
import com.influxdb.client.domain.WritePrecision;
import com.influxdb.client.write.Point;
import com.influxdb.exceptions.InfluxException;

import org.ream.power.domain.TedPacket;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;

public class TedPacketInfluxAdapter implements InitializingBean, DisposableBean {

	private static final Logger log = Logger.getLogger(TedPacketInfluxAdapter.class.getName());
	
	private static char[] token = "cAFXmNezyPLnthsyZf4FoWzPYhimEqFTI-gs_8B9hIoEJ02Lm_4fP05-FxmprP7eddlVkr6xgAiWeYSq186KWg==".toCharArray();
    private static String org = "jream";
    private static String bucket = "powerdb/autogen";

	private InfluxDBClient influxDBClient;
	private WriteApiBlocking writeApi;
	@Override
	public void afterPropertiesSet() throws Exception {
		influxDBClient = InfluxDBClientFactory.create("http://192.168.1.24:8086", token, org, bucket);

		if(influxDBClient == null || !influxDBClient.ping()) {
			throw new RuntimeException("Unable to start/reach InfluxDB");
		}

		writeApi = influxDBClient.getWriteApiBlocking();

		log.info("Connected to InfluxDB");
	}
	
	public void writeMessage(TedPacket packet) {
		try {
			Point point1 = Point.measurement("kwh")
								.time(packet.getReadingTime().toInstant(), WritePrecision.MS)
								.addField("value", packet.getKwh())
								.addTag("source", "ted");
			Point point2 = Point.measurement("vrms")
								.time(packet.getReadingTime().toInstant(), WritePrecision.MS)
								.addField("value", packet.getVoltsRms())
								.addTag("source", "ted");
			writeApi.writeMeasurements(WritePrecision.NS, Arrays.asList(point1, point2));
		} catch(InfluxException ie) {
			log.log(Level.SEVERE, "Error writing measurement", ie);
		}
	}

	@Override
	public void destroy() throws Exception {
		log.info("Shutting down InfluxDB connection");
		influxDBClient.close();
	}


	
}
