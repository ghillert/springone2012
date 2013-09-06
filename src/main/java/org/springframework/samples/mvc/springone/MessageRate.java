package org.springframework.samples.mvc.springone;

import java.util.Random;

public class MessageRate {

	private static final Random random = new Random();

	private String label;

	private double rate;

	private double baseRate;

	public MessageRate(String label, double rate) {
		super();
		this.label = label;
		this.rate = rate;
		this.baseRate = rate;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public double getRate() {
		return this.rate;
	}

	public void setRate(double rate) {
		this.rate = rate;
	}

	public void adjustRate() {
		rate =  this.baseRate + new Double(Math.abs(random.nextGaussian()));
	}


}
