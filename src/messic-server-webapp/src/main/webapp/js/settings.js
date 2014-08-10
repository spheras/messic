/**
 * @source: https://github.com/spheras/messic
 *
 * @licstart  The following is the entire license notice for the 
 *  JavaScript code in this page.
 *
 * The JavaScript code in this page is free software: you can
 * redistribute it and/or modify it under the terms of the GNU
 * General Public License (GNU GPL) as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option)
 * any later version.  The code is distributed WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE.  See the GNU GPL for more details.
 *
 * As additional permission under GNU GPL version 3 section 7, you
 * may distribute non-source (e.g., minimized or compacted) forms of
 * that code without the copy of the GNU GPL normally required by
 * section 4, provided you include this license notice and a URL
 * through which recipients can access the Corresponding Source.
 *
 * @licend  The above is the entire license notice
 * for the JavaScript code in this page.
 *
 */

/** validators for settings  */
var settings_validator_basic = null;
var settings_validator_music = null;
var settings_validator_admin = null;
var settings_validator_stats = null;

/** id of the captcha to be validated after */
var messic_user_settings_captcha_id = "";


function initSettings(flagNewUser)
{
	getCaptchaImage();
	$("#messic-user-settings-captcha-reload").click(function(){
		getCaptchaImage();
	});
	
	settingsLoadValidators();

	$(".messic-user-settings-basic-avatar-container").click(function(event){
		event.stopPropagation();
		event.preventDefault();
		$("#messic-user-settings-avatar-file").click();
	});
	
	$("#messic-user-settings-avatar-file").change(function() {
		//loading the image file selected
		var reader = new FileReader();
        reader.onload = function (e) {
            $('#messic-user-settings-avatar-preview').attr('src', e.target.result);
        }
		var file = $("#messic-user-settings-avatar-file")[0].files[0];
        reader.readAsDataURL(file);
		
	});

	if(flagNewUser){
		initSettingsNewUser();
	}else{
		initSettingsExistingUser();
	}
	
}

/**
 * function to remove the account of the current user 
 * @param sid long sid of the current user
 */
function removeAccount(sid){
	definitiveFunction=function(removeContentAlso){
		UtilShowWait(messicLang.settingsRemoveUserWait);

  	    $.ajax({
	        type: "DELETE",
	        async: true,
	        url: "services/settings/"+sid+"?removeMusicContent="+removeContentAlso,
	        error: function (XMLHttpRequest, textStatus, errorThrown){
	    		UtilHideWait();
	        	UtilShowInfo("Error while removing!");
	        },
	        success: function (data){
	    		UtilHideWait();
	    		mainLogout();
	        	UtilShowInfo(messicLang.settingsRemoveAccountDone);
			}
		});
	}

	
	nextFunction=function(){
		$.confirm({
			'title' : messicLang.settingsRemoveUserDeleteMusicContentTitle,
			'message' : messicLang.settingsRemoveUserDeleteMusicContentContent,
			'buttons' : {
				'Yes' : {
					'title' : messicLang.confirmationYes,
					'class' : 'blue',
					'action' : function() {
						definitiveFunction(true);
					}
				},
				'No' : {
					'title' : messicLang.confirmationNo,
					'class' : 'gray',
					'action' : function() {
						definitiveFunction(false);
					}
				}
			}
		});
	}

	lastOportunity=function(){
		$.confirm({
			'title' : messicLang.settingsRemoveAccountLastOportunityTitle,
			'message' : messicLang.settingsRemoveAccountLastOportunityContent,
			'buttons' : {
				'Yes' : {
					'title' : messicLang.confirmationYes,
					'class' : 'blue',
					'action' : function() {
						nextFunction();
					}
				},
				'No' : {
					'title' : messicLang.confirmationNo,
					'class' : 'gray',
					'action' : function() {
					}
				}
			}
		});
	}

	$.confirm({
		'title' : messicLang.settingsRemoveAccountTitle,
		'message' : messicLang.settingsRemoveAccountContent,
		'buttons' : {
			'Yes' : {
				'title' : messicLang.confirmationYes,
				'class' : 'blue',
				'action' : function() {
					lastOportunity();
				}
			},
			'No' : {
				'title' : messicLang.confirmationNo,
				'class' : 'gray',
				'action' : function() {
				}
			}
		}
	});
}

