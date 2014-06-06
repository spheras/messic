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
function initUser(){
	
	$("#preview").click(function(){
		$("#messic-avatar").click();
	});
	  	
  	$("#messic-avatar").change(function() {
  		
  		var data = new FormData();
  		
  		var file = $("#messic-avatar")[0].files[0];
  		data.append("avatar", file);
  		
  		$.ajax({
		    type: "POST",
		    url: "user/avatar/create.do",
		    data: data,
		    cache: false,
		    contentType: false,
		    processData: false,
		    error: function (XMLHttpRequest, textStatus, errorThrown) 
		    	{
		        	console.log("error");
		    	},
		    success: function (data) 
		    	{
		    		$("#preview").attr("src", "user/avatar/show.do");		    		
		    	}
			});
  		
  	});
  	
  	$("#accept-buttom").click(function(){
		if(validatePassword())
		{
			if(validateEmail())
			{
				createUser();
			}
			else
			{
				alert("email incorrecto!!!");
				$("#email").focus();
			}
		}
		else
		{
			alert("confirmaci�n de contrase�a incorrecta!!!");
			$("#confirm-password").val("");
			$("#confirm-password").focus();
		}
		
 	});
  	
  	function createUser(){
  		var info = $("#user_data").serialize();	
		$.ajax({
		    type: "POST",
		    url: "user/create.do",
		    data: info, // serializes the form's elements.
		    error: function (XMLHttpRequest, textStatus, errorThrown) 
		    	{
		        	console.log('error');
		    	},
		    success: function (data) 
		    	{
			    	if(data.success==true)
			    	{
			    		$("#messic-user-window").fadeOut(500,function(){ 
						    $(this).remove();
                        });
			    	}
		    	}
			});
 	}
  	
  	function validateEmail() { 
    	var email = $("#email").val();	        	
        var re = /^(([^<>()[\]\\.,;:\s@\"]+(\.[^<>()[\]\\.,;:\s@\"]+)*)|(\".+\"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/;
        return re.test(email);
    } 
    
    function validatePassword() {
    	var password_1 = $("#password").val();	 
    	var password_2 = $("#confirm-password").val();	 
    	if(password_1==password_2)
		{
			return true;
		}
    	else
		{
			return false;
		}
    }
  	  	
}


