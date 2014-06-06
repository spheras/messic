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
/*
* Class UploadSongResource
* constructor
* type int, type of resource (1:music, 2:graphic, 3:other)
*/
var UploadResource=function (type,file,domElement){
	//resource Type, 1->music, 2->bitmap, 3->other
	this.type=type;
	//Resource File associated
	this.file=file;
	//track, only valid for songs
	this.track=0;
	//the DOM element
	this.domElement=domElement;
	//flag to known if the resources has been uploaded
	this.uploaded=false;
	//code for this resource
	this.code=UtilGetGUID();
	//xhr associated with the upload of the resource
	this.xhr=null;

	this.alert=function(){
		alert("UploadResource, type:"+this.type+", track:"+this.track);
	}

	this.getSongName=function(){
		if(type==1){
			//audio
			return this.domElement.find(".messic-upload-song-content-header-filename").val();
		}	
	}

	this.getSongTrack=function(){
		if(type==1){
			//audio
			return this.domElement.find(".messic-upload-song-content-header-tracknumber").val();
		}
	}

}
