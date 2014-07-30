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

		<link rel="shortcut icon" href="img/messic-icon.png" type="image/png">
		<!-- For third-generation iPad with high-resolution Retina display: -->
		<link rel="apple-touch-icon-precomposed" sizes="144x144" href="img/apple-touch-icon-144x144-precomposed.png">
		<!-- For iPhone with high-resolution Retina display running iOS 7: -->
		<link rel="apple-touch-icon-precomposed" sizes="120x120" href="img/apple-touch-icon-120x120-precomposed.png">
		<!-- For iPhone with high-resolution Retina display running iOS 6: -->
		<link rel="apple-touch-icon-precomposed" sizes="114x114" href="img/apple-touch-icon-114x114-precomposed.png">
		<!-- For first- and second-generation iPad: -->
		<link rel="apple-touch-icon-precomposed" sizes="72x72" href="img/apple-touch-icon-72x72-precomposed.png">
		<!-- for non-Retina iPhone and iPod Touch: -->
		<link rel="apple-touch-icon-precomposed" sizes="57x57" href="img/apple-touch-icon-52x52-precomposed.png">
		<!-- For non-Retina iPhone, iPod Touch, and Android 2.1+ devices: -->
		<link rel="apple-touch-icon-precomposed" href="img/apple-touch-icon-precomposed.png">


		<!-- Kendo styles -->
		<!--
		<link href="css/kendoui/kendo.common.min.css" rel="stylesheet" />
		<link href="css/kendoui/kendo.default.min.css" rel="stylesheet" />
		-->
		
		<!-- normalization -->
        <link rel="stylesheet" href="css/normalize.min.css">
        <link rel="stylesheet" href="css/main.css">

		<!-- Theme Style -->
        <link rel="stylesheet" href="css/themes/hansv2/base.css">
        <link rel="stylesheet" href="css/themes/hansv2/home.css">
        <link rel="stylesheet" href="css/themes/hansv2/login.css">
        <link rel="stylesheet" href="css/themes/hansv2/playlist.css">
        <link rel="stylesheet" href="css/themes/hansv2/playlists.css">
        <link rel="stylesheet" href="css/themes/hansv2/about.css">
        <link rel="stylesheet" href="css/themes/hansv2/upload.css">
        <link rel="stylesheet" href="css/themes/hansv2/settings.css">
        <link rel="stylesheet" href="css/themes/hansv2/kendo.css">
        <link rel="stylesheet" href="css/themes/hansv2/jquery.confirm.css">
        <link rel="stylesheet" href="css/themes/hansv2/api.css">
        <link rel="stylesheet" href="css/themes/hansv2/explore.css">
        <link rel="stylesheet" href="css/themes/hansv2/album.css">
        <link rel="stylesheet" href="css/themes/hansv2/author.css">
		
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
	
			<div id="messic-login-window" style="display:none">
				<div class="messic-login-form-container">
					<div class="messic-form">
						<h1><fmt:message key="login-welcome" bundle="${ message }"/></h1>
						<!--  TODO <a href="/restpassword"><fmt:message key="login-forgot" bundle="${ message }"/></a> -->
						<form id="messic-login-form" class="login-form-style">
				            <ul>
				                <li>
									<label for="username" class="login-form-label"><fmt:message key="login-username" bundle="${ message }"/></label>
									<input id="username" name="j_username" type="text" value="" autofocus>
								</li>
								<li>
									<label for="password" class="login-form-label"><fmt:message key="login-password" bundle="${ message }"/></label>
									<input id="password" name="j_password" type="password" value=""/>
								</li>
							</ul>
						</form>
						<div class="checkboxcontainer">
							<input id="messic_login_rememberme" type="checkbox"/>
							<label for="messic_login_rememberme" class="login-form-label"><fmt:message key="login-rememberme" bundle="${ message }"/></label>
							<div class="divclearer"></div>
						</div>
						<button id="messic-login-button" class="button play"><fmt:message key="login-button" bundle="${ message }"/></button>
						<div id="messic-login-logo"></div>
					</div>
				</div>
				<%
					boolean allowCreation=(Boolean)request.getAttribute("allowUserCreation");
					if(allowCreation){
				%>
				<div class="messic-login-new-account-container">
					<div class="messic-form">
						<h1><fmt:message key="login-newaccount" bundle="${ message }"/></h1>
						<label><fmt:message key="login-newaccount-explanation1" bundle="${ message }"/></label>
						<label><fmt:message key="login-newaccount-explanation2" bundle="${ message }"/></label>
						<button id="newAccount" class="button spark"><fmt:message key="login-newaccount-button" bundle="${ message }"/></button>
					</div>
				</div>			
				<%}%>
			</div>
			<div id="messic-login-shadow" style="display:none;position:absolute;"></div>
		
			<div id="messic-wait"><div id="messic-wait-box"><p></p></div></div>

        <script src="js/vendor/jquery/jquery-1.9.1.min.js"></script>
		<script src="js/vendor/jquery/jquery-ui.js"></script>
		<script src="js/vendor/kendoui/kendo.web.min.js"></script>
        <script src="js/vendor/jplayer/jquery.jplayer.min.js"></script>
        <script src="js/vendor/jquery.tinyscrollbar.min.js"></script>
        <!-- <script src="js/vendor/jquery.tinycarousel.min.js"></script> -->
        <script src="js/vendor/jquery.tinycarousel.js"></script>
        <script src="js/jplayer.playlist.messic.js"></script>
        <script src="lang.do"></script>
        <script src="js/utils.js"></script>
        <script src="js/plugins.js"></script>
        <script src="js/login.js"></script>
        <script src="js/main.js"></script>
        <script src="js/upload.js"></script>
        <script src="js/explore.js"></script>
        <script src="js/album.js"></script>
        <script src="js/author.js"></script>
        <script src="js/playlist.js"></script>
        <script src="js/uploadresource.js"></script>
        <script src="js/uploadalbum.js"></script>
        <script src="js/uploadprocess.js"></script>
        <script src="js/uploadPool.js"></script>
        <script src="js/settings.js"></script>
        <script src="js/apidoc.js"></script>
        <script src="js/jquery.confirm.js"></script>

		<%
		boolean firstTime=(Boolean)request.getAttribute("firstTime");
		if(firstTime){%>
			<script type="text/javascript">
				$( window ).load(function() {
				    var messages = [
	                  messicLang.messicMessagesWelcome1_1,
	                  "||",
	                  messicLang.messicMessagesWelcome1_2,
	                  "||",
	                  messicLang.messicMessagesWelcome1_3,
	                  "||",
	                  messicLang.messicMessagesWelcome1_4
	              	].join("");
				
					UtilShowMessic(messicLang.messicMessagesWelcome1,messages);
				});
			</script>		    
		<%
		}
		%>
    </body>
</html>
