package org.springframework.samples.mvc.springone;

import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.CharacterCodingException;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.TreeSet;

import javax.management.MalformedObjectNameException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.joda.time.DateMidnight;
import org.joda.time.DateTime;
import org.joda.time.Interval;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.jolokia.client.J4pClient;
import org.jolokia.client.exception.J4pException;
import org.jolokia.client.request.J4pReadRequest;
import org.jolokia.client.request.J4pReadResponse;
import org.springframework.web.client.RestTemplate;
import org.springframework.xd.rest.client.domain.metrics.AggregateCountsResource;
import org.springframework.xd.rest.client.domain.metrics.FieldValueCounterResource;

public class RealtimeDataCollector {

	private static final Log log = LogFactory.getLog(RealtimeDataCollector.class);

	private DateTimeFormatter dailyFormatter = DateTimeFormat.forPattern("yyyy-MM-dd");

	public RealtimeDataCollector() {
	}

	public MessageRate getTweetRate() {

		final J4pClient j4pClient = new J4pClient("http://localhost:8778/jolokia/");
		final J4pReadRequest req;

		try {
			req = new J4pReadRequest("xd.tweetStream:type=MessageChannel,name=output,index=0,module=twitterstream","MeanSendRate");
		}
		catch (MalformedObjectNameException e) {
			throw new IllegalStateException(e);
		}

		J4pReadResponse resp = null;
		try {
			resp = j4pClient.execute(req);
		}
		catch (J4pException e) {
			log.warn(e.getMessage());
		}

		if (resp != null) {
			log.debug("Message Rate:" + resp.getValue());
			return new MessageRate("tweets", (Double) resp.getValue());
		}

		log.warn("No Message Rate Respponse received.");
		return new MessageRate("tweets", 0d);

	}

	public Set<NameCountData> getGardenHoseRecent() {

		RestTemplate restTemplate = new RestTemplate();
		String url = "http://localhost:8080/metrics/field-value-counters/hashtags";
		FieldValueCounterResource metricsResource = restTemplate.getForObject(url, FieldValueCounterResource.class);

		final Map<String, Double> fieldValueCounts = metricsResource.getFieldValueCounts();

		FieldValueComparator fvc = new FieldValueComparator(fieldValueCounts);
		TreeMap<String, Double> sortedFvc = new TreeMap<String, Double>(fvc);
		sortedFvc.putAll(fieldValueCounts);

		int i = 1;
		TreeSet<NameCountData> results = new TreeSet<NameCountData>();
		for (Map.Entry<String, Double> entry : sortedFvc.entrySet()) {
			results.add(new NameCountData(entry.getKey(), entry.getValue().intValue()));
			if (i == 10) {
				break;
			}
			i++;
		}

		return results;
	}

	  public static boolean isPureAscii(String v) {
		    byte bytearray []  = v.getBytes();
		    CharsetDecoder d = Charset.forName("US-ASCII").newDecoder();
		    try {
		      CharBuffer r = d.decode(ByteBuffer.wrap(bytearray));
		      r.toString();
		    }
		    catch(CharacterCodingException e) {
		      return false;
		    }
		    return true;
		  }


//	public void generateGardenHoseRecent() {
//		addToHashTag("ReplaceAColdplaySongWithLlama", 100.0);
//		addToHashTag("12ekimmucizesiEnginAkyurek", 90.0);
//		addToHashTag("ATENÇÃO", 30.0);
//		addToHashTag("BEAUTYandaBEAT", 1.0);
//
//	}

//	private void addToHashTag(String name, Double value) {
//		redisTemplate.opsForZSet().add("hashtags", name, value);
//	}

	public List<NameCountData> getTodayData() {
		List<NameCountData> todayData = new ArrayList<NameCountData>();
		todayData.add(new NameCountData("obama", getCountForToday("obama")));
		todayData.add(new NameCountData("romney", getCountForToday("romney")));
		todayData.add(new NameCountData("bieber", getCountForToday("bieber")));
		return todayData;
	}

	protected String getDailyKey(String candidate, DateTime dateTime) {
		return "election:" + candidate + ":" + dailyFormatter.print(dateTime);
	}

//	public int getTotalForCandidateByDate(String candidate, DateTime dateTime) {
//		String dailyTotKey = "tot:" + this.getDailyKey(candidate, dateTime);
//		String dailyTotValue = redisTemplate.opsForValue().get(dailyTotKey);
//
//		if (dailyTotValue != null) {
//			return Double.valueOf(redisTemplate.opsForValue().get(dailyTotKey)).intValue();
//		}
//
//		return 0;
//
//	}

	public List<GroupedData> getHistoricalData() {

		final DateTime dateTime = new DateTime();

		final DateMidnight dateTime1 = new DateMidnight(dateTime.minusDays(3));
		final DateMidnight dateTime2 = new DateMidnight(dateTime.minusDays(2));
		final DateMidnight dateTime3 = new DateMidnight(dateTime.minusDays(1));
		final DateMidnight dateTime4 = new DateMidnight(dateTime);

		final GroupedData groupedData1 = new GroupedData(dateTime1.toString(dailyFormatter))
			.bieberCount(this.getCountForDay("bieber", dateTime1))
			.obama(this.getCountForDay("obama", dateTime1))
			.romney(this.getCountForDay("romney", dateTime1));

		final GroupedData groupedData2 = new GroupedData(dateTime2.toString(dailyFormatter))
			.bieberCount(this.getCountForDay("bieber", dateTime2))
			.obama(this.getCountForDay("obama", dateTime2))
			.romney(this.getCountForDay("romney", dateTime2));

		final GroupedData groupedData3 = new GroupedData(dateTime3.toString(dailyFormatter))
			.bieberCount(this.getCountForDay("bieber", dateTime3))
			.obama(this.getCountForDay("obama", dateTime3))
			.romney(this.getCountForDay("romney", dateTime3));

		final GroupedData groupedData4 = new GroupedData(dateTime4.toString(dailyFormatter))
			.bieberCount(this.getCountForDay("bieber", dateTime4))
			.obama(this.getCountForDay("obama", dateTime4))
			.romney(this.getCountForDay("romney", dateTime4));

		final List<GroupedData> weeklyData = new ArrayList<GroupedData>();
		weeklyData.add(groupedData1);
		weeklyData.add(groupedData2);
		weeklyData.add(groupedData3);
		weeklyData.add(groupedData4);

		return weeklyData;
	}

	public GroupedData getLastSixHours() {
		GroupedData lastSixHours = new GroupedData("last6");
		DateTime now = new DateTime();
		lastSixHours.setObamaCount(getCountForLastXHours("obama", now, 5));
		lastSixHours.setBieberCount(getCountForLastXHours("bieber", now, 5));
		lastSixHours.setRomneyCount(getCountForLastXHours("romney", now, 5));
		return lastSixHours;
	}

	public GroupedData getLastThreeHours() {
		GroupedData lastThreeHours = new GroupedData("lasthour");
		DateTime now = new DateTime();
		lastThreeHours.setObamaCount(getCountForLastXHours("obama", now, 2));
		lastThreeHours.setBieberCount(getCountForLastXHours("bieber", now, 2));
		lastThreeHours.setRomneyCount(getCountForLastXHours("romney", now, 2));
		return lastThreeHours;

	}

	public GroupedData getLastHour() {
		GroupedData lastHour = new GroupedData("lastsubhour");
		DateTime now = new DateTime();
		lastHour.setObamaCount(getCountForLastXMinutes("obama", now, 59));
		lastHour.setBieberCount(getCountForLastXMinutes("bieber", now, 59));
		lastHour.setRomneyCount(getCountForLastXMinutes("romney", now, 59));
		return lastHour;
	}

	public GroupedData getLast15Minutes() {
		GroupedData lastHour = new GroupedData("last15minutes");
		DateTime now = new DateTime();
		lastHour.setObamaCount(getCountForLastXMinutes("obama", now, 14));
		lastHour.setBieberCount(getCountForLastXMinutes("bieber", now, 14));
		lastHour.setRomneyCount(getCountForLastXMinutes("romney", now, 14));
		return lastHour;
	}

