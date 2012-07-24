<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>	
		
		<s:if test="filesSuccessful.size()>0 || filesWithEmptyEadid.size()>0 || filesWithErrors.size()>0 || existingFiles.size()>0 || filesBlocked.size()>0">
			<form id="overwriteexistingfiles" method="post">					
			<s:if test="filesSuccessful.size()>0" >				
				<div id="filesSuccessful" class="uploadedfiles_div">
					<div id="text_filesSuccessful">
	        			<p class="uploadedfiles_title">
	        				<img id="filesSuccessfulHCimage" src="images/expand/menos.gif"/> 
	        				<s:property value="getText('content.message.titlesuccessfulfiles')" />
	        			</p>
	        			<hr/>
	        		</div>
				
					<div id="content_filesSuccessful" style="display:inline;">				
						<s:property value="getText('content.message.filessuccessful')" />
				  		<br>
				  		<br>
				  		<br>
				  		<s:iterator value="filesSuccessful" status="stat">
	                        <span id="filesSuccessfulId<s:property value="#stat.index"/>" class="linkText">
							    <s:property value="%{(#stat.index+1) + '- ' + top.fileName}"/>
	                        </span>
	                        <br/><br/>
	       				</s:iterator>
	       			</div>
				</div>
				<br>
			</s:if>
			<%--<s:if test="filesNotUploaded.size()>0" >
				<div id="filesNotUploaded">
				<div id="text_filesNotUploaded">
	        	<p style="font-weight: bold; font-style: italic; font-size: 1.5em; color: black;">
	        	<img id="filesNotUploadedHCimage" src="images/expand/arrow-down.gif"/> FILES WITH ERRORS 1
	        	</p><hr/></div>
	        	<div id="content_filesNotUploaded">											
						<s:property value="content.message.filesnotuploaded"/>
				  		<br>
				  		<br>
				  		<br>
				  		<s:iterator value="filesNotUploaded" status="stat">
							<s:property value="%{(#stat.index+1) + '- ' + top.fileName}"/>       				<br>
	       				</s:iterator>
	       				</div>
				</div>
					<br>					
			</s:if> --%>
			<s:if test="filesWithErrors.size()>0" >
				<div id="filesWithErrors" class="uploadedfiles_div">
				<div id="text_filesWithErrors">
	        	<p class="uploadedfiles_title">
	        	<img id="filesWithErrorsHCimage" src="images/expand/menos.gif"/> 
	        		<s:property value="getText('content.message.titlefileswitherrors')" />
	        	</p><hr/></div>
	        		<div id="content_filesWithErrors" style="display:inline;">							
					<s:property value="getText('content.message.fileswitherrors')" /> 
			  		<br>
			  		<br>
			  		<br>
			  		<s:iterator value="filesWithErrors" status="stat">
                        <span id="warnId<s:property value="#stat.index"/>" class="linkText">
						    <s:property value="%{(#stat.index+1) + '- ' + top.fileName}"/><s:if test="top.errorInformation != null"> (<s:property value="getText('label.moreinf')" />)</s:if>
                        </span>
                        <s:if test="top.errorInformation != null">
                            <div id="warnErrorId<s:property value="#stat.index"/>" style="display:none;" class="warnErrorId">
                                 <s:property value="%{top.errorInformation}"/>
                            </div>
                            <script type="text/javascript">
                                $('#warnId<s:property value="#stat.index"/>').click(function() {
                                    if ($('#warnErrorId<s:property value="#stat.index"/>').is(":visible")){
                                        $(".warnErrorId").hide();
                                    } else {
                                        $(".warnErrorId").hide();
                                        $('#warnErrorId<s:property value="#stat.index"/>').toggle();
                                    }
                                });
                            </script>
                        </s:if>
                        <br/><br/>
       				</s:iterator>
       				</div>
       				</div>
			</s:if>					
			<s:if test="existingFiles.size()>0" >
	        	<div id ="filesWithEADIDrepeated" class="uploadedfiles_div">
	        	<div id="text_filesWithEADIDrepeated">
	        	<p class="uploadedfiles_title">
	        	<img id="firstHCimage" src="images/expand/menos.gif"/> 
	        		<s:property value="getText('content.message.titlefileswitheadidrepeated')" />	        		
	        	</p><hr/></div>
	        	
	        	<div id="content_filesWithEADIDrepeated" style="display:inline;">
	        	<s:property value="getText('content.message.filesalreadyindashboard')" />
	        	<br>
	            	<s:property value="getText('content.message.filesremovingwarning')" />
	            	<br><br>
	            	<s:iterator value="existingFiles" status="stat">
	            			        				
							<div id="titleListRepeated<s:property value="%{top.eadid}" />">
		        				<label style="text-align:left;"><s:property value="%{(#stat.index+1) + '- ' + '(' + top.eadType + ') ' + top.fileName}"/></label>
		        				<div id="right<s:property value="%{top.eadid}" />" style="position:relative; top: -15px;" align="right"><s:select onchange="changeEADID(this, '%{top.eadid}', 'Change');" list="existingFilesChoice" name="existingFilesAnswers" theme="simple"></s:select></div>
		        			</div>
		        			<div id="divGeneralChangeEadid<s:property value="%{top.eadid}" />" style="display:none;">
		        			
	        				<s:hidden name="existingFiles[%{#stat.index}].fileId" value="%{top.fileId}"></s:hidden>
		       				<s:hidden name="existingFiles[%{#stat.index}].fileType" value="%{top.fileType}"></s:hidden>
		       				<s:hidden name="existingFiles[%{#stat.index}].fileName" value="%{top.fileName}"></s:hidden>
		       				<s:hidden name="existingFiles[%{#stat.index}].eadType" value="%{top.eadType}"></s:hidden>
		       				<s:hidden name="existingFiles[%{#stat.index}].eadid" value="%{top.eadid}"></s:hidden>
		       				<s:hidden name="existingFiles[%{#stat.index}].permId" value="%{top.permId}"></s:hidden>
	        				<!-- <s:hidden name="uno" value="%{top.fileId}" /> -->
	        				<div id="divChangeEadid<s:property value="%{top.eadid}" />" style="display:none;">		        				
								<p style="text-align: center; font-weight:bold;"><s:property value="getText('content.message.changeEADID')"/></p><hr/>
								<p><br></p>
			        			<label for="textEADID" style="font-weight: bold;"><s:property value="getText('content.message.currentEADID')"/></label> <s:property value="%{top.eadid}" />
			        			<p><br></p>		        			
			        			<span style="font-weight: bold;"><s:property value="getText('content.message.newEADID')"/></span><input type="text" name="arrayneweadid" id="neweadid<s:property value="%{top.eadid}" />" size="30%" style="padding-left:4px;"/>
			        			<p><br></p>
			        			
			        			<input type="button" style="display:none;" id="SaveChangesButton<s:property value="%{top.eadid}" />" name="SaveChangesButton<s:property value="%{top.eadid}" />" onclick="var iddivneweadid= 'neweadid' + '<s:property value="%{top.eadid}" />'; var neweadid= document.getElementById(iddivneweadid).value;checkEADIDavailability('<s:property value="%{top.eadid}" />',neweadid, '<s:property value="%{top.fileId}" />');" value="<s:property value="getText('content.message.checkbutton')"/>" />
			        			<p><br><br></p>
			        		</div>
			        			<p></p>
									<label id="resultChangeEADID<s:property value="%{top.eadid}" />"></label>									
									<select list="existingEADIDAnswersChoice" name="existingChangeEADIDAnswers" id="existingChangeEADIDAnswers<s:property value="%{top.eadid}" />" style="display:none;">
										<option value="KO">KO</option>		
									</select>
									<br>
									<div id="divCancelOverwriteEADID<s:property value="%{top.eadid}" />" style="display:none;">
										<label><s:property value="getText('content.message.OverwriteCancelEadid')"/></label>
										<s:select onchange="var iddivneweadid= 'neweadid' + '%{top.eadid}'; var neweadid= document.getElementById(iddivneweadid).value;CancelOverwriteExistingEADID(this, '%{top.eadid}', neweadid);" list="existingFilesChoiceOverwriteCancelEADID" name="existingCancelOverwriteEADIDAnswers" theme="simple"></s:select>
									</div>												        			
		        			</div>
	            	</s:iterator>
	            	</div>
	            </div>
	             <br>	           
	        </s:if>
	        <s:if  test="filesWithEmptyEadid.size()>0" >
				<div id="filesWithEmptyEadid" class="uploadedfiles_div">
				<div id="text_filesWithEmptyEadid">
				<p class="uploadedfiles_title">
					<img id="secondHCimage" src="images/expand/menos.gif"/>
					<s:property value="getText('content.message.titlefileswithemptyeadid')" />
				</p><hr/>
				</div>
					
					<div id="content_filesWithEmptyEadid" style="display:inline;">
						<s:property value="getText('content.message.fileswithempyeadid')" />
			  			<br></br>
			  					  		
			  		<s:iterator value="filesWithEmptyEadid" status="stat">                                                	        			
							        											
		        			<div id="titleListEmpty<s:property value="%{#stat.index}" />" style="text-align:left; display:inline;">
		        				<label style="text-align:left;"><s:property value="%{(#stat.index+1) + '- ' + '(' + top.eadType + ') ' + top.fileName}"/></label>
		        				<div id="right<s:property value="%{#stat.index}" />" style="position:relative; top: -15px;" align="right"><s:select onchange="changeEADID(this, '%{#stat.index}','Add');" list="existingFilesChoiceAddEADID" name="existingFilesAnswers" theme="simple"></s:select></div>
		        			</div> 
		        			
		        			<div id="divGeneralAddEadid<s:property value="%{#stat.index}" />" style="display:none;">		        			
		        			
	        				<input type="hidden" name="filesWithEmptyEadid[<s:property value="%{#stat.index}"/>].fileId" value="<s:property value="%{top.fileId}"/>" />
		       				<input type="hidden" name="filesWithEmptyEadid[<s:property value="%{#stat.index}"/>].fileType" value="<s:property value="%{top.fileType}"/>" />
		       				<input type="hidden" name="filesWithEmptyEadid[<s:property value="%{#stat.index}"/>].fileName" value="<s:property value="%{top.fileName}"/>" />
		       				<input type="hidden" name="filesWithEmptyEadid[<s:property value="%{#stat.index}"/>].eadType" value="<s:property value="%{top.eadType}"/>" />
		       				<input type="hidden" name="filesWithEmptyEadid[<s:property value="%{#stat.index}"/>].eadid" value="<s:property value="%{top.eadid}"/>" />
		       				<input type="hidden" name="filesWithEmptyEadid[<s:property value="%{#stat.index}"/>].permId" value="<s:property value="%{top.permId}"/>" />
	        				<!-- <input type="hidden" name="uno" value="<s:property value="%{top.fileId}"/>" />  -->	        				
	        				<div id="divAddEadid<s:property value="%{#stat.index}" />" style="display:none;" >		        				
								<p style="text-align: center; font-weight:bold;"><s:property value="getText('content.message.addEADID')" /></p><hr/>
								<p><br></p>			        			
			        			<span style="font-weight: bold;"><s:property value="getText('content.message.newEADID')"/></span><input type="text" name="arrayneweadid" id="neweadid<s:property value="%{#stat.index}" />" size="30%" style="padding-left:4px;"/>
			        			<p><br></p>
			        			
			        			<input type="button" style="display:none;" id="SaveChangesButton<s:property value="%{#stat.index}" />" name="SaveChangesButton<s:property value="%{#stat.index}" />" onclick="var iddivneweadid= 'neweadid' + '<s:property value="%{#stat.index}" />'; var neweadid= document.getElementById(iddivneweadid).value;checkEADIDavailability('<s:property value="%{#stat.index}" />',neweadid, '<s:property value="%{top.fileId}" />');" value="<s:property value="getText('content.message.checkbutton')"/>" />			        			
			        			
			        		</div>
			        			<p></p>
									<label id="resultChangeEADID<s:property value="%{#stat.index}" />"></label>									
									<select list="existingEADIDAnswersChoice" name="existingChangeEADIDAnswers" id="existingChangeEADIDAnswers<s:property value="%{#stat.index}" />" style="display:none;">
										<option value="KO">KO</option>		
									</select>
									<br>
									<div id="divCancelOverwriteEADID<s:property value="%{#stat.index}" />" style="display:none;">
										<label><s:property value="getText('content.message.OverwriteCancelEadid')"/></label>
										<s:select onchange="var iddivneweadid= 'neweadid' + '%{#stat.index}'; var neweadid= document.getElementById(iddivneweadid).value;CancelOverwriteExistingEADID(this, '%{#stat.index}', neweadid);" list="existingFilesChoiceOverwriteCancelEADID" name="existingCancelOverwriteEADIDAnswers" theme="simple"></s:select>
										<br>
									</div>	<br>		        			
		        			</div>
       				</s:iterator> 
       				</div>      				
				</div>
				<br>
			</s:if>
			<s:if test="filesBlocked.size()>0">
				<div id="filesBlocked" class="uploadedfiles_div">
					<div id="text_filesBlocked">
	        			<p class="uploadedfiles_title">
	        				<img id="filesBlockedHCimage" src="images/expand/menos.gif"/> 
	        				<s:property value="getText('content.message.titlefilesblocked')" />
	        			</p>
	        			<hr/>
	        		</div>
				
					<div id="content_filesBlocked" style="display:inline;">				
						<s:property value="getText('content.message.filesblocked.explanation')" />
				  		<br>
				  		<br>
				  		<br>
				  		<div id="titleListBlocked" style="text-align:left; display:inline;">
					  		<s:iterator value="filesBlocked" status="stat">
				  			    <input type="hidden" name="filesBlocked[<s:property value="%{#stat.index}"/>].fileId" value="<s:property value="%{top.fileId}"/>" />
			       				<input type="hidden" name="filesBlocked[<s:property value="%{#stat.index}"/>].fileType" value="<s:property value="%{top.fileType}"/>" />
			       				<input type="hidden" name="filesBlocked[<s:property value="%{#stat.index}"/>].fileName" value="<s:property value="%{top.fileName}"/>" />
			       				<input type="hidden" name="filesBlocked[<s:property value="%{#stat.index}"/>].eadType" value="<s:property value="%{top.eadType}"/>" />
			       				<input type="hidden" name="filesBlocked[<s:property value="%{#stat.index}"/>].eadid" value="<s:property value="%{top.eadid}"/>" />
			       				<input type="hidden" name="filesBlocked[<s:property value="%{#stat.index}"/>].permId" value="<s:property value="%{top.permId}"/>" />
					  		
								<s:property value="%{(#stat.index+1) + '- ' + top.fileName}"/>
		                        <br/><br/>
		       				</s:iterator>
				  		</div>
	       			</div>
				</div>
			</s:if>
			<s:submit theme="simple" cssClass="mainButton" key="label.accept" action="overwriteexistingfiles"/>
			<s:if  test="filesWithEmptyEadid.size()>0 || existingFiles.size()>0" >
				<!--<s:submit theme="simple" style="font-weight:bold; background-color:#B0D0FF;" key="label.cancel" action="canceloverwriteexistingfiles"/>-->
			</s:if>
			  <br>  <br>  <br>  <br>				
		</form>
		</s:if>
		<s:else>
				
	        	<div id="existingFilesList">
	        		<s:property value="getText('content.continue.contentmanager')"/>	            	
	 			<br>
	        	</div>
	        	<br>
	        	<br>
				<form id="content" method="get">
					<s:hidden id="aiId" value="%{ai_id}" name="aiId" />
					<!--<s:iterator value="filesWithEmptyEadid" status="stat">
					<s:submit key="label.accept" action="addneweadid"/>
					</s:iterator>-->
					<s:submit theme="simple" key="content.continue.contentmanager" action="content"/>
					
				</form>
	        </s:else>
	        <script type="text/javascript">
	        var eadidarray = new Array();
			function changeEADID(text,eadid,method)
			{
				var textvalue = text.options[text.selectedIndex].text;
				var buttonid= "SaveChangesButton" + eadid;
				
				if (method == "Add") {
					if (textvalue == "Add EADID") {
						var divname="divGeneralAddEadid" + eadid;
						document.getElementById(divname).style.display='inline';	
						document.getElementById(buttonid).style.display='inline';
						var divgeneralname= "divAddEadid" + eadid;
						document.getElementById(divgeneralname).style.display='inline';
					}
					else {
						var divname="divGeneralAddEadid" + eadid;
						document.getElementById(divname).style.display='none';
						document.getElementById(buttonid).style.display='none';
						var divgeneralname= "divAddEadid" + eadid;
						document.getElementById(divgeneralname).style.display='none';
					}
				}
				else if (method=="Change") {
					if (textvalue == "Change EADID") {
						var divname= "divChangeEadid" + eadid;
						document.getElementById(divname).style.display='inline';
						document.getElementById(buttonid).style.display='inline';
						var divgeneralname= "divGeneralChangeEadid" + eadid;
						document.getElementById(divgeneralname).style.display='inline';
						var textboxid = "neweadid" + eadid;
						document.getElementById(textboxid).focus();
					}
					else {
						var divname= "divChangeEadid" + eadid;
						document.getElementById(divname).style.display='none';
						document.getElementById(buttonid).style.display='none';
						var divgeneralname= "divGeneralChangeEadid" + eadid;
						document.getElementById(divgeneralname).style.display='none';
					}
				}
			}
			
			function CancelOverwriteExistingEADID(text, eadid, neweadid) {
				var textvalue = text.options[text.selectedIndex].text;				
				var labelanswermessage = "resultChangeEADID" + eadid;
				if (textvalue == "Cancel"){
					//Display you will cancel this operation for this file.						
					document.getElementById(labelanswermessage).innerHTML="You will cancel this operation for this file";
				}
				else {
					//Display you will overwrite the file with the eadid selected for this one.
					document.getElementById(labelanswermessage).innerHTML="You will overwrite the file with the eadid selected for this one.";										
				}
			}
			
			function checkEADIDavailability(oldeadid, neweadid, fileId) {
				$.getJSON("${pageContext.request.contextPath}/generateEadidResponseJSON.action", 
						{ eadid: oldeadid, 
					      neweadid: neweadid, 
					      fileId: fileId }, 
					    function(dataResponse)
						{
							//Show the message.
							var labelanswermessage = "resultChangeEADID" + dataResponse.eadid;							
							document.getElementById(labelanswermessage).innerHTML=dataResponse.message;

							//Check the availability in the array
							var value= eadidarray.length;
							if (value==0){
								eadidarray[value] = neweadid;
							}
							else {
								var found=false;
								for (var i=0; i< value; i++){																		
									if (eadidarray[i]==neweadid){										
										found=true;
										if (dataResponse.existingChangeEADIDAnswers!= "KO"){
											dataResponse.existingChangeEADIDAnswers= "KO";
											document.getElementById(labelanswermessage).innerHTML=dataResponse.komessage;
										}
									}																
								}
								if (found==false){
									value++;
									eadidarray[value] = neweadid;
								}
							}
							if (dataResponse.existingChangeEADIDAnswers == "OK") {
								//Change the value of the select OK or KO
								document.getElementById(labelanswermessage).style.color="green";
								document.getElementById(labelanswermessage).style.font.bold = "true";
								var selectanswer = "existingChangeEADIDAnswers" + dataResponse.eadid;
								var select = document.getElementById(selectanswer);
								select.options[0] = new Option(dataResponse.existingChangeEADIDAnswers, dataResponse.existingChangeEADIDAnswers);
								var checkavailabilitybutton = "SaveChangesButton" + dataResponse.eadid;
								document.getElementById(checkavailabilitybutton).style.display='none';
								var div= "divCancelOverwriteEADID" + dataResponse.eadid;								
								document.getElementById(div).style.display='none';
							}
							else if (dataResponse.existingChangeEADIDAnswers == "KO") {
								document.getElementById(labelanswermessage).style.color="red";
								document.getElementById(labelanswermessage).style.font.bold = "true";
								var selectanswer = "existingChangeEADIDAnswers" + dataResponse.eadid;
								var select = document.getElementById(selectanswer);
								select.options[0] = new Option(dataResponse.existingChangeEADIDAnswers, dataResponse.existingChangeEADIDAnswers);								
								var div= "divCancelOverwriteEADID" + dataResponse.eadid;								
								document.getElementById(div).style.display='inline';
							}
						}
			);
				
			}
			
			$('#text_filesSuccessful').click(function(){
				if($('#content_filesSuccessful').is(':hidden')){
					$('#content_filesSuccessful').show('slow');
					$('#filesSuccessfulHCimage').attr("src","images/expand/menos.gif");
				}else{
					$('#content_filesSuccessful').hide('slow');
					$('#filesSuccessfulHCimage').attr("src","images/expand/mas.gif");
				}
			});
			//filesWithErrors
			$('#text_filesWithErrors').click(function(){
				if($('#content_filesWithErrors').is(':hidden')){
					$('#content_filesWithErrors').show('slow');
					$('#filesWithErrorsHCimage').attr("src","images/expand/menos.gif");
				}else{
					$('#content_filesWithErrors').hide('slow');
					$('#filesWithErrorsHCimage').attr("src","images/expand/mas.gif");
				}
			});
			$('#text_filesWithEADIDrepeated').click(function(){
				if($('#content_filesWithEADIDrepeated').is(':hidden')){
					$("div[id^='titleListRepeated']").show('slow');
					//$("div[id^='divChangeEadid']").show('slow');
					$('#content_filesWithEADIDrepeated').show('slow');
					$('#firstHCimage').attr("src","images/expand/menos.gif");
				}else{
					$("div[id^='titleListRepeated']").hide('slow');
					$("div[id^='divChangeEadid']").hide('slow');
					$('#content_filesWithEADIDrepeated').hide('slow');
					$('#firstHCimage').attr("src","images/expand/mas.gif");
				}
			});
			$('#text_filesWithEmptyEadid').click(function(){
				if($('#content_filesWithEmptyEadid').is(':hidden')){
					$("div[id^='titleListEmpty']").show('slow');
					//$("div[id^='divAddEadid']").show('slow');
					$('#content_filesWithEmptyEadid').show('slow');
					$('#secondHCimage').attr("src","images/expand/menos.gif");
				}else{
					$('#content_filesWithEmptyEadid').hide('slow');
					$("div[id^='titleListEmpty']").hide('slow');
					$("div[id^='divAddEadid']").hide('slow');
					$('#secondHCimage').attr("src","images/expand/mas.gif");
				}
			});
			$('#text_filesBlocked').click(function(){
				if($('#content_filesBlocked').is(':hidden')){
					$("div[id^='titleListBlocked']").show('slow');
					$('#content_filesBlocked').show('slow');
					$('#filesBlockedHCimage').attr("src","images/expand/menos.gif");
				}else{
					$('#content_filesBlocked').hide('slow');
					$("div[id^='titleListBlocked']").hide('slow');
					$('#filesBlockedHCimage').attr("src","images/expand/mas.gif");
				}
			});

			</script>		
<div style="margin-bottom:30px;"></div>