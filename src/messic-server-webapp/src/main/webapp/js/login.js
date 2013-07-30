$(document).ready(function() {
	var window = $("#messic-login-window");
	var shadow = $("#messic-login-shadow");
	
	//make the window dragablle
	window.draggable({
	    drag: function(event, ui){
	 		moveShadow();	 
	    }
	});
	
	//the shadow of the window will move at the same time as the window
	function moveShadow(){	 
	    logoX           =   parseInt(window.offset().left);
	 	logoY           =   parseInt(window.offset().top);
	 	shadowPosLeft   =   logoX + "px";
	 	shadowPosTop    =   logoY + window.height() + "px";
	 	shadow.css({ "left": shadowPosLeft, "top": shadowPosTop});	 
	}
	
	//centering the window
	jQuery.fn.center = function () {
		this.css("position","absolute");
		this.css("top", ( $(document).height() - this.height() ) / 2+$(window).scrollTop() + "px");
		this.css("left", ( $(document).width() - this.width() ) / 2+$(window).scrollLeft() + "px");
		shadow.css({"width": window.width(), "height": "100px", "top": window.offset().top + window.height()  ,"left": window.offset().left });
	}
	window.center();
	

	//add the login button functionality
	$("#messic-login-button").click(function(){
		info = $("#messic-login-form").serialize();
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
						//let's hide and remove the login window!
						$("#messic-login-shadow").fadeOut(500);
						$("#messic-login-window").fadeOut(1000,function(){ 
							$(this).remove();
						});

						$.get("main.do #content", function(data){ 
							var posts = $($.parseHTML(data)).filter('#content').children();
							$("body").append(posts);
							initMain();
						});
			    	}
			    	else
			    	{
						//error, shake!!
			    		$("#messic-login-window").parent().effect("shake");
			    	}
		    	}
			});
 	});
});


