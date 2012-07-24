<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles" %>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="apenet" uri="http://commons.apenet.eu/tags"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<div >
		<s:form action="indexAccount" method="POST" theme="simple">
		<s:actionmessage />
  			<br/>
  			<p><s:select  id="countrySelected" name="countrySelected" list="countriesList" listKey="couId" listValue="cname" headerKey = "-1" headerValue="%{getText('admin.menu.countryselection')}">
			</s:select>  			
			&nbsp;  &nbsp; 
			
			<s:submit key="label.accept" id="accept"/>
			</p>
			<br/>
		</s:form>		
		<s:form action="institutionAccount" method="POST" theme="simple">
  			<br/>
  			<p><s:select  id="institutionSelected" name="institutionSelected" list="institutionsList" listKey="aiId" listValue="ainame" headerKey = "-1" headerValue="%{getText('breadcrumb.section.selectArchive')}">
			</s:select>  			
			&nbsp;  &nbsp; 
			
			<s:submit key="label.accept" id="accept"/>
			</p>
			<br/>
		</s:form>
	</div>
