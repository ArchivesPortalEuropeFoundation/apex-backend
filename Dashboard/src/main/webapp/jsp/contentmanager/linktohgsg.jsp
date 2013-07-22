<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<script type='text/javascript'>
	$(function() {
  
		$('#selectHgSg').change(function() {
            $.get("displayLinkToHgSgAjax.action", {ecId:  $("#selectHgSg").val()}, function(data){
				$("#selectClevelTd").html(data);
            });
			});

	});
</script>

<s:form method="POST" theme="simple">
	<table>
		<tr>
			<td class="inputLabel"><s:label key="dashboard.hgcreation.selecthgsg" for="selectHgSg" />:</td>
			<td><s:select id="selectHgSg" name="ecId" list="dynamicHgSgs" listKey="value" listValue="content" required="true"></s:select></td>
		</tr>
		<tr>
			<td class="inputLabel"><s:label key="dashboard.hgcreation.selectclevel" for="selectCLevel" />:</td>
			<td id="selectClevelTd"><s:select id="selectCLevel" name="parentCLevelId" list="clevels" listKey="value" listValue="content" required="true"></s:select></td>
		</tr>
		<tr>
			<td class="inputLabel"><s:label key="dashboard.hgcreation.selectprefixmethod" for="selectPrefixMethod" />:</td>
			<td><s:radio name="selectPrefixMethod" list="selectPrefixMethodSet" listKey="value" listValue="content" id="selectPrefixMethod"></s:radio>
			</td>
		</tr>			
		<tr>
			<td colspan="2"><s:submit action="linkToHgSg" key="dashboard.hgcreation.addfindingaids"  cssClass="mainButton"/> <s:submit action="contentmanager"
					key="label.cancel" />
			</td>
		</tr>
	</table>
	<s:hidden name="batchItems" />
	<s:hidden name="id" />
</s:form>