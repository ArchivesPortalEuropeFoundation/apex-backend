<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<div>
	<s:actionmessage/>
	<s:form id="eagold" method="post" enctype= "multipart/form-data">
		<input id="eagoldhttpupload_label_ai_tabs_commons_option_yes" type="submit" value="Yes" name="action:eagoldhttpupload">
		<input id="eagoldhttpupload_label_ai_tabs_commons_option_no" type="submit" value="No" name="action:eagoldnothttpupload">
	</s:form>
</div>