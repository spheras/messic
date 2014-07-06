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

function initAPIDoc(div){
		$.getJSON( "services/jsondoc", function( data ) {
			$(div).empty();
			var code="";
			code=code+"<div class='messic-restapi'>";
			code=code+"  <div class='messic-restapi-info'>";
			code=code+"    <div class='messic-restapi-version'>Version:<span>"+data.version+"</span></div>";
			code=code+"    <div class='messic-restapi-basepath'>Base Path:<span>"+data.basePath+"</span></div>";
			code=code+"  </div>";
			for(var i=0;i<data.apis.length;i++){
				code=code+"<div class='messic-restapi-api'>";
				code=code+"<div class='messic-restapi-title'>"+data.apis[i].name+"</div>";
				code=code+"<div class='messic-restapi-description'>"+data.apis[i].description+"</div>";
				for(var j=0;j<data.apis[i].methods.length;j++){
					var method=data.apis[i].methods[j];
					code=code+"<div class='messic-restapi-method'>";
					code=code+"  <div class='messic-restapi-method-path'>"+method.path+"</div>";
					code=code+"  <div class='messic-restapi-method-description'>"+method.description+"</div>";
					code=code+"  <div class='messic-restapi-method-verb'>Method&nbsp;&nbsp;<span>"+method.verb+"</span></div>";		
					code=code+"  <div class='messic-restapi-method-produces'>Produces&nbsp;&nbsp;<span>"+method.produces+"</span></div>";
					if(method.consumes && method.consumes.length>0){
						code=code+"  <div class='messic-restapi-method-consumes'>Consumes&nbsp;&nbsp;<span>"+method.consumes+"</span></div>";
					}

					for(var k=0;k<method.pathparameters.length;k++){
						var pathp=method.pathparameters[k];
						code=code+"<div class='messic-restapi-method-pathp'>";
						code=code+"  <div class='messic-restapi-method-pathp-name'>Parameter&nbsp;&nbsp;<span>"+pathp.name+"</span></div>";
						code=code+"  <div class='messic-restapi-method-pathp-description'>"+pathp.description+"</div>";
						code=code+"  <div class='messic-restapi-method-pathp-type'>Type:&nbsp;&nbsp;"+pathp.type+"</div>";
						code=code+"  <div class='messic-restapi-method-pathp-required'>Required:&nbsp;&nbsp;"+pathp.required+"</div>";
						code=code+"</div>";
					}
					for(var k=0;k<method.queryparameters.length;k++){
						var queryp=method.queryparameters[k];
						code=code+"<div class='messic-restapi-method-queryp'>";
						code=code+"  <div class='messic-restapi-method-queryp-name'>Parameter&nbsp;&nbsp;<span>"+queryp.name+"</span></div>";
						code=code+"  <div class='messic-restapi-method-queryp-description'>"+queryp.description+"</div>";
						code=code+"  <div class='messic-restapi-method-queryp-type'>Type&nbsp;&nbsp;"+queryp.type+"</div>";
						code=code+"  <div class='messic-restapi-method-queryp-required'>Required&nbsp;&nbsp;"+queryp.required+"</div>";
						code=code+"</div>"
					}
					for(var k=0;k<method.apierrors.length;k++){
						var apie=method.apierrors[k];
						code=code+"<div class='messic-restapi-method-apie'>";
						code=code+"  <div class='messic-restapi-method-apie-code'>"+apie.code+"</div>";
						code=code+"  <div class='messic-restapi-method-apie-description'>"+apie.description+"</div>";
						code=code+"</div>";
					}
					code=code+"</div>";
				}
				code=code+"</div>";
			}
			code=code+"</div>";

			$(div).append($(code));
 		});
}