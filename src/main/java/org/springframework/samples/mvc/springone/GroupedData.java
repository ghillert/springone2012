package org.springframework.samples.mvc.springone;

public class GroupedData {

	private String label;

	private int obamaCount;

	private int romneyCount;

	private int bieberCount;

	public GroupedData(String label) {
		this.label = label;
	}

	public GroupedData obama(int obamaCount) {
		this.obamaCount = obamaCount;
		return this;
	}

	public GroupedData romney(int romneyCount) {
		this.romneyCount = romneyCount;
		return this;
	}

	public GroupedData bieberCount(int bieberCount) {
		this.bieberCount = bieberCount;
		return this;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public int getObamaCount() {
		return obamaCount;
	}

	public void setObamaCount(int obamaCount) {
		this.obamaCount = obamaCount;
	}

	public int getRomneyCount() {
		return romneyCount;
	}

	public void setRomneyCount(int romneyCount) {
		this.romneyCount = romneyCount;
	}

	public int getBieberCount() {
		return bieberCount;
	}

	public void setBieberCount(int bieberCount) {
		this.bieberCount = bieberCount;
	}

	public void incrementCounters() {
		obamaCount++;
		romneyCount++;
		bieberCount++;
	}

}
