<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<html>
	<head>
		
	</head>
    <body>

		<c:if test="${ message == null }">
		    <fmt:setBundle basename="ResourceBundle" var="message" scope="application"/>
		</c:if>
		    
		<div id="content">
            <div id="messic-about-window" onclick="$(this).remove()">
		        <div id="messic-about-close" onclick="$(this).parent().remove()"></div>
		        <div id="messic-about-tape">
			        <div id="messic-tape-leftwheel" class="messic-tape-wheel"></div>
			        <div id="messic-tape-rightwheel" class="messic-tape-wheel"></div>
		        </div>
		        <div id="messic-about-page">
			        <div id="messic-about-page-content">
				        <h2><center>Messic</center></h2>
                        <p><b>Author:&nbsp;</b>Jos&eacute; Amuedo Salmer&oacute;n [spheras]</p>
                        <p><b>Coauthor:&nbsp;</b>Manuel Hans Uber</p>
                        <p><b>Thanks to..&nbsp;</b></p>
                        <p>· Francsico Javier Coira</p>
                        </br>
                        <p> <b>Date:</b> 2013 </p>
                        <p> <b>Version:&nbsp;</b>${version}</p>
                        </br>
                        Copyright (C) 2013 José Amuedo Salmerón
                        </br></br>
                        This program is free software: you can redistribute it and/or modify
                        it under the terms of the GNU General Public License as published by
                        the Free Software Foundation, either version 3 of the License, or
                        (at your option) any later version.</br></br>
                        This program is distributed in the hope that it will be useful,
                        but WITHOUT ANY WARRANTY; without even the implied warranty of
                        MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
                        GNU General Public License for more details.</br></br>
                        You should have received a copy of the GNU General Public License
                        along with this program.  If not, see <a href="http://www.gnu.org/licenses/" target="_blank">http://www.gnu.org/licenses/</a>.                            
                        </br></br></br>
				        <h3> Third Party resources </h3>
                        <lu>
                            <li><a href="http://subtlepatterns.com/honey-im-subtle/" target="_blank">Honey Subtle</a>: Pattern used for background provided from <a href="http://subtlepatterns.com/" target="_blank">subtlepatterns</a>.</li>
                            <li><a href="http://freepsdfiles.net/graphics/free-psd-retro-audio-tape/" target="_blank">Free PSD retro audio Tape</a>: Audio tape design from <a href="http://freepsdfiles.net/" target="_blank">free psd files.</a></li>
                        </lu>
                        </br>
                        </br>
				        <p>Lorem ipsum dolor sit amet, consectetur adipiscing elit. Sed at mi et magna congue fringilla. Curabitur sit amet libero vitae est consectetur pharetra. Sed ac ante nec purus lacinia dignissim ut id nisl. Proin tempus tempor magna, ac molestie orci rutrum ut. Phasellus tristique dui eu urna convallis, condimentum euismod ligula auctor. Etiam arcu turpis, euismod ac risus sed, dapibus rutrum mi. Nunc posuere vulputate nibh at dignissim. Duis et erat facilisis, accumsan metus ac, pellentesque sapien. Praesent nisi nunc, auctor eu sem nec, ornare facilisis ante. Quisque vel justo ac lacus fermentum sollicitudin. Quisque sit amet orci eget ligula placerat suscipit a quis tellus. Aliquam sodales rhoncus feugiat. Nullam at ligula a nulla gravida mattis. Quisque urna augue, consectetur vitae ultricies non, volutpat et diam.</p>
				        <p>Nunc scelerisque suscipit malesuada. Nullam vitae justo bibendum turpis pulvinar aliquam eget ac elit. Ut fermentum a tortor ac rhoncus. Maecenas sed massa est. Integer facilisis dui id varius ullamcorper. Morbi et scelerisque libero. Integer dictum eu nunc ac consectetur.</p>
				        <p>Nullam et placerat eros, at laoreet tortor. Proin pretium pharetra massa ac placerat. Curabitur rhoncus dolor odio. Sed a est vel urna semper mattis. Donec et nibh ut metus sagittis suscipit. Nullam aliquam ligula nec dolor porttitor, a porttitor lorem porta. Proin tristique augue vitae quam varius convallis. Praesent quis ipsum felis. Ut ullamcorper lectus libero, vitae fermentum metus aliquet nec. Praesent at porta nunc. In gravida, tellus nec tincidunt egestas, erat purus suscipit urna, eget sodales magna tellus ut tortor. Maecenas ut rhoncus arcu, nec placerat nisl. Nam vestibulum mauris dui, at varius felis laoreet non.</p>
			        <div>
		        </div>
            </div>
		</div>
    </body>
</html>
