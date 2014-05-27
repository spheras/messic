<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<html>
	<head>
		
	</head>
	
    <body>
    	
    	<div id="content">
	        <div id="messic-background"></div>
	        <div id="messic-decorator1"></div>
	        <div id="messic-decorator2"></div>
            <div id="messic-user-page-border">
            
            	<div id="messic-page-content">
                
					<div class="messic-user-settings-menu-container">
			
						<div id="basic-option" class="messic-user-settings-menu-option-selected">
							<img class="messic-user-settings-menu-option-basic-image-selected">
							</img>
							<label class="messic-user-settings-menu-option-title">Basic</label>
							<label class="messic-user-settings-menu-option-subtitle">Basic information</label>
						</div>
						
						<div class="messic-user-settings-menu-option-separator">
						</div>
						
						<div id="music-option" class="messic-user-settings-menu-option-unselected">
							<img class="messic-user-settings-menu-option-music-image-unselected">
							</img>
							<label class="messic-user-settings-menu-option-title">Music</label>
							<label class="messic-user-settings-menu-option-subtitle">Music information</label>
						</div>
						
						<div class="messic-user-settings-menu-option-separator">
						</div>
						
						<div id="admin-option" class="messic-user-settings-menu-option-unselected">
							<img class="messic-user-settings-menu-option-admin-image-unselected">
							</img>
							<label class="messic-user-settings-menu-option-title">Admin</label>
							<label class="messic-user-settings-menu-option-subtitle">Admin information</label>
						</div>
						
						<div class="messic-user-settings-menu-option-separator">
						</div>
						
						<div id="stats-option" class="messic-user-settings-menu-option-unselected">
							<img class="messic-user-settings-menu-option-stats-image-unselected">
							</img>
							<label class="messic-user-settings-menu-option-title">Statistics</label>
							<label class="messic-user-settings-menu-option-subtitle">Statistics information</label>
						</div>
						
						<div class="messic-user-settings-menu-option-separator">
						</div>
				
					</div>
	                
	                
	                <div id="data-container" class="messic-user-settings-content">
	                
						<div >
							<div id="basic-data" class="messic-user-settings-container messic-user-settings-menu-visible">
								
								<div class="messic-user-settings-basic-data-container">
								
									<ul>
										<li>
											<label>Usuario</label>
											<input id="user" name="login" type="text" class="k-textbox" required></input>
										</li>
										<li>
											<label>Password</label>
											<input id="password" name="password" type="password" class="k-textbox" required></input>
										</li>
										<li>
											<label>Confirmar Password</label>
											<input id="confirm-password" type="password" class="k-textbox"required></input> 
										</li>
										<li>
											<label>Nombre</label>
											<input id="name" name="name" type="text" class="k-textbox" required></input>
										</li>
										<li>
											<label>Email</label>
											<input type="email" id="email" name="email" class="k-textbox" placeholder="e.g. myname@example.net" required data-email-msg="Email format is not valid"/>
										</li>										
										<li>
											<label>Directorio Base</label>
											<input id="storePath" name="storePath" type="text" class="k-textbox" required></input>
										</li>
									</ul>
																		  
								</div>
								
								<div class="messic-user-settings-basic-avatar-container">
							
									<div class="messic-user-settings-avatar-mark">
										
									</div>
									
									<img id="preview" class="messic-user-settings-avatar" src="avatar.png"/>
									
									<input id="messic-avatar" type="file" name="avatar" style="display:none;"/>
								
								</div>	                                  		    		
							
							</div>
							
							<div id="music-data" class="messic-user-settings-container messic-user-settings-menu-notvisible">
							
	                            <div class="messic-user-settings-basic-data-container">
								
									<ul>
										<li>
											<label>Configuración de música</label>
										</li>
									</ul>
																		  
								</div>
	                            
							</div>
							
							<div id="admin-data" class="messic-user-settings-container messic-user-settings-menu-notvisible">
							
	                            <div class="messic-user-settings-basic-data-container">
								
									<ul>
										<li>
											<label>Configuración de administrador</label>
										</li>										
									</ul>
																		  
								</div>
	                            
							</div>
							
							<div id="stats-data" class="messic-user-settings-container messic-user-settings-menu-notvisible">
							
	                            <div class="messic-user-settings-basic-data-container">
								
									<ul>
										<li>
											<label>Configuración de estadísticas</label>
										</li>
									</ul>
																		  
								</div>
	                            
							</div>
	                        
						</div>
	                    
						<div class="messic-user-settings-buttons-container">
							<button id="previous-button" class="button previous">Atras</button>
							<button id="accept-button" class="button play">Siguiente</button>
	                        <button id="cancel-button" class="button cancel">Cancelar</button>
						</div>
	                       
	                </div>

				</div>
									
            </div>
            
        </div>
				
    </body>
   
</html>

