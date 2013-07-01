$(document).ready(function() {
	var window = $("#window");

	var onClose = function() {
		alert('closing!');
	}

	var win = $("#window").kendoWindow({
		width: "600px",
		title: "About Alvar Aalto",
		close: onClose
	}).data("kendoWindow").center();
    		
});

function on_login()
{
	info = $("#login_form").serialize();
	console.log(info);
	$.ajax({
        type: "POST",
        url: "j_spring_security_check",
        data: info, // serializes the form's elements.
        dataType: "json",
        error: function (XMLHttpRequest, textStatus, errorThrown) 
        	{
            	console.log('error');
        	},
        success: function (data) 
        	{
	        	if(data.success==true)
	        	{
	        		$("body").load('main.html');
	        	}
	        	else
	        	{
	        		$("#window").parent().effect("shake");
	        	}
        	}
      });
}