function settingsFuseGenres(){
	var genresToFuse=new Array();
	var divTable=$("#messic-user-settings-content-genres-table tr").each(function(){
		var checked=$(this).find("input").is(":checked");
		if(checked){
			genresToFuse.push($(this).data("sid"));
		}
	});

	if(genresToFuse.length>1){
		var code="<div id=\"messic-settings-genre-edit-container-overlay\">";
		code=code+"  <div id=\"messic-settings-genre-fuse-container\">";
		code=code+"      <p>"+messicLang.settingsFuseGenreExplanation+"</p>";
		code=code+"      <input type=\"text\" value=\"\" placeholder=\""+messicLang.settingsFuseGenrePlaceholder+"\">";
		code=code+"      <button id=\"messic-settings-genre-edit-ok\" class=\"button\">"+messicLang.settingsChangeGenreOk+"</button>";
		code=code+"      <button id=\"messic-settings-genre-edit-cancel\" class=\"button\" onclick=\"$(this).parent().parent().remove()\">"+messicLang.settingsChangeGenreCancel+"</button>";
		code=code+"  </div>";
		code=code+"</div>";

		$(code).hide().appendTo('body').fadeIn();

		$("#messic-settings-genre-edit-ok").click(function(){
	    	var newValue=$("#messic-settings-genre-fuse-container input").val();
	    	
	    	if(newValue.length<=0){
	    		UtilShowInfo(messicLang.settingsFuseGenreNeedName);
	    		return;
	    	}
	    	
	    	var data={}
	  	    $.ajax({
		        type: "POST",
		        async: false,
		        url: "services/genres/fuse?newName="+escape(newValue),
		        error: function (XMLHttpRequest, textStatus, errorThrown){
		        	UtilShowInfo("Error while merging!");
		        },
		        success: function (data){
		        	$("#messic-settings-genre-edit-container-overlay").remove();

		        	var duplicated;
		        	$("#messic-user-settings-content-genres-table tr").each(function(){
		        		var checked=$(this).find("input").is(":checked");
		        		if(checked){
		        			duplicated=$(this).clone();
		        			$(this).remove();
		        		}
		        	});
		        	
		        	$(duplicated).data("sid",data);
		        	$(duplicated).find(".messic-user-settings-genres-col-name").text(newValue);
		        	$("#messic-user-settings-content-genres-table tbody").prepend(duplicated);

				},
				processData: false,
				data: JSON.stringify(genresToFuse),
				contentType: "application/json"
			});
	    	
		});

	}else{
		UtilShowInfo(messicLang.settingsFuseGenreSelectMore);
	}
}

function settingsEditGenre(sid, name, trDiv){
	var code="<div id=\"messic-settings-genre-edit-container-overlay\">";
	code=code+"  <div id=\"messic-settings-genre-edit-container\">";
	code=code+"      <input type=\"text\" value=\""+name+"\">";
	code=code+"      <button id=\"messic-settings-genre-edit-ok\" class=\"button\">"+messicLang.settingsChangeGenreOk+"</button>";
	code=code+"      <button id=\"messic-settings-genre-edit-cancel\" class=\"button\" onclick=\"$(this).parent().parent().remove()\">"+messicLang.settingsChangeGenreCancel+"</button>";
	code=code+"  </div>";
	code=code+"</div>";
	
	
	$(code).hide().appendTo('body').fadeIn();
	
	$("#messic-settings-genre-edit-ok").click(function(){
    	var newValue=$("#messic-settings-genre-edit-container input").val();
  	    $.ajax({
	        type: "POST",
	        async: false,
	        url: "services/genres/"+sid+"?newName="+escape(newValue),
	        error: function (XMLHttpRequest, textStatus, errorThrown){
	        	UtilShowInfo("Error while updating!");
	        },
	        success: function (data){
	        	$("#messic-settings-genre-edit-container-overlay").remove();
	        	$(trDiv).find(".messic-user-settings-genres-col-name").text(newValue);
			}
		});
		
	});
	
}

