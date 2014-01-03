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
