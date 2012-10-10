package org.springframework.samples.mvc.springone;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class ElectionController {

	private RealtimeData realTimeData = new RealtimeData();
	
	@RequestMapping(value="/election/counts/historical", method=RequestMethod.GET, produces="application/json")
	public @ResponseBody List<GroupedData> historicalData() {
		return getHistoricalData();
	}
	
	@RequestMapping(value="/election/counts/today", method=RequestMethod.GET, produces="application/json")
	public @ResponseBody List<CandidateData> todayData() {
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

	@RequestMapping(value="/")
	public String home() {
		return "home";
	}

	@RequestMapping(value="/gardenhose")
	public String gardenhose() {
		return "gardenhose";
	}
	
	private List<MessageRate> getMessageRates() {
		List<MessageRate> rates = new ArrayList();
		rates.add(realTimeData.getTweetRate());
		rates.add(realTimeData.getHashTagHitRate());		
		return rates;
	}

	private List<GroupedData> getBreakdowns() {
		List<GroupedData> breakdownToday = new ArrayList();
		breakdownToday.add(realTimeData.getLastFewHours());
		breakdownToday.add(realTimeData.getLastHour());
		breakdownToday.add(realTimeData.getLastSubHour());
		return breakdownToday;
	}
	
	private List<CandidateData> getTodayData() {
		List<CandidateData> todayData = new ArrayList();
		todayData.add(new CandidateData("obama", realTimeData.getTodayObama()));
		todayData.add(new CandidateData("romney", realTimeData.getTodayRomney()));
		todayData.add(new CandidateData("bieber", realTimeData.getTodayBieber()));		
		return todayData;
	}

	private List<GroupedData> getHistoricalData() {
		GroupedData monday = new GroupedData("10/8/2012").bieberCount(400).obama(200).romney(200);
		GroupedData tuesday = new GroupedData("10/9/2012").bieberCount(500).obama(300).romney(300);
		GroupedData wednesday = new GroupedData("10/10/2012").bieberCount(450).obama(250).romney(225);
		GroupedData thursday = new GroupedData("10/11/2012").bieberCount(400).obama(225).romney(250);
		GroupedData friday = new GroupedData("10/12/2012").bieberCount(300).obama(325).romney(376);
	
		
		List<GroupedData> weeklyData = new ArrayList();
		weeklyData.add(monday);
		weeklyData.add(tuesday);
		weeklyData.add(wednesday);
		weeklyData.add(thursday);
		weeklyData.add(friday);
		return weeklyData;
	}

}
