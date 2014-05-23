<%@ page contentType="text/javascript" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<c:if test="${ message == null }">
    <fmt:setBundle basename="ResourceBundle" var="message" scope="application"/>
</c:if>
		    
messicLang = {
	uploadImageRemoved : '<fmt:message key="js-upload-imageremoved" bundle="${message}"/>',
	uploadCoverSelected : '<fmt:message key="js-upload-coverselected" bundle="${message}"/>',
	uploadTrackRemoved : '<fmt:message key="js-upload-trackremoved" bundle="${message}"/>',
	uploadResourceRemoved : '<fmt:message key="js-upload-resourceremoved" bundle="${message}"/>',
	uploadAuthorPlaceholder : '<fmt:message key="js-upload-author-placeholder" bundle="${message}"/>',
	uploadYearPlaceholder : '<fmt:message key="js-upload-year-placeholder" bundle="${message}"/>',
	uploadTitlePlaceholder : '<fmt:message key="js-upload-title-placeholder" bundle="${message}"/>',
	uploadGenrePlaceholder : '<fmt:message key="js-upload-genre-placeholder" bundle="${message}"/>',
	uploadAlbumEdit : '<fmt:message key="js-upload-album-edit" bundle="${message}"/>',
	uploadAlbumNew : '<fmt:message key="js-upload-album-new" bundle="${message}"/>',
	uploadAlbumWizardNotracks : '<fmt:message key="js-upload-album-wizard-notracks" bundle="${message}"/>',
	uploadAlbumUploadWizard: '<fmt:message key="js-upload-album-upload-wizard" bundle="${message}"/>',
	uploadAlbumSendConfirmationTitle: '<fmt:message key="js-upload-album-send-confirmation-title" bundle="${message}"/>',
	uploadAlbumSendConfirmationMessage: '<fmt:message key="js-upload-album-send-confirmation-message" bundle="${message}"/>',
	uploadAlbumSendCancel: '<fmt:message key="js-upload-album-send-cancel" bundle="${message}"/>',
	uploadAlbumChangeSectionTitle: '<fmt:message key="js-upload-album-changesection-title" bundle="${message}"/>',
	uploadAlbumChangeSectionMessage: '<fmt:message key="js-upload-album-changesection-message" bundle="${message}"/>',
	uploadWizardCancel: '<fmt:message key="js-upload-wizard-cancel" bundle="${message}"/>',	
	uploadWizardTitle: '<fmt:message key="js-upload-wizard-title" bundle="${message}"/>',	
	uploadWizardTitle2: '<fmt:message key="js-upload-wizard-title2" bundle="${message}"/>',	
	uploadWizardSubtitle2: '<fmt:message key="js-upload-wizard-subtitle2" bundle="${message}"/>',	
	uploadWizardSubtitle3: '<fmt:message key="js-upload-wizard-subtitle3" bundle="${message}"/>',
	uploadWizardTrack: '<fmt:message key="upload-songs-track" bundle="${message}"/>',
	uploadWizardName: '<fmt:message key="upload-songs-name" bundle="${message}"/>',
	confirmationYes: '<fmt:message key="js-confirmation-yes" bundle="${message}"/>',
	confirmationNo: '<fmt:message key="js-confirmation-no" bundle="${message}"/>',
	randomlistplayall: '<fmt:message key="js-randomlist-playall" bundle="${message}"/>',
	albumLeaveTitle: '<fmt:message key="js-album-leave-title" bundle="${message}"/>',
	albumLeaveContent: '<fmt:message key="js-album-leave-content" bundle="${message}"/>',
	albumDiscardTitle: '<fmt:message key="js-album-discard-title" bundle="${message}"/>',
	albumDiscardContent: '<fmt:message key="js-album-discard-content" bundle="${message}"/>',
	albumDiscardYes: '<fmt:message key="js-album-discard-yes" bundle="${message}"/>',
	albumDiscardNo: '<fmt:message key="js-album-discard-no" bundle="${message}"/>',
	albumSavePendingTitle:'<fmt:message key="js-album-savepending-title" bundle="${message}"/>',
	albumSavePendingContent:'<fmt:message key="js-album-savepending-content" bundle="${message}"/>',
	albumSavePendingYes:'<fmt:message key="js-album-savepending-yes" bundle="${message}"/>',
	albumSaveCreateTitle: '<fmt:message key="js-album-savecreate-title" bundle="${message}"/>',
	albumSaveCreateContent: '<fmt:message key="js-album-savecreate-content" bundle="${message}"/>',
	albumSaveCreateYes: '<fmt:message key="js-album-savecreate-yes" bundle="${message}"/>',
	albumSaveCreateNo: '<fmt:message key="js-album-savecreate-no" bundle="${message}"/>',
	albumSaveFail: '<fmt:message key="js-album-save-fail" bundle="${message}"/>',
	albumSaveOK: '<fmt:message key="js-album-save-ok" bundle="${message}"/>',
	albumSongDelete:  '<fmt:message key="album-songdelete-title" bundle="${message}"/>',
	albumSongPlay: '<fmt:message key="album-songplay-title" bundle="${message}"/>',
	albumArtworkShow: '<fmt:message key="album-artworkshow-title" bundle="${message}"/>',
	albumArtworkRemove:  '<fmt:message key="album-artworkremove-title" bundle="${message}"/>',
	albumSongRemoveTitle:  '<fmt:message key="js-album-song-remove-title" bundle="${message}"/>',
	albumSongRemoveContent:  '<fmt:message key="js-album-song-remove-content" bundle="${message}"/>',
	albumSongRemoveFail:  '<fmt:message key="js-album-song-remove-fail" bundle="${message}"/>',
	albumSongRemoveOK:  '<fmt:message key="js-album-song-remove-ok" bundle="${message}"/>',
	albumSongRemoveCancel:  '<fmt:message key="js-album-song-remove-cancel" bundle="${message}"/>',
	albumResourceRemoveTitle:  '<fmt:message key="js-album-resource-remove-title" bundle="${message}"/>',
	albumResourceRemoveContent:  '<fmt:message key="js-album-resource-remove-content" bundle="${message}"/>',
	albumResourceRemoveFail:  '<fmt:message key="js-album-resource-remove-fail" bundle="${message}"/>',
	albumResourceRemoveOK:  '<fmt:message key="js-album-resource-remove-ok" bundle="${message}"/>',
	albumResourceRemoveCancel:  '<fmt:message key="js-album-resource-remove-cancel" bundle="${message}"/>',
	albumMusicinfoLoading:  '<fmt:message key="js-album-musicinfo-loading" bundle="${message}"/>',
	albumRemoveTitle:  '<fmt:message key="js-album-remove-title" bundle="${message}"/>',
	albumRemoveContent:  '<fmt:message key="js-album-remove-content" bundle="${message}"/>',
	albumRemoveOK:  '<fmt:message key="js-album-remove-ok" bundle="${message}"/>',
	albumRemoveFail:  '<fmt:message key="js-album-remove-fail" bundle="${message}"/>',
	albumRemoveCancel:  '<fmt:message key="js-album-remove-cancel" bundle="${message}"/>',
	
	
	search: function(what){
		if(what=='RandomListName-Random'){
			return '<fmt:message key="js-randomlistname-random" bundle="${message}"/>';
		}
		if(what=='RandomListTitle-Random'){
			return '<fmt:message key="js-randomlisttitle-random" bundle="${message}"/>';
		}
		if(what=='RandomListName-Author'){
			return '<fmt:message key="js-randomlistname-author" bundle="${message}"/>';
		}
		if(what=='RandomListTitle-Author'){
			return '<fmt:message key="js-randomlisttitle-author" bundle="${message}"/>';
		}
		if(what=='RandomListName-Search'){
			return '<fmt:message key="js-randomlistname-search" bundle="${message}"/>';
		}
		if(what=='RandomListTitle-Search'){
			return '<fmt:message key="js-randomlisttitle-search" bundle="${message}"/>';
		}
	}
}
