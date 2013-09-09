<%@ include file="/WEB-INF/views/includes/taglibs.jsp"%>
<html>
	<head>
		<title>Election - Garden Hose</title>
	</head>
	<body>
		<div class="row">
			<div class="span4">
				<h3>Garden Hose Recent</h3>
				<div id="garden-hose-data-recent">Loading...</div>
			</div>
			<div class="span8">
				<div id="garden-hose-data-recent-container">
					<canvas width="400" height="400" id="garden-hose-data-recent-canvas" style="display: none;">
						<p>Anything in here will be replaced on browsers that support the canvas element</p>
					</canvas>
				</div>
				<div id="garden-hose-data-recent-tags">
				</div>
			</div>
		</div>
		<div class="row">
			<div class="span4">
				<h3>Garden Hose Historical</h3>
				<div id="garden-hose-data-historical">Loading...</div>
			</div>
			<div class="span8">
				<div id="garden-hose-data-historical-container">
					<canvas width="400" height="400" id="garden-hose-data-historical-canvas" style="display: none;">
						<p>Anything in here will be replaced on browsers that support the canvas element</p>
					</canvas>
				</div>
				<div id="garden-hose-data-historical-tags">
				</div>
			</div>
		</div>

		<content tag='bottom'>

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

					$.mynamespace = {
						gauge: null,
						gaugeOptions : null,
						gaugeHeader: ['Label', 'Value']
					};

					var total = 0;

					Handlebars.registerHelper('weight', function(count) {
						return new Handlebars.SafeString(
							Math.ceil(count/total * 50) + 20
						);
					});

					function updateGardenHoseData(divId, url) {

						$.getJSON(url, function(data) {

							total = 0;
							var filteredData = [];
							console.log(data);

							for (i in data) {
								total = total + data[i].count;
								filteredData.push(data[i]);
								if (i==9) { break; }
							}

							console.log(total);

							var source   = $("#garden-hose-template").html();
							var template = Handlebars.compile(source);
							var context = {
								tag: filteredData
							};

							var html    = template(context);
							var sourceCloud   = $("#garden-hose-template-tag-cloud").html();
							var templateCloud = Handlebars.compile(sourceCloud);
							var htmlCloud = templateCloud(context);

							$(divId).html(html);
							$(divId + "-canvas").html(htmlCloud);

							if(!$(divId + "-canvas").tagcanvas({
								textColour: '#ff0000',
								outlineColour: '#aaaaaa',
								reverse: true,
								depth: 0,
								maxSpeed: 0.0,
								weight: true,
								weightMode: "both",
								weightGradient: {
									0:"red",
									0.5:"orange",
									1:"blue"
								},
								weightFrom: 'data-weight',
								wheelZoom: false
							})) {
								// something went wrong, hide the canvas container
								$('#myCanvasContainer').hide();
							}
							$(divId + "-canvas").show();
						});
					}

					function updateAll() {
						updateGardenHoseData("#garden-hose-data-recent", "twitter/gardenhose/recent");
						updateGardenHoseData("#garden-hose-data-historical", "twitter/gardenhose/historical");
						setTimeout(updateAll, 5000);
					}
					updateAll();
				});
			</script>

			<script type="text/javascript" src="http://www.google.com/jsapi"></script>

		</content>
	</body>
</html>
