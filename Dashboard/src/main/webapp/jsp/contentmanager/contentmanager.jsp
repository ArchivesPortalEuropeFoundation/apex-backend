<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
		<script type="text/javascript">
			$(document).ready(function() {
				initContentManager();
			});
		</script>
<div id="contentmanager">
    <div id="listFiles">
    	<div id="listFilesActions">
        <span id="clearAll" class="linkList leftSide">[<s:text name="content.message.clearall" />]</span>
        <span id="selectAllFAs" class="linkList leftSide">[<s:text name="content.message.selectall" />]</span>
        </div>
        <div id="sizeFiles"></div>
    </div>
	<div class="searchOptionsContent">
		<s:form id="newSearchForm" theme="simple" action="updateContentmanager">
			<s:hidden name="refreshFromSession" value="false"/>
			<s:hidden name="orderByField"/>
			<s:hidden name="orderByAscending"/>
				<table>
					<tr>
						<th><s:text name="content.message.type" />:</th><td colspan="5"><s:radio list="typeList" name="xmlTypeId"/></td>
					</tr>
					<tr>
						<th><s:text name="content.message.converted" />:</th><td><s:checkboxlist  list="convertedStatusList" name="convertedStatus"/></td>
						<th><s:text name="content.message.validated" />:</th><td><s:checkboxlist  list="validatedStatusList" name="validatedStatus"/></td>
						<th><s:text name="content.message.published" />:</th><td><s:checkboxlist  list="publishedStatusList" name="publishedStatus"/></td>
					</tr>
					<tr>
						<th><s:text name="content.message.europeana" />:</th><td><s:checkboxlist  list="europeanaStatusList" name="europeanaStatus"/></td>
						<th>Holdings guide status: </th><td> <input type="checkbox" checked="checked" disabled="disabled">Ok</input><input type="checkbox" checked="checked" disabled="disabled">No</input></td>
						
					</tr>
					
					<tr><td colspan="7"><s:textfield name="searchTerms"/><s:select cssClass="small left" list="searchTermsFieldList" name="searchTermsField"/><input type="submit" id="searchButton" value="Search" class="mainButton"></td></tr>
				</table>
		</s:form>
	</div>
	<div id="eads">

	<div id="ead-results-container">
		<jsp:include page="contentmanager-results.jsp"/>
	</div>
	</div>
</div>
