function initUpload(){
			alert('init upload');
			// Makes sure the dataTransfer information is sent when we
			// Drop the item in the drop box.
			jQuery.event.props.push('dataTransfer');
	
			$('#messic-upload-zone').bind('drop', function(e) {
				e.originalEvent.stopPropagation();
				e.originalEvent.preventDefault();

				// This variable represents the files that have been dragged into the drop area
				var files = e.dataTransfer.files;
				$.each(files, function(index, file) {
					alert("tipo:"+files[index].type);
					var newFile="<div class='messic-upload-zone-file'></div>";
					$('#messic-upload-zone').append(newFile);
				});
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




