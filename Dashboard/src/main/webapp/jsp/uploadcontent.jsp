<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
        <div id="divForm">
        	<div style="align: center;" >
                <s:actionmessage />
                <s:actionerror />

	            <s:radio list="uploadType" key="label.uploadtype" name="uploadTypeChoice" onchange="changeOptions(this)"/>
				<s:form id="httpUpload" method="POST" enctype="multipart/form-data" action="httpUpload">
					<p style="margin-top:10px;">
						<s:file id="httpFile" theme="simple" name="httpFile" key="label.filetoupload"/>
					</p>
					<p id="uploadControls" style="margin-top:10px;">
						<script type="text/javascript">
							textWhileIngesting = '<s:property value="getText('label.upload.filewasingested')"/>';
							writeStatusBar('<s:property value="getText('label.upload')"/>','<s:property value="getText('label.upload.fileextensionerror')"/>','<s:property value="getText('label.upload.confirmExit')"/>');
						</script>
						<noscript>
							<s:submit onclick="document.getElementById('stopButton').style.display='block';" key="label.upload" action="httpUpload" />
						</noscript>
					</p>
				</s:form>
	            <s:form action="connectFTP" method="post">
	                <s:textfield name="ftpUrl" key="labe.url" value="ftp://" />
	                <s:textfield name="ftpPort" key="label.port" value="21"/>
	                <s:textfield name="ftpUser" key="label.username" />
	                <s:password name="ftpPwd" key="label.password" />
	                <s:submit onclick="document.getElementById('stopButton').style.display='block';" method="connectFTP" key="label.ftpconexion" />
	            </s:form>
	            <s:form action="harvestOAI" method="POST">
	                <s:textfield name="oaiUrl" key="labe.url" value="http://" />
	                <s:textfield name="oaiSet" key="label.setname" />
	                <s:textfield name="oaiMetadataFormat" key="label.metadataformat" />
	                <s:select list="oaiType" name="oaiType" key="label.harvesttype"/>
	                <s:textfield name="oaiToken" key="label.token"/>
	                <s:hidden name="ai_id" value="%{ai_id}" />
	                <s:submit onclick="document.getElementById('stopButton').style.display='block';" key="label.harvestdata" />
	            </s:form>
	            <div style="width:100%;float:left;"><div style="width:50%;float:left;margin-left: -1em;">&nbsp;</div><input id="stopButton" style="display:none;float:left;" type="button" value="<s:property value="getText('label.stop')"/>" onclick="stopBrowser('stopButton','<s:property value="getText('al.message.canceled')" />')" /></div>
            </div>
        </div>
        <script type="text/javascript">
            function changeOptions(option){
                if(option.value == "OAI-PMH"){
                    document.getElementById("httpUpload").style.display = "none";
                    document.getElementById("connectFTP").style.display = "none";
                    document.getElementById("harvestOAI").style.display = "block";
                } else if(option.value == "FTP"){
                    document.getElementById("httpUpload").style.display = "none";                    
                	document.getElementById("harvestOAI").style.display = "none";
                    document.getElementById("connectFTP").style.display = "block";
                } else {
                    document.getElementById("httpUpload").style.display = "block";                    
                    document.getElementById("harvestOAI").style.display = "none";
                    document.getElementById("connectFTP").style.display = "none";
                }
            }
            function changeOptionsLoad(){
                if(document.getElementById("uploadTypeChoiceFTP").checked){
                    changeOptions(document.getElementById("uploadTypeChoiceFTP"));
                } else if(document.getElementById("uploadTypeChoiceHTTP").checked){
                    changeOptions(document.getElementById("uploadTypeChoiceHTTP"));
                } else {
                    changeOptions(document.getElementById("uploadTypeChoiceOAI-PMH"));
                }
            }
            changeOptionsLoad();
        </script>