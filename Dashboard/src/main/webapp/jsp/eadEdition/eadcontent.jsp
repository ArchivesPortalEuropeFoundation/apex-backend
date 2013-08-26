<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<c:set var="fileId" value="${param['id']}"/>
<c:set var="xmlTypeId" value="${param['xmlTypeId']}"/>
<script type="text/javascript">
	$(document).ready(function() {
		initEadTree("${fileId}", "${xmlTypeId}");
	});
</script>
<div id="eadEdition">
	<div id="eadcontent">
		<div id="left-pane" class="pane">
			<div id="eadTree"></div>
		</div>
		<div id="splitter" class="pane"></div>
		<div id="right-pane" class="pane">
		
		            <p id="editionFormContainer">
                    </p>
                    <div id="controls" class="hidden">
                   			<input id="deleteButton" type="submit" name="deleteButton" value="<s:property value="getText('content.message.delete')" />"/>
                   			<input id="saveEADButton" type="button" name="saveEADButton" value="<s:property value="getText('dashboard.hgcreation.sbm.btn.save')" />" />
                    </div>
		</div>
	</div>
</div>