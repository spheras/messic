<%@page import="java.util.List"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="messic" uri="/WEB-INF/functions.tld" %>

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
						<%
						boolean creation=(Boolean)request.getAttribute("creation");
						if(!creation){
						%>
						<div onclick="removeAccount(100)" id="messic-user-settings-removeaccount" title="<fmt:message key="settings-remove-account-title" bundle="${ message }"/>"></div>
						<%
						}
						%>
						
						
						<div id="messic-user-settings-menu-basic" class="messic-user-settings-menu-option-selected">
							<div class="messic-user-settings-menu-option-image"></div>
							<label class="messic-user-settings-menu-option-title"><fmt:message key="settings-menu-basic-title" bundle="${ message }"/></label>
							<label class="messic-user-settings-menu-option-subtitle"><fmt:message key="settings-menu-basic-description" bundle="${ message }"/></label>
						</div>
						<%
						   User user = (User) request.getAttribute("user");
						   if(user.getAdministrator()){ 
						%>
						<div id="messic-user-settings-menu-admin" class="messic-user-settings-menu-option-unselected">
							<div class="messic-user-settings-menu-option-image"></div>
							<label class="messic-user-settings-menu-option-title"><fmt:message key="settings-menu-admin-title" bundle="${ message }"/></label>
							<label class="messic-user-settings-menu-option-subtitle"><fmt:message key="settings-menu-admin-description" bundle="${ message }"/></label>
						</div>
						<%}%>
						<div id="messic-user-settings-menu-music" class="messic-user-settings-menu-option-unselected">
							<div class="messic-user-settings-menu-option-image"></div>
							<label class="messic-user-settings-menu-option-title"><fmt:message key="settings-menu-music-title" bundle="${ message }"/></label>
							<label class="messic-user-settings-menu-option-subtitle"><fmt:message key="settings-menu-music-description" bundle="${ message }"/></label>
						</div>
						<div id="messic-user-settings-menu-stats" class="messic-user-settings-menu-option-unselected">
							<div class="messic-user-settings-menu-option-image"></div>
							<label class="messic-user-settings-menu-option-title"><fmt:message key="settings-menu-statistics-title" bundle="${ message }"/></label>
							<label class="messic-user-settings-menu-option-subtitle"><fmt:message key="settings-menu-statistics-description" bundle="${ message }"/></label>
						</div>
					</div>
					<h1 class="messic-user-settings-title"></h1>
	                <div id="messic-user-settings-content">
							<div id="messic-user-settings-content-basic" class="messic-user-settings-container messic-user-settings-menu-visible">
								<div class="messic-user-settings-basic-data-container">
									<ul>
										<li>
											<label><fmt:message key="settings-content-basic-user-title" bundle="${ message }"/> *</label>
											<input id="messic-user-settings-user" name="login" type="text" class="k-textbox" placeholder="<fmt:message key="settings-content-basic-user-placeholder" bundle="${ message }"/>" required <%if(!creation){ %>readonly<%}%>></input>
											<div class="divclearer"></div>
										</li>
										<li>
											<label><fmt:message key="settings-content-basic-password-title" bundle="${ message }"/> *</label>
											<input id="messic-user-settings-password" name="password" type="password" class="k-textbox" placeholder="<fmt:message key="settings-content-basic-password-placeholder" bundle="${ message }"/>" required></input>
											<div class="divclearer"></div>
										</li>
										<li>
											<label><fmt:message key="settings-content-basic-passwordr-title" bundle="${ message }"/> *</label>
											<input id="messic-user-settings-password-confirm" name="password" type="password" class="k-textbox" placeholder="<fmt:message key="settings-content-basic-passwordr-placeholder" bundle="${ message }"/>" required></input> 
											<div class="divclearer"></div>
										</li>
										<li>
											<label><fmt:message key="settings-content-basic-name-title" bundle="${ message }"/> *</label>
											<input id="messic-user-settings-name" name="name" type="text" class="k-textbox" placeholder="<fmt:message key="settings-content-basic-name-placeholder" bundle="${ message }"/>" required></input>
											<div class="divclearer"></div>
										</li>
										<li>
											<label><fmt:message key="settings-content-basic-email-title" bundle="${ message }"/></label>
											<input type="email" id="messic-user-settings-email" name="email" class="k-textbox" placeholder="e.g. myname@example.net" data-email-msg="<fmt:message key="settings-content-basic-email-message" bundle="${ message }"/>"/>
											<div class="divclearer"></div>
										</li>
										<%
										if(creation) {
										%>
										<li>
											<div id="messic-user-settings-captcha-explanation"><fmt:message key="settings-content-basic-captcha-explanation" bundle="${ message }"/></div>
											<img id="messic-user-settings-captcha" /><div id="messic-user-settings-captcha-reload" title="<fmt:message key="settings-content-basic-captcha-reload" bundle="${ message }"/>"></div>
											<input type="text" name="captcha" id="messic-user-settings-captcha-resolved" value="" />										
										</li>
										<%}%>
									</ul>
								</div>
								<div class="messic-user-settings-basic-avatar-container">
									<div class="messic-user-settings-avatar-mark"><fmt:message key="settings-content-basic-avatar-click" bundle="${ message }"/></div>
									<img id="messic-user-settings-avatar-preview" class="messic-user-settings-avatar-preview" src="img/siluetas/silueta01.png"/>
								</div>
								<input id="messic-user-settings-avatar-file" type="file" name="avatar" style="display:none;"/>
								<div class="divclearer"></div>
							</div>
							<%if(user.getAdministrator()){ %>
							<% MessicSettings settings = (MessicSettings) request.getAttribute("settings");  %>
							<div id="messic-user-settings-content-admin" class="messic-user-settings-container messic-user-settings-menu-notvisible">
								<ul>
									<li>
										<input id="messic-user-settings-allowusercreation" name="allowUserCreation" type="checkbox" <%if (settings.isAllowUserCreation()){%>checked<%}%>></input>
										<label><fmt:message key="settings-content-admin-allowusercreation-title" bundle="${ message }"/></label>
										<div class="messic-user-settings-content-help"></div>
										<div class="messic-user-settings-content-info"><fmt:message key="settings-content-admin-allowusercreation-info" bundle="${ message }"/></div>
										<div class="divclearer"></div>
									</li>
									<li>
										<input id="messic-user-settings-allowuserespecificmusicfolder" name="allowUserSpecificFolder" type="checkbox" <%if (settings.isAllowUserSpecificFolder()){%>checked<%}%>/>
										<label><fmt:message key="settings-content-admin-allowuserspecificfolder-title" bundle="${ message }"/></label>
										<div class="messic-user-settings-content-help"></div>
										<div class="messic-user-settings-content-info"><fmt:message key="settings-content-admin-allowuserspecificfolder-info" bundle="${ message }"/></div>
										<div class="divclearer"></div>
									</li>
									<li>
										<label><fmt:message key="settings-content-admin-illegalcharacter-title" bundle="${ message }"/></label>
										<input id="messic-user-settings-illegalcharacterreplacement" name="illegalCharacterReplacement" type="text" class="k-textbox" required value="${settings.getIllegalCharacterReplacement()}" pattern=".{1,1}"/>
										<div class="messic-user-settings-content-help"></div>
										<div class="messic-user-settings-content-info"><fmt:message key="settings-content-admin-illegalcharacter-info" bundle="${ message }"/></div>
										<div class="divclearer"></div>
									</li>
									<li>
										<label><fmt:message key="settings-content-admin-defaultmusicfolder-title" bundle="${ message }"/></label>
										<input id="messic-user-settings-defaultstorepath" name="genericBaseStorePath" type="text" class="k-textbox" required value="${settings.getGenericBaseStorePath()}"/>
										<div class="messic-user-settings-content-help"></div>
										<div class="messic-user-settings-content-info"><fmt:message key="settings-content-admin-defaultmusicfolder-info1" bundle="${ message }"/></div>
										<div class="messic-user-settings-content-info"><fmt:message key="settings-content-admin-defaultmusicfolder-info2" bundle="${ message }"/></div>
										<div class="divclearer"></div>
									</li>
									<li>
										<input id="messic-user-settings-admin-allowdlna" name="allowadmindlna" type="checkbox" <%if (settings.isAllowDLNA()){%>checked<%}%>/>
										<label><fmt:message key="settings-content-admin-allowdlna-title" bundle="${ message }"/></label>
										<div class="messic-user-settings-content-help"></div>
										<div class="messic-user-settings-content-info"><fmt:message key="settings-content-admin-allowdlna-info" bundle="${ message }"/></div>
										<div class="divclearer"></div>
									</li>
								</ul>
									<%
									List<User> users=(List<User>)request.getAttribute("users");
									if(users!=null){
									%>
										<label>The following is the list of current users at Messic. You can delete each of them, reset password and so on.</label>
										<table id="messic-user-settings-content-users-table">
											<thead>
												<tr>
													<th class="messic-user-settings-users-col-user" scope="col">User</th>
													<th class="messic-user-settings-users-col-login" scope="col">Login</th>
													<th class="messic-user-settings-users-col-actions" scope="col">Actions</th>
												</tr>
											</thead>
											<!-- Table body -->
											<tbody>
											<%for(int i=0;i<users.size( );i++){
												User useri=users.get(i);
											%>
												<tr>
													<td><%=useri.getName()%></td>
													<td><%=useri.getLogin()%></td>
													<td class="messic-user-settings-users-col-actions">
														<div id="messic-user-settings-content-users-remove" class="messic-user-settings-content-users-remove" title="Remove User" onclick="settingsRemoveUser(<%=useri.getSid()%>,'${messic:escapeAll(useri.name)}',$(this).parent().parent())"></div>
														<div id="messic-user-settings-content-users-resetpassword" class="messic-user-settings-content-users-resetpassword" onclick="settingsResetPassword(<%=useri.getSid()%>,'${messic:escapeAll(useri.name)}')"></div>
													</td>
												</tr>
											<%}%>
											</tbody>
										</table>
										<%}%>
							</div>
							<%}%>
							<div id="messic-user-settings-content-music" class="messic-user-settings-container messic-user-settings-menu-notvisible">
								<ul>
									<li>
										<%
										boolean allowSpecificMusicFolder=(Boolean)request.getAttribute("allowSpecificMusicFolder");
										%>
										<label><fmt:message key="settings-content-music-musicdirectory-title" bundle="${ message }"/></label>
										<input id="messic-user-settings-userStorePath" name="storePath" type="text" class="k-textbox" required value="${user.storePath}" <%if(!allowSpecificMusicFolder){ %>readonly<%}%>/>
										<div class="messic-user-settings-content-help"></div>
										<div class="messic-user-settings-content-info"><fmt:message key="settings-content-music-musicdirectory-info1" bundle="${ message }"/></div>
										<div class="messic-user-settings-content-info"><fmt:message key="settings-content-music-musicdirectory-info2" bundle="${ message }"/></div>
										<%
										if(!allowSpecificMusicFolder){ 
										%>
										<div class="messic-user-settings-content-info"><fmt:message key="settings-content-music-musicdirectory-info3" bundle="${ message }"/></div>
										<%} %>
										<div class="divclearer"></div>
									</li>
									<li>
										<input id="messic-user-settings-music-allowdlna" name="allowuserdlna" type="checkbox" <%if (user.getAllowDLNA()){%>checked<%}%>/>
										<label><fmt:message key="settings-content-music-allowdlna-title" bundle="${ message }"/></label>
										<div class="messic-user-settings-content-help"></div>
										<div class="messic-user-settings-content-info"><fmt:message key="settings-content-music-allowdlna-info" bundle="${ message }"/></div>
										<div class="divclearer"></div>
									</li>
								</ul>
							</div>
							<div id="messic-user-settings-content-stats" class="messic-user-settings-container messic-user-settings-menu-notvisible">
								<ul>
									<li>
										<input id="messic-user-settings-allowstatistics" name="allowStatistics" type="checkbox" <%if (user.getAllowStatistics()){%>checked<%}%>></input>
										<label><fmt:message key="settings-content-statistics-allowstatistics-title" bundle="${ message }"/></label>
										<div class="messic-user-settings-content-help"></div>
										<div class="messic-user-settings-content-info"><fmt:message key="settings-content-statistics-allowstatistics-info1" bundle="${ message }"/></div>
										<div class="divclearer"></div>
									</li>
								</ul>
							</div>
					</div>
	                    
					<div class="messic-user-settings-buttons">
						<button id="messic-user-settings-button-previous" class="button back"><fmt:message key="settings-buttons-back" bundle="${ message }"/></button>
						<button id="messic-user-settings-button-accept" class="button play"><fmt:message key="settings-buttons-next" bundle="${ message }"/></button>
                        <button id="messic-user-settings-button-cancel" class="button skull"><fmt:message key="settings-buttons-cancel" bundle="${ message }"/></button>
                        <button id="messic-user-settings-button-savechanges" class="button save"><fmt:message key="settings-buttons-save" bundle="${ message }"/></button>
                        <button id="messic-user-settings-button-cancelchanges" class="button skull"><fmt:message key="settings-buttons-cancel" bundle="${ message }"/></button>
					</div>
					
					<div class="divclearer"></div>
				</div>
									
            </div>
            
        </div>
				
    </body>
   
</html>

