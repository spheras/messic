$(document).ready(function() {
	var loginWindow = $("#messic-login-window");
	var loginShadow = $("#messic-login-shadow");

	//make the window dragablle
	loginWindow.draggable({
	    drag: function(event, ui){
	 		moveShadow();	 
	    }
	});
	
	//the loginShadow of the window will move at the same time as the loginWindow
	function moveShadow(){	 
	    logoX           =   parseInt(loginWindow.offset().left);
	 	logoY           =   parseInt(loginWindow.offset().top);
	 	shadowPosLeft   =   logoX + "px";
	 	shadowPosTop    =   logoY + (loginWindow.height()+1) + "px";
	 	loginShadow.css({ "left": shadowPosLeft, "top": shadowPosTop});	 
	}
	
	//centering the loginWindow
	jQuery.fn.center = function () {
		this.css("position","absolute");
		this.css("top", ( $(document).height() - this.height() ) / 2+$(loginWindow).scrollTop() + "px");
		this.css("left", ( $(document).width() - this.width() ) / 2+$(loginWindow).scrollLeft() + "px");
		loginShadow.css({"top": loginWindow.offset().top + loginWindow.height() +1 ,"left": loginWindow.offset().left });
	}
	loginWindow.center();

	//add the login button functionality
	$("#messic-login-button").click(function(){
		info = $("#messic-login-form").serialize();
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
			    		var userId = data.userId;
			    		
						$.get("main.do", function(data){ 
    						$("#messic-logo1").attr("class","messic-main");
    						$("#messic-logo2").attr("class","messic-main");

						    //let's hide and remove the login window!
						    $("#messic-login-shadow").fadeOut(500,function(){ 
							    $(this).remove();
                            });
						    $("#messic-login-window").fadeOut(1000,function(){ 
							    $(this).remove();
							    var posts = $($.parseHTML(data)).filter('#content').children();
							    $("body").append(posts);
							    initMessic(userId);
						    });
 
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

    //show the about page on logo1 click
    $("#messic-logo1").click(function(){
		$.get("about.do", function(data){ 
	        var posts = $($.parseHTML(data)).filter('#content').children();
	        $("body").append(posts);

            var window = $("#messic-about-window");
            window.kendoWindow({
                            width: "410px",
                            height: "400px",
                            title: "About Messic",
                            modal: true,
                            actions: [
                                "Close"
                            ],
                            deactivate: function() {
                                this.destroy();                                           
                            }
                        });
            var kendoWindow = $("#messic-about-window").data("kendoWindow");
            kendoWindow.center();
        });
    });
    
    
    //show the new user window
    $("#newAccount").click(function(){
		$.get("settings.do", function(data){ 
	        $("body").html(data);
            initUser();
        });
    });
    

});