	private  int getCountForToday(String candidate) {

		DateTime dateTime = new DateTime();
		RestTemplate restTemplate = new RestTemplate();
		String url = "http://localhost:8080/metrics/aggregate-counters/" + candidate + "AggregatedCounter";
		AggregateCountsResource count = restTemplate.getForObject(url, AggregateCountsResource.class);

		DateTime currentDateTime = dateTime.hourOfDay().roundCeilingCopy();
		DateTime beginDateTime = dateTime.hourOfDay().setCopy(0);
		Interval interval = new Interval(beginDateTime, currentDateTime);

		int totalCount = 0;

		final SortedMap<Date, Long> values = new TreeMap<Date, Long>(Collections.reverseOrder());
		values.putAll(count.getValues());

		for (Map.Entry<Date, Long> mapEntry : values.entrySet()) {
			DateTime date = new DateTime(mapEntry.getKey());

			if (interval.contains(date)) {
				totalCount = totalCount + mapEntry.getValue().intValue();
			}

		}

		return totalCount;
	}

	private  int getCountForLastXHours(String candidate, DateTime dateTime, int hoursBackInTime) {

		RestTemplate restTemplate = new RestTemplate();
		String url = "http://localhost:8080/metrics/aggregate-counters/" + candidate + "AggregatedCounter";
		AggregateCountsResource count = restTemplate.getForObject(url, AggregateCountsResource.class);

		DateTime currentDateTime = dateTime.hourOfDay().roundCeilingCopy();
		DateTime beginDateTime = dateTime.hourOfDay().roundFloorCopy().minusHours(hoursBackInTime);
		Interval interval = new Interval(beginDateTime, currentDateTime);

		int totalCount = 0;

		final SortedMap<Date, Long> values = new TreeMap<Date, Long>(Collections.reverseOrder());
		values.putAll(count.getValues());

		for (Map.Entry<Date, Long> mapEntry : values.entrySet()) {
			DateTime date = new DateTime(mapEntry.getKey());

			if (interval.contains(date)) {
				totalCount = totalCount + mapEntry.getValue().intValue();
			}

		}

		return totalCount;
	}

	private  int getCountForLastXMinutes(String candidate, DateTime dateTime, int minutesBackInTime) {

		RestTemplate restTemplate = new RestTemplate();
		String url = "http://localhost:8080/metrics/aggregate-counters/" + candidate + "AggregatedCounter?resolution=minute";
		AggregateCountsResource count = restTemplate.getForObject(url, AggregateCountsResource.class);

		DateTime currentDateTime = dateTime.minuteOfHour().roundCeilingCopy();
		DateTime beginDateTime = dateTime.minuteOfHour().roundFloorCopy().minusMinutes(minutesBackInTime);
		Interval interval = new Interval(beginDateTime, currentDateTime);

		int totalCount = 0;

		final SortedMap<Date, Long> values = new TreeMap<Date, Long>(Collections.reverseOrder());
		values.putAll(count.getValues());

		for (Map.Entry<Date, Long> mapEntry : values.entrySet()) {
			DateTime date = new DateTime(mapEntry.getKey());

			if (interval.contains(date)) {
				totalCount = totalCount + mapEntry.getValue().intValue();
			}

		}

		return totalCount;
	}

	private  int getCountForDay(String candidate, DateMidnight dateMidnight) {

		RestTemplate restTemplate = new RestTemplate();
		String url = "http://localhost:8080/metrics/aggregate-counters/" + candidate + "AggregatedCounter";
		AggregateCountsResource count = restTemplate.getForObject(url, AggregateCountsResource.class);

		DateMidnight endOfDay = dateMidnight.plusDays(1);

		Interval interval = new Interval(dateMidnight, endOfDay);

		int totalCount = 0;

		final SortedMap<Date, Long> values = new TreeMap<Date, Long>(Collections.reverseOrder());
		values.putAll(count.getValues());

		for (Map.Entry<Date, Long> mapEntry : values.entrySet()) {
			DateTime date = new DateTime(mapEntry.getKey());

			if (interval.contains(date)) {
				totalCount = totalCount + mapEntry.getValue().intValue();
			}

		}

		return totalCount;
	}

	private class FieldValueComparator implements Comparator<String> {

		Map<String, Double> fieldValueCounts;

		public FieldValueComparator(Map<String, Double> fieldValueCounts) {
			this.fieldValueCounts = fieldValueCounts;
		}

		@Override
		public int compare(String a, String b) {
			if (fieldValueCounts.get(a) > fieldValueCounts.get(b)) {
				return -1;
			}
			if (fieldValueCounts.get(a) == fieldValueCounts.get(b)) {
				return 0;
			}
			else {
				return 1;
			}
		}

	}

}
