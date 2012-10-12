package org.springframework.samples.mvc.springone;

public class RealtimeDataCollector {

	private int todayObama = 4304;

	private int todayRomney = 4304;

	private int todayBieber = 13450;

	private GroupedData lastFewHours;

	private GroupedData lastHour;

	private GroupedData lastSubHour;

	private MessageRate tweetRate;

	private MessageRate hashTagHitRate;

	public RealtimeDataCollector() {
		lastFewHours = new GroupedData("last6").bieberCount(600).obama(600).romney(2400);
		lastHour = new GroupedData("lasthour").bieberCount(100).obama(100).romney(400);
		lastSubHour = new GroupedData("lastsubhour").bieberCount(25).obama(25).romney(100);
		tweetRate = new MessageRate("tweets", 120);
		hashTagHitRate = new MessageRate("hashtag-hit", 10);
	}

	public MessageRate getTweetRate() {
		tweetRate.adjustRate();
		return tweetRate;
	}

	public MessageRate getHashTagHitRate() {
		hashTagHitRate.adjustRate();
		return hashTagHitRate;
	}

	public int getTodayObama() {
		return todayObama++;
	}

	public int getTodayRomney() {
		return todayRomney++;
	}

	public int getTodayBieber() {
		return todayBieber++;
	}

	public GroupedData getLastFewHours() {
		lastFewHours.incrementCounters();
		return lastFewHours;
	}

	public GroupedData getLastHour() {
		lastHour.incrementCounters();
		return lastHour;
	}

	public GroupedData getLastSubHour() {
		lastSubHour.incrementCounters();
		return lastSubHour;
	}
}
