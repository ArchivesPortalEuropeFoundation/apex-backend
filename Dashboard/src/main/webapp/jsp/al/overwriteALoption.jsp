<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
        <div id="fileList">
        <br></br>
        	<s:property value="getText('a.message.overwritefile')" />            
        <br></br>
        </div>
        <div>
       		<s:property value="getText('a.message.overwritefile.question')" />
        	<br></br>
        	<table width="10%">
        	<tr>
        		<td><s:form action="ALOverwrite" method="POST" enctype="multipart/form-data">
        			<s:hidden name="httpFileFileName" value="%{httpFileFileName}"></s:hidden>
        			<s:submit key="label.ok"><s:hidden name="Overwrite" value="true"></s:hidden> </s:submit>        			
        		</s:form>
        	</td>
        	<td>
        		<s:form action="ALCancelOverwrite" method="POST" enctype="multipart/form-data">
        			<s:submit key="label.cancel"><s:hidden name="Overwrite" value="false"></s:hidden></s:submit>
        		</s:form>
        	</td>
        	</tr>
        	</table> 
 	
        </div>