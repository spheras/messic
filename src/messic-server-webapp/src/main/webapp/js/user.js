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
			alert("confirmación de contraseña incorrecta!!!");
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


