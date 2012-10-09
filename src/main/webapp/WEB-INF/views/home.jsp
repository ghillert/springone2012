<%@ include file="/WEB-INF/views/includes/taglibs.jsp"%>
<!DOCTYPE html>
<html lang="en">
	<head>
		<meta charset="utf-8">
		<title>Election - Historical Analysis</title>
		<meta name="viewport" content="width=device-width, initial-scale=1.0">
		<meta name="description" content="">
		<meta name="author" content="">

		<!-- Le styles -->

		<link href="<c:url value='/assets/css/bootstrap.css'/>" rel="stylesheet" />
		<link href="<c:url value='/assets/css/custom.css'/>" rel="stylesheet" />
		<link href="<c:url value='/assets/css/bootstrap-responsive.css'/>" rel="stylesheet" />

		<!-- Le HTML5 shim, for IE6-8 support of HTML5 elements -->
		<!--[if lt IE 9]>
			<script src="http://html5shim.googlecode.com/svn/trunk/html5.js"></script>
		<![endif]-->

		<!-- Le fav and touch icons -->
		<link rel="shortcut icon" href="<c:url value='/assets/ico/favicon.ico'/>">
		<link rel="apple-touch-icon-precomposed" sizes="144x144" href="<c:url value='/assets/ico/apple-touch-icon-144-precomposed.png'/>">
		<link rel="apple-touch-icon-precomposed" sizes="114x114" href="<c:url value='/assets/ico/apple-touch-icon-114-precomposed.png'/>">
		<link rel="apple-touch-icon-precomposed" sizes="72x72" href="<c:url value='/assets/ico/apple-touch-icon-72-precomposed.png'/>">
		<link rel="apple-touch-icon-precomposed" href="<c:url value='/assets/ico/apple-touch-icon-57-precomposed.png'/>">
	</head>

	<body>

		<div class="navbar navbar-inverse navbar-fixed-top">
			<div class="navbar-inner">
				<div class="container">
					<a class="btn btn-navbar" data-toggle="collapse" data-target=".nav-collapse">
						<span class="icon-bar"></span>
						<span class="icon-bar"></span>
						<span class="icon-bar"></span>
					</a>
					<a class="brand" href="#">Election Analytics</a>
					<div class="nav-collapse">
						<ul class="nav">
							<li class="active"><a href="#">Home</a></li>
							<li><a href="#aboutModal" data-toggle="modal">About</a></li>
							<li class="dropdown" id="menu-resources">
								<a class="dropdown-toggle" data-toggle="dropdown" href="#menu-resources">
									Resources
									<b class="caret"></b>
								</a>
								<ul class="dropdown-menu">
									<li><a href="https://github.com/ghillert/springone2012"><i class="icon-home"></i> Source Code</a></li>
									<li><a href="http://www.springone2gx.com/"><i class="icon-home"></i> SpringOne</a></li>
								</ul>
							</li>
						</ul>
					</div><!--/.nav-collapse -->
				</div>
			</div>
		</div>

		<div class="container">
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
			<div class="row">
				<div class="span12" id="message-rates">
					<h3>Message Rates</h3>
					<div id='gauge_chart_div' style="width: 500px; margin-left: auto; margin-right: auto;"></div>
				</div>
			</div>
			<hr/>
		<footer>
			<a class="brand" href="http://www.springsource.org/"><img alt="SpringSource"
					title="SpringSource" src="${ctx}/assets/img/spring/SpringSource-logo.png"></a>
		</footer>

		</div> <!-- /container -->

		<div id="aboutModal" class="modal" style="display: none;">
			<div class="modal-header">
				<button class="close" data-dismiss="modal" type="button">×</button>
				<h3>About...</h3>
			</div>
			<div class="modal-body">
				<h4>Election Demo</h4>
				<p>
					Implemented by Gunnar Hillert.
				</p>
				<hr>
			</div>
			<div class="modal-footer">
				<a class="btn" data-dismiss="modal" href="#">Close</a>
			</div>
		</div>

		<!-- Le javascript
		=================================================================== -->

		<%-- <script src="<c:url value='/wro/g1.js'/>"></script> --%>

		<script src="<c:url value='/assets/js/jquery.js'/>"></script>
		<script src="<c:url value='/assets/js/bootstrap.js'/>"></script>
		<script src="<c:url value='/assets/js/handlebars.js'/>"></script>

		<script src="<c:url value='/assets/js/custom.js'/>"></script>

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

		<script type="text/javascript">

			$(function() {

				if (!window.console) {
					console = {log: function() {}};
				}

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
				
				function updateMessageRates() {
					
					$.getJSON('election/monitoring/rates', function(data) {

						  console.log(data);
						  
						  var items = [];
						  
						  var tweets = data[0].rate;
						  var hashTagHits = data[1].rate;
						  
						  items.push($.mynamespace.gaugeHeader);

						  items.push(['Tweets', tweets]);
						  items.push(['# Hit Rate', hashTagHits]);

						  console.log(items);
						  
						  var data = google.visualization.arrayToDataTable(items);
						  $.mynamespace.gauge.draw(data, $.mynamespace.gaugeOptions	);
					});
					
				}
				
				$("#updateHistory").click(function() {					
					updateVerticalChart();
				});

				$("#updateTodaysVotes").click(function() {					
					updateTodaysVote();
				});
				
				$("#updateDailyBreakDowns").click(function() {					
					updateDailyBreakDowns();
				});
				
				$("#updateMessageRates").click(function() {					
					updateMessageRates();
				});

				function updateAll() {
					updateVerticalChart();
					updateTodaysVote();
					updateDailyBreakDowns();
					updateMessageRates();
					setTimeout(updateAll, 5000);
				}
				
				updateAll();
				
			});
		</script>

<script type="text/javascript" src="https://www.google.com/jsapi"></script>
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
	</body>
</html>
