/**
 * @source: https://github.com/spheras/messic
 *
 * @licstart  The following is the entire license notice for the 
 *  JavaScript code in this page.
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

var UploadPool=function(){
	/** this indicates the maximum of elements that are running in parallel */
	this.maxElements=5;
	this.started=false;
	this.pendingElements=new Array();
	this.runningElements=new Array();
	
	this.setMaxElements=function(max){
		this.maxElements=max;
	}
	
	/** start running the upload */
	this.start=function(){
		this.started=true;
		this.refreshStatus();
	}

	/** Add an upload element */
	this.addUpload=function(albumCode, file, successFunction, errorFunction, xhrFunction){
		var element={
				_albumCode: albumCode,
				_file: file,
				_successFunction: successFunction,
				_errorFunction: errorFunction,
				_xhrFunction: xhrFunction,
		}
		this.pendingElements.push(element);
		this.refreshStatus();
	}

	/** Check the pool, and see if it's necessary to upload a new one or not */
	this.refreshStatus=function(){
		if(this.started){
			while(this.runningElements.length<this.maxElements && this.pendingElements.length>0){
				//we need to put another element
				var element=this.pendingElements.splice(0,1);
				this.runElement(element[0]);
			}
		}
	}

	/** run an element */
	this.runElement=function(element){
		this.runningElements.push(element);
		
		//reading the file to show the image
		var reader = new FileReader();
		var uploadPoolSelf=this;
		// Closure to capture the file information.
		reader.onload = function(e) {
				    var bin = e.target.result;
				     $.ajax({
				        url: 'services/albums/'+element._albumCode+"?fileName="+encodeURIComponent(element._file.name),
				        type: 'PUT',
				        //Ajax events
				        success:function(){
				        	element._successFunction();
				        	uploadPoolSelf.runningElements.splice(0,1);
				        	uploadPoolSelf.refreshStatus();
				        },
				        error: function(){
				        	element._errorFunction();
				        	uploadPoolSelf.runningElements.splice(0,1);
				        	uploadPoolSelf.refreshStatus();
				        },
						xhr: element._xhrFunction,
				        processData: false,
				        data: bin
				    });
	    }
		
		// Read in the image file as a data URL.
		reader.readAsArrayBuffer(element._file);
	}
}