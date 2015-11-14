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
            <link rel="stylesheet" href="css/themes/hansv2/jquery.confirm.css?timestamp=${timestamp}">

            <!-- normalization -->
            <link rel="stylesheet" href="css/normalize.min.css?timestamp=${timestamp}">
            <link rel="stylesheet" href="css/main.css?timestamp=${timestamp}">

            <!-- Theme Style -->
            <link rel="stylesheet" href="css/themes/hansv2/base.css?timestamp=${timestamp}">
            <link rel="stylesheet" href="css/themes/hansv2/home.css?timestamp=${timestamp}">
            <link rel="stylesheet" href="css/themes/hansv2/login.css?timestamp=${timestamp}">
            <link rel="stylesheet" href="css/themes/hansv2/playlist.css?timestamp=${timestamp}">
            <link rel="stylesheet" href="css/themes/hansv2/playlists.css?timestamp=${timestamp}">
            <link rel="stylesheet" href="css/themes/hansv2/about.css?timestamp=${timestamp}">
            <link rel="stylesheet" href="css/themes/hansv2/upload.css?timestamp=${timestamp}">
            <link rel="stylesheet" href="css/themes/hansv2/settings.css?timestamp=${timestamp}">
            <link rel="stylesheet" href="css/themes/hansv2/base.css?timestamp=${timestamp}">
            <link rel="stylesheet" href="css/themes/hansv2/api.css?timestamp=${timestamp}">
            <link rel="stylesheet" href="css/themes/hansv2/explore.css?timestamp=${timestamp}">
            <link rel="stylesheet" href="css/themes/hansv2/album.css?timestamp=${timestamp}">
            <link rel="stylesheet" href="css/themes/hansv2/author.css?timestamp=${timestamp}">

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

            <div id="messic-login-android-container">
                <a href="https://play.google.com/store/apps/details?id=org.messic.android" target="_blank" id="messic-login-android-logo-googleplay"></a>
                <a href="http://www.amazon.com/gp/product/B010LXOPB0/ref=mas_pm_messic" target="_blank" id="messic-login-android-logo-amazon"></a>
            </div>

            <label class="messic-login-version">${version}</label>
            <div id="messic-login-window" style="display:none">
                <div class="messic-login-form-container">
                    <div class="messic-form">
                        <h1><fmt:message key="login-welcome" bundle="${message}"/></h1>
                        <!--  TODO <a href="/restpassword"><fmt:message key="login-forgot" bundle="${message}"/></a> -->
                        <form id="messic-login-form" class="login-form-style">
                            <ul>
                                <li>
                                    <label for="username" class="login-form-label">
                                        <fmt:message key="login-username" bundle="${message}" />
                                    </label>
                                    <input id="username" name="j_username" type="text" value="" autofocus>
                                </li>
                                <li>
                                    <label for="password" class="login-form-label">
                                        <fmt:message key="login-password" bundle="${message}" />
                                    </label>
                                    <input id="password" name="j_password" type="password" value="" />
                                </li>
                            </ul>
                        </form>
                        <div class="checkboxcontainer">
                            <input id="messic_login_rememberme" type="checkbox" />
                            <label for="messic_login_rememberme" class="login-form-label">
                                <fmt:message key="login-rememberme" bundle="${message}" />
                            </label>
                            <div class="divclearer"></div>
                        </div>
                        <button id="messic-login-button" class="button play">
                            <fmt:message key="login-button" bundle="${message}" />
                        </button>
                        <div id="messic-login-logo"></div>
                    </div>
                </div>
                <% boolean allowCreation=(Boolean)request.getAttribute( "allowUserCreation"); if(allowCreation){ %>
                    <div class="messic-login-new-account-container">
                        <div class="messic-form">
                            <h1><fmt:message key="login-newaccount" bundle="${message}"/></h1>
                            <label>
                                <fmt:message key="login-newaccount-explanation1" bundle="${message}" />
                            </label>
                            <label>
                                <fmt:message key="login-newaccount-explanation2" bundle="${message}" />
                            </label>
                            <button id="newAccount" class="button spark">
                                <fmt:message key="login-newaccount-button" bundle="${message}" />
                            </button>
                        </div>
                    </div>
                    <%}%>
            </div>

            <div id="messic-wait">
                <div id="messic-wait-box">
                    <p></p>
                </div>
            </div>

            <!-- JQUERY -->
            <script src="js/vendor/jquery/jquery-1.11.3.min.js?timestamp=${timestamp}"></script>
            <script src="js/vendor/jquery/jquery-ui-1.11.14.min.js?timestamp=${timestamp}"></script>
            <script src="js/vendor/jquery/jquery.validate-1.14.0.min.js?timestamp=${timestamp}"></script>

            <!-- jplayer -->
            <script src="js/vendor/jplayer/jquery.jplayer.min.js?timestamp=${timestamp}"></script>
            <script src="js/jplayer.playlist.messic.js?timestamp=${timestamp}"></script>
            <script src="js/jplayer-hack-drag.js?timestamp=${timestamp}"></script>

            <!-- jquery plugins -->
            <script src="js/jquery.confirm.js?timestamp=${timestamp}"></script>
            <script src="js/jquery.longpress.js?timestamp=${timestamp}"></script>

            <!-- tiny scrollbar and tinycarousel -->
            <script src="js/vendor/jquery.tinyscrollbar.2.1.8.js?timestamp=${timestamp}"></script>
            <script src="js/vendor/jquery.tinycarousel.js?timestamp=${timestamp}"></script>

            <!-- internal messic js -->
            <script src="lang.do?timestamp=${timestamp}"></script>
            <script src="js/utils.js?timestamp=${timestamp}"></script>
            <script src="js/plugins.js?timestamp=${timestamp}"></script>
            <script src="js/login.js?timestamp=${timestamp}"></script>
            <script src="js/main.js?timestamp=${timestamp}"></script>
            <script src="js/upload.js?timestamp=${timestamp}"></script>
            <script src="js/explore.js?timestamp=${timestamp}"></script>
            <script src="js/album.js?timestamp=${timestamp}"></script>
            <script src="js/author.js?timestamp=${timestamp}"></script>
            <script src="js/playlist.js?timestamp=${timestamp}"></script>
            <script src="js/uploadresource.js?timestamp=${timestamp}"></script>
            <script src="js/uploadalbum.js?timestamp=${timestamp}"></script>
            <script src="js/uploadprocess.js?timestamp=${timestamp}"></script>
            <script src="js/uploadpool.js?timestamp=${timestamp}"></script>
            <script src="js/uploadvolume.js?timestamp=${timestamp}"></script>
            <script src="js/uploadwizard.js?timestamp=${timestamp}"></script>
            <script src="js/uploadwizardmatcher.js?timestamp=${timestamp}"></script>
            <script src="js/settings.js?timestamp=${timestamp}"></script>
            <script src="js/apidoc.js?timestamp=${timestamp}"></script>
            <script src="js/about.js?timestamp=${timestamp}"></script>
            <script src="js/utilajaxpool.js?timestamp=${timestamp}"></script>

            <% boolean firstTime=(Boolean)request.getAttribute( "firstTime"); if(firstTime){%>
                <script type="text/javascript">
                    $(window).load(function () {
                        var messages = [
                   messicLang.messicMessagesWelcome1_1,
                   "||",
                   messicLang.messicMessagesWelcome1_2,
                   "||",
                   messicLang.messicMessagesWelcome1_3,
                   "||",
                   messicLang.messicMessagesWelcome1_4
                ].join("");

                        UtilShowMessic(messicLang.messicMessagesWelcome1, messages);
                    });
                </script>
                <% } %>
        </body>

        </html>