package org.ream.power.targets;

import java.util.concurrent.TimeUnit;

import java.util.logging.Logger;
import org.influxdb.InfluxDB;
import org.influxdb.InfluxDB.ConsistencyLevel;
import org.influxdb.InfluxDBFactory;
import org.influxdb.dto.BatchPoints;
import org.influxdb.dto.Point;
import org.ream.power.domain.TedPacket;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;

public class TedPacketInfluxAdapter implements InitializingBean, DisposableBean {

	private static final Logger log = Logger.getLogger(TedPacketInfluxAdapter.class.getName());
	private String dbName = "powerdb";
	private InfluxDB influxDB;
	
	@Override
	public void afterPropertiesSet() throws Exception {
		influxDB = InfluxDBFactory.connect("http://192.168.1.24:8086", "root", "root");
//		influxDB.createDatabase(dbName);
//		String rpName = "aRetentionPolicy";
//		influxDB.createRetentionPolicy(rpName, dbName, "30d", "30m", 2, true);
		if(influxDB == null || influxDB.ping() == null) {
			throw new RuntimeException("Unable to start/reach InfluxDB");
		}
		log.info("Connected to InfluxDB");
	}
	
	public void writeMessage(TedPacket packet) {
		BatchPoints batchPoints = BatchPoints
						.database(dbName)
						.tag("async", "true")
						.consistency(ConsistencyLevel.ALL)
						.build();
		Point point1 = Point.measurement("kwh")
							.time(packet.getReadingTime().getTime(), TimeUnit.MILLISECONDS)
							.addField("value", packet.getKwh())
							.tag("source", "ted")
							.build();
		Point point2 = Point.measurement("vrms")
							.time(packet.getReadingTime().getTime(), TimeUnit.MILLISECONDS)
							.addField("value", packet.getVoltsRms())
							.tag("source", "ted")
							.build();
		batchPoints.point(point1);
		batchPoints.point(point2);
		influxDB.write(batchPoints);
	}

	@Override
	public void destroy() throws Exception {
		log.info("Shutting down InfluxDB connection");
		influxDB.close();
	}


	
}
