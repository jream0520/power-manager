package org.ream.power.domain;

import java.math.BigDecimal;
import java.sql.Timestamp;

public class TedPacket {
	private Timestamp readingTime;
	private BigDecimal voltsRms;
	private BigDecimal kwh;
	
	public TedPacket() {
		super();
	}

	public Timestamp getReadingTime() {
		return readingTime;
	}

	public void setReadingTime(Timestamp readingTime) {
		this.readingTime = readingTime;
	}

	public BigDecimal getVoltsRms() {
		return voltsRms;
	}

	public void setVoltsRms(BigDecimal voltsRms) {
		this.voltsRms = voltsRms;
	}

	public BigDecimal getKwh() {
		return kwh;
	}

	public void setKwh(BigDecimal kwh) {
		this.kwh = kwh;
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("[");
		sb.append("readingTime=");
		sb.append(readingTime);
		sb.append(", voltsRms=");
		sb.append(voltsRms);
		sb.append(", kwh=");
		sb.append(kwh);
		sb.append("]");
		
		return sb.toString();
	}
	
}
