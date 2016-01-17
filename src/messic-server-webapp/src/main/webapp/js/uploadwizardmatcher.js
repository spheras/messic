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
//This class will try to match perfectly songs info from third providers
//to the current selection of songs
var UploadWizardMatcher = function () {

    //list of tracks of the selected list
    this.divtrackS;
    //list of names of the selected list
    this.divnamesS;
    //list of tracks of the uploaded list
    this.divtracksO;
    //list of names of the uploaded list
    this.divnamesO;

    var DATA_NAME = "matcher";


    /**
     * locate the track div which correspond to the name div at the list of uploaded tracks
     */
    var utilFindNameDivOrigin = function (divTrackOrigin) {
        return divTrackOrigin.next();
    }

    /**
     * var selectedDiv -> the selected div from the third provider
     */
    this.uploadwizardmatch = function (selectedDiv) {
        //trying to catch the songs tracks and names
        //first get all the tracks and names from the selected option
        this.divtracksS = selectedDiv.find(".messic-upload-wizard-albumsong-track");
        this.divnamesS = selectedDiv.find(".messic-upload-wizard-albumsong-name");
        //after get all the tracks and names that we have uploaded
        var curTabPanel = $(".messic-upload-wizard-albuminfo-body-real")[$("#messic-upload-wizard-albuminfo-body-tabs").tabs('option', 'active')];

        this.divtracksO = $(curTabPanel).find(".messic-upload-wizard-albumsong-track");
        this.divnamesO = $(curTabPanel).find(".messic-upload-wizard-albumsong-name");

        //first we reset the valoration of each matcher done previously
        for (var o = 0; o < this.divtracksO.length; o++) {
            this.divtracksO.eq(o).data(DATA_NAME, 0);
        }

        for (var s = 0; s < this.divtracksS.length; s++) {
            //first, getting all the divs that has this track number
            var trackS = this.divtracksS.eq(s).text();
            var repeatedListTrackS = getRepeatedDivTracks(trackS, this.divtracksS);
            var repeatedListTrackO = new Array();
            if (repeatedListTrackS.length == this.divtracksS.length) {
                //then all selected tracks are repeated, the source has no track differentiation.. :(
                //let's force the matching all against all
                //nothing to do
            } else {
                //let's see how many repeated from the uploaded
                repeatedListTrackO = getRepeatedDivTracks(trackS, this.divtracksO);
            }

            if (repeatedListTrackS.length == 1 && repeatedListTrackO.length == 1) {
                //the simplest case is if we have only 1 repeatedTrackS and only 1 repeatedTrack0
                //we have found easily the corresponding track. Let's change the name
                var field = utilFindNameDivOrigin(repeatedListTrackO[0]);
                field.val(utilFindNameDivOrigin(repeatedListTrackS[0]).text());
                $(field).addClass("messic-upload-wizard-albuminfofield-modified");
                repeatedListTrackO[0].data(DATA_NAME, 100); //perfect matcher
            } else if (repeatedListTrackS.length > 1 && repeatedListTrackO.length == 1) {
                //now, from the selected track list, at least this track is repeated.. we need to compare by string what is the best matcher
                var listOfNameS = getNamesListFromTrackList(repeatedListTrackS);
                var listOfNameO = getNamesListFromTrackList(repeatedListTrackO);
                var bestMatcherDiv = getBestMatcher(listOfNameS, listOfNameO[0].val());

                var newPercent = bestMatcherDiv.data(DATA_NAME);
                var oldPercent = repeatedListTrackO[0].data(DATA_NAME);
                if (oldPercent < newPercent) {
                    //saving the match percent and the new name
                    repeatedListTrackO[0].data(DATA_NAME, bestMatcherDiv.data(DATA_NAME));
                    listOfNameO[0].prev().val(bestMatcherDiv.prev().text());
                    listOfNameO[0].prev().addClass("messic-upload-wizard-albuminfofield-modified");
                    listOfNameO[0].val(bestMatcherDiv.text());
                    listOfNameO[0].addClass("messic-upload-wizard-albuminfofield-modified");
                }
            } else if (repeatedListTrackO.length > 1) {
                //now, there is only 1 selected track (or a lot, doesn't mind), but there are some possibilities to match at the upload files
                //in the case that there are a lot repeated at the selected one, it is important to compare only with the current divtracksS
                var divtrackS = this.divtracksS.eq(s);
                var divnameS = divtrackS.next();

                var listOfNameO = getNamesListFromTrackList(repeatedListTrackO);
                var bestMatcherDivNameO = getBestMatcher(listOfNameO, divnameS.text());

                var newPercent = bestMatcherDivNameO.data(DATA_NAME);
                var oldPercent = bestMatcherDivNameO.prev().data(DATA_NAME);
                if (oldPercent < newPercent) {
                    //saving the match percent and the new name
                    bestMatcherDivNameO.prev().data(DATA_NAME, newPercent);
                    bestMatcherDivNameO.prev().val(divtrackS.text());
                    bestMatcherDivNameO.prev().addClass("messic-upload-wizard-albuminfofield-modified");
                    bestMatcherDivNameO.val(divnameS.text());
                    bestMatcherDivNameO.addClass("messic-upload-wizard-albuminfofield-modified");
                }
            } else if (repeatedListTrackO.length == 0) {
                //now, there aren't any track found at the uploaded files!!, lets search...
                var divtrackS = this.divtracksS.eq(s);
                var divnameS = divtrackS.next();

                //var bestMatcherDivNameO=getBestMatcher(this.divnamesO,divnameS.text());
                //var newPercent=bestMatcherDivNameO.data(DATA_NAME);
                //var oldPercent=bestMatcherDivNameO.prev().data(DATA_NAME);
                //if(oldPercent<newPercent){
                //    //saving the match percent and the new name
                //    bestMatcherDivNameO.prev().data(DATA_NAME,newPercent);
                //    bestMatcherDivNameO.val(divnameS.text());
                //}

                //we get the list of match percent of all divs
                var matcherDivsNameO = getMatcherNameOrder(this.divnamesO, divnameS.text());
                for (var i = 0; i < matcherDivsNameO.length; i++) {
                    //let's try to locate the best matcher that receive a percent better than the old match
                    var bestMatcherDivNameO = matcherDivsNameO[i];

                    var newPercent = bestMatcherDivNameO.data(DATA_NAME);
                    var oldPercent = bestMatcherDivNameO.prev().data(DATA_NAME);
                    if (oldPercent < newPercent) {
                        //saving the match percent and the new name
                        bestMatcherDivNameO.prev().data(DATA_NAME, newPercent);
                        bestMatcherDivNameO.prev().val(divtrackS.text());
                        bestMatcherDivNameO.prev().addClass("messic-upload-wizard-albuminfofield-modified");

                        bestMatcherDivNameO.val(divnameS.text());
                        bestMatcherDivNameO.addClass("messic-upload-wizard-albuminfofield-modified");
                        break;
                    }
                }
            }

        }

        UtilShowInfo(messicLang.uploadWizardInfoUpdated);
    }

    /**
     * return the list of div names that correspond to the list of track names
     */
    var getNamesListFromTrackList = function (trackList) {
        var result = new Array();

        for (var i = 0; i < trackList.length; i++) {
            var divname = trackList[i].next();
            result.push(divname);
        }

        return result;
    }

    /**
     * get the best matcher from the list, compared to a name like matchername
     */
    var getBestMatcher = function (matchernamelist, matchername) {
        var bestdiv;
        var bestpercent = 0;

        for (var i = 0; i < matchernamelist.length; i++) {
            var div = matchernamelist[i];
            var name1 = div.val();
            if (name1 == "") {
                name1 = div.text();
            }
            var percent = UtilStringComparator(name1, matchername);
            if (bestpercent < percent) {
                bestpercent = percent;
                bestdiv = div;
            }
        }
        bestdiv.data(DATA_NAME, bestpercent);
        return bestdiv;
    }

    /**
     * return the list ordered by match percent.  Best match first.
     */
    var getMatcherNameOrder = function (matchernamelist, matchername) {
        var newMatcherList = new Array();

        for (var i = 0; i < matchernamelist.length; i++) {
            var div = $(matchernamelist[i]);
            var name1 = div.val();
            if (name1 == "") {
                name1 = div.text();
            }
            var percent = UtilStringComparator(name1, matchername);
            div.data(DATA_NAME, percent);
            newMatcherList.push(div);
        }

        //finally we order the list by the percent obtained
        newMatcherList.sort(function (a, b) {
            var amn = a.data(DATA_NAME);
            var bmn = b.data(DATA_NAME);
            return bmn - amn;
        });

        return newMatcherList;
    }

    /**
     * return the list of divs that has the same track number at the list
     */
    var getRepeatedDivTracks = function (trackN, divtracks) {
        var repeated = new Array();
        for (var i = 0; i < divtracks.length; i++) {
            var tracki = divtracks.eq(i).val();
            if (tracki == "") {
                tracki = divtracks.eq(i).text();
            }
            if (tracki == trackN) {
                repeated.push(divtracks.eq(i));
            }
        }
        return repeated;
    }

    /**
     * match the general info
     */
    this.uploadwizardmatchGeneralInfo = function (selectedDiv) {
        //getting the final destination info fields
        /*
        var authorCombo = $("#messic-upload-album-author").data("kendoComboBox");
        var titleCombo = $("#messic-upload-album-title").data("kendoComboBox");
        var genreCombo = $("#messic-upload-album-genre").data("kendoComboBox");
        var yearEdit = $("#messic-upload-album-year").data("kendoNumericTextBox");

        //matching there the general info
        authorCombo.text(selectedDiv.find(".messic-upload-wizard-albuminfofield-author").text());
        titleCombo.text(selectedDiv.find(".messic-upload-wizard-albuminfofield-title").text());
        genreCombo.text(selectedDiv.find(".messic-upload-wizard-albuminfofield-genre").text());
        yearEdit.value(selectedDiv.find(".messic-upload-wizard-albuminfofield-year").text());
        */
        var comments = selectedDiv.find(".messic-upload-wizard-albuminfofield-comments").text();

        if (comments.length > 255) {
            comments = comments.substr(0, 254);
        }

        /*
        $("#messic-upload-album-comments").text(comments);
        */

        $("#messic-upload-wizard-authorname").val(selectedDiv.find(".messic-upload-wizard-albuminfofield-author").text());
        $("#messic-upload-wizard-authorname").addClass("messic-upload-wizard-albuminfofield-modified");
        $("#messic-upload-wizard-albumtitle").val(selectedDiv.find(".messic-upload-wizard-albuminfofield-title").text());
        $("#messic-upload-wizard-albumtitle").addClass("messic-upload-wizard-albuminfofield-modified");
        $("#messic-upload-wizard-albumyear").val(selectedDiv.find(".messic-upload-wizard-albuminfofield-year").text());
        $("#messic-upload-wizard-albumyear").addClass("messic-upload-wizard-albuminfofield-modified");
        $("#messic-upload-wizard-genre").val(selectedDiv.find(".messic-upload-wizard-albuminfofield-genre").text());
        $("#messic-upload-wizard-genre").addClass("messic-upload-wizard-albuminfofield-modified");
        $("#messic-upload-wizard-albumcomments").val(comments);
        $("#messic-upload-wizard-albumcomments").addClass("messic-upload-wizard-albuminfofield-modified");


        UtilShowInfo(messicLang.uploadWizardInfoUpdated);
    }

    /* copy the author name info */
    this.uploadwizardmatchCopyAuthorName = function (selectedDiv) {
        $("#messic-upload-wizard-authorname").val(selectedDiv.find(".messic-upload-wizard-albuminfofield-author").text());
        $("#messic-upload-wizard-authorname").addClass("messic-upload-wizard-albuminfofield-modified");
        UtilShowInfo(messicLang.uploadWizardInfoUpdated);
    }

    /* copy the album title info */
    this.uploadwizardmatchCopyAlbumTitle = function (selectedDiv) {
        $("#messic-upload-wizard-albumtitle").val(selectedDiv.find(".messic-upload-wizard-albuminfofield-title").text());
        $("#messic-upload-wizard-albumtitle").addClass("messic-upload-wizard-albuminfofield-modified");
        UtilShowInfo(messicLang.uploadWizardInfoUpdated);
    }

    /* copy the album year info */
    this.uploadwizardmatchCopyAlbumYear = function (selectedDiv) {
        $("#messic-upload-wizard-albumyear").val(selectedDiv.find(".messic-upload-wizard-albuminfofield-year").text());
        $("#messic-upload-wizard-albumyear").addClass("messic-upload-wizard-albuminfofield-modified");
        UtilShowInfo(messicLang.uploadWizardInfoUpdated);
    }

    /* copy the album genre info */
    this.uploadwizardmatchCopyAlbumGenre = function (selectedDiv) {
        $("#messic-upload-wizard-genre").val(selectedDiv.find(".messic-upload-wizard-albuminfofield-genre").text());
        $("#messic-upload-wizard-genre").addClass("messic-upload-wizard-albuminfofield-modified");
        UtilShowInfo(messicLang.uploadWizardInfoUpdated);
    }

    /* copy the album comments info */
    this.uploadwizardmatchCopyAlbumComments = function (selectedDiv) {
        var comments = selectedDiv.find(".messic-upload-wizard-albuminfofield-comments").text();
        if (comments.length > 255) {
            comments = comments.substr(0, 254);
        }

        $("#messic-upload-wizard-albumcomments").val(comments);
        $("#messic-upload-wizard-albumcomments").addClass("messic-upload-wizard-albuminfofield-modified");
        UtilShowInfo(messicLang.uploadWizardInfoUpdated);
    }

}