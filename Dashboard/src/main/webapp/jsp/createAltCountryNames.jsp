<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles" %>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="apenet" uri="http://commons.apenet.eu/tags"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<div>
		<s:form action="storeAlternativeCountryNames" method="POST" >
		<s:actionmessage />
  			<br></br>
  			<select id="countrySelected" name="countrySelected" >
			<s:iterator value="countriesList" var="row">
				<option value="<s:property value="#row.couId" />"><s:property value="#row.cname" /></option>
			</s:iterator>
			</select>  		
			&nbsp;  &nbsp; 
				
			<select id="languageSelected" name="languageSelected">
			<s:iterator value="languagesList" var="row">
				<option value="<s:property value="#row.lngId" />"><s:property value="#row.lname" /></option>
			</s:iterator>
			</select>
			<br></br>
			
			<s:textfield name="altCountryName" value="%{altCountryName}" key="admin.menu.label.alternativecountryname" required="true" ></s:textfield>

    		<s:submit  key="label.accept" id="accept"></s:submit>    		
		</s:form>
		<br></br>
		<s:property value = "getText('admin.menu.label.question')"/> <a href="createLanguage.action" title="Contact"><s:property value = "getText('admin.menu.label.here')"/></a>
		
       
	</div>
