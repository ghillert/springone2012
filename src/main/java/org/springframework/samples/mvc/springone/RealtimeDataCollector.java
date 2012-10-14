package org.springframework.samples.mvc.springone;

import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.CharacterCodingException;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import org.apache.commons.lang.math.NumberUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.connection.srp.SrpConnectionFactory;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.data.redis.core.ZSetOperations.TypedTuple;
import org.springframework.util.StringUtils;

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
		todayData.add(new NameCountData("obama", getTodayObama()));
		todayData.add(new NameCountData("romney", getTodayRomney()));
		todayData.add(new NameCountData("bieber", getTodayBieber()));
		return todayData;
	}
	
	public int getTodayObama() {
		String dailyTotKey = "tot:" + this.getDailyKey("obama", new DateTime());
		return Double.valueOf(redisTemplate.opsForValue().get(dailyTotKey)).intValue();
	}

	public int getTodayRomney() {
		String dailyTotKey = "tot:" + this.getDailyKey("romney", new DateTime());
		return Double.valueOf(redisTemplate.opsForValue().get(dailyTotKey)).intValue();
	}

	public int getTodayBieber() {
		String dailyTotKey = "tot:" + this.getDailyKey("bieber", new DateTime());
		return Double.valueOf(redisTemplate.opsForValue().get(dailyTotKey)).intValue();
	}
	
	protected String getDailyKey(String candidate, DateTime dateTime) {
		return "election:" + candidate + ":" + dailyFormatter.print(dateTime);
	}
	
	public int getTotalForCandidateByDate(String candidate, DateTime dateTime) {
		String dailyTotKey = "tot:" + this.getDailyKey(candidate, dateTime);
		return Double.valueOf(redisTemplate.opsForValue().get(dailyTotKey)).intValue();
	}

	public List<GroupedData> getHistoricalData() {
		ValueOperations<String,String> valueOps = redisTemplate.opsForValue();
		//redisTemplate.opsForValue().get("tot:election:obama:2012-10-12")		
		GroupedData friday = new GroupedData("10/12/2012");//.bieberCount(400).obama(200).romney(200);
		addCountsToGroup(friday, new DateTime(2012, 10, 12, 0, 0));

		GroupedData saturday = new GroupedData("10/13/2012");//.bieberCount(500).obama(300).romney(300);
		addCountsToGroup(saturday, new DateTime(2012, 10, 13, 0, 0));
		
		GroupedData sunday = new GroupedData("10/14/2012");//.bieberCount(450).obama(250).romney(225);
		addCountsToGroup(sunday, new DateTime(2012, 10, 14, 0, 0));
		
		GroupedData monday = new GroupedData("10/15/2012");//.bieberCount(400).obama(225).romney(250);
		addCountsToGroup(monday, new DateTime(2012, 10, 15, 0, 0));
	
		List<GroupedData> weeklyData = new ArrayList<GroupedData>();
		weeklyData.add(friday);
		weeklyData.add(saturday);
		weeklyData.add(sunday);
		weeklyData.add(monday);
		return weeklyData;
	}	

	
	private void addCountsToGroup(GroupedData groupedData, DateTime dateTime) {
		groupedData.setObamaCount(getTotalForCandidateByDate("obama", dateTime));
		groupedData.setRomneyCount(getTotalForCandidateByDate("romney", dateTime));
		groupedData.setBieberCount(getTotalForCandidateByDate("bieber", dateTime));
	}
	
	
	//lastFewHours = new GroupedData("last6").bieberCount(600).obama(600).romney(2400);
	//lastHour = new GroupedData("lasthour").bieberCount(100).obama(100).romney(400);
	//lastSubHour = new GroupedData("lastsubhour").bieberCount(25).obama(25).romney(100);
	
	public GroupedData getLastSixHours() {
		GroupedData lastSixHours = new GroupedData("last6");
		DateTime now = new DateTime();
		lastSixHours.setObamaCount(getCountForLastXHours("obama", now, 6));
		lastSixHours.setBieberCount(getCountForLastXHours("bieber", now, 6));		
		lastSixHours.setRomneyCount(getCountForLastXHours("romney", now, 6));	
		return lastSixHours;
	}

	public GroupedData getLastThreeHours() {				
		GroupedData lastThreeHours = new GroupedData("lasthour");
		DateTime now = new DateTime();
		lastThreeHours.setObamaCount(getCountForLastXHours("obama", now, 3));
		lastThreeHours.setBieberCount(getCountForLastXHours("bieber", now, 3));		
		lastThreeHours.setRomneyCount(getCountForLastXHours("romney", now, 3));			
		return lastThreeHours;
		
	}

	public GroupedData getLastHour() {
		//TODO note, name mismatch with UI
		GroupedData lastHour = new GroupedData("lastsubhour");
		DateTime now = new DateTime();
		lastHour.setObamaCount(getCountForHour("obama", now));
		lastHour.setBieberCount(getCountForHour("bieber", now));		
		lastHour.setRomneyCount(getCountForHour("romney", now));	
		return lastHour;
	}
	
	
	private  int getCountForLastXHours(String candidate, DateTime dateTime, int hoursBackInTime) {
		
		int totalCount = getCountForHour(candidate, dateTime);
		for (int i=1; i<=hoursBackInTime; i++) {
			totalCount += getCountForHour(candidate, dateTime.minusHours(i));
		}			
		return totalCount;
	}
	
	private int getCountForHour(String candidate, DateTime today) {
		
		String lastHourHashKey = "hash:" + this.getHourlyKey(candidate, today);
		int totalCount = 0;
		if (jedisTemplate.hasKey(lastHourHashKey)) {
			// System.out.println(lastHourHashKey);
			Map<Object, Object> map = jedisTemplate.opsForHash().entries(
					lastHourHashKey);

			for (Map.Entry<Object, Object> cursor : map.entrySet()) {
				// System.out.println(cursor.getKey() + "," +
				// cursor.getValue());
				totalCount += Double.valueOf(cursor.getValue().toString())
						.intValue();
			}
		} else {
			//System.err.println("no key found for " + lastHourHashKey);
		}
		//System.out.println(lastHourHashKey + ", total count = "	+ totalCount);
		//System.out.println("------");
		return totalCount;
	}
	public String getHourlyKey(String candidate, DateTime dateTime) {
		return "election:" + candidate + ":" + hourlyFormatter.print(dateTime);
	}
}
