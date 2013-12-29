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

function UtilShowInfo(info){
	UtilShowInfoDelay(info,1500);
}

function UtilShowInfoDelay(info, delay){
	$('.messic-smallinfo').remove(); 
	var code="<div class='messic-smallinfo'>"+info+"</div>";
	$('body').append($(code));
	setTimeout(function () { 
		$('.messic-smallinfo').remove(); 
	}, delay);
	//$('.messic-smallinfo').delay(delay).remove();
}
