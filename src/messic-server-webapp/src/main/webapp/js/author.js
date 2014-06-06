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
