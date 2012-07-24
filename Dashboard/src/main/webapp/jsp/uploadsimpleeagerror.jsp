<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
        <div id="fileList">
            Some errors occurred during the EAG creation process:<br>
			The EAG could't be properly created
        </div>
        <div id="global">
			<s:form method="POST" action="index">
				<s:submit value="Accept"/>			
			</s:form>  
		</div> 