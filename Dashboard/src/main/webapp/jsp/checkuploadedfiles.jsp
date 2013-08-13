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
	            	
					<br><p></p></br>

	            	<s:iterator value="existingFiles" status="stat">
	            	
	            	<br><p></p></br>
	            	     				
							<div id="titleListRepeated<s:property value="%{top.eadid}" />" style="text-align:left; display:inline;width:100%;">
		        				<label style="text-align:left;"><s:property value="%{(#stat.index+1) + '- ' + '(' + top.eadType + ') ' + top.fileName}"/></label>
		        				<div id="right<s:property value="%{top.eadid}" />" style="float:right;" >
			        					<!-- '%{#stat.index}' <---   '%{top.eadid}' -->
			        					<select  id="existingFilesAnswers" onchange="changeEADID(this, '${top.eadid}', 'Change');" >
										<s:iterator value="existingFilesChoice" var="action"> 
											<option value="<s:property value="#action.key" />"><s:property value="#action.value" /></option>
										</s:iterator>
									</select>
		        				</div>
		        			</div>
		        			<div id="divGeneralChangeEadid<s:property value="%{top.eadid}" />">
		        			
	        				<s:hidden name="existingFiles[%{#stat.index}].fileId" value="%{top.fileId}"></s:hidden>
		       				<s:hidden name="existingFiles[%{#stat.index}].fileType" value="%{top.fileType}"></s:hidden>
		       				<s:hidden name="existingFiles[%{#stat.index}].fileName" value="%{top.fileName}"></s:hidden>
		       				<s:hidden name="existingFiles[%{#stat.index}].eadType" value="%{top.eadType}"></s:hidden>
		       				<s:hidden name="existingFiles[%{#stat.index}].eadid" value="%{top.eadid}"></s:hidden>
		       				<s:hidden name="existingFiles[%{#stat.index}].permId" value="%{top.permId}"></s:hidden>
		       						       				
	        				<div id="divChangeEadid<s:property value="%{top.eadid}" />" style="display:none;">		        				
								<p style="text-align: center; font-weight:bold;"><s:property value="getText('content.message.changeEADID')"/></p><hr/>
								<p><br></p>
			        			<label for="textEADID" style="font-weight: bold;"><s:property value="getText('content.message.currentEADID')"/></label> <s:property value="%{top.eadid}" />
			        			<p><br></p>		        			
			        			<span style="font-weight: bold;"><s:property value="getText('content.message.newEADID')"/></span> 
			        				        			
			        			<input type="text" name="arrayneweadid"
									id="neweadid<s:property value="%{top.eadid}" />" size="30%"
									style="padding-left: 4px;" />
								
								<!--EAD file with repeated EADID -->
								<input type="button"
									id="SaveChangesButton<s:property value="%{top.eadid}" />"
									name="SaveChangesButton<s:property value="%{top.eadid}" />"
									onclick="getAndCheckEADIDavailability('<s:property value="%{#stat.index}" />','<s:property value='%{top.eadid}' />','<s:property value="%{top.fileId}" />')" 
									value="<s:property value="getText('content.message.checkbutton')"/>" />
															
			        		</div>
			        			<p></p>
									<label id="resultChangeEADID<s:property value="%{top.eadid}" />"></label>									
									<select list="existingEADIDAnswersChoice" name="existingChangeEADIDAnswers" id="existingChangeEADIDAnswers<s:property value="%{top.eadid}" />" style="display:none;">
										<option value="KO">KO</option>		
									</select>
									<br>
									<div id="divCancelOverwriteEADID<s:property value="%{top.eadid}" />" style="display:none;">
										<label><s:property value="getText('content.message.OverwriteCancelEadid')"/></label>
										<s:select
											onchange="var iddivneweadid= 'neweadid' + '%{top.eadid}'; var neweadid= document.getElementById(iddivneweadid).value;CancelOverwriteExistingEADID(this, '%{top.eadid}', neweadid);"
											list="existingFilesChoiceOverwriteCancelEADID"
											name="existingCancelOverwriteEADIDAnswers" 
											theme="simple">
										</s:select>
									</div>																		        			
		        			</div>
		        			<p></p>
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
				</p>
				<hr/>
				</div>
					
					<div id="content_filesWithEmptyEadid" style="display:inline;">
						<s:property value="getText('content.message.fileswithempyeadid')" />
			  			
			  			<br><p></p></br>
			  					  		
			  		<s:iterator value="filesWithEmptyEadid" status="stat">                                                	        			
							
							<br><p></p></br>
							        											
		        			<div id="titleListEmpty<s:property value="%{#stat.index}" />" style="text-align:left; display:inline;width:100%;">
		        				<label style="text-align:left;"><s:property value="%{(#stat.index+1) + '- ' + '(' + top.eadType + ') ' + top.fileName}"/></label>
		        				<div id="right<s:property value="%{#stat.index}" />" style="float:right;" >
			        				<s:select onchange="changeEADID(this, '%{#stat.index}','Add');" list="existingFilesChoiceAddEADID"  name="existingFilesAnswers" theme="simple"></s:select>
		        				</div>
		        			</div> 
		        			
		        			<%-- <div id="<s:property value="%{#stat.index}" />" style="display:none;">  --%>
			        		<div id="divGeneralAddEadid<s:property value="%{#stat.index}" />" style="display:inline;">
									

							<p style="text-align: center; font-weight: bold;">
								<s:property value="getText('content.message.addEADID')" />
							</p>
							<hr>
							<p>
								<br>
							</p>	  
								<input type="hidden" name="filesWithEmptyEadid[<s:property value="%{#stat.index}"/>].fileId" value="<s:property value="%{top.fileId}"/>" /> 
								<input type="hidden" name="filesWithEmptyEadid[<s:property value="%{#stat.index}"/>].fileType" value="<s:property value="%{top.fileType}"/>" /> 
								<input type="hidden" name="filesWithEmptyEadid[<s:property value="%{#stat.index}"/>].fileName" value="<s:property value="%{top.fileName}"/>" /> 
								<input type="hidden" name="filesWithEmptyEadid[<s:property value="%{#stat.index}"/>].eadType" value="<s:property value="%{top.eadType}"/>" /> 
								<input type="hidden" name="filesWithEmptyEadid[<s:property value="%{#stat.index}"/>].eadid"	value="<s:property value="%{top.eadid}"/>" /> 
								<input type="hidden"name="filesWithEmptyEadid[<s:property value="%{#stat.index}"/>].permId"	value="<s:property value="%{top.permId}"/>" />
																
								<div id="divAddEadid<s:property value="%{#stat.index}" />"><!-- style="display:none;" -->
									
								<!--EAD file with no EADID keyup method over the textbox-->					
								<input type="text" name="arrayneweadid"
									id="neweadid<s:property value="%{#stat.index}" />" size="30%"
									style="padding-left: 4px;" />
	
								<!--EAD file with no EADID -->
								<input type="button" style="display: inline;"
									id="SaveChangesButton<s:property value="%{#stat.index}" />"
									name="SaveChangesButton<s:property value="%{#stat.index}" />"
									onclick="getAndCheckEADIDavailability('<s:property value="%{#stat.index}" />','<s:property value='%{top.eadid}' />','<s:property value="%{top.fileId}" />')"
									value="<s:property value="getText('content.message.checkbutton')"/>" />
							</div>
							<p></p>
								<label
									id="resultChangeEADID<s:property value="%{#stat.index}" />"></label> <select list="existingEADIDAnswersChoice"
									name="existingChangeEADIDAnswers"
									id="existingChangeEADIDAnswers<s:property value="%{#stat.index}" />"
									style="display: none;">
									<option value="KO">KO</option>
								</select> <br>
																
								<div
									id="divCancelOverwriteEADID<s:property value="%{#stat.index}" />"
									style="display: none;">
									<label><s:property value="getText('content.message.OverwriteCancelEadid')" /></label>
									<s:select
										onchange="var iddivneweadid= 'neweadid' + '%{#stat.index}'; var neweadid= document.getElementById(iddivneweadid).value;CancelOverwriteExistingEADID(this, '%{#stat.index}', neweadid);"
										list="existingFilesChoiceOverwriteCancelEADID"
										name="existingCancelOverwriteEADIDAnswers" theme="simple"></s:select>
									<br>
								</div>						        			
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
							<hr>
							<p>
								<br>
							</p>
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
			<input type="button" class="mainButton" id="form_submit" value="<s:property value='getText("label.accept")' />" name="form_submit" onclick="checkEadIdAndSubmit()" disabled="disabled" />
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
					<s:submit theme="simple" key="content.continue.contentmanager" action="contentmanager"/>
					
				</form>
	     </s:else>
	        <script type="text/javascript">
	        $(document).ready(function(){
	        	
	        	changes = new Array();
	        	
	        	eadidarray.splice(0,eadidarray.length);
	        	
	        	$("div[id^=divChangeEadid]").each(function(){
	        		var id = $(this).attr("id");
	        		id = id.substring("divChangeEadid".length);
	        		//changes[id] = "";
	        		changes.push(id);
	        	});	       
	        	        	
	        });
	        
	        function checkEadIdAndSubmit(){
	        	if(changes.length>0 && $("select#existingFilesAnswers option:selected").val()=="Change EADID"){
	        		var strOut='<s:property value='getText("content.message.UserHasNotCheckedAvailability")'/>';
	        		alert(strOut);
	        	}else{
	        		$("form#overwriteexistingfiles").attr("action","overwriteexistingfiles.action");//action="overwriteexistingfiles"
	        		$("form#overwriteexistingfiles").submit();
	        	}
	        }
	        
        	function hideExistingEADID(eadid,divname,buttonid){
        		var divname="divGeneralChangeEadid" + eadid;
				document.getElementById(divname).style.display='none';
				document.getElementById(buttonid).style.display='none';
				var divgeneralname= "divChangeEadid" + eadid;
				document.getElementById(divgeneralname).style.display='none';
        	}
        	function showExistingEADID(eadid,divname,buttonid){
        		var divname="divGeneralChangeEadid" + eadid;
				document.getElementById(divname).style.display='inline';
				document.getElementById(buttonid).style.display='inline';
				var divgeneralname= "divChangeEadid" + eadid;
				document.getElementById(divgeneralname).style.display='inline';
				var textboxid = "neweadid" + eadid;
				document.getElementById(textboxid).focus();
        	}
	        
	        var eadidarray = new Array();
			
	        function changeEADID(text,eadid,method)
			{
				var textvalue = text.options[text.selectedIndex].value;
				var buttonid= "SaveChangesButton" + eadid;
				
				if (method == "Add") {
					if (textvalue == "Add EADID") {
						var divname="divGeneralAddEadid" + eadid;
						document.getElementById(divname).style.display='inline';	
						document.getElementById(buttonid).style.display='inline';
						var divgeneralname= "divAddEadid" + eadid;
						document.getElementById(divgeneralname).style.display='inline';
						// Disable accept button.
						$("input#form_submit").attr("disabled","disabled");
					}
					else { 
						//Cancel
						var divname="divGeneralAddEadid" + eadid;
						document.getElementById(divname).style.display='none';
						document.getElementById(buttonid).style.display='none';
						var divgeneralname= "divAddEadid" + eadid;
						document.getElementById(divgeneralname).style.display='none';
						//enable accept button
						$("input#form_submit").removeAttr("disabled");	
 						//clean textbox
						$("input[id^='neweadid']").each(function(){
								$(this).attr("value","");
						});
						//clean label
						$("label[id^='resultChangeEADID']").each(function(){
							$(this).hide();
							var strLabelOut=$(this).text();				
						});
						//clean text and combo too
						$("label[id^='divCancelOverwriteEADID']").each(function(){
							$(this).hide();
						});	 		
						//clean all values from the array when cancel
						eadidarray.splice(0,eadidarray.length);
					}
				}
				else if (method=="Change") {
					//files with existing EADID
					if (textvalue == "Change EADID") {
						showExistingEADID(eadid,divname,buttonid);
						// Disable accept button.
						$("input#form_submit").attr("disabled","disabled");
					}
					else {
						//overwrite or cancel
						hideExistingEADID(eadid,divname,buttonid);
						//enable accept button
						$("input#form_submit").removeAttr("disabled");
						//clean all values from the array when cancel
						if (textvalue == "Cancel") {
							eadidarray.splice(0,eadidarray.length);
							$("input#form_submit").removeAttr("disabled");
						}
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
				$("label#" + labelanswermessage).show();
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
							$("label#" + labelanswermessage).show();
							var object = document.getElementById(labelanswermessage);
							object.innerHTML=dataResponse.message;
							//in case the textbox is empty the user is not allowed to add an empty value to overwrite						
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
							$("input#form_submit").attr("disabled","disabled");
							if (dataResponse.existingChangeEADIDAnswers == "OK") {
								//changes[oldeadid] = neweadid;
								if(changes.indexOf(oldeadid)>=0){
									changes.splice(changes.indexOf(oldeadid));
								}
								//Change the value of the select OK or KO
								document.getElementById(labelanswermessage).style.color="green";
								document.getElementById(labelanswermessage).style.font.bold = "true";
								var selectanswer = "existingChangeEADIDAnswers" + dataResponse.eadid;
								var select = document.getElementById(selectanswer);
								select.options[0] = new Option(dataResponse.existingChangeEADIDAnswers, dataResponse.existingChangeEADIDAnswers);
								var checkavailabilitybutton = "SaveChangesButton" + dataResponse.eadid;
								/*Do not hide the check availavility button*/
								/* document.getElementById(checkavailabilitybutton).style.display='none'; */
								var div= "divCancelOverwriteEADID" + dataResponse.eadid;								
								document.getElementById(div).style.display='none';
								$("input#form_submit").removeAttr("disabled");
							}
							else if (dataResponse.existingChangeEADIDAnswers == "KO") {
								if(changes.indexOf(oldeadid)){
									changes.push(oldeadid);
								}
								document.getElementById(labelanswermessage).style.color="red";
								document.getElementById(labelanswermessage).style.font.bold = "true";
								var selectanswer = "existingChangeEADIDAnswers" + dataResponse.eadid;
								var select = document.getElementById(selectanswer);
								select.options[0] = new Option(dataResponse.existingChangeEADIDAnswers, dataResponse.existingChangeEADIDAnswers);								
								var div= "divCancelOverwriteEADID" + dataResponse.eadid;								
								document.getElementById(div).style.display='none';
								$("input#form_submit").attr("disabled","disabled");
							}
						}
			);
				
			}
			
			
			function getAndCheckEADIDavailability(index,eadid,fileId){
				var iddivneweadid = "";
				if (eadid != "") {
					iddivneweadid= 'neweadid' + eadid ;
				} else {
					iddivneweadid= 'neweadid' + index ;
					eadid=index;
				}
				var neweadid= document.getElementById(iddivneweadid).value;
				checkEADIDavailability(eadid,neweadid,fileId);
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
					$("div[id^='divChangeEadid']").show('slow');
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
					$("div[id^='divAddEadid']").show('slow');
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