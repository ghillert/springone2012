<%@ include file="/WEB-INF/views/includes/taglibs.jsp"%>
<html>
	<head>
		<title>Election - Twitter Search</title>
	</head>
		<body>
			<div class="row">
				<div class="span12" id="historical-analysis">
					<h3>Historical Analysis</h3>
					<div id="chart_div" style="width: 900px; height: 300px;"></div>
				</div>
			</div>
			<div class="row">
				<div class="span12" id="todays-votes">
					<h3>Todays Votes</h3>
					<div class="row">
						<div class="span3">
							<div style="text-align: right;">
							<img alt="" src="https://twimg0-a.akamaihd.net/profile_images/2325704772/wrrmef61i6jl91kwkmzq.png" width="164" height="164"/>
							<div id="todays-votes-obama" style="width: 164px; text-align: center; margin-left: auto;">0</div>
							</div>
						</div>
						<div class="span3">
							<div style="text-align: center;">
							<img alt="" src="https://twimg0-a.akamaihd.net/profile_images/2624978379/chw1hdzozfdew973pvjr.png"  width="164" height="164"/>
							<div id="todays-votes-romney" style="width: 164px; text-align: center; margin-left: auto; margin-right: auto;">0</div>
							</div>

						</div>
						<div class="span3">
							<div style="text-align: left;">
							<img alt="" src="https://twimg0-a.akamaihd.net/profile_images/2385531870/ffb6obdzkxc3pk7lvbw2.jpeg"  width="164" height="164"/>
							<div id="todays-votes-bieber" style="width: 164px; text-align: center; ">0</div>
							</div>

						</div>
					</div>
				</div>
			</div>
			<div class="row">
				<div class="span12" id="daily-count-break-downs">
					<h3>Daily Count Break Downs</h3>
					<div class="row">
						<div class="span3">
							<div class="row">
								<div class="span3"><h4>Last 6 hours</h4></div>
							</div>
							<div class="row">
								<div class="span3" id="daily-count-break-downs-last-6-hours">
									<table class="table table-striped table-bordered table-hover" style="margin-left: auto; margin-right: auto;">
										<thead>
											<tr><th>Candidate</th><th>Votes</th></tr>
										</thead>
										<tbody>
											<tr><td>Obama</td><td>0</td></tr>
											<tr><td>Romney</td><td>0</td></tr>
											<tr><td>Bieber</td><td>0</td></tr>
										</tbody>
									</table>
								</div>
							</div>
						</div>
						<div class="span3">
							<div class="row">
								<div class="span3"><h4>Last hour</h4></div>
							</div>
							<div class="row">
								<div class="span3" id="daily-count-break-downs-last-hour">
									<table class="table table-striped table-bordered table-hover" style="margin-left: auto; margin-right: auto;">
										<thead>
											<tr><th>Candidate</th><th>Votes</th></tr>
										</thead>
										<tbody>
											<tr><td>Obama</td><td>0</td></tr>
											<tr><td>Romney</td><td>0</td></tr>
											<tr><td>Bieber</td><td>0</td></tr>
										</tbody>
									</table>
								</div>
							</div>
						</div>
						<div class="span3">
							<div class="row">
								<div class="span3"><h4>Last 15 minutes</h4></div>
							</div>
							<div class="row">
								<div class="span3" id="daily-count-break-downs-last-15-min">
									<table class="table table-striped table-bordered table-hover" style="margin-left: auto; margin-right: auto;">
										<thead>
											<tr><th>Candidate</th><th>Votes</th></tr>
										</thead>
										<tbody>
											<tr><td>Obama</td><td>0</td></tr>
											<tr><td>Romney</td><td>0</td></tr>
											<tr><td>Bieber</td><td>0</td></tr>
										</tbody>
									</table>
								</div>
							</div>
						</div>
					</div>
				</div>
			</div>
		</body>

		<content tag='bottom'>
			<script id="daily-template" type="text/x-handlebars-template">
			<table class="table table-striped table-bordered table-hover" style="margin-left: auto; margin-right: auto;">
				<thead>
					<tr><th>Candidate</th><th>Votes</th></tr>
				</thead>
				<tbody>
					<tr><td>Obama</td><td>{{countObama}}</td></tr>
					<tr><td>Romney</td><td>{{countRomney}}</td></tr>
					<tr><td>Bieber</td><td>{{countBieber}}</td></tr>
				</tbody>
			</table>
			</script>
			<script>
				$(function() {
					$.mynamespace = {
							gauge: null,
							gaugeOptions : null,
							gaugeHeader: ['Label', 'Value'],
							barChart : null,
							barChartOptions : null,
							barChartDataHeader: ['Date', 'Obama', 'Romney', 'Bieber'],
							barChartData: [
											['Date', 'Obama', 'Romney', 'Bieber'],
											 ['2004',  1000, 400, 2000]
										]
					};

					function updateVerticalChart() {

						$.getJSON('election/counts/historical', function(data) {

							console.log(data);

							var items = [];

							items.push($.mynamespace.barChartDataHeader);

							for (var i = 0, len = data.length; i < len; i++) {
								var result = data[i];
									items.push([result.label, result.obamaCount, result.romneyCount, result.bieberCount]);
							}
							console.log(items);

							var data = google.visualization.arrayToDataTable(items);
							$.mynamespace.barchart.draw(data, $.mynamespace.barChartOptions	);
						});
					}

					function updateTodaysVote() {

						$.getJSON('election/counts/today', function(data) {

							console.log(data);

							var todaysVotesObama  = data[0];
							var todaysVotesRomney = data[1];
							var todaysVotesBieber = data[2];

							$("#todays-votes-obama").html(todaysVotesObama.count);
							$("#todays-votes-romney").html(todaysVotesRomney.count);
							$("#todays-votes-bieber").html(todaysVotesBieber.count);
						});
					}

					function updateDailyBreakDowns() {

						function generateHtml(data, divId) {
							var source   = $("#daily-template").html();
							var template = Handlebars.compile(source);

							var context = {
								countObama: data.obamaCount,
								countRomney: data.romneyCount,
								countBieber: data.bieberCount
							};

							var html    = template(context);

							$(divId).html(html);
						}

						$.getJSON('election/breakdowns/today', function(data) {

							console.log(data);

							var last6Hours = data[0];
							var lastHour   = data[1];
							var last15min  = data[2];

							generateHtml(last6Hours, "#daily-count-break-downs-last-6-hours");
							generateHtml(lastHour,   "#daily-count-break-downs-last-hour");
							generateHtml(last15min,  "#daily-count-break-downs-last-15-min");
						});
					}

					function updateAll() {
						updateVerticalChart();
						updateTodaysVote();
						updateDailyBreakDowns();
						setTimeout(updateAll, 4000);
					}

					updateAll();
				});
			</script>
			<script type="text/javascript" src="http://www.google.com/jsapi"></script>
			<script type="text/javascript">
				google.load("visualization", "1", {packages:["corechart"]});
				google.setOnLoadCallback(drawChart);
				function drawChart() {
					var data = google.visualization.arrayToDataTable($.mynamespace.barChartData);

					$.mynamespace.barChartOptions = {
						hAxis: {title: 'Dates', titleTextStyle: {color: 'red'}},
						vAxis: {title: 'Votes', titleTextStyle: {color: 'red'}}
					};

					$.mynamespace.barchart =  new google.visualization.ColumnChart(document.getElementById('chart_div'));
					$.mynamespace.barchart.draw(data, $.mynamespace.barChartOptions);
				}
			</script>
			<script type='text/javascript'>
				google.load('visualization', '1', {packages:['gauge']});
				google.setOnLoadCallback(drawChart);
				function drawChart() {
					var data = google.visualization.arrayToDataTable([
						['Label', 'Value'],
						['Tweets', 0],
						['# Hit Rate', 0],
					]);

					$.mynamespace.gaugeOptions = {
						width: 500, height: 200,
						redFrom: 130, redTo: 150,
						yellowFrom:100, yellowTo: 130,
						minorTicks: 5,
						max: 150
					};

					$.mynamespace.gauge = new google.visualization.Gauge(document.getElementById('gauge_chart_div'));
					$.mynamespace.gauge.draw(data, $.mynamespace.gaugeOptions);
				}
			</script>
		</content>
	</body>
</html>
