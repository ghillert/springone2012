package org.springframework.samples.mvc.springone;

import java.util.Random;

public class MessageRate {
	
	private static final Random random = new Random();
	
	private String label;
	
	private float rate;
	
	private float baseRate;
	
	public MessageRate(String label, float rate) {
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

	public float getRate() {
		return this.rate;
	}

	public void setRate(float rate) {
		this.rate = rate;
	}

	public void adjustRate() {
		rate =  this.baseRate + new Float(Math.abs(random.nextGaussian()));
	}
	
	
}
