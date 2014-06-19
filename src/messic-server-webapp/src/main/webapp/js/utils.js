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
function UtilGetRandom(from,to){
	return Math.floor(Math.random() * to) + from;
}
/* function to escape quotes in string variables, useful when are inserted in html/javascript code */
function UtilEscapeHTML(str) {
    return String(str).replace(/&/g, '&amp;').replace(/</g, '&lt;').replace(/>/g, '&gt;').replace(/"/g, '&quot;');
}
/* function that escape all those characteres that are used in javascript, like ' by \' ... */
function UtilEscapeJS(str){
    return String(str).replace(/&/g, '&amp;').replace(/</g, '&lt;').replace(/>/g, '&gt;').replace(/"/g, '&quot;').replace(/'/g,'\\\'');
}
/* Obtain a GUID - globally unique identifier */
function UtilGetGUID() {
  return UtilS4() + UtilS4() + '-' + UtilS4() + '-' + UtilS4() + '-' +
         UtilS4() + '-' + UtilS4() + UtilS4() + UtilS4();
}
function UtilS4() {
  return Math.floor((1 + Math.random()) * 0x10000)
             .toString(16)
             .substring(1);
};

/* Obtain the extension of a filename */
function UtilGetFileExtension(filename){
	var a = filename.split(".");
	if( a.length === 1 || ( a[0] === "" && a.length === 2 ) ) {
	    return "";
	}
	return a.pop();
}

/* Obtain the name of the track from the filename, trying to identify if there is a pattern for the track number, and removing it */
function UtilRemoveTrackNumberFromFileName(filename){
	filename=UtilGetFileNameWithoutExtension(filename);
	if(filename.indexOf('-')>0 || filename.indexOf('.')>0){
		var where=filename.indexOf('-');
		if(where<0){
			where=filename.indexOf('.');
		}
		if(where<4){
			return filename.substring(where+1).trim();
		}
	}

	return filename.trim();
}

/* Obtain the file name without the extension */
function UtilGetFileNameWithoutExtension(filename){
	var a = filename.split(".");
	if( a.length === 1 || ( a[0] === "" && a.length === 2 ) ) {
	    return filename;
	}
	var extension=a.pop();
	var name=filename.substring(0,filename.length-(extension.length+1));
	return name;
}

function UtilShowWait(message){
	$("#messic-wait").css({display:"block"});
	$("#messic-wait p").text(message);
}
function UtilHideWait(){
	$("#messic-wait").css({display:"none"});
}

function UtilShowInfo(info){
	UtilShowInfoDelay(info,1500);
}
/**
 * Show messages from messic. A set of phrases can be showed, once after other. Phrases must be separated by "||"
 */
function UtilShowMessic(infoTitle,infoPhrases){
	UtilShowMessic(infoTitle,infoPhrases,null);
}
/**
 * Show messages from messic. A set of phrases can be showed, once after other. Phrases must be separated by "||"
 * the nextFunction param is a function that can be executed after the end of the phrases.
 */
function UtilShowMessic(infoTitle,infoPhrases,nextFunction){
	var phrases=infoPhrases.split("||");
	var currentPhrase=0;
    var markup = [
                  '<div id="messicOverlay">',
                  '  <div id="messicOverlayContent">',
                  '    <div id="messicAvatar"></div>',
                  '    <h1>',infoTitle,'</h1>',
                  '    <p id="messicPhrase">',phrases[0],'</p>',
                  '    <div id="messicClose">Aceptar</div>',
                  '  </div>',
                  '</div>'
              ].join('');
    
    $(markup).hide().appendTo('body').fadeIn();

    $("#messicClose").keyup(function(event){
        if(event.keyCode == 13){
            $("#messicClose").click();
        }
    });
    
    $("#messicClose").click(function(){
    	currentPhrase++;
    	if(phrases.length>currentPhrase){
    		$("#messicPhrase").fadeOut(400,function(){
    			$(this).html(phrases[currentPhrase]).fadeIn();
    		});
    	}else{
    		$("#messicOverlay").fadeOut(400,function(){
    			$(this).remove();
    			if(nextFunction){
        			nextFunction();
    			}
    		});
    	}
    });
	
}

function UtilShowInfoDelay(info, delay){
	$('.messic-smallinfo').stop();
	$('.messic-smallinfo').remove();
	var code="<div class='messic-smallinfo'>"+info+"</div>";
	$('body').append($(code));
	setTimeout(function () {
		$('.messic-smallinfo').remove();
	}, delay);
	//$('.messic-smallinfo').delay(delay).remove();
}
