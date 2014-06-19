<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<%@ page import="org.messic.server.api.datamodel.User" %>
<%@ page import="org.messic.server.api.datamodel.MessicSettings" %>

<html>
	<head>
		
	</head>
	
    <body>
    	
    	<div id="content">

            <div id="messic-page-border" class="messic-user-settings-border">
            
            	<div id="messic-page-content">
                
					<div class="messic-user-settings-menu-container">
						<div id="messic-user-settings-menu-basic" class="messic-user-settings-menu-option-selected">
							<div class="messic-user-settings-menu-option-image"></div>
							<label class="messic-user-settings-menu-option-title">Basic</label>
							<label class="messic-user-settings-menu-option-subtitle">Basic Information</label>
						</div>
						<% 
						   User user = (User) request.getAttribute("user"); 
						   if(user.getAdministrator()){ 
						%>
						<div id="messic-user-settings-menu-admin" class="messic-user-settings-menu-option-unselected">
							<div class="messic-user-settings-menu-option-image"></div>
							<label class="messic-user-settings-menu-option-title">Admin</label>
							<label class="messic-user-settings-menu-option-subtitle">Admin Settings</label>
						</div>
						<%}%>
						<div id="messic-user-settings-menu-music" class="messic-user-settings-menu-option-unselected">
							<div class="messic-user-settings-menu-option-image"></div>
							<label class="messic-user-settings-menu-option-title">Music</label>
							<label class="messic-user-settings-menu-option-subtitle">Music Settings</label>
						</div>
						<div id="messic-user-settings-menu-stats" class="messic-user-settings-menu-option-unselected">
							<div class="messic-user-settings-menu-option-image"></div>
							<label class="messic-user-settings-menu-option-title">Statistics</label>
							<label class="messic-user-settings-menu-option-subtitle">Statistics Settings</label>
						</div>
					</div>
					<h1 class="messic-user-settings-title"></h1>
	                <div id="messic-user-settings-content">
							<div id="messic-user-settings-content-basic" class="messic-user-settings-container messic-user-settings-menu-visible">
								<div class="messic-user-settings-basic-data-container">
									<ul>
										<%
										boolean creation=(Boolean)request.getAttribute("creation");
										%>
										<li>
											<label>User *</label>
											<input id="messic-user-settings-user" name="login" type="text" class="k-textbox" placeholder="Write here an username" required <%if(!creation){ %>readonly<%}%>></input>
										</li>
										<li>
											<label>Password *</label>
											<input id="messic-user-settings-password" name="password" type="password" class="k-textbox" placeholder="Write here your password (min 5 characters)" required></input>
										</li>
										<li>
											<label>Repeat Password *</label>
											<input id="messic-user-settings-password-confirm" name="password" type="password" class="k-textbox" placeholder="Repeat the password to confirm" required></input> 
										</li>
										<li>
											<label>Name *</label>
											<input id="messic-user-settings-name" name="name" type="text" class="k-textbox" placeholder="Put here your real name" required></input>
										</li>
										<li>
											<label>Email (not mandatory)</label>
											<input type="email" id="messic-user-settings-email" name="email" class="k-textbox" placeholder="e.g. myname@example.net" data-email-msg="Email format is not valid"/>
										</li>
										<%
										if(creation) {
										%>
										<li>
											<div id="messic-user-settings-captcha-explanation">Please, just to confirm that you are a human (and not a robot!), write below what you read in this image</div>
											<img id="messic-user-settings-captcha" /><div id="messic-user-settings-captcha-reload" title="try with another captcha image, don't understand this image!"></div>
											<input type="text" name="captcha" id="messic-user-settings-captcha-resolved" value="" />										
										</li>
										<%}%>
									</ul>
								</div>
								<div class="messic-user-settings-basic-avatar-container">
									<div class="messic-user-settings-avatar-mark">Click to select avatar</div>
									<img id="messic-user-settings-avatar-preview" class="messic-user-settings-avatar" src="img/siluetas/silueta01.png"/>
									<input id="messic-user-settings-avatar" type="file" name="avatar" style="display:none;"/>
								</div>
								<div class="divclearer"></div>
							</div>
							<%if(user.getAdministrator()){ %>
							<% MessicSettings settings = (MessicSettings) request.getAttribute("settings");  %>
							<div id="messic-user-settings-content-admin" class="messic-user-settings-container messic-user-settings-menu-notvisible">
								<ul>
									<li>
										<label>Allow User Creation</label>
										<input id="messic-user-settings-allowusercreation" name="allowUserCreation" type="checkbox" <%if (settings.isAllowUserCreation()){%>checked<%}%>></input>
										<div class="messic-user-settings-content-info">This parameter indicates if a new user can be created from the login page, without any authorization.</div>
									</li>
									<li>
										<label>Allow User Especific Music Folder</label>
										<input id="messic-user-settings-allowuserespecificmusicfolder" name="allowUserSpecificFolder" type="checkbox" <%if (settings.isAllowUserSpecificFolder()){%>checked<%}%>/>
										<div class="messic-user-settings-content-info">This parameter indicates if users (that are not administrators) can especify a different folder to store their music (different from the default setting).</div>
									</li>
									<li>
										<label>Illegal character replacement</label>
										<input id="messic-user-settings-illegalcharacterreplacement" name="illegalCharacterReplacement" type="text" class="k-textbox" required value="${settings.getIllegalCharacterReplacement()}" pattern=".{1,1}"/>
										<div class="messic-user-settings-content-info">This character is the replacement character for filenames and folder when found an invalid character for the filename (suppose that you upload a song with a illegal name for filenames, the filename will replace the invalid character by this one... for sure, the real song name will be visible through the Messic User Interface)</div>
									</li>
									<li>
										<label>Default Music Directory</label>
										<input id="messic-user-settings-defaultstorepath" name="genericBaseStorePath" type="text" class="k-textbox" required value="${settings.getGenericBaseStorePath()}"/>
										<div class="messic-user-settings-content-info">Remember that this path is a folder from the server machine, it means, the place where messic is installed (in fact you can access to Messic User Interface from every pc/mobile/... at your network, but Messic is usually installed only in one of them)</div>
										<div class="messic-user-settings-content-info">This is the default folder to store all the music uploaded to messic for all the users.  Each user will have it's own subfolder (named by the username) where the music will be saved.  The goal for Messic is to get the music that you upload and organize it in the folder that you especify, in an understable way, allowing also the reproduction, search, and so on from the main user interface.  You can always access directly to this folder in order to get what you want, but don't remove anything directly!!!! Deletions, Renames, etc.. should be done ALWAYS through the interface in order to have a consistent database!!!</div>
									</li>
								</ul>
							</div>
							<%}%>
							<div id="messic-user-settings-content-music" class="messic-user-settings-container messic-user-settings-menu-notvisible">
								<ul>
									<li>
										<%
										boolean allowSpecificMusicFolder=(Boolean)request.getAttribute("allowSpecificMusicFolder");
										%>
										<label>Music Directory</label>
										<input id="messic-user-settings-userStorePath" name="storePath" type="text" class="k-textbox" required value="${user.storePath}" <%if(!allowSpecificMusicFolder){ %>readonly<%}%>/>
										<div class="messic-user-settings-content-info">Remember that this path is a folder from the server machine, it means, the place where messic is installed (in fact you can access to Messic User Interface from every pc/mobile/... at your network, but Messic is usually installed only in one of them)</div>
										<div class="messic-user-settings-content-info">This is the path where all the music will be stored. When you upload music to Messic, this music is stored here, but structured and with all the metadatas for the audio correctly.  You can always access to this folder directly, it is YOUR MUSIC, and you can access directly if you want, but DON'T remove directly or rename from that folder because then Messic will have an INCONSISTENT DATABASE.  If you need to remove or rename something, you can do it through Messic User Interface.</div>
										<%
										if(!allowSpecificMusicFolder){ 
										%>
										<div class="messic-user-settings-content-info">You cannot modify this folder due to a restriction for the administrator!</div>
										<%} %>
									</li>
								</ul>
							</div>
							<div id="messic-user-settings-content-stats" class="messic-user-settings-container messic-user-settings-menu-notvisible">
								<ul>
									<li>
										<label>Allow Messic Getting User Information</label>
										<input id="messic-user-settings-allowstatistics" name="allowStatistics" type="checkbox" <%if (user.getAllowStatistics()){%>checked<%}%>></input>
										<div class="messic-user-settings-content-info">Indicates if Messic is able to get information about the use of messic, like the times you play a song, your preferences, etc... Only regarding music reproduction with the objetives to show you, and only you, this information through statistics, and also to create better personalized playlist for you, with your loved music!  Messic DON'T use this information for anymore, and this information is always at your computer.</div>
									</li>
								</ul>
							</div>
					</div>
	                    
					<div class="messic-user-settings-buttons">
						<button id="messic-user-settings-button-previous" class="button back">Back</button>
						<button id="messic-user-settings-button-accept" class="button play">Next</button>
                        <button id="messic-user-settings-button-cancel" class="button skull">Cancel</button>
                        <button id="messic-user-settings-button-savechanges" class="button save">Save</button>
                        <button id="messic-user-settings-button-cancelchanges" class="button skull">Cancel</button>
					</div>
				</div>
									
            </div>
            
        </div>
				
    </body>
   
</html>