function settingsRemoveGenre(sid, name, trDiv){
	nextFunction=function(removeContentAlso){
  	    $.ajax({
	        type: "DELETE",
	        async: true,
	        url: "services/genres/"+sid,
	        error: function (XMLHttpRequest, textStatus, errorThrown){
	    		UtilHideWait();
	        	UtilShowInfo("Error while removing!");
	        },
	        success: function (data){
	        	$(trDiv).remove();
	        	UtilShowInfo(messicLang.settingsRemoveGenreDone);
			}
		});
	}
	
	$.confirm({
		'title' : messicLang.settingsRemoveGenreTitle + " " + name,
		'message' : messicLang.settingsRemoveGenreContent,
		'buttons' : {
			'Yes' : {
				'title' : messicLang.confirmationYes,
				'class' : 'blue',
				'action' : function() {
					nextFunction();
				}
			},
			'No' : {
				'title' : messicLang.confirmationNo,
				'class' : 'gray',
				'action' : function() {
				} // Nothing to do in this case. You can as
					// well omit the action property.
			}
		}
	});
	
}

/** function to delete the user with sid */
function settingsRemoveUser(sid, name, trDiv){
	definitiveFunction=function(removeContentAlso){
		UtilShowWait(messicLang.settingsRemoveUserWait);

  	    $.ajax({
	        type: "DELETE",
	        async: true,
	        url: "services/settings/"+sid+"?removeMusicContent="+removeContentAlso,
	        error: function (XMLHttpRequest, textStatus, errorThrown){
	    		UtilHideWait();
	        	UtilShowInfo("Error while removing!");
	        },
	        success: function (data){
	    		UtilHideWait();
	        	$(trDiv).remove();
	        	UtilShowInfo(messicLang.settingsRemoveUserDone);
			}
		});
	}
	
	nextFunction=function(){
		$.confirm({
			'title' : messicLang.settingsRemoveUserDeleteMusicContentTitle,
			'message' : messicLang.settingsRemoveUserDeleteMusicContentContent,
			'buttons' : {
				'Yes' : {
					'title' : messicLang.confirmationYes,
					'class' : 'blue',
					'action' : function() {
						definitiveFunction(true);
					}
				},
				'No' : {
					'title' : messicLang.confirmationNo,
					'class' : 'gray',
					'action' : function() {
						definitiveFunction(false);
					}
				}
			}
		});
	}
	
	$.confirm({
		'title' : messicLang.settingsRemoveUserTitle + " " + name,
		'message' : messicLang.settingsRemoveUserContent,
		'buttons' : {
			'Yes' : {
				'title' : messicLang.confirmationYes,
				'class' : 'blue',
				'action' : function() {
					nextFunction();
				}
			},
			'No' : {
				'title' : messicLang.confirmationNo,
				'class' : 'gray',
				'action' : function() {
				} // Nothing to do in this case. You can as
					// well omit the action property.
			}
		}
	});

}

/** funcion to reset passwor of the user with sid */ 
function settingsResetPassword(sid, name){
	
	$.confirm({
		'title' : messicLang.settingsResetPasswordUserTitle + " " + name,
		'message' : messicLang.settingsResetPasswordUserContent,
		'buttons' : {
			'Yes' : {
				'title' : messicLang.confirmationYes,
				'class' : 'blue',
				'action' : function() {

			  	    $.ajax({
				        type: "POST",
				        async: true,
				        url: "services/settings/"+sid+"/resetPassword",
				        error: function (XMLHttpRequest, textStatus, errorThrown){
				        	UtilShowInfo("Error while reseting!");
				        },
				        success: function (data){
				        	UtilShowInfo(messicLang.settingsResetPasswordDone);
						}
					});

				}
			},
			'No' : {
				'title' : messicLang.confirmationNo,
				'class' : 'gray',
				'action' : function() {
				} // Nothing to do in this case. You can as
					// well omit the action property.
			}
		}
	});
	
}

