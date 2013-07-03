$(document).ready(function() {
	var window = $("#window");
	var shadow = $("#shadow");
	
	
	
	window.draggable({
	    drag: function(event, ui){
	 		moveShadow();	 
	    }
	});
	
	function moveShadow(){	 
	    logoX           =   parseInt(window.offset().left);
	 	logoY           =   parseInt(window.offset().top);
	 	shadowPosLeft   =   logoX + "px";
	 	shadowPosTop    =   logoY + window.height() + "px";
	 	shadow.css({ "left": shadowPosLeft, "top": shadowPosTop});	 
	}
	
	jQuery.fn.center = function () {
		this.css("position","absolute");
		this.css("top", ( $(document).height() - this.height() ) / 2+$(window).scrollTop() + "px");
		this.css("left", ( $(document).width() - this.width() ) / 2+$(window).scrollLeft() + "px");
		shadow.css({"width": window.width(), "height": "100px", "top": window.offset().top + window.height()  ,"left": window.offset().left });
	}
	// Ejecutamos la función
	window.center();
	
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
