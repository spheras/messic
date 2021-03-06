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

/* init the album page */
function initAuthor() {

	// function to leave the upload section
	VAR_changeSection = function(nextFunction) {
		nextFunction();
	}
    
    $('.messic-author-songs-bodyrow').longpress(function (e) {
            if (e) {
                e.stopPropagation();
                e.preventDefault();
            }
            var $div = $(e.target).parent();
            var authorSid = $div.data("authorsid");
            var albumSid = $div.data("albumsid");
            var songSid = $div.data("songsid");
            var songName = $div.data("songname");
            var albumName = $div.data("albumname");
            var authorName = $div.data("authorname");
            var songRate = $div.data("songrate");
            addSong('raro', authorSid, UtilEscapeHTML(authorName), albumSid, UtilEscapeHTML(albumName), songSid, UtilEscapeHTML(songName), songRate, true);


            //alert('You just longpressed something.');
        }, function (e) {
            if (e) {
                e.stopPropagation();
                e.preventDefault();
            }
            var $div = $(e.target).parent();
            var authorSid = $div.data("authorsid");
            var albumSid = $div.data("albumsid");
            var songSid = $div.data("songsid");
            var songName = $div.data("songname");
            var albumName = $div.data("albumname");
            var authorName = $div.data("authorname");
            var songRate = $div.data("songrate");
            addSong('raro', authorSid, UtilEscapeHTML(authorName), albumSid, UtilEscapeHTML(albumName), songSid, UtilEscapeHTML(songName), songRate);

            //alert('You released before longpress duration and that\'s why its a shortpress now.');
        });


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

/* Remove the author */
function authorRemove(authorSid) {
	$.confirm({
				'title' : messicLang.authorRemoveTitle,
				'message' : messicLang.authorRemoveContent,
				'buttons' : {
					'Yes' : {
						'title' : messicLang.confirmationYes,
						'class' : 'blue',
						'action' : function() {

							$.ajax({
										url : 'services/authors/' + authorSid,
										type : 'DELETE',
										success : function(result) {
											UtilShowInfo(messicLang.authorRemoveOK);
											$.get(
															"explore.do",
															function(data) {
																$("#messic-page-content").empty();
																var posts = $($.parseHTML(data)).filter('#content').children();
																$("#messic-page-content").append(posts);
																initExplore();
															});
										},
										fail : function(result) {
											UtilShowInfo(messicLang.authorRemoveFail);
										}
									});

						}
					},
					'No' : {
						'title' : messicLang.confirmationNo,
						'class' : 'gray',
						'action' : function() {
							UtilShowInfo(messicLang.authorRemoveCancel);
						} // Nothing to do in this case. You can as well omit
							// the action property.
					}
				}
			});
}
