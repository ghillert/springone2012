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
							<li><a href="<c:url value="/"/>">Twitter Search</a></li>
							<li class="active"><a href='<c:url value="/gardenhose"/>'>Twitter Gardenhose</a></li>
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
				<div class="span6">
					<h3>Garden Hose Recent</h3>
					<div id="garden-hose-data-recent">Loading...</div>
				</div>
				<div class="span6">
					<div id="garden-hose-data-recent-container">
				      <canvas width="400" height="300" id="garden-hose-data-recent-canvas" style="display: none;">
				        <p>Anything in here will be replaced on browsers that support the canvas element</p>
				      </canvas>
				    </div>
				    <div id="garden-hose-data-recent-tags">
				    </div>
				</div>
			</div>
			<div class="row">
				<div class="span6">
					<h3>Garden Hose Historical</h3>
					<div id="garden-hose-data-historical">Loading...</div>
				</div>
				<div class="span6">
					<div id="garden-hose-data-historical-container">
				      <canvas width="400" height="300" id="garden-hose-data-historical-canvas" style="display: none;">
				        <p>Anything in here will be replaced on browsers that support the canvas element</p>
				      </canvas>
				    </div>
				    <div id="garden-hose-data-historical-tags">
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
		<script src="<c:url value='/assets/js/jquery.tagcanvas.js'/>"></script>
		<script src="<c:url value='/assets/js/custom.js'/>"></script>

		<script id="garden-hose-template" type="text/x-handlebars-template">
			<table class="table table-striped table-bordered table-hover" style="margin-left: auto; margin-right: auto;">
				<thead>
					<tr><th>Tag</th><th>Count</th></tr>
				</thead>
				<tbody>
					{{#each tag}}
						<tr><td>{{name}}</td><td>{{count}}</td></tr>
					{{/each}}
				</tbody>
			</table>
		</script>

		<script id="garden-hose-template-tag-cloud" type="text/x-handlebars-template">
			<ul>
				{{#each tag}}
					<li><a data-weight="{{{weight count}}}" href="https://twitter.com/search?q=%23{{name}}&src=hash" target="_blank">{{name}}</a></li>
				{{/each}}
			</ul>
		</script>

		<script type="text/javascript">

			$(function() {

				if (!window.console) {
					console = {log: function() {}};
				}

				$.mynamespace = {
						gauge: null,
						gaugeOptions : null,
						gaugeHeader: ['Label', 'Value']
				};
				
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
				
				var total = 0;

				Handlebars.registerHelper('weight', function(count) {
					  return new Handlebars.SafeString(
							  count/total * 100
					  );
				});
				
				function updateGardenHoseData(divId, url) {

					$.getJSON(url, function(data) {

						total = 0;
						
						console.log(data);

						for (i in data) {
					          total = total + data[i].count;
					    }
						
						console.log(total);
							  
						var source   = $("#garden-hose-template").html();
						
						var template = Handlebars.compile(source);
						
						var context = {
								  tag: data,
						};
						
						var html    = template(context);
						
						var sourceCloud   = $("#garden-hose-template-tag-cloud").html();
						
						var templateCloud = Handlebars.compile(sourceCloud);
						
						var htmlCloud = templateCloud(context);

						$(divId).html(html);
						
						console.log(htmlCloud);
						
						$(divId + "-canvas").html(htmlCloud);
						
						
						if(!$(divId + "-canvas").tagcanvas({
				            textColour: '#ff0000',
				            outlineColour: '#ff00ff',
				            reverse: true,
				            depth: 0.8,
				            maxSpeed: 0.01,
				            weight: true,
				            weightMode: "both",
				            weightGradient: {
				                    0:"red",
				                    0.5:"orange",
				                    1:"blue"
				                  },
				      		weightFrom: 'data-weight'
				          })) {
				            // something went wrong, hide the canvas container
				            $('#myCanvasContainer').hide();
				          } 
				          
						$(divId + "-canvas").show();

					});
					
				}

				function updateAll() {
					updateMessageRates();
					updateGardenHoseData("#garden-hose-data-recent", "twitter/gardenhose/recent");
					updateGardenHoseData("#garden-hose-data-historical", "twitter/gardenhose/historical");
					setTimeout(updateAll, 5000);
				}
				
				updateAll();
				
			});
		</script>

	<script type="text/javascript" src="http://www.google.com/jsapi"></script>

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
      
      $(document).ready(function() {

/*     	  TagCanvas.depth = .95;
          TagCanvas.textFont = 'Impact, Helvetica, sans-serif';
          TagCanvas.weight = true;
          TagCanvas.weightFrom = "data-weight";
          TagCanvas.weightMode = "both";
          TagCanvas.weightGradient = {
            0:"red",
            0.5:"orange",
            1:"blue"
          };
           */
          
           
        });
    </script>    
	</body>
</html>
