<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
        <div id="fileList">
            <s:property value="getText('uploadsimpleeagerror.errorsOccurred')" /><br>
			<s:property value="getText('uploadsimpleeagerror.errEAG')" />
        </div>
        <div id="global">
			<s:form method="POST" action="index">
				<s:submit value="Accept"/>			
			</s:form>  
		</div> 