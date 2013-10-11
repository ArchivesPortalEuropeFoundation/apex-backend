<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<div id="curtain" class="curtain" align="center" > 
	<div style="width:100%; height:50%; float:left;">&nbsp;</div>
	<div style="width:100%; float:left;">
		<div id="curtain_message" class="curtain_message">					
			<s:property value="getText('content.message.errorLinkHgSg')" />  
		</div> 
		<div>
			<input type="submit" onclick='$("#curtain").hide();' value="<s:property value="getText('content.message.errorLinkHgSgClose')"/>"></input> 
		</div>
	</div>		
</div>

