<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<div align="center">
	<s:property value="getText('startpage.wellcome')" /><br/>
    <s:property value="getText('startpage.wellcome2')" /><br/><br/>
    <s:property value="getText('startpage.wellcome3')" /><br/>
    <s:form method="POST">

        <s:textfield name="username" key="label.username" size="25" />
		<s:password name="password" key="label.password" size="25" />
		<s:textfield name="emailAddress" key="label.email" size="25" />

        <s:submit action="startpage" />
    </s:form>
</div>

