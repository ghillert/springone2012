package org.springframework.samples.mvc.springone;

import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.CharacterCodingException;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.util.Set;
import java.util.TreeSet;

import org.apache.commons.lang.math.NumberUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.data.redis.connection.srp.SrpConnectionFactory;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ZSetOperations.TypedTuple;
import org.springframework.util.StringUtils;

public class RealtimeDataCollector {

	private static final Log log = LogFactory.getLog(RealtimeDataCollector.class);
	
	private int todayObama = 4304;

	private int todayRomney = 4304;

	private int todayBieber = 13450;

	private GroupedData lastFewHours;

	private GroupedData lastHour;

	private GroupedData lastSubHour;

	private MessageRate tweetRate;

	private MessageRate hashTagHitRate;
	
	private StringRedisTemplate redisTemplate;

	public RealtimeDataCollector() {
		lastFewHours = new GroupedData("last6").bieberCount(600).obama(600).romney(2400);
		lastHour = new GroupedData("lasthour").bieberCount(100).obama(100).romney(400);
		lastSubHour = new GroupedData("lastsubhour").bieberCount(25).obama(25).romney(100);
		tweetRate = new MessageRate("tweets", 120);
		hashTagHitRate = new MessageRate("hashtag-hit", 10);
		
		SrpConnectionFactory connectionFactory = new SrpConnectionFactory();
		connectionFactory.afterPropertiesSet();
		StringRedisTemplate stringRedisTemplate = new StringRedisTemplate();
		stringRedisTemplate.setConnectionFactory(connectionFactory);
		stringRedisTemplate.afterPropertiesSet();
		this.redisTemplate = stringRedisTemplate;
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
}