/** function to load the validators */
function settingsLoadValidators(){
	settings_validator_basic = $(".messic-user-settings-basic-data-container").kendoValidator({
		rules: {
	        validUser: function(input){
	          if (input.is("[name=login]")) {
	        	var resultAjax=false;
	      	    $.ajax({
	    	        type: "POST",
	    	        async: false,
	    	        url: "services/settings/"+escape($("#messic-user-settings-user").val())+"/validate",
	    	        error: function (XMLHttpRequest, textStatus, errorThrown){
	    	        	resultAjax=false;
	    	        },
	    	        success: function (data){
	    	        	resultAjax=true;
	    			}
	    		});
	        	return resultAjax;
	          }
	          return true;
	        },
	        passwordLength: function(input) {
	          if (input.is("[name=password]")) {
	        	  return input.val().length>=5;
	          }
	          return true;
	        },
	        validPassword: function(input) {
	          if (input.is("[name=password]")) {
	        	  var pass1=$("#messic-user-settings-password").val();
	        	  var pass2=$("#messic-user-settings-password-confirm").val();
	            return pass1==pass2;
	          }
	          return true;
	        },
	        validCaptcha: function(input){
		          if (input.is("[name=captcha]")) {
		        	var resultAjax=false;
		      	    $.ajax({
		    	        type: "GET",
		    	        async: false,
		    	        url: "services/captcha/validate?id="+messic_user_settings_captcha_id+"&response="+input.val(),
		    	        error: function (XMLHttpRequest, textStatus, errorThrown){
		    	        	getCaptchaImage();
		    	        	resultAjax=false;
		    	        },
		    	        success: function (data){
		    	        	resultAjax=true;
		    			}
		    		});
		        	return resultAjax;
		          }
		          return true;
	        }
	      },
	      messages: {
	    	validUser: messicLang.settingsValidUser,
	        validPassword: messicLang.settingsValidPassword,
	        passwordLength: messicLang.settingsValidPasswordLength,
	        validCaptcha: messicLang.settingsValidCaptcha	        	
	      }
	}).data("kendoValidator");
	settings_validator_music = $("#messic-user-settings-content-music").kendoValidator().data("kendoValidator");
	settings_validator_admin = $("#messic-user-settings-content-admin").kendoValidator().data("kendoValidator");
	settings_validator_stats = $("#messic-user-settings-content-stats").kendoValidator().data("kendoValidator");
}

