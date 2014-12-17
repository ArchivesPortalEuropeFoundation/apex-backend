<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>

<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles" %>
<%@ taglib prefix="apenet" uri="http://commons.apenet.eu/tags"%>

<div align="center"> 
        <s:form name="form" method="POST" action="selectArchive"  theme="simple">
         <s:actionmessage/>
         <br></br>
         <s:if test="Archives!=null && Archives.size()>0">
			<s:select  id="Ai_selected" name="Ai_selected" list="Archives" listKey="aiId" listValue="ainame" ></s:select> 
			&nbsp;
        	<s:submit value="%{getText('content.message.go')}"/>
        </s:if>
        <s:else>
        	<s:property value="getText('dashboard.error.noinstitutionsmanaged')"/>
        </s:else>
        </s:form>
</div>
