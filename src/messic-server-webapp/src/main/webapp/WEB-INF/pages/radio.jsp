<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
    <%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

        <!DOCTYPE html>
        <!--[if lt IE 7]>      <html class="no-js lt-ie9 lt-ie8 lt-ie7"> <![endif]-->
        <!--[if IE 7]>         <html class="no-js lt-ie9 lt-ie8"> <![endif]-->
        <!--[if IE 8]>         <html class="no-js lt-ie9"> <![endif]-->
        <!--[if gt IE 8]><!-->
        <html class="no-js">
        <!--<![endif]-->

        <head>
            <meta charset="utf-8">
            <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
            <title>messic</title>
            <meta name="description" content="messic for the music mess">
            <meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=no">

            <link rel="shortcut icon" href="img/messic-icon.png" type="image/png">
            <!-- For third-generation iPad with high-resolution Retina display: -->
            <link rel="apple-touch-icon-precomposed" sizes="144x144" href="img/apple-touch-icon-144x144-precomposed.png">
            <!-- For iPhone with high-resolution Retina display running iOS 7: -->
            <link rel="apple-touch-icon-precomposed" sizes="120x120" href="img/apple-touch-icon-120x120-precomposed.png">
            <!-- For iPhone with high-resolution Retina display running iOS 6: -->
            <link rel="apple-touch-icon-precomposed" sizes="114x114" href="img/apple-touch-icon-114x114-precomposed.png">
            <!-- For first- and second-generation iPad: -->
            <link rel="apple-touch-icon-precomposed" sizes="72x72" href="img/apple-touch-icon-72x72-precomposed.png">
            <!-- for non-Retina iPhone and iPod Touch: -->
            <link rel="apple-touch-icon-precomposed" sizes="57x57" href="img/apple-touch-icon-52x52-precomposed.png">
            <!-- For non-Retina iPhone, iPod Touch, and Android 2.1+ devices: -->
            <link rel="apple-touch-icon-precomposed" href="img/apple-touch-icon-precomposed.png">


            <!-- jquery-ui styles -->
            <link rel="stylesheet" href="css/themes/hansv2/jquery-ui/jquery-ui.css?timestamp=${timestamp}">

            <!-- normalization -->
            <link rel="stylesheet" href="css/normalize.min.css?timestamp=${timestamp}">
            <link rel="stylesheet" href="css/main.css?timestamp=${timestamp}">

            <!-- Theme Style -->
            <link rel="stylesheet" href="css/themes/hansv2/base.css?timestamp=${timestamp}">
            <link rel="stylesheet" href="css/themes/hansv2/playlist.css?timestamp=${timestamp}">
            <link rel="stylesheet" href="css/themes/hansv2/radio.css?timestamp=${timestamp}">

            <!-- other css -->
            <link rel="stylesheet" href="css/themes/hansv2/animate.css?timestamp=${timestamp}">

            <!-- modernizr -->
            <script src="js/vendor/modernizr-2.6.2.min.js?timestamp=${timestamp}"></script>
        </head>

        <body>
            <c:if test="${ message == null }">
                <fmt:setBundle basename="org.messic.jsp.resourcebundles.ResourceBundle" var="message" scope="session" />
            </c:if>

            <!--[if lt IE 11]>
	            <p class="browsehappy"><fmt:message key="login-outdated" bundle="${message}"/></p>
	        <![endif]-->



            <!-- JQUERY -->
            <script src="js/vendor/jquery/jquery-1.11.3.min.js?timestamp=${timestamp}"></script>
            <script src="js/vendor/jquery/jquery-ui-1.11.14.min.js?timestamp=${timestamp}"></script>

            <!-- jplayer -->
            <script src="js/vendor/jplayer/jquery.jplayer.min.js?timestamp=${timestamp}"></script>
            <script src="js/jplayer.playlist.messic.js?timestamp=${timestamp}"></script>
            <script src="js/jplayer-hack-drag.js?timestamp=${timestamp}"></script>
            
            
            <!-- radio -->
            <script src="js/main.js?timestamp=${timestamp}"></script>
            <script src="js/utils.js?timestamp=${timestamp}"></script>
            <script src="js/radio.js?timestamp=${timestamp}"></script>
            
            <c:if test="${radioStatus==true}">
            	<div id="messic-background" class="messic-background-radio"></div>
            	<div id="messic_radio_logo"><a href="http://spheras.github.io/messic/" target="_blank">messic</a></div>
	        	<p class="messic_radio_alone_visit"><fmt:message key="radio-advisory-visitus" bundle="${message}"/> <a href="http://spheras.github.io/messic/" target="_blank">messic</a></p>
				
					<div id="jquery_jplayer" class="jp-jplayer"></div>
				
                    <div class="jp-audio messic_radio_player_alone" id="jquery_jplayer_content" data-radiourl="${radioInfoURL}">
                        <div class="jp-type-playlist">
                            <div class="jp-gui jp-interface">
                                <ul class="jp-controls">
                                    <li><a tabindex="1" class="jp-previous" href="javascript:;">previous</a>
                                    </li>
                                    <li><a tabindex="1" class="jp-play" href="javascript:;">play</a>
                                    </li>
                                    <li><a tabindex="1" class="jp-pause" href="javascript:;" style="display: none;">pause</a>
                                    </li>
                                    <li><a tabindex="1" class="jp-next" href="javascript:;">next</a>
                                    </li>
                                </ul>
                                
                                <div class="jp-progress">

                                    <div id="jp-seek-bar" class="jp-seek-bar" style="width: 100%;">
                                        <div id="jp-play-bar" class="jp-play-bar" style="width: 0%;">
                                            <div class="jp-play-marker"></div>
                                        </div>
                                    </div>

                                </div>
                                
                                <div class="jp-volume-bar">
                                    <div class="jp-volume-bar-line">
                                        <div class="jp-volume-bar-value">
                                            <div class="jp-volume-marker"></div>
                                        </div>
                                    </div>
                                </div>
                                                                
                                <div class="jp-time-holder"> <span class="jp-current-time">00:00</span><span class="jp-duration">04:27</span> 
                                </div>
                                <ul class="jp-toggles">
                                    <li><a title="random off" tabindex="1" class="jp-shuffle" href="javascript:;"></a>
                                    </li>
                                    <li><a title="random on" tabindex="1" class="jp-shuffle-off" href="javascript:;" style="display: none;"></a>
                                    </li>
                                    <li><a title="repeat off" tabindex="1" class="jp-repeat" href="javascript:;"></a>
                                    </li>
                                    <li><a title="repeat on" tabindex="1" class="jp-repeat-off" href="javascript:;" style="display: none;"></a>
                                    </li>
                                </ul>
                            </div>

                            <div class="jp-no-solution" style="display: none;"> <span>Update Required</span> To play the media you will need to either update your browser to a recent version or update your <a target="_blank" href="http://get.adobe.com/flashplayer/">Flash plugin</a>.</div>
                        </div>
	                    <div id="messic_radio_alone_description_container">
	                    	<div id="messic_radio_alone_info">
	                    		<img id="messic_radio_alone_cover" ></img>
	                    		<div id="messic_radio_alone_author"></div>
	                    		<div id="messic_radio_alone_album"></div>
	                    		<div id="messic_radio_alone_song"></div>
	                    		<div id="messic_radio_alone_actions">
	                    			<div id="messic_radio_alone_action_wikipedia"></div>
	                    			<div id="messic_radio_alone_action_youtube"></div>
	                    			<div id="messic_radio_alone_action_fx"></div>
	                    		</div>
	                    	</div>
	                    	<label>
	                    	<a id="messic_radio_alone_showinfo" href="#" class="animated bounceInLeft"><fmt:message key="radio-message-continue" bundle="${message}"/></a><br>
	                    	<center>
	                    		<b><u><fmt:message key="radio-message-title" bundle="${message}"/></b></u>
	                    		<p><fmt:message key="radio-message-content" bundle="${message}"/><a href="http://spheras.github.io/messic/"><fmt:message key="radio-message-content-messicwebpage" bundle="${message}"/></a>.</p>
	                    	</center></label>
	                    	
	                    </div>
                    </div>
				
           </c:if> 
	        <c:if test="${radioStatus==false}">
	        	<div id="messic_radio_false"></div>
	        	<div class="messic_radio_alone_noradio"><label><fmt:message key="radio-message-noradio" bundle="${message}"/> <br><u><b><fmt:message key="radio-message-noradio-comeback" bundle="${message}"/></b></u></label></div>
	        	<p class="messic_radio_alone_visit"><fmt:message key="radio-advisory-visitus" bundle="${message}"/> <a href="http://spheras.github.io/messic/" target="_blank">messic</a></p>
	        </c:if>
        </body>
        
        	<script>
			    $( document ).ready(function() {
			        messicRadioPlayerLoad();
			    });
		    </script>

        </html>