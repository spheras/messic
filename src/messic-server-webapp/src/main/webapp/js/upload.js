function initUpload(){
			// Makes sure the dataTransfer information is sent when we
			// Drop the item in the drop box.
			jQuery.event.props.push('dataTransfer');

	
			$('#messic-upload-zone').bind('drop', function(e) {
				e.originalEvent.stopPropagation();
				e.originalEvent.preventDefault();

				// This variable represents the files that have been dragged into the drop area
				var files = e.dataTransfer.files;
				$.each(files, function(index, file) {
					//alert("tipo:"+files[index].type);
					var stype=files[index].type;
					//alert(stype);
					var newFile="<div class='messic-upload-zone-file messic-upload-zone-file";
					if(stype.contains("image/")){
						newFile=newFile+"-image";
					}else if(stype.contains("audio/")){
						newFile=newFile+"-music";
					}else{
						newFile=newFile+"-unknown";
					}
					newFile=newFile+"'></div>";
					$('#messic-upload-zone').append(newFile);
				});


				//change the messic-info data class, useful if different visualization behaviour is needed before and after some files are added
				$('.messic-upload-info-before').attr( "class", "messic-upload-info-after" );

				return false;
			});

			$('#messic-upload-zone').bind('dragenter', function(e) {
				e.originalEvent.stopPropagation();
				e.originalEvent.preventDefault();

				$('#messic-upload-zone').attr( "class", "messic-upload-zone-over" );
			});

			$('#messic-upload-zone').bind('dragleave', function(e) {
				e.originalEvent.stopPropagation();
				e.originalEvent.preventDefault();

				$('#messic-upload-zone').attr( "class", "" );
			});
}




