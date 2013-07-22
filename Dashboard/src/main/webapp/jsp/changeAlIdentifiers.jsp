<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles" %>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<div>  
	<s:form action="storeAlIdentifier" method="post" enctype="multipart/form-data" >
	
		<br></br>
  		<s:actionerror />
		<s:actionmessage/>
  		<s:select  id="institutionSelected" name="institutionSelected" list="institutionList" listKey="aiId" 
  		listValue="ainame" headerKey = "-1" headerValue="%{getText('al.message.chageIdentifier.selectitem')}">
		</s:select>  

		<select id="identifierSelected" name="identifierSelected" style="display:none;">
		<s:iterator value="institutionList" var="row">
			<option value="<s:property value="#row.aiId" />" ><s:property value="#row.internalAlId" /></option>
		</s:iterator>
		</select>  
		
		<br></br>
		<s:textfield id="identifier" name="identifier" required="true" key="label.identier" />
		<br></br>
		<script type="text/javascript">
			$("#institutionSelected").click(function(){
				$("#identifier").attr("value",$("#identifierSelected option[value="+$(this).val()+"]").text());
			});
		</script>
		<br></br>
		<s:submit key="label.changeidentifier" />
	</s:form>
</div>
