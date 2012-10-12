<%@ include file="/WEB-INF/views/includes/taglibs.jsp"%>
<!DOCTYPE html>
<html lang="en">
	<head>
		<meta charset="utf-8">
		<title><sitemesh:write property='title' default="Election - Historical Analysis"/></title>
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
					<a class="brand" href="<c:url value='/'/>">Election Analytics</a>
					<div class="nav-collapse">
						<ul class="nav">
							<li class="active"><a href="<c:url value="/"/>">Twitter Search</a></li>
							<li><a href='<c:url value="/gardenhose"/>'>Twitter Garden Hose</a></li>
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
			<sitemesh:write property='body'/>
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

		<script src="<c:url value='/assets/js/jquery.js'/>"></script>
		<script src="<c:url value='/assets/js/bootstrap.js'/>"></script>
		<script src="<c:url value='/assets/js/handlebars.js'/>"></script>
		<script src="<c:url value='/assets/js/jquery.tagcanvas.js'/>"></script>
		<script src="<c:url value='/assets/js/custom.js'/>"></script>

		<script type="text/javascript">

			$(function() {

				if (!window.console) {
					console = {log: function() {}};
				}

			});

		</script>
		<sitemesh:write property='page.bottom'/>
	</body>
</html>
