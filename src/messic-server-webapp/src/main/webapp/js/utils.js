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
