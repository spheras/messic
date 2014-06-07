/**
 * @source: https://github.com/spheras/messic
 *
 * @licstart  The following is the entire license notice for the 
 *  JavaScript code in this page.
 *
 * Copyright (C) 2013  José Amuedo Salmerón
 *
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
var logged_user_sid = null;

function initUser()
{
	initUser(null);
}

function initUser(logged_user){
	
	var validator_basic = $("#basic-data").kendoValidator().data("kendoValidator");
    var validator_admin = $("#admin-data").kendoValidator().data("kendoValidator");
    var validator_music = $("#music-data").kendoValidator().data("kendoValidator");
    var validator_stats = $("#stats-data").kendoValidator().data("kendoValidator");
		
    $("#previous-button").hide();
    
    if(logged_user==null)
	{
		$("#accept-button").click(function() {
			
			var selected = $("#data-container").find("div[class='messic-user-settings-container messic-user-settings-menu-visible']")[0];
			if(selected.id=='basic-data')
	        {
	            if(validator_basic.validate())
	            {
	                $("#previous-button").show();
	                selectTab($("#music-option"),'music');
	            }
	        }
	        else if(selected.id=='music-data')
	        {
	            if(validator_music.validate())
	            {
	                selectTab($("#admin-option"),'admin');
	            }
	        }
	        else if(selected.id=='admin-data')
	        {
	            if(validator_admin.validate())
	            {
	                $("#accept-button").text('Aceptar');
	                selectTab($("#stats-option"),'stats');
	            }
	        }
	        else
	        {            
	            if(validator_stats.validate())
	            {                     
	                sendData();
	            }            
	        }
			
		});
		
		$("#previous-button").click(function() {
			
			var selected = $("#data-container").find("div[class='messic-user-settings-container messic-user-settings-menu-visible']")[0];
			if(selected.id=='music-data')
	        {
	            $("#previous-button").hide();
	            selectTab($("#basic-option"),'basic');            
	        }
	        else if(selected.id=='admin-data')
	        {            
	            selectTab($("#music-option"),'music');            
	        }
	        else if(selected.id=='stats-data')
	        {
	             $("#accept-button").text('Siguiente');
	             selectTab($("#admin-option"),'admin');            
	        }
			
		});
		
		$("#cancel-button").click(function() {		
			closeUserWindow();	
		});
		
	}
	else
	{
		
	    $("#cancel-button").hide();
	    
		loadUserSettings(logged_user);
		
		$("#accept-button").text('Aceptar');
		
		$("#basic-option").click(function() {
			selectTab($("#basic-option"),'basic');			
		});
		
		$("#music-option").click(function() {
			selectTab($("#music-option"),'music');			
		});

		$("#admin-option").click(function() {
			selectTab($("#admin-option"),'admin');			
		});

		$("#stats-option").click(function() {
			selectTab($("#stats-option"),'stats');			
		});
		
		$("#accept-button").click(function() {
			
			var selected = $("#data-container").find("div[class='messic-user-settings-container messic-user-settings-menu-visible']")[0];
			if(selected.id=='basic-data')
	        {
	            if(validator_basic.validate())
	            {
	            	sendData();
	            }
	        }
	        else if(selected.id=='music-data')
	        {
	            if(validator_music.validate())
	            {
	            	sendData();
	            }
	        }
	        else if(selected.id=='admin-data')
	        {
	            if(validator_admin.validate())
	            {
	            	sendData();
	            }
	        }
	        else
	        {            
	            if(validator_stats.validate())
	            {                     
	                sendData();
	            }            
	        }
			
		});
		
	}
    
	$("#preview").click(function(){
		$("#messic-avatar").click();
	});
	
	$("#messic-avatar").change(function() {
	
		var reader = new FileReader();

        reader.onload = function (e) {
            $('#preview').attr('src', e.target.result);
        }
		
		var file = $("#messic-avatar")[0].files[0];
		
        reader.readAsDataURL(file);
		
	});
	
	
	
}

function sendData() {
    
    var form_data = new FormData();

    $("#data-container").find("input[type='text'],[type='password'],[type='email']").each(function () {
        form_data.append(this.name,this.value);
    });
    
    var file = $("#messic-avatar")[0].files[0];
    if(file!=null)
	{
		form_data.append("avatar", file);
	}    
    
    if(logged_user_sid!=null)
    {
    	form_data.append("sid", logged_user_sid);
    }    
    
    $.ajax({
        type: "POST",
        url: "users/create.do",
        data: form_data,
        cache: false,
        contentType: false,
        processData: false,
        error: function (XMLHttpRequest, textStatus, errorThrown) 
	    	{
	           console.log("error");
	        },
        success: function (data) 
		  	{
        		closeUserWindow();		    		
		  	}
	});
			
}

function selectTab(element, type){
	
	$(".messic-user-settings-menu-container").children("div").each(function() {
	    if($(this).attr("class")=="messic-user-settings-menu-option-selected")
	    {
	    	$(this).removeClass("messic-user-settings-menu-option-selected").addClass("messic-user-settings-menu-option-unselected");
	    	var current_class = $(this).children("img").attr("class");
	    	$(this).children("img").removeClass(current_class).addClass(current_class.replace("selected","unselected"));
	    }
	});
	
	$(element).removeClass("messic-user-settings-menu-option-unselected").addClass("messic-user-settings-menu-option-selected");
	$(element).children("img").removeClass("messic-user-settings-menu-option-"+type+"-image-unselected").addClass("messic-user-settings-menu-option-"+type+"-image-selected");	
	
	loadSection(type);
				
}

function loadSection(type)
{
	$("#basic-data").removeClass("messic-user-settings-menu-visible").addClass("messic-user-settings-menu-notvisible");
	$("#music-data").removeClass("messic-user-settings-menu-visible").addClass("messic-user-settings-menu-notvisible");
	$("#admin-data").removeClass("messic-user-settings-menu-visible").addClass("messic-user-settings-menu-notvisible");
	$("#stats-data").removeClass("messic-user-settings-menu-visible").addClass("messic-user-settings-menu-notvisible");
	
	$("#"+type+"-data").removeClass("messic-user-settings-menu-notvisible").addClass("messic-user-settings-menu-visible");
	
}

function loadUserSettings(logged_user)
{
	$.ajax({
	    type: "GET",
	    url: "services/users/" + logged_user,
	    dataType: "json",
	    error: function (XMLHttpRequest, textStatus, errorThrown) 
	    {
	        console.log('error');
	    },
	    success: function (data) 
	    {	
	    	$("#user").prop('disabled', true);
		    $("#user").val(data.login);
		    $("#name").val(data.name);
		    $("#password").val(data.password);
		    $("#confirm-password").val(data.password);
		    $("#email").val(data.email);
		    $("#storePath").val(data.storePath);
		    $("#preview").attr('src', 'data:image/png;base64,' + data.avatar);
		    logged_user_sid = data.sid;
	    }
	});
}

function closeUserWindow()
{
	if(logged_user==null)
	{
		$.get("login.do", function(data) {
			 $("body").html(data);
		});
	}
	else
	{
		$("#messic-page-content").empty();
	}
	
}
