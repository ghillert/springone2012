package org.springframework.samples.mvc.springone;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ResourceLoaderAware;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class ElectionController implements InitializingBean, ResourceLoaderAware {

	private RealtimeDataCollector realTimeData = new RealtimeDataCollector();

	@Value("${mapReduceOutputResourceName}")
	private String resourceName;
	
	private String defaultResourceName = "classpath:/test-data/part-00000";
	private ResourceLoader resourceLoader;
	
	@Autowired
	private MapReduceResults mapReduceResults;

	@RequestMapping(value="/election/counts/historical", method=RequestMethod.GET, produces="application/json")
	public @ResponseBody List<GroupedData> historicalData() {
		return getHistoricalData();
	}

	@RequestMapping(value="/election/counts/today", method=RequestMethod.GET, produces="application/json")
	public @ResponseBody List<NameCountData> todayData() {
		return getTodayData();
	}

	@RequestMapping(value="/election/breakdowns/today", method=RequestMethod.GET, produces="application/json")
	public @ResponseBody List<GroupedData> breakdowns() {
		return getBreakdowns();
	}

	@RequestMapping(value="/election/monitoring/rates", method=RequestMethod.GET, produces="application/json")
	public @ResponseBody List<MessageRate> messageRates() {
		return getMessageRates();
	}

	@RequestMapping(value="/twitter/gardenhose/recent", method=RequestMethod.GET, produces="application/json")
	public @ResponseBody Set<NameCountData> gardenhoseRecent() {
		return getGardenHoseRecent();
	}
	
	@RequestMapping(value="/twitter/gardenhose/recent/generate", method=RequestMethod.GET, produces="application/json")
	public @ResponseBody String gardenhoseGenerateRecent() {
		realTimeData.generateGardenHoseRecent();
		return "ok";
	}

	@RequestMapping(value="/twitter/gardenhose/historical", method=RequestMethod.GET, produces="application/json")
	public @ResponseBody Set<NameCountData> gardenhoseHistorical() {
		return getGardenHoseHistorical();
	}

	@RequestMapping(value="/")
	public String home() {
		return "home";
	}

	@RequestMapping(value="/gardenhose")
	public String gardenhose() {
		return "gardenhose";
	}

	private List<MessageRate> getMessageRates() {
		List<MessageRate> rates = new ArrayList<MessageRate>();
		rates.add(realTimeData.getTweetRate());
		rates.add(realTimeData.getHashTagHitRate());
		return rates;
	}

	private List<GroupedData> getBreakdowns() {
		List<GroupedData> breakdownToday = new ArrayList<GroupedData>();
		breakdownToday.add(realTimeData.getLastFewHours());
		breakdownToday.add(realTimeData.getLastHour());
		breakdownToday.add(realTimeData.getLastSubHour());
		return breakdownToday;
	}

	private List<NameCountData> getTodayData() {
		List<NameCountData> todayData = new ArrayList<NameCountData>();
		todayData.add(new NameCountData("obama", realTimeData.getTodayObama()));
		todayData.add(new NameCountData("romney", realTimeData.getTodayRomney()));
		todayData.add(new NameCountData("bieber", realTimeData.getTodayBieber()));
		return todayData;
	}

	private List<GroupedData> getHistoricalData() {
		GroupedData monday = new GroupedData("10/8/2012").bieberCount(400).obama(200).romney(200);
		GroupedData tuesday = new GroupedData("10/9/2012").bieberCount(500).obama(300).romney(300);
		GroupedData wednesday = new GroupedData("10/10/2012").bieberCount(450).obama(250).romney(225);
		GroupedData thursday = new GroupedData("10/11/2012").bieberCount(400).obama(225).romney(250);
		GroupedData friday = new GroupedData("10/12/2012").bieberCount(300).obama(325).romney(376);


		List<GroupedData> weeklyData = new ArrayList<GroupedData>();
		weeklyData.add(monday);
		weeklyData.add(tuesday);
		weeklyData.add(wednesday);
		weeklyData.add(thursday);
		weeklyData.add(friday);
		return weeklyData;
	}

	private  Set<NameCountData> getGardenHoseRecent() {
		return realTimeData.getGardenHoseRecent();
	}


	private Set<NameCountData> getGardenHoseHistorical() {
		Set<NameCountData> todayData = mapReduceResults.getResults(resourceName);
		return todayData;
	}

	@Override
	public void setResourceLoader(ResourceLoader resourceLoader) {
		this.resourceLoader = resourceLoader;
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		Resource resource = resourceLoader.getResource(resourceName);
		if (!resource.exists()) {
			this.resourceName = this.defaultResourceName;
		}
		
	}

}