/** Init settings page for a new User */
function initSettingsNewUser(){

    $("#messic-user-settings-button-cancel").show();
	$("#messic-user-settings-button-accept").show();
	$("#messic-user-settings-button-previous").show();
	$("#messic-user-settings-button-savechanges").hide();
	$("#messic-user-settings-button-cancelchanges").hide();

	var messages=messicLang.messicMessagesNewUser1_1;
	if($("#messic-user-settings-content-admin").size()>0){
		//then it's an admin
		messages=messages+"||"+messicLang.messicMessagesNewUser1_2;
	}
   	UtilShowMessic(messicLang.messicMessagesNewUser1,messages);
	
    $("#messic-user-settings-button-previous").hide();
    

    	$(".messic-user-settings-title").text(messicLang.settingsNewUserTitle);
    	
		$("#messic-user-settings-button-accept").click(function() {
			var selected = $(".messic-user-settings-menu-visible")[0];
			if(selected.id=='messic-user-settings-content-basic')
	        {
	            if(settings_validator_basic.validate()) 
	            {
	            	
	                $("#messic-user-settings-button-previous").show();
	                if($("#messic-user-settings-menu-admin").size()>0){
		                selectTab($("#messic-user-settings-menu-admin"),$("#messic-user-settings-content-admin"));
	                }else{
		                selectTab($("#messic-user-settings-menu-music"),$("#messic-user-settings-content-music"));
	                }
	            }
	        }
	        else if(selected.id=='messic-user-settings-content-admin')
	        {
	            if(settings_validator_admin.validate())
	            {
	                $("#accept-button").text('Aceptar');
	                selectTab($("#messic-user-settings-menu-music"),$("#messic-user-settings-content-music"));
	            }
	        }
	        else if(selected.id=='messic-user-settings-content-music')
	        {
	            if(settings_validator_music.validate())
	            {
	                selectTab($("#messic-user-settings-menu-stats"),$("#messic-user-settings-content-stats"));
	            }
	        }
	        else
	        {            
	            if(settings_validator_stats.validate())
	            {                     
	                createUser();
	            }            
	        }
			
		});
		
		$("#messic-user-settings-button-previous").click(function() {
			
			var selected = $(".messic-user-settings-menu-visible")[0];
			if(selected.id=='messic-user-settings-content-music')
	        {
	            $("#previous-button").hide();
                if($("#messic-user-settings-menu-admin").size()>0){
    	            selectTab($("#messic-user-settings-menu-admin"),$("#messic-user-settings-content-admin"));            
                }else{
    	            selectTab($("#messic-user-settings-menu-basic"),$("#messic-user-settings-content-basic"));            
                }

	        }
	        else if(selected.id=='messic-user-settings-content-admin')
	        {            
	            selectTab($("#messic-user-settings-menu-basic"),$("#messic-user-settings-content-basic"));            
	        }
	        else if(selected.id=='messic-user-settings-content-stats')
	        {
	             $("#accept-button").text('Next');
	             selectTab($("#messic-user-settings-menu-music"),$("#messic-user-settings-content-music"));            
	        }
			
		});
		
		$("#messic-user-settings-button-cancel").click(function() {		
			cancelNewUser();	
		});
	}


/** function to change section in upload section */
function settingsChangeSection(nextFunction){
	var visible = $("#messic-user-settings-button-savechanges").is(':visible');
	if(visible){
		$.confirm({
			'title' : messicLang.settingsChangeSectionTitle,
			'message' : messicLang.settingsChangeSectionMessage,
			'buttons' : {
				'Yes' : {
					'title' : messicLang.confirmationYes,
					'class' : 'blue',
					'action' : function() {
						nextFunction();
					}
				},
				'No' : {
					'title' : messicLang.confirmationNo,
					'class' : 'gray',
					'action' : function() {
					} // Nothing to do in this case. You can as
						// well omit the action property.
				}
			}
		});
		
	}else{
		nextFunction();
	}
}

function initSettingsExistingUser(){
	
		//function to leave the upload section
		VAR_changeSection=settingsChangeSection;
	
    	$(".messic-user-settings-title").text(messicLang.settingsExistingUserTitle);

	    $("#messic-user-settings-button-cancel").hide();
		$("#messic-user-settings-button-accept").hide();
		$("#messic-user-settings-button-previous").hide();
		$("#messic-user-settings-button-savechanges").hide();
		$("#messic-user-settings-button-cancelchanges").hide();
		
	    
		loadUserSettings();

		$("input").change(function(){
			if(!$(this).hasClass("messic-user-settings-noedit")){
				$("#messic-user-settings-button-savechanges").show();
				$("#messic-user-settings-button-cancelchanges").show();
			}
		});

		$("#messic-user-settings-button-cancelchanges").click(function(){
			$.ajax({
				url: "settings.do",
				success: function(data){
					$("#messic-page-content").empty();
				    var posts = $($.parseHTML(data)).find("#messic-page-content").children();
				    $("#messic-page-content").append(posts);
				    initSettings(false);
				}
			});
		});
		
		$("#messic-user-settings-button-savechanges").click(function(){
			saveChanges();
		});

		$("#messic-user-settings-menu-basic").click(function() {
			selectTab($("#messic-user-settings-menu-basic"),$("#messic-user-settings-content-basic"));			
		});
		
		$("#messic-user-settings-menu-admin").click(function() {
			selectTab($("#messic-user-settings-menu-admin"),$("#messic-user-settings-content-admin"));			
		});
		
		$("#messic-user-settings-menu-music").click(function() {
			selectTab($("#messic-user-settings-menu-music"),$("#messic-user-settings-content-music"));			
		});

		$("#messic-user-settings-menu-stats").click(function() {
			selectTab($("#messic-user-settings-menu-stats"),$("#messic-user-settings-content-stats"));			
		});
		
	}

