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
