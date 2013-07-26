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
        <title>Messic Login</title>
        <meta name="description" content="">
        <meta name="viewport" content="width=device-width">

		<link href="css/kendoui/kendo.common.min.css" rel="stylesheet" />
		<link href="css/kendoui/kendo.default.min.css" rel="stylesheet" />
		
        <link rel="stylesheet" href="css/normalize.min.css">
        <link rel="stylesheet" href="css/main.css">
		
        <script src="js/vendor/modernizr-2.6.2.min.js"></script>
    </head>
    <body>

		<c:if test="${ message == null }">
		    <fmt:setBundle basename="ResourceBundle" var="message" scope="application"/>
		</c:if>
		    
        <!--[if lt IE 7]>
            <p class="chromeframe">You are using an <strong>outdated</strong> browser. Please <a href="http://browsehappy.com/">upgrade your browser</a> or <a href="http://www.google.com/chromeframe/?redirect=true">activate Google Chrome Frame</a> to improve your experience.</p>
        <![endif]-->

		<h1>EJEMPLO DE MENSAJE MULTILENGUAJE: <fmt:message key="welcome" bundle="${ message }"/></h1>
		
			<div id="window" class="login-container">
				<div class="login-form-container">
					<div class="text-container">
						<label class="login-title">Sign-in to your account - ${msg}</label>
						<div class="horizontal-separator">
						</div>
						
						<div class="follow-password-container">
						<button id="restorePassword" onClick="resetPassword()" class="button-as-link">Forgot your password??</button>	
						</div>
						<form id="login_form" class="login-form-style">
							<label for="username" class="login-form-label">Username</label>
							<input id="username" name="j_username" type="text" value="joseUser" class="k-input"/>
							<label for="password" class="login-form-label">Password</label>
							<input id="password" name="j_password" type="text" value="audreyPass" class="k-input"/>
						</form>
						<button id="login" onSubmit="login()" class="k-button">Log in</button>
						<img src="css/hans/logo-messic-vinyl.png" width="190" height="75" style="float: right">
						</img>
					</div>
				</div>				
				<div class="new-account-container">
					<div class="text-container">
						<label class="login-title">Create an account</label>
						<div class="horizontal-separator">
						</div>
						<label>It�s necessary one account to enter. If you don�t have any account you can create a new one.</label>
						<label>Press 'Create New' to create a new user.</label>
						<button id="newAccount" onSubmit="createAccount()" class="k-button">Create New</button>
					</div>
				</div>			
			</div>
			
			<div id="shadow" style="position:absolute">
				hola
			</div>
		
		<style scoped>
			
			.horizontal-separator{
				margin-top:2px;
				margin-bottom:2px;
				margin-right:0px;
				margin-left:0px;
				background-image:url('css/hans/separator-texture.gif'); 
				background-repeat:repeat-x;
				min-height: 5px;
			}
			
			.login-container{
				height: 270px; 
				background: -webkit-gradient(linear, 0% 0%, 0% 200%, from(#FFFFFF), to(#D6D6D6));
				-webkit-box-shadow: 4px 4px 4px rgba(0,0,0,0.3);
				box-shadow: 4px 4px 4px rgba(0,0,0,0.3);
				border-radius: 5px 5px 5px 5px;
				border-style: solid;
				border-width: 1px;
				border-color: rgba(0, 0, 0, 0.3);
			}
			
			.login-form-container{
				width: 370px; 
				height: 100%; 
				float: left; 
				border-right:1px solid #313030;
			}
			
			.new-account-container{
				width: 200px; 
				height: 100%; 
				float: left; 
				background: url('css/hans/light_noise_diagonal_@2X_darker.png'); 
				background-repeat : repeat-y;
				border-radius: 0px 5px 5px 0px;
			}
			
			.text-container{
				margin: 10px;
			}
			
			.button-as-link{
				float: right;  
				background-color: transparent;
				text-decoration: underline;
				border: none;
				color: #313030;
				cursor: pointer;
			}
			
			.login-title{
				margin: 5px;
			}
			
			label {
				display: block; 				
			}
			
			.login-form-label{
				margin: 5px 0px 2px 0px;
			}
			
			.login-form-style{
				margin: 0 0 10px 0;
			}
			
			.follow-password-container{
				width: 100%; 
				height: 20px;
			}
		
		</style>
        <!--<script src="js/vendor/jquery-1.9.1.min.js"></script>-->
        
        <script src="js/vendor/jquery/jquery-1.9.1.js"></script>
		<script src="js/vendor/jquery/jquery-ui.js"></script>
		<script src="js/vendor/kendoui/kendo.web.min.js"></script>

        <script src="js/plugins.js"></script>
        <script src="js/login.js"></script>
    </body>
</html>