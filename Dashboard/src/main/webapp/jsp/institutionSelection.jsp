<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>    
    <%@ taglib prefix="s" uri="/struts-tags"%>
<div align="center"> 
        <s:form name="form" method="POST" action="selectArchive"  theme="simple">
         <s:actionmessage/>
         <br></br>
			<s:select  id="Ai_selected" name="Ai_selected" list="Archives" listKey="aiId" listValue="ainame" ></s:select> 
			&nbsp;
        	<s:submit value="Go"/>
        </s:form>
</div>