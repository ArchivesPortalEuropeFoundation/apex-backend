<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles" %>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="apenet" uri="http://commons.apenet.eu/tags"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<div >
		<s:form action="storeLanguage" method="POST" >
			<s:actionmessage />
			<s:fielderror>
			
    		<br></br>		
    		<s:textfield name="englishlanguageName" value="%{englishlanguageName}" key="admin.menu.label.englishcountryname" required="true" ></s:textfield>
    		<s:textfield name="isoLanguageName" value="%{isoLanguageName}" key="admin.menu.label.isocode2" required="true" cssClass="smallest"></s:textfield>
    		<s:textfield name="iso2LanguageName" value="%{iso2LanguageName}" key="admin.menu.label.isocode" required="true" cssClass="smallest"></s:textfield>
    		<s:textfield name="nativeLanguageName" value="%{nativeLanguageName}" key="admin.menu.label.nativelanguagename" required="true" ></s:textfield>
    		<s:submit key="label.accept" id="accept" ></s:submit>   
    		</s:fielderror> 		
		</s:form>
       
	</div>
