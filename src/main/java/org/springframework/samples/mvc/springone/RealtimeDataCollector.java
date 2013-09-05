package org.springframework.samples.mvc.springone;

import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.CharacterCodingException;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.TreeSet;

import org.apache.commons.lang.math.NumberUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.joda.time.DateMidnight;
import org.joda.time.DateTime;
import org.joda.time.Interval;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.connection.srp.SrpConnectionFactory;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.data.redis.core.ZSetOperations.TypedTuple;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;
import org.springframework.xd.rest.client.domain.metrics.AggregateCountsResource;

public class RealtimeDataCollector {

	private static final Log log = LogFactory.getLog(RealtimeDataCollector.class);

	private DateTimeFormatter dailyFormatter = DateTimeFormat.forPattern("yyyy-MM-dd");

	private DateTimeFormatter hourlyFormatter = DateTimeFormat.forPattern("yyyy-MM-dd-HH");

	private int todayObama = 4304;

	private int todayRomney = 4304;

	private int todayBieber = 13450;

	//private GroupedData lastFewHours;

	//private GroupedData lastHour;

	//private GroupedData lastSubHour;

	private MessageRate tweetRate;

	private MessageRate hashTagHitRate;

	private StringRedisTemplate redisTemplate;

	private StringRedisTemplate jedisTemplate;

	public RealtimeDataCollector() {

		//lastFewHours = new GroupedData("last6").bieberCount(600).obama(600).romney(2400);
		//lastHour = new GroupedData("lasthour").bieberCount(100).obama(100).romney(400);
		//lastSubHour = new GroupedData("lastsubhour").bieberCount(25).obama(25).romney(100);

		tweetRate = new MessageRate("tweets", 120);
		hashTagHitRate = new MessageRate("hashtag-hit", 10);

		//TODO cleanup, configure with DI
		SrpConnectionFactory connectionFactory = new SrpConnectionFactory();
		connectionFactory.afterPropertiesSet();
		StringRedisTemplate stringRedisTemplate = new StringRedisTemplate();
		stringRedisTemplate.setConnectionFactory(connectionFactory);
		stringRedisTemplate.afterPropertiesSet();
		this.redisTemplate = stringRedisTemplate;

		//opsForHash.entries didn't work for SrpConnectionFactory - debug later.

		JedisConnectionFactory cf = new JedisConnectionFactory();
		cf.afterPropertiesSet();
		jedisTemplate = new StringRedisTemplate();
		jedisTemplate.setConnectionFactory(cf);
		jedisTemplate.afterPropertiesSet();
	}

	public MessageRate getTweetRate() {
		tweetRate.adjustRate();
		return tweetRate;
	}

	public MessageRate getHashTagHitRate() {
		hashTagHitRate.adjustRate();
		return hashTagHitRate;
	}

	public Set<NameCountData> getGardenHoseRecent() {
		Set<TypedTuple<String>> results = redisTemplate.opsForZSet().rangeByScoreWithScores("hashtags", 0, 100);

		TreeSet<NameCountData> convertedResults = new TreeSet<NameCountData>();
		for (TypedTuple<String> typedTuple : results) {
			try {
			String tag = typedTuple.getValue();
			String score = typedTuple.getScore().toString();
			if (StringUtils.hasText(tag) && StringUtils.hasText(score)) {
				//sometimes hastags are numbers
				if (NumberUtils.isNumber(tag) && NumberUtils.isNumber(score) && isPureAscii(tag)) {
					convertedResults.add(new NameCountData(tag, typedTuple.getScore().intValue()));
				} else if (NumberUtils.isNumber(tag) && !NumberUtils.isNumber(score)) {
					// in Right to left languages, the order is reversed.
					// so tag would be a number and often score would be text
					convertedResults.add(new NameCountData(score, Double.valueOf(tag).intValue()));
				} else {
					convertedResults.add(new NameCountData(tag, typedTuple.getScore().intValue()));
				}
			}
			} catch (NumberFormatException e) {
				log.error("NumberFormatException" ,e);
			}
		}
		return convertedResults;
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


	public void generateGardenHoseRecent() {
		addToHashTag("ReplaceAColdplaySongWithLlama", 100.0);
		addToHashTag("12ekimmucizesiEnginAkyurek", 90.0);
		addToHashTag("ATENÇÃO", 30.0);
		addToHashTag("BEAUTYandaBEAT", 1.0);

	}

	private void addToHashTag(String name, Double value) {
		redisTemplate.opsForZSet().add("hashtags", name, value);
	}

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

	public int getTotalForCandidateByDate(String candidate, DateTime dateTime) {
		String dailyTotKey = "tot:" + this.getDailyKey(candidate, dateTime);
		String dailyTotValue = redisTemplate.opsForValue().get(dailyTotKey);

		if (dailyTotValue != null) {
			return Double.valueOf(redisTemplate.opsForValue().get(dailyTotKey)).intValue();
		}

		return 0;

	}

	public List<GroupedData> getHistoricalData() {

		DateTime dateTime = new DateTime();

		DateTimeFormatter formatter = DateTimeFormat.forPattern("MM/dd/yyyy");

		DateMidnight dateTime1 = new DateMidnight(dateTime.minusDays(3));
		DateMidnight dateTime2 = new DateMidnight(dateTime.minusDays(2));
		DateMidnight dateTime3 = new DateMidnight(dateTime.minusDays(1));
		DateMidnight dateTime4 = new DateMidnight(dateTime);

		GroupedData groupedData1 = new GroupedData(dateTime1.toString(formatter)) //"10/12/2012"
			.bieberCount(this.getCountForDay("bieber", dateTime1))
			.obama(this.getCountForDay("obama", dateTime1))
			.romney(this.getCountForDay("romney", dateTime1));

		GroupedData groupedData2 = new GroupedData(dateTime2.toString(formatter))
		.bieberCount(this.getCountForDay("bieber", dateTime2))
		.obama(this.getCountForDay("obama", dateTime2))
		.romney(this.getCountForDay("romney", dateTime2));

		GroupedData groupedData3 = new GroupedData(dateTime3.toString(formatter))
		.bieberCount(this.getCountForDay("bieber", dateTime3))
		.obama(this.getCountForDay("obama", dateTime3))
		.romney(this.getCountForDay("romney", dateTime3));

		GroupedData groupedData4 = new GroupedData(dateTime4.toString(formatter))
		.bieberCount(this.getCountForDay("bieber", dateTime4))
		.obama(this.getCountForDay("obama", dateTime4))
		.romney(this.getCountForDay("romney", dateTime4));

		List<GroupedData> weeklyData = new ArrayList<GroupedData>();
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
		String url = "http://localhost:8080/metrics/aggregate-counters/" + candidate + "Count";
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
		String url = "http://localhost:8080/metrics/aggregate-counters/" + candidate + "Count";
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
		String url = "http://localhost:8080/metrics/aggregate-counters/" + candidate + "Count?resolution=minute";
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
		String url = "http://localhost:8080/metrics/aggregate-counters/" + candidate + "Count";
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

}
