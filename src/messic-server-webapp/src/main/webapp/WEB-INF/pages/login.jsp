<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<!DOCTYPE html>
<!--[if lt IE 7]>      <html class="no-js lt-ie9 lt-ie8 lt-ie7"> <![endif]-->
<!--[if IE 7]>         <html class="no-js lt-ie9 lt-ie8"> <![endif]-->
<!--[if IE 8]>         <html class="no-js lt-ie9"> <![endif]-->
<!--[if gt IE 8]><!--> <html class="no-js"> <!--<![endif]-->
	<head>
        <meta charset="utf-8">
        <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
        <title>Messic</title>
        <meta name="description" content="Messic for the music mess">
        <meta name="viewport" content="width=device-width">

		<link rel="shortcut icon" href="/img/favicon.ico" type="image/x-icon">
		<!-- For third-generation iPad with high-resolution Retina display: -->
		<link rel="apple-touch-icon-precomposed" sizes="144x144" href="/img/apple-touch-icon-144x144-precomposed.png">
		<!-- For iPhone with high-resolution Retina display running iOS ≥ 7: -->
		<link rel="apple-touch-icon-precomposed" sizes="120x120" href="/img/apple-touch-icon-120x120-precomposed.png">
		<!-- For iPhone with high-resolution Retina display running iOS ≤ 6: -->
		<link rel="apple-touch-icon-precomposed" sizes="114x114" href="/img/apple-touch-icon-114x114-precomposed.png">
		<!-- For first- and second-generation iPad: -->
		<link rel="apple-touch-icon-precomposed" sizes="72x72" href="/img/apple-touch-icon-72x72-precomposed.png">
		<!-- for non-Retina iPhone and iPod Touch: -->
		<link rel="apple-touch-icon-precomposed" sizes="57x57" href="/img/apple-touch-icon-52x52-precomposed.png">
		<!-- For non-Retina iPhone, iPod Touch, and Android 2.1+ devices: -->
		<link rel="apple-touch-icon-precomposed" href="/img/apple-touch-icon-precomposed.png">


		<!-- Kendo styles -->
		<link href="css/kendoui/kendo.common.min.css" rel="stylesheet" />
		<link href="css/kendoui/kendo.default.min.css" rel="stylesheet" />
		
		<!-- normalization -->
        <link rel="stylesheet" href="css/normalize.min.css">
        <link rel="stylesheet" href="css/main.css">

		<!-- Theme Style -->
        <link rel="stylesheet" href="css/themes/hans/base.css">
        <link rel="stylesheet" href="css/themes/hans/login.css">
        <link rel="stylesheet" href="css/themes/hans/playlist.css">
        <link rel="stylesheet" href="css/themes/hans/about.css">
        <link rel="stylesheet" href="css/themes/hans/upload.css">
		
		<!-- modernizr -->
        <script src="js/vendor/modernizr-2.6.2.min.js"></script>
    </head>
    <body>

		<c:if test="${ message == null }">
		    <fmt:setBundle basename="ResourceBundle" var="message" scope="application"/>
		</c:if>
		    
        <!--[if lt IE 7]>
            <p class="chromeframe">You are using an <strong>outdated</strong> browser. Please <a href="http://browsehappy.com/">upgrade your browser</a> or <a href="http://www.google.com/chromeframe/?redirect=true">activate Google Chrome Frame</a> to improve your experience.</p>
        <![endif]-->
	
		<div id="messic-logo1"></div>
		<div id="messic-logo2"></div>

			<div id="messic-login-window" >
				<div class="messic-login-form-container">
					<div class="messic-text-container">
						<h1><fmt:message key="login-welcome" bundle="${ message }"/></h1>
						<a href="/restpassword"><fmt:message key="login-forgot" bundle="${ message }"/></a>
						<form id="messic-login-form" class="login-form-style">
							<label for="username" class="login-form-label"><fmt:message key="login-username" bundle="${ message }"/></label>
							<input id="username" name="j_username" type="text" value="joseUser"/>
							<label for="password" class="login-form-label"><fmt:message key="login-password" bundle="${ message }"/></label>
							<input id="password" name="j_password" type="text" value="12345"/>
						</form>
						<button id="messic-login-button" class="button play"><fmt:message key="login-button" bundle="${ message }"/></button>
						<div id="messic-login-logo"></div>
					</div>
				</div>
				<div class="messic-login-new-account-container">
					<div class="messic-text-container">
						<h1><fmt:message key="login-newaccount" bundle="${ message }"/></h1>
						<label><fmt:message key="login-newaccount-explanation1" bundle="${ message }"/></label>
						<label><fmt:message key="login-newaccount-explanation2" bundle="${ message }"/></label>
						<button id="newAccount" class="button spark"><fmt:message key="login-newaccount-button" bundle="${ message }"/></button>
					</div>
				</div>			
			</div>
			<div id="messic-login-shadow" style="position:absolute"></div>
		

        <script src="js/vendor/jquery/jquery-1.9.1.min.js"></script>
		<script src="js/vendor/jquery/jquery-ui.js"></script>
		<script src="js/vendor/kendoui/kendo.web.min.js"></script>
        <script src="js/vendor/jplayer/jquery.jplayer.min.js"></script>
        <script src="js/vendor/jquery.tinyscrollbar.js"></script>
        <script src="js/jplayer.playlist.messic.js"></script>
        <script src="js/plugins.js"></script>
        <script src="js/login.js"></script>
        <script src="js/main.js"></script>
        <script src="js/upload.js"></script>
    </body>
</html>
