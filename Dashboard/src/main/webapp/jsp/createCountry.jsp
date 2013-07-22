<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles" %>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="apenet" uri="http://commons.apenet.eu/tags"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<div align="center">
		<s:form action="storeCountry" method="POST">
		<s:actionmessage />
		<br></br>
    		<s:textfield name="englishCountryName" value="%{englishCountryName}" key="admin.menu.label.englishcountryname" required="true" ></s:textfield>
    		<s:textfield name="isoCountryName" value="%{isoCountryName}" key="admin.menu.label.isocode" required= "true" cssClass="smallest"></s:textfield>    		
    		<s:submit id="accept" key="label.accept"></s:submit>    		
		</s:form>
		
		<br></br>		
	<!-- 	In order to display this country for different languages, <a href="insertAlternativeCountryNames.action" title="Contact">please introduce the translations you know</a> -->
		
</div>
