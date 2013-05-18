$(document).ready(function() {
	var window = $("#window");

	var onClose = function() {
		alert('closing!');
	}

	if (!window.data("kendoWindow")) {
		window.kendoWindow({
			width: "600px",
			title: "About Alvar Aalto",
			close: onClose
		});
	}
});
