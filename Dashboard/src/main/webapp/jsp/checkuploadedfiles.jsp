<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>

<s:if
	test="filesSuccessful.size()>0 || filesWithEmptyEadid.size()>0 || filesWithErrors.size()>0 || existingFiles.size()>0 || filesBlocked.size()>0 || filesWithEadidTooLong.size()>0">
	<form id="overwriteexistingfiles" method="post">
		<s:if test="filesSuccessful.size()>0">
			<div id="filesSuccessful" class="uploadedfiles_div">
				<div id="text_filesSuccessful">
					<p class="uploadedfiles_title">
						<img id="filesSuccessfulHCimage" src="images/expand/menos.gif" />
						<s:property
							value="getText('content.message.titlesuccessfulfiles')" />
					</p>
					<hr />
				</div>
				<div id="content_filesSuccessful" style="display: inline;">
					<s:property value="getText('content.message.filessuccessful')" />
					<br> <br> <br>
					<s:iterator value="filesSuccessful" status="stat">
						<span id="filesSuccessfulId<s:property value="#stat.index"/>"
							class="linkText"> <s:property
								value="%{(#stat.index+1) + '- ' + top.fileName}" />
						</span>
						<br />
						<br />
					</s:iterator>
				</div>
			</div>
			<br>
		</s:if>
		<s:if test="filesWithErrors.size()>0">
			<div id="filesWithErrors" class="uploadedfiles_div">
				<div id="text_filesWithErrors">
					<p class="uploadedfiles_title">
						<img id="filesWithErrorsHCimage" src="images/expand/menos.gif" />
						<s:property
							value="getText('content.message.titlefileswitherrors')" />
					</p>
					<hr />
				</div>
				<div id="content_filesWithErrors" style="display: inline;">
					<s:property value="getText('content.message.fileswitherrors')" />
					<br> <br> <br>
					<s:iterator value="filesWithErrors" status="stat">
						<span id="warnId<s:property value="#stat.index"/>"
							class="linkText"> <s:property
								value="%{(#stat.index+1) + '- ' + top.fileName}" />
							<s:if test="top.errorInformation != null"> (<s:property
									value="getText('label.moreinf')" />)</s:if>
						</span>
						<s:if test="top.errorInformation != null">
							<div id="warnErrorId<s:property value="#stat.index"/>"
								style="display: none;" class="warnErrorId">
								<s:property value="%{top.errorInformation}" />
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
						<br />
						<br />
					</s:iterator>
				</div>
			</div>
		</s:if>
		<s:if test="filesWithEadidTooLong.size()>0">
			<div id="filesWithEadidTooLong" class="uploadedfiles_div">
				<div id="text_filesWithEadidTooLong">
					<p class="uploadedfiles_title">
						<img id="filesWithErrorsHCimage" src="images/expand/menos.gif" />
						<s:property
								value="getText('content.message.titlefileswitherrors')" />
					</p>
					<hr />
				</div>
				<div id="content_filesWithEadidTooLong" style="display: inline;">
					<s:property value="getText('content.message.filesWithEadidTooLong')" />
					<br> <br> <br>
					<s:iterator value="filesWithEadidTooLong" status="stat">
						<span id="warnId<s:property value="#stat.index"/>"
							  class="linkText"> <s:property
								value="%{(#stat.index+1) + '- ' + top.fileName}" />
							<s:if test="top.errorInformation != null"> (<s:property
									value="getText('label.moreinf')" />)</s:if>
						</span>
						<s:if test="top.errorInformation != null">
							<div id="warnErrorId<s:property value="#stat.index"/>"
								 style="display: none;" class="warnErrorId">
								<s:property value="%{top.errorInformation}" />
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
						<br />
						<br />
						<s:hidden name="filesWithEadidTooLong[%{#stat.index}].fileId"
								  value="%{top.fileId}"></s:hidden>
						<s:hidden name="filesWithEadidTooLong[%{#stat.index}].fileType"
								  value="%{top.fileType}"></s:hidden>
						<s:hidden name="filesWithEadidTooLong[%{#stat.index}].fileName"
								  value="%{top.fileName}"></s:hidden>
						<s:hidden name="filesWithEadidTooLong[%{#stat.index}].filePath"
								  value="%{top.filePath}"></s:hidden>
						<s:hidden name="filesWithEadidTooLong[%{#stat.index}].eadType"
								  value="%{top.eadType}"></s:hidden>
						<s:hidden name="filesWithEadidTooLong[%{#stat.index}].eadid"
								  value="%{top.eadid}"></s:hidden>
						<s:hidden name="filesWithEadidTooLong[%{#stat.index}].permId"
								  value="%{top.permId}"></s:hidden>
					</s:iterator>
				</div>
			</div>
		</s:if>
		<s:if test="existingFiles.size()>0">
			<div id="filesWithEADIDrepeated" class="uploadedfiles_div">
				<div id="text_filesWithEADIDrepeated">
					<p class="uploadedfiles_title">
						<img id="firstHCimage" src="images/expand/menos.gif" />
						<s:property
							value="getText('content.message.titlefileswitheadidrepeated')" />
					</p>
					<hr />
				</div>

				<div id="content_filesWithEADIDrepeated" style="display: inline;">
					<s:property
						value="getText('content.message.filesalreadyindashboard')" />
					<br>
					<s:property value="getText('content.message.filesremovingwarning')" />

					<br>
					<p></p>
					</br>

					<s:iterator value="existingFiles" status="stat">

						<br>
						<p></p>
						</br>

						<div id="titleListRepeated<s:property value="%{#stat.index}" />"
							style="text-align: left; display: inline; width: 100%;">
							<label style="text-align: left;"><s:property
									value="%{(#stat.index+1) + '- ' + '(' + top.eadType + ') ' + top.fileName}" /></label>
							<div id="rightRepeated<s:property value="%{#stat.index}" />"
								style="float: right;">
								<select id="existingFilesAnswers"
									onchange="changeEADID(this, '<s:property value="%{#stat.index}" />', 'Change', true);"
									name="existingFilesAnswers" style="display: inline;">
									<s:iterator value="existingFilesChoice" var="action">
										<option value="<s:property value="#action.key" />">
											<s:property value="#action.value" />
										</option>
									</s:iterator>
								</select>
							</div>
						</div>
						<div id="divGeneralChangeEadid<s:property value="%{#stat.index}" />">

							<s:hidden name="existingFiles[%{#stat.index}].fileId"
								value="%{top.fileId}"></s:hidden>
							<s:hidden name="existingFiles[%{#stat.index}].fileType"
								value="%{top.fileType}"></s:hidden>
							<s:hidden name="existingFiles[%{#stat.index}].fileName"
								value="%{top.fileName}"></s:hidden>
							<s:hidden name="existingFiles[%{#stat.index}].filePath"
								value="%{top.filePath}"></s:hidden>
							<s:hidden name="existingFiles[%{#stat.index}].eadType"
								value="%{top.eadType}"></s:hidden>
							<s:hidden name="existingFiles[%{#stat.index}].eadid"
								value="%{top.eadid}"></s:hidden>
							<s:hidden name="existingFiles[%{#stat.index}].permId"
								value="%{top.permId}"></s:hidden>

							<div id="divChangeEadid<s:property value="%{#stat.index}" />"
								style="display: none;">
								<p style="text-align: center; font-weight: bold;">
									<s:property value="getText('content.message.changeEADID')" />
								</p>
								<hr />
								<p>
									<br>
								</p>
								<label for="textEADID" style="font-weight: bold;"><s:property
										value="getText('content.message.currentEADID')" /></label>
								<s:property value="%{top.eadid}" />
								<input id="textEADIDRepeated<s:property value="%{#stat.index}" />" type="hidden" value="<s:property value="%{top.eadid}" />">
								<p>
									<br>
								</p>
								<span style="font-weight: bold;"><s:property
										value="getText('content.message.newEADID')" /></span> <input
									type="text" name="arrayneweadid"
									id="neweadidRepeated<s:property value="%{#stat.index}" />" size="30%"
									style="padding-left: 4px;" />

								<!--EAD file with repeated EADID -->
								<input type="button"
									id="SaveChangesButtonRepeated<s:property value="%{#stat.index}" />"
									name="SaveChangesButtonRepeated<s:property value="%{#stat.index}" />"
									onclick="getAndCheckEADIDavailability('<s:property value="%{#stat.index}" />','<s:property value="%{#stat.index}" />','<s:property value="%{top.fileId}" />', '<s:property value="%{top.eadType}" />', true)" 
									value="<s:property value="getText('content.message.checkbutton')"/>"
									disabled="disabled" />

							</div>
							<p></p>
							<label id="resultChangeEADIDRepeated<s:property value="%{#stat.index}" />"></label>
							<select list="existingEADIDAnswersChoice"
								name="existingChangeEADIDAnswersRepeated"
								id="existingChangeEADIDAnswersRepeated<s:property value="%{#stat.index}" />"
								style="display: none;">
								<option value="KO">KO</option>
							</select> <br>
							<div
								id="divCancelOverwriteEADIDRepeated<s:property value="%{#stat.index}" />"
								style="display: none;">
								<label><s:property
										value="getText('content.message.OverwriteCancelEadid')" /></label> <select
									onchange="var iddivneweadidRepeated= 'neweadidRepeated' + '<s:property value="%{#stat.index}" />'; var neweadidRepeated= document.getElementById(iddivneweadidRepeated).value;CancelOverwriteExistingEADID(this, '<s:property value="%{#stat.index}" />', neweadidRepeated, true);"
									name="existingCancelOverwriteEADIDAnswers">
									<s:iterator value="existingFilesChoiceOverwriteCancelEADID"
										var="actionEFCOCEADID">
										<option value="<s:property value="#actionEFCOCEADID.key" />">
											<s:property value="#actionEFCOCEADID.value" />
										</option>
									</s:iterator>
								</select>
							</div>
						</div>
						<p></p>
					</s:iterator>
				</div>
			</div>
			<br>
		</s:if>
		<s:if test="filesWithEmptyEadid.size()>0">
			<div id="filesWithEmptyEadid" class="uploadedfiles_div">
				<div id="text_filesWithEmptyEadid">
					<p class="uploadedfiles_title">
						<img id="secondHCimage" src="images/expand/menos.gif" />
						<s:property
							value="getText('content.message.titlefileswithemptyeadid')" />
					</p>
					<hr />
				</div>

				<div id="content_filesWithEmptyEadid">
					<s:property value="getText('content.message.fileswithempyeadid')" />

					<br>
					<p></p>
					</br>

					<s:iterator value="filesWithEmptyEadid" status="stat">

						<br>
						<p></p>
						</br>

						<div id="titleListEmpty<s:property value="%{#stat.index}" />"
							style="text-align: left;  width: 100%;">
							<label style="text-align: left;"><s:property
									value="%{(#stat.index+1) + '- ' + '(' + top.eadType + ') ' + top.fileName}" /></label>
							<div id="rightEmpty<s:property value="%{#stat.index}" />"
								style="position: relative; top: -15px;" align="right">

								<select id="existingFilesAnswers"
									onchange="changeEADID(this, '<s:property value="%{#stat.index}" />', 'Add', false);"
									name="existingFilesAnswers" style="display: inline;">
									<s:iterator value="existingFilesChoiceAddEADID" var="action">
										<s:set name="tempVar">Add EADID</s:set>
										<option value="<s:property value="#action.key" />"
											<s:if test="%{#action.key==#tempVar}" > selected=selected </s:if>>
											<s:property value="#action.value" />
										</option>
									</s:iterator>
								</select>

							</div>
						</div>

						<div id="divGeneralAddEadid<s:property value="%{#stat.index}" />"
							style="display: inline;">

							<p style="text-align: center; font-weight: bold;">
								<s:property value="getText('content.message.addEADID')" />
							</p>
							<hr>
							<p>
								<br>
							</p>
							<input type="hidden"
								name="filesWithEmptyEadid[<s:property value="%{#stat.index}"/>].fileId"
								value="<s:property value="%{top.fileId}"/>" /> <input
								type="hidden"
								name="filesWithEmptyEadid[<s:property value="%{#stat.index}"/>].fileType"
								value="<s:property value="%{top.fileType}"/>" /> <input
								type="hidden"
								name="filesWithEmptyEadid[<s:property value="%{#stat.index}"/>].fileName"
								value="<s:property value="%{top.fileName}"/>" /> <input
								type="hidden"
								name="filesWithEmptyEadid[<s:property value="%{#stat.index}"/>].filePath"
								value="<s:property value="%{top.filePath}"/>" /> <input
								type="hidden"
								name="filesWithEmptyEadid[<s:property value="%{#stat.index}"/>].eadType"
								value="<s:property value="%{top.eadType}"/>" /> <input
								type="hidden"
								name="filesWithEmptyEadid[<s:property value="%{#stat.index}"/>].eadid"
								value="<s:property value="%{top.eadid}"/>" /> <input
								type="hidden"
								name="filesWithEmptyEadid[<s:property value="%{#stat.index}"/>].permId"
								value="<s:property value="%{top.permId}"/>" />

							<div id="divAddEadid<s:property value="%{#stat.index}" />">
								<!-- style="display:none;" -->

								<!--EAD file with no EADID keyup method over the textbox-->
								<input type="text" name="arrayneweadid"
									id="neweadidEmpty<s:property value="%{#stat.index}" />" size="30%"
									style="padding-left: 4px;" />

								<!--EAD file with no EADID -->
								<input type="button" style="display: inline;"
									id="SaveChangesButtonEmpty<s:property value="%{#stat.index}" />"
									name="SaveChangesButtonEmpty<s:property value="%{#stat.index}" />"
									onclick="getAndCheckEADIDavailability('<s:property value="%{#stat.index}" />','<s:property value='%{top.eadid}' />','<s:property value="%{top.fileId}" />', '<s:property value="%{top.eadType}" />', false)"
									value="<s:property value="getText('content.message.checkbutton')"/>" />
							</div>
							<p></p>
							<label
								id="resultChangeEADIDEmpty<s:property value="%{#stat.index}" />"></label>
							<select list="existingEADIDAnswersChoice"
								name="existingChangeEADIDAnswersEmpty" style="display: none;"
								id="existingChangeEADIDAnswersEmpty<s:property value="%{#stat.index}" />">
								<option value="KO">KO</option>
							</select> <br>

							<div
								id="divCancelOverwriteEADIDEmpty<s:property value="%{#stat.index}" />"
								style="display: none;">
								<label><s:property
										value="getText('content.message.OverwriteCancelEadid')" /></label> <select
									onchange="var iddivneweadidEmpty= 'neweadidEmpty' + '%{#stat.index}'; var neweadidEmpty= document.getElementById(iddivneweadidEmpty).value;CancelOverwriteExistingEADID(this, '%{#stat.index}', neweadidEmpty, true);"
									name="existingCancelOverwriteEADIDAnswers">
									<s:iterator value="existingFilesChoiceOverwriteCancelEADID"
										var="actionEFCOCEADID">
										<option value="<s:property value="#actionEFCOCEADID.key" />">
											<s:property value="#actionEFCOCEADID.value" />
										</option>
									</s:iterator>
								</select> <br>
							</div>
							<br>
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
						<img id="filesBlockedHCimage" src="images/expand/menos.gif" />
						<s:property value="getText('content.message.titlefilesblocked')" />
					</p>
					<hr>
					<p>
						<br>
					</p>
				</div>

				<div id="content_filesBlocked" style="display: inline;">
					<s:property
						value="getText('content.message.filesblocked.explanation')" />
					<br> <br> <br>
					<div id="titleListBlocked"
						style="text-align: left; display: inline;">
						<s:iterator value="filesBlocked" status="stat">
							<input type="hidden"
								name="filesBlocked[<s:property value="%{#stat.index}"/>].fileId"
								value="<s:property value="%{top.fileId}"/>" />
							<input type="hidden"
								name="filesBlocked[<s:property value="%{#stat.index}"/>].fileType"
								value="<s:property value="%{top.fileType}"/>" />
							<input type="hidden"
								name="filesBlocked[<s:property value="%{#stat.index}"/>].fileName"
								value="<s:property value="%{top.fileName}"/>" />
							<input type="hidden"
								name="filesBlocked[<s:property value="%{#stat.index}"/>].filePath"
								value="<s:property value="%{top.filePath}"/>" />
							<input type="hidden"
								name="filesBlocked[<s:property value="%{#stat.index}"/>].eadType"
								value="<s:property value="%{top.eadType}"/>" />
							<input type="hidden"
								name="filesBlocked[<s:property value="%{#stat.index}"/>].eadid"
								value="<s:property value="%{top.eadid}"/>" />
							<input type="hidden"
								name="filesBlocked[<s:property value="%{#stat.index}"/>].permId"
								value="<s:property value="%{top.permId}"/>" />

							<s:property value="%{(#stat.index+1) + '- ' + top.fileName}" />
							<br />
							<br />
						</s:iterator>
					</div>
				</div>
			</div>
		</s:if>
		<!--  <input type="button" class="mainButton" id="form_submit" value="<s:property value='getText("label.accept")' />" name="form_submit" onclick="checkEadIdAndSubmit()" />-->
		<input type="button" class="mainButton" id="form_submit"
			value="<s:property value='getText("label.accept")' />"
			name="form_submit" onclick="checkEadIdAndSubmit()"
			disabled="disabled" />
		<s:if test="filesWithEmptyEadid.size()>0 || existingFiles.size()>0">
		</s:if>
		<br> <br> <br> <br>
	</form>
</s:if>
<s:else>

	<div id="existingFilesList">
		<s:property value="getText('content.continue.contentmanager')" />
		<br>
	</div>
	<br>
	<br>
	<form id="content" method="get">
		<s:submit theme="simple" key="content.continue.contentmanager"
			action="contentmanager" />

	</form>
</s:else>
<script type="text/javascript">
			function stopRKey(evt) {
				var evt = (evt) ? evt : ((event) ? event : null);
				var node = (evt.target) ? evt.target : ((evt.srcElement) ? evt.srcElement : null);
				if ((evt.keyCode == 13) && (node.type=="text")) {
						return false;
					}
				}
			 document.onkeypress = stopRKey; 

    		 $(document).ready(function(){

	        	eadidarray.splice(0,eadidarray.length);
	        	recordidarray.splice(0, recordidarray.length);

	        	checkActiveButtonAccept();

	        	$("input[id^=neweadidRepeated]").each(function(){
					var inputElement = $(this).attr("id");
					inputElement = inputElement.replace(".", "\\.");
		        	$("#" + inputElement).on('input', function() {
		        		var eadid = inputElement.substring("neweadidRepeated".length, inputElement.length);
		        		activate(eadid, true);
		        	});
	        	});

	        	$("input[id^=neweadidEmpty]").each(function(){
					var inputElement = $(this).attr("id");
					inputElement = inputElement.replace(".", "\\.");
		        	$("#" + inputElement).on('input', function() {
		        		var eadid = inputElement.substring("neweadidEmpty".length, inputElement.length);
		        		activate(eadid, false);
		        	});
	        	});
	        });

	        function checkActiveButtonAccept() {
	        	var isOverwrite = true;
	        	var existsAdd = false;

	        	$("div#filesWithEADIDrepeated select[id^='existingFilesAnswers']").each(function(){
	        		if ($(this).val() != "Overwrite") {
	        			isOverwrite = false;
	        		}
	        	});

	        	$("div#filesWithEmptyEadid select[id^='existingFilesAnswers']").each(function(){
	        		if ($(this).val() == "Add EADID") {
	        			existsAdd = true;
	        		}
	        	});

	        	/* avoid showing wrong controls when maximize/minimize */
	        	$("div#filesWithEADIDrepeated select[id^='existingFilesAnswers']").each(function(){
	        		if ($(this).val() != "Change ID") {
	        			var titleListRepeatedId = $(this).parent().parent().attr("id");
						var EaDiD  = titleListRepeatedId.substring(17,titleListRepeatedId.length);
						var divgeneralname= "divChangeEadid" + EaDiD;
						document.getElementById(divgeneralname).style.display='none';
	        		}
	        	});

	        	if (isOverwrite && !existsAdd) {
		        	$("input#form_submit").removeAttr("disabled");
	        	}
	        }

			function activate(eadid, isRepeated) {
				// Replace the escaped name.
				if (eadid.indexOf("\.") != "-1")  {
					eadid = eadid.replace("\\.", ".");
				}
				
				var name = "";
				// Activate button check.
				if (isRepeated) {
					name = "Repeated";
				} else {
					name = "Empty";
				}

				document.getElementById("resultChangeEADID" + name + eadid).style.display='none';
				document.getElementById("SaveChangesButton" + name + eadid).disabled=false;

				//normal activate behavior
				$("input#form_submit").attr("disabled","disabled");
			}

	        function checkEadIdAndSubmit(){
				//control for values of the textbox
 	        	var exit=false;
				//values for the array with errors
				var value=0;
				//for each visible div means then have any kind of value
	        	$("div[id^='divChangeEadid']").each(function(){
	        		var divID = $(this).attr("id");
	        		if (document.getElementById(divID).style.display!='none') {
	        			//value of existing EADID
	        			var oldEadid = divID.substring("divChangeEadid".length);
	        			//each file has a control in a combobox "OK / KO""
	        			var id = $("select#existingChangeEADIDAnswersRepeated" + oldEadid).attr("id");
	        			var control;
	        			if (id != undefined) {
	        				control=$("select#existingChangeEADIDAnswersRepeated" + oldEadid).val();
	        			} else {
	        				control=$("select#existingChangeEADIDAnswersEmpty" + oldEadid).val();
	        			}
	        			var flag= (control!="KO");
	        			if(!flag){
	        				exit=true;
	        				filesWithErrors[value++] = oldEadid;
	        			}
	        		}
	        	}); 
	        	
	        	if (!exit){
		        	$("form#overwriteexistingfiles").attr("action","overwriteexistingfiles.action");//action="overwriteexistingfiles"
		        	$("form#overwriteexistingfiles").submit();
	        	}
 	        	else{
        			var strErr = '<s:property value="getText('content.message.filesEADIDerror')"/>' + " " + filesWithErrors;
        			alertAndDecode(strErr);
	        	} 
	        }
	        	        
	        var eadidarray = new Array();
	        var recordidarray = new Array();
	        var filesWithErrors = new Array();
			//check all inputs to check if the EADID or RECORDID is already used, in case no, remove it from the array
			var eadidarrayInText=new Array();
			var recordidarrayInText = new Array();

			
	        function changeEADID(text,eadid,method, isRepeated)
			{
				var textvalue = text.options[text.selectedIndex].value;
				var buttonid;
				if (isRepeated) {
					buttonid= "SaveChangesButtonRepeated" + eadid;
				} else {
					buttonid= "SaveChangesButtonEmpty" + eadid;
				}
				
				if (method == "Add") {
					if (textvalue == "Add EADID") {
			        	var divname="divGeneralAddEadid" + eadid;
						document.getElementById(divname).style.display='inline';	
						document.getElementById(buttonid).style.display='inline';
						if (isRepeated) {
					   		$("input#SaveChangesButtonRepeated" + eadid).removeAttr("disabled");
						} else {
							$("input#SaveChangesButtonEmpty" + eadid).removeAttr("disabled");
						}
						var divgeneralname= "divAddEadid" + eadid;
						document.getElementById(divgeneralname).style.display='inline';		
						$("input#form_submit").attr("disabled","disabled");   // Disable accept button.
					}else { //Cancel
						var divname="divGeneralAddEadid" + eadid;
						if (isRepeated) {
					    	$("input#SaveChangesButtonRepeated" + eadid).attr("disabled","disabled");
						} else {
							$("input#SaveChangesButtonEmpty" + eadid).attr("disabled","disabled");
						}
						var divgeneralname= "divAddEadid" + eadid;
						document.getElementById(divgeneralname).style.display='none';
						var enableAccept = true;
						$("input[id^='SaveChangesButton']").each(function(){
							var disabled = $(this).attr("disabled");
							if (disabled == null || (disabled != "" && disabled != "disabled")) {
								enableAccept = false;
							}
						});
						if (enableAccept) {
							$("input#form_submit").removeAttr("disabled");
						} 
						//clean the value in the eadidarray or recordidarray
						var textToRemove;
						if (isRepeated) {
							textToRemove = $("input#neweadidRepeated" + eadid).val();
						} else {
							textToRemove = $("input#neweadidEmpty" + eadid).val();
						}
						var indexLabel = parseInt(eadid);

						// Check the correct array to recover.
						var fileType = $("div#" + divname + " input[name$='.eadType']").val();
						var indexArray;
						if (fileType == "EAC-CPF") {
							indexArray = recordidarray.indexOf(textToRemove);
						} else {
							indexArray = eadidarray.indexOf(textToRemove);
						}
						if (indexLabel > -1 && indexArray > -1){
							var label;
							if (isRepeated) {
								label = $("label#resultChangeEADIDRepeated"+indexLabel);
							} else {
								label = $("label#resultChangeEADIDEmpty"+indexLabel);
							}
					    	if(label!=null && label.length){
					    		var color = label.text();
					    		if(color=="<s:property value="getText('content.message.EadidAvailable')" />"){
									if (fileType == "EAC-CPF") {
										recordidarray.splice(indexArray,1);
									} else {
						    			eadidarray.splice(indexArray,1);
									}
					    		}
						     }
						}
				
						document.getElementById(divname).style.display='none';
						document.getElementById(buttonid).setAttribute("disabled","disabled");
						document.getElementById(buttonid).style.display='none';
						//clean textbox
						var textBoxName;
						if (isRepeated) {
							textBoxName="neweadidRepeated" + eadid;
						} else {
							textBoxName="neweadidEmpty" + eadid;
						}
						document.getElementById(textBoxName).value = "";
						//clean label
						if (isRepeated) {
							$("label#resultChangeEADIDRepeated" + eadid).hide();
						} else {
							$("label#resultChangeEADIDEmpty" + eadid).hide();
						}
					}//end Cancel
				
				} //end Add
				else if (method=="Change") {
					//files with existing EADID
					if (textvalue == "Change ID") {
		        		var divname="divGeneralChangeEadid" + eadid;
						document.getElementById(divname).style.display='inline';
						document.getElementById(buttonid).style.display='inline';
						var divgeneralname= "divChangeEadid" + eadid;
						document.getElementById(divgeneralname).style.display='';
						var textboxid;
						if (isRepeated) {
							textboxid = "neweadidRepeated" + eadid;
						} else {
							textboxid = "neweadidEmpty" + eadid;
						}
						document.getElementById(textboxid).focus();
						// Disable accept button.
						$("input#form_submit").attr("disabled","disabled");
						// Enable check button.
						document.getElementById(buttonid).removeAttribute("disabled");
					}
					else {
						//overwrite or cancel
			       		var divname="divGeneralChangeEadid" + eadid;
						document.getElementById(divname).style.display='none';
						document.getElementById(buttonid).setAttribute("disabled","disabled");
						document.getElementById(buttonid).style.display='none';
						var divgeneralname= "divChangeEadid" + eadid;
						document.getElementById(divgeneralname).style.display='none';
						// Check if is necessary to enable accept button.
						var enableAccept = true;
						$("input[id^='SaveChangesButton']").each(function(){
							var id = $(this).attr("id");
							var disabled = document.getElementById(id).getAttribute("disabled");
							if (disabled == null || (disabled != "" && disabled != "disabled")) {
								enableAccept = false;
							}
						});
						if (enableAccept) {
							$("input#form_submit").removeAttr("disabled");
						}
						//clean all values from the array when cancel
						// Check the correct array to recover.
						var fileType = $("div#" + divname + " input[name$='.eadType']").val();
						if (fileType == "EAC-CPF") {
							recordidarray.splice(0,recordidarray.length);
						} else {
							eadidarray.splice(0,eadidarray.length);
						}
						//clean textbox
						var textBoxName;
						if (isRepeated) {
							textBoxName="neweadidRepeated" + eadid;
						} else {
							textBoxName="neweadidEmpty" + eadid;
						}
						document.getElementById(textBoxName).value = "";
						//clean label
						var labelName;
						if (isRepeated) {
							labelName="resultChangeEADIDRepeated" + eadid;
						} else {
							labelName="resultChangeEADIDEmpty" + eadid;
						}
						document.getElementById(labelName).style.display='none';
						//clean text and combo too
						var labelCancelName;
						if (isRepeated) {
							labelCancelName="divCancelOverwriteEADIDRepeated" + eadid;
						} else {
							labelCancelName="divCancelOverwriteEADIDEmpty" + eadid;
						}
						document.getElementById(labelCancelName).style.display='none';
					}
				}
			}
			
			function CancelOverwriteExistingEADID(text, eadid, neweadid, isRepeated) {
				var textvalue = text.options[text.selectedIndex].text;
				var labelanswermessage;
				if (isRepeated) {
					labelanswermessage = "resultChangeEADIDRepeated" + eadid;
				} else {
					labelanswermessage = "resultChangeEADIDEmpty" + eadid;
				}
				if (textvalue == "Cancel"){
					//Display you will cancel this operation for this file.						
					document.getElementById(labelanswermessage).innerHTML="You will cancel this operation for this file";
				}
				else {
					//Display you will overwrite the file with the eadid selected for this one.
					document.getElementById(labelanswermessage).innerHTML="You will overwrite the file with the eadid selected for this one.";										
				}
				document.getElementById(labelanswermessage).style.display='block';
			}
						
			function checkEADIDavailability(oldeadid, neweadid, fileId, type, indexRepeated, isRepeated) {
				var pattern = /^[a-zA-Z0-9\.\:\_\-]*$/i;
				if ((type == "EAC-CPF" && neweadid.toUpperCase().match(pattern))
						|| type == "Finding Aid"
						|| type == "Holdings Guide"
						|| type == "Source Guide"
                                                || type == "EAD3") {
					$.getJSON("${pageContext.request.contextPath}/generateEadidResponseJSON.action", 
						{ eadid: oldeadid, 
					      neweadid: neweadid, 
					      fileId: fileId,
					      type: type}, 
					    function(dataResponse)
						{
							//Show the message.
							var labelanswermessage;
							if (isRepeated) {
								labelanswermessage = "resultChangeEADIDRepeated" + indexRepeated;
							} else {
								labelanswermessage = "resultChangeEADIDEmpty" + dataResponse.eadid;
							}
							document.getElementById(labelanswermessage).style.display='block';
							var object = document.getElementById(labelanswermessage);
							object.innerHTML=dataResponse.message;

							// Check the correct array to recover.
							var id = $("label#" + labelanswermessage).parent().attr("id");
							var fileType = $("div#" + id + " input[name$='.eadType']").val();
							var currentArray;
							if (fileType == "EAC-CPF") {
								currentArray = recordidarray;
							} else {
								currentArray = eadidarray;
							}

							var sizeValue = currentArray.length;
							
							if(sizeValue>0){
			                  //remove all the values in the array
			                  currentArray.splice(0, currentArray.length);
			                }
							$("input[id^='neweadid']").each(function(i,value){
			                	//for each input keep the value if it is not repeated and not empty
			                  $(value).val($.trim($(value).val())); //remove unussed whitespaces

							  var innerId = $(this).parent().parent().attr("id");
			                  var innerFileType = $("div#" + innerId + " input[name$='.eadType']").val();

			                  var textInput= $(value).val();
			                  if (innerFileType == fileType) {
				                   if(currentArray.length==0){
				                     if(textInput!=""){
							            currentArray.push(textInput);
						             }
				                   }else{
				                    if (textInput!=""){
					                    var index = currentArray.indexOf(textInput);
					                    if (index > -1){
					                      //the value is repeated
					                      if (dataResponse.existingChangeEADIDAnswers!= "KO"){
											 dataResponse.existingChangeEADIDAnswers= "KO";
											 document.getElementById(labelanswermessage).innerHTML=dataResponse.komessage;
										  }
					                    }else{
					                    	//keep the value in the currentArray
					                      currentArray.push(textInput);
					                    }
				                    }
				                   }
								}

			                   // Set the current array to the correct array.
								if (innerFileType == "EAC-CPF") {
									recordidarray = currentArray;
								} else {
									eadidarray = currentArray;
								}
			                });
														
							$("input#form_submit").attr("disabled","disabled");
							if (dataResponse.existingChangeEADIDAnswers == "OK") {
								//Change the value of the select OK or KO
								dataResponse.existingChangeEADIDAnswers= "OK";
								document.getElementById(labelanswermessage).style.color="green";
								document.getElementById(labelanswermessage).style.font.bold = "true";
								var selectanswer;
								if (isRepeated) {
									selectanswer = "existingChangeEADIDAnswersRepeated" + indexRepeated;
								} else {
									selectanswer = "existingChangeEADIDAnswersEmpty" + dataResponse.eadid;
								}
								var select = document.getElementById(selectanswer);
								select.options[0] = new Option(dataResponse.existingChangeEADIDAnswers, dataResponse.existingChangeEADIDAnswers);
								var checkavailabilitybutton;
								if (isRepeated) {
									checkavailabilitybutton = "SaveChangesButtonRepeated" + indexRepeated;
								} else {
									checkavailabilitybutton = "SaveChangesButtonEmpty" + dataResponse.eadid;
								}
								/*Do not hide the check availavility button*/
								var div;
								if (isRepeated) {
									div= "divCancelOverwriteEADIDRepeated" + indexRepeated;
								} else {
									div= "divCancelOverwriteEADIDEmpty" + dataResponse.eadid;
								}							
								document.getElementById(div).style.display='none';
								if (isRepeated) {
									document.getElementById("SaveChangesButtonRepeated" + indexRepeated).disabled=true;
								} else {
									document.getElementById("SaveChangesButtonEmpty" + oldeadid).disabled=true;
								}
							    var enableAccept = true;
								$("input[id^='SaveChangesButton']").each(function(){
									var disabled = $(this).attr("disabled");
									if (disabled == null || (disabled != "" && disabled != "disabled")) {
										enableAccept = false;
									}
								});
								if (enableAccept) {
									$("input#form_submit").removeAttr("disabled");
								} 
							}
							else if (dataResponse.existingChangeEADIDAnswers == "KO") {
								document.getElementById(labelanswermessage).style.color="red";
								document.getElementById(labelanswermessage).style.font.bold = "true";
								var selectanswer;
								if (isRepeated) {
									selectanswer = "existingChangeEADIDAnswersRepeated" + indexRepeated;
								} else {
									selectanswer = "existingChangeEADIDAnswersEmpty" + dataResponse.eadid;
								}
								var select = document.getElementById(selectanswer);
								select.options[0] = new Option(dataResponse.existingChangeEADIDAnswers, dataResponse.existingChangeEADIDAnswers);
								var div;
								if (isRepeated) {
									div= "divCancelOverwriteEADIDRepeated" + indexRepeated;
								} else {
									div= "divCancelOverwriteEADIDEmpty" + dataResponse.eadid;
								}								
								document.getElementById(div).style.display='none';
								$("input#form_submit").attr("disabled","disabled");
							}
						}
			    	);
				}else{
					$("input").filter(function() {
						if($(this).val()==neweadid){
							var eadid = $(this).attr("id");
							if(eadid.length>"neweadidRepeated".length){
								eadid = eadid.substring("neweadidRepeated".length);
								var targetText = "<s:property value="getText('content.message.errorinvalidcharacters')" />";
								$("input#SaveChangesButtonRepeated" + eadid).attr("disabled","disabled");
								$("label#resultChangeEADIDRepeated" + eadid).text(targetText);
								$("label#resultChangeEADIDRepeated" + eadid).css("color","red");
								$("label#resultChangeEADIDRepeated" + eadid).css("font-weight","bold");
								$("label#resultChangeEADIDRepeated" + eadid).show();
							}
						}
					});
				}
			}
			
			
			function getAndCheckEADIDavailability(index, eadid, fileId, type, isRepeated){
				var iddivneweadid = "";
				var indexRepeated = -1;
				if (eadid != "") {
					iddivneweadid= 'neweadidRepeated' + eadid ;
					indexRepeated = eadid;
					eadid = $("input#textEADIDRepeated" + indexRepeated).val();
				} else {
					iddivneweadid= 'neweadidEmpty' + index ;
					eadid=index;
				}
				var neweadid= document.getElementById(iddivneweadid).value;
				checkEADIDavailability(eadid, neweadid, fileId, type, indexRepeated, isRepeated);
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
					/* Control which value has the select option */
					checkActiveButtonAccept();
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
<div style="margin-bottom: 30px;"></div>