function sendData(flagNewUser){
	var userData={
			login: $("#messic-user-settings-user").val(),
			password: $("#messic-user-settings-password").val(),
			name: $("#messic-user-settings-name").val(),
			email: $("#messic-user-settings-email").val(),
			allowStatistics: $("#messic-user-settings-allowstatistics").is(":checked"),
			allowDLNA: $("#messic-user-settings-music-allowdlna").is(":checked"),
	}

    var file = $("#messic-user-settings-avatar-file")[0].files[0];
    if(file!=null)
	{
    	var reader = new FileReader();
    	reader.onload = function(e){
    		var bin = e.target.result;
    		userData.avatar_b64=UtilBase64ArrayBuffer(bin);
    		continueSendData(flagNewUser,userData);
    	}
    	reader.readAsArrayBuffer(file);
	}else{
		continueSendData(flagNewUser,userData);
	}
}

function continueSendData(flagNewUser, userData){
	
	if($("#messic-user-settings-content-admin").size()>0){
		var settingsData={
			allowUserCreation:$("#messic-user-settings-allowusercreation").is(":checked"),
			illegalCharacterReplacement:$("#messic-user-settings-illegalcharacterreplacement").val(),
			allowDLNA:$("#messic-user-settings-admin-allowdlna").is(":checked"),
		}

	    $.ajax({
	        url: "services/settings/admin",
	        type: "POST",
			processData: false,
			data: JSON.stringify(settingsData),
			contentType: "application/json",
			async: false,

	        error: function (XMLHttpRequest, textStatus, errorThrown) 
	    	{
	           console.log("error");
	        },
	        success: function (data) 
		  	{
	           console.log("sucess");
	           //nothing, just continue saving user 
		  	}
		});
	}

    $.ajax({
        url: "services/settings",
        type: "POST",
		processData: false,
		data: JSON.stringify(userData),
		contentType: "application/json",
		async: false,

        error: function (XMLHttpRequest, textStatus, errorThrown) 
    	{
           console.log("error");
        },
        success: function (data) 
	  	{
           console.log("sucess");
           if(flagNewUser){
		   	       	$.get("login.do", function(data) {
				   		 
				   		var nextFunction=function(){
				   		    $("body").html(data);
					   		 $( document ).ready(function() {
							       	$("#username").val(userData.login);
							       	$("#password").val(userData.password);
							       	$("#messic-login-button").click();
		
							   		 $( document ).ready(function() {
									       	UtilShowMessic(messicLang.messicMessagesWelcome2,
								       			       messicLang.messicMessagesWelcome2_1 + userData.name + messicLang.messicMessagesWelcome2_2+
								       			       "||"+
								       			       messicLang.messicMessagesWelcome2_3+
								       			       "||"+
								       			       messicLang.messicMessagesWelcome2_4+
								       			       "||"+
								       			       messicLang.messicMessagesWelcome2_5);
							   		 });
					   		});
				   		}
				   		
				       	UtilShowMessic(messicLang.settingsUserCreatedTitle,
				       			       messicLang.settingsUserCreatedMessage,
				       			       nextFunction);
				   	});
           }else{
	       		$("#messic-user-settings-button-savechanges").hide();
	    		$("#messic-user-settings-button-cancelchanges").hide();
	    		UtilShowInfo(messicLang.settingsChangesSaved);
           }
	  	}
	});	
}

