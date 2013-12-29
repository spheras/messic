<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<html>
	<head>
		<style type="text/css">	
			
			#messic-user-content {	
				margin-left: auto;	
				margin-right: auto;			
				padding: 10px;				
				width: 360px; 				
			}
			
			.messic-user-form-container {
				float: left;
				margin-bottom: 0;
			}
			
			/* titles */
			/* ---------------------------------------- */
			#messic-user-content h1{
				border-bottom: 1px double;
				padding-bottom: 10px;			
			    color: #555555;
			    font-family: 'Helvetica' !important;
			    font-size: 1.1em;
			    font-weight: bold;			
			}
			
			/* login panel at right */ 
			/* ---------------------------------------- */
			.messic-user-password-container {
				width: 195px;  
				float: left; 
				margin-right: 10px;
			}
			
			.messic-name-email-container {
				width: 350px;  
				float: left; 
			}
			
			#messic-avatar-container {
				width: 155px;
				height: 165px;  
				float: left; 
			}
			
			#preview {
				width: 145px;
				height: 155px;
				margin: 10px 0 0 10px;
			}
			
			.messic-input-max-width {
				width: 100%;
			}
						
		</style>
		
		
		<!-- Kendo styles -->
		<link href="css/kendoui/kendo.common.min.css" rel="stylesheet" />
		<link href="css/kendoui/kendo.default.min.css" rel="stylesheet" />
		
		<!-- normalization -->
        <link rel="stylesheet" href="../../css/main.css">

		<!-- Theme Style -->
        <link rel="stylesheet" href="../../css/themes/hans/base.css">
                
	</head>
    <body>
    	
    	<div id="content">
            <div id="messic-page-border">
            
                <div id="messic-page-content">
                
        			<div id="messic-user-content">
	    	
				    	<form id="user_data" class="messic-user-form-container">
				    	
				    		<div class="messic-user-password-container">
					    		<label>Usuario</label>
					    		<input id="user" name="login" type="text" class="messic-input-max-width"></input>
					    		
					    		<label>Password</label>
					    		<input id="password" name="password" type="text" class="messic-input-max-width"></input>
					    		
					    		<label>Confirmar Password</label>
					    		<input id="confirm-password" type="text" class="messic-input-max-width"></input>    		
				    		</div>
				    		
				    		<div id="messic-avatar-container">
				    			<img id="preview" src="prueba"/>
				    			<input id="messic-avatar" type="file" name="avatar" style="display:none;"/>
				    		</div>
				    		
				    		<div class="messic-name-email-container">
					    		<label>Nombre</label>
					    		<input id="name" name="name" type="text" class="messic-input-max-width"></input>
					    		
					    		<label>Email</label>
					    		<input id="email" name="email" type="text" class="messic-input-max-width"></input>
				    		</div>	    		    		
				    	
				    	</form>
				    	
				    	<button id="accept-buttom" class="button play">Aceptar</button>
					
					</div>	
					
                </div>
            </div>
		</div>
    	
				
    </body>
   
    <script src="js/vendor/jquery/jquery-1.9.1.min.js"></script>
	<script src="js/vendor/jquery/jquery-ui.js"></script>
	<script src="js/vendor/kendoui/kendo.web.min.js"></script>
    <script src="js/plugins.js"></script>
    <script src="js/user.js"></script>
    
</html>

