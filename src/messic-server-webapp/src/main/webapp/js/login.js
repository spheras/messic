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
/* messic token for petitions */
var VAR_MessicToken;


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

    $("#password").keyup(function(event){
        if(event.keyCode == 13){
            $("#messic-login-button").click();
        }
    });

	//add the login button functionality
	$("#messic-login-button").click(function(){
		info = $("#messic-login-form").serialize();
		$.ajax({
		    type: "POST",
		    url: "messiclogin",
		    async: false,
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
			    		VAR_MessicToken=data.messic_token;
			    		$.ajaxSetup({
			    		    beforeSend: function(xhr) {
			    		        xhr.setRequestHeader('messic_token', data.messic_token);
			    		    }
			    		});
			    		
			    		$.ajax({
			    		    type: "GET",
			    		    url: "main.do",
			    		    error: function (XMLHttpRequest, textStatus, errorThrown){
		    		        	console.log('error');
		    		    	},
			    		    success: function(data){ 
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
    							    initMessic();
    						    });
    						}
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
            initSettings(true);
        });
    });
    

});