function saveChanges() {
	$.confirm({
        'title'		: messicLang.settingsUserSavedTitle,
        'message'	: messicLang.settingsUserSavedMessage,
        'buttons'	: {
            'Yes'	: {
            	'title' : messicLang.confirmationYes,
                'class'	: 'blue',
                'action': function(){
                	sendData(false);
                }
            },
            'No'	: {
            	'title' : messicLang.confirmationNo,
                'class'	: 'gray',
                'action': function(){
                }	// Nothing to do in this case. You can as well omit the action property.
            }
        }
		});
}

function createUser() {
	$.confirm({
        'title'		: messicLang.settingsUserCreateTitle,
        'message'	: messicLang.settingsUserCreateMessage,
        'buttons'	: {
            'Yes'	: {
            	'title' : messicLang.confirmationYes,
                'class'	: 'blue',
                'action': function(){
                	sendData(true);
                }
            },
            'No'	: {
            	'title' : messicLang.confirmationNo,
                'class'	: 'gray',
                'action': function(){
                }	// Nothing to do in this case. You can as well omit the action property.
            }
        }
		});
}

function selectTab(menuElement, contentElement){

	//first, the menu element
	$(".messic-user-settings-menu-option-selected").each(function() {
    	$(this).removeClass("messic-user-settings-menu-option-selected").addClass("messic-user-settings-menu-option-unselected");
	});
	$(menuElement).removeClass("messic-user-settings-menu-option-unselected").addClass("messic-user-settings-menu-option-selected");
	
	//after the content element
	$(".messic-user-settings-menu-visible").each(function() {
		$(this).removeClass("messic-user-settings-menu-visible").addClass("messic-user-settings-menu-notvisible");
	});
	$(contentElement).removeClass("messic-user-settings-menu-notvisible").addClass("messic-user-settings-menu-visible");

    window.scrollTo(0,0);
}

function loadUserSettings()
{
	//first getting normal user settings
	$.ajax({
	    type: "GET",
	    url: "services/settings/",
	    dataType: "json",
	    error: function (XMLHttpRequest, textStatus, errorThrown) 
	    {
	        console.log('error');
	    },
	    success: function (data) 
	    {
	    	$("#messic-user-settings-user").val(data.login);
	    	$("#messic-user-settings-password").val(data.password);
	    	$("#messic-user-settings-password-confirm").val(data.password);
	    	$("#messic-user-settings-name").val(data.name);
	    	$("#messic-user-settings-email").val(data.email);
	    	$("#messic-user-settings-allowstatistics").val(data.allowStatistics);
	    	if(data.avatar_b64){
		    	$("#messic-user-settings-avatar-preview").attr("src","data:image/jpeg;base64,"+data.avatar_b64);
	    	}
	    	/*
	        var file = $("#messic-avatar")[0].files[0];
	        if(file!=null)
	    	{
	    		form_data.append("avatar", file);
	    	}
	    	*/   
	    }
	});

	if($("#messic-user-settings-content-admin").size()>0){
		//secondly adding admin user settings
		$.ajax({
		    type: "GET",
		    url: "services/settings/admin",
		    dataType: "json",
		    error: function (XMLHttpRequest, textStatus, errorThrown) 
		    {
		        console.log('error');
		    },
		    success: function (data) 
		    {
				$("#messic-user-settings-allowusercreation").val(data.allowUserCreation);
				$("#messic-user-settings-illegalcharacterreplacement").val(data.illegalCharacterReplacement);
		    }
		});
	}
}

function cancelNewUser()
{
	$.get("login.do", function(data) {
		 $("body").html(data);
	});
	
}

/** get the captcha image from the server */
function getCaptchaImage(){
    $.ajax({
        type: "GET",
        async: true,
	    dataType: "json",
        url: "services/captcha",
        success: function (data){
        	messic_user_settings_captcha_id=data.id;
        	$("#messic-user-settings-captcha").attr("src", "data:image/png;base64,"+data.captchaImage);
		}
	});
}
