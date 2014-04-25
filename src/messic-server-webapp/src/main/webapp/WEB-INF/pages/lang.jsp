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
	confirmationYes: '<fmt:message key="js-confirmation-yes" bundle="${message}"/>',
	confirmationNo: '<fmt:message key="js-confirmation-no" bundle="${message}"/>',
	randomlistplayall: '<fmt:message key="js-randomlist-playall" bundle="${message}"/>',
	
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
