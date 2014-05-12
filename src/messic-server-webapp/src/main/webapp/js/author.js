
/* init the album page */
function initAuthor() {

	// function to leave the upload section
	VAR_changeSection = function(nextFunction) {
		nextFunction();
	}

}

/* Show the musicinfo obtained from a plugin in the web page */
function authorShowAuthorInfo(authorName, pluginName, div) {
	// selected the new one
	$(".messic-author-plugincontainer-menuitem").removeClass(
			"messic-author-plugincontainer-menuitem-selected");
	$(div).addClass("messic-author-plugincontainer-menuitem-selected");

	var contentdiv = $(".messic-author-plugincontainer-content");
	$(contentdiv).empty();
	$(contentdiv)
			.append(
					"<div class=\"messic-author-plugincontainer-content-wait\">"+messicLang.albumMusicinfoLoading+"</div>");

	$.getJSON("services/musicinfo?pluginName=" + pluginName + "&authorName=" + authorName, function(data) {
		var resulthtml = data.htmlContent;

		$(contentdiv).empty();
		$(contentdiv).append(resulthtml);
	});
}
