<%@page import="java.util.Iterator"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.List"%>
<%@page import="com.opensymphony.xwork2.ActionContext"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
    <div id="hasElementChanged" style="visibility:hidden;"><s:property value="hasElementChanged"/></div>
    <div id="aLandscape" class="aLandscape">
		<div id="divCountry">
			<p style="text-align: center;"><s:property value="country"/> <s:property value="getText('al.message.archivallandscape')" /></p><hr/>
		<s:if test="{AL}">
			<s:form name="form" method="post" theme="simple">
			<div id="divError" class="divError">
				<s:actionmessage/>
			</div>
			<div id="jActions" style="display:none;">
				<div id="filterSelectContainer" class="filterContainer">
					<div id="addDiv" class="divAction" ><s:property value="getText('al.message.addtolist')" /></div>
				</div>
				<div id="actionsSelectDivContainer" class="filterContainer">
					<div id="actionsButtons">
						<div id="deleteDiv" class="divAction"><s:property value="getText('al.message.modify')" /></div>
						<div id="moveUpDiv" class="divAction"><s:property value="getText('al.message.up')" /></div>
						<div id="moveDownDiv" class="divAction"><s:property value="getText('al.message.down')" /></div>
						<div id="unselectDiv" class="divAction">Unselect List Elements</div>
					</div>
				</div>
				<div id="divGroupNodesContainer" class="filterContainer">
					<div id="changeNodeDiv" class="divAction"><s:property value="getText('al.message.changeGroup')" /></div>
				</div>
				<div id="showEditLanguagesDiv" class="filterContainer">
					<div id="showLanguagesDiv" class="divAction"><s:property value="getText('al.message.showalternativenames')" /></div>
					<div id="editDiv" class="divAction"><s:property value="getText('al.message.edittarget')" /></div>
				</div>
				<div id="stateDiv" class="divStatus"> <s:actionmessage/></div>
				<div id="divForSubmitButtom" class="filterContainer" style="border:0px;margin-top:5px;margin-bottom:30px;"> </div>
			</div>
			<script type="text/javascript">
				function updateStatusWindow(text){
					$("#stateDiv").html(text);
					//$("body").append('<div id="divBlock" style="background-color:white;top:0px;left:0px;position:fixed;width:100%;height:100%;opacity:0.7;z-index:2;"> </div>');
					$("#stateDiv").fadeIn('slow');
					setTimeout('$("#stateDiv").fadeOut("slow")',2000);
					//setTimeout('$("#divBlock").remove()',1500);
				}
				$("#changeNodeDiv").click(function(){
					var selectedIndex = $("select[name=ALElement] option:selected").index();
					var elementName = $("select[name=ALElement] option:selected").text();
					var elementValue = $("select[name=ALElement] option:selected").val();
					var father = $("#father option:selected").text();
					var fatherId = $("#father option:selected").val();
					if(selectedIndex!=-1){
						$.post('changeNode.action',{"textAL":"","element":"","father" :fatherId,"ALElement":$("select[name=ALElement] option:selected").val()},function(response){
							$("body").html(response);
							$("select[name=ALElement] option[value="+elementValue+"]").attr('selected','selected');
							if($("#changeNode_").length>0){
								updateStatusWindow('<span style="font-weight:bold;">'+elementName+'</span> <s:property value="getText('al.message.institutionhascontentindexed')"/>!');
							}else{
								updateStatusWindow('<span style="font-weight:bold;">'+elementName+'</span> <s:property value="getText('al.message.nodechangedto')"/> '+father+'!');
							}
							$("#editDiv").show();
							$("#showEditLanguagesDiv").show();
							$("#actionsButtons").show();
							$("#divGroupNodesContainer").show();
						});
					}else{
						updateStatusWindow("<s:property value='getText("al.message.noElementSelected")' />");
					}
				});
				$("#addDiv").click(function(){
					var selectedIndex = $("select[name=ALElement] option:selected").index();
					var elementName = $("select[name=ALElement] option:selected").text();
					var elementValue = $("select[name=ALElement] option:selected").val();
					var textAL = $("#textAL").val();
					var element = $("#element option:selected").val();
					var selectedLang = $("#selectedLang option:selected").val();
					if(selectedIndex!=-1){
						$.post('editAL.action','&selectedLang='+selectedLang+'&element='+element+'&textAL='+textAL+'&ALElement='+$("select[name=ALElement] option:selected").val(),function(response){
							$("body").html(response);
							$("select[name=ALElement] option[value="+elementValue+"]").attr('selected','selected');
							updateStatusWindow('<span style="font-weight:bold;">'+elementName+'</span> <s:property value="getText('al.message.nodechangedto')"/> '+father+'!');
						});
					}else{
						//updateStatusWindow("<s:property value='getText("al.message.noElementSelected")' />");
						$.post('editAL.action','&selectedLang='+selectedLang+'&element='+element+'&textAL='+textAL,function(response){
							$("body").html(response);
							$("select[name=ALElement] option[value="+elementValue+"]").attr('selected','selected');
                            if($("#hasElementChanged:contains('true')").length > 0) {
							    updateStatusWindow('<span style="font-weight:bold;">'+textAL+"</span> <s:property value="getText('al.message.elementEdited')" />");
                            } else {
                                updateStatusWindow('<span style="font-weight:bold;">'+textAL+"</span> <s:property value="getText('al.message.existsAlready')" />");
                            }
						});
					}
				});
				$("#moveUpDiv").click(function(){
					var selectedIndex = $("select[name=ALElement] option:selected").index();
					var elementName = $("select[name=ALElement] option:selected").text();
					var elementValue = $("select[name=ALElement] option:selected").val();
					if(selectedIndex!=-1){
						$.post('moveUpAL.action',{"textAL":"","element":"","ALElement": $("select[name=ALElement] option:selected").val()},function(response){
							$("body").html(response);
							$("select[name=ALElement] option[value="+elementValue+"]").attr('selected','selected');
							updateStatusWindow('<span style="font-weight:bold;">'+elementName+'</span> <s:property value="getText('al.message.movedup')"/>');
							$("#showLanguagesDiv").show();
							$("#actionsButtons").show();
							$("#showEditLanguagesDiv").show();
							$("#divGroupNodesContainer").show();
						});
					}else{
						updateStatusWindow("<s:property value='getText("al.message.noElementSelected")' />");
					}
				});
				$("#moveDownDiv").click(function(){
					var selectedIndex = $("select[name=ALElement] option:selected").index();
					var elementName = $("select[name=ALElement] option:selected").text();
					var elementValue = $("select[name=ALElement] option:selected").val();
					if(selectedIndex!=-1){
						$.post('moveDownAL.action',{"textAL":"","element":"" ,"ALElement": $("select[name=ALElement] option:selected").val()},function(response){
							$("body").html(response);
							$("select[name=ALElement] option[value="+elementValue+"]").attr('selected','selected');
							updateStatusWindow('<span style="font-weight:bold;">'+elementName+'</span> <s:property value="getText('al.message.moveddown')"/>');
							$("#showLanguagesDiv").show();
							$("#actionsButtons").show();
							$("#showEditLanguagesDiv").show();
							$("#divGroupNodesContainer").show();
						});
					}else{
						updateStatusWindow("<s:property value='getText("al.message.noElementSelected")' />");
					}
				});
				$("#deleteDiv").click(function(){
					var elementName = $("select[name=ALElement] option:selected").text();
					if($("select[name=ALElement] option:selected").index()!=-1){
						$.post('modifyStructure.action','ALElement='+$("select[name=ALElement] option:selected").val(),function(response){
							$("body").html(response);
							updateStatusWindow('<span style="font-weight:bold;">'+elementName+'</span> <s:property value="getText('al.message.wasdeleted')"/>');
						});
					}else{
						updateStatusWindow("<s:property value='getText("al.message.noElementSelected")' />");
					}
				});
				
				$("#unselectDiv").click(function(){
					var elementName = $("select[name=ALElement] option:selected").text();
					$("select[name=ALElement]")[0].selectedIndex = -1;
					updateStatusWindow('<span style="font-weight:bold;">'+elementName+'</span> <s:property value="getText('al.message.unselected')"/>');
					$("#actionsButtons").hide();
					$("#showEditLanguagesDiv").hide();
					$("#divGroupNodesContainer").hide();
				});
				
				$("#showLanguagesDiv").click(function(){
					var selectedIndex = $("select[name=ALElement] option:selected").index();
					if(selectedIndex!=-1){
						$.post('showLanguages.action','ALElement='+$("select[name=ALElement] option:selected").val(),function(response){
							$("body").html(response);
							$("select[name=ALElement]")[0].selectedIndex = selectedIndex;
							updateStatusWindow('Show alternatives names done!');
							$("#showLanguagesDiv").hide();
							$("#editDiv").show();
							$("#showEditLanguagesDiv").show();
							$("#actionsButtons").show();
							$("#divGroupNodesContainer").show();
						});
					}else{
						updateStatusWindow("<s:property value='getText("al.message.noElementSelected")' />");
					}
				});
				$("#editDiv").click(function(){
					var elementName = $("select[name=ALElement] option:selected").text();
					var selectedIndex = $("select[name=ALElement] option:selected").index();
					if($("select[name=ALElement] option:selected").index()!=-1){
						$.post('editElement.action','ALElement='+$("select[name=ALElement] option:selected").val(),function(response){
							$("body").html(response);
							$("select[name=ALElement]")[0].selectedIndex = selectedIndex;
							updateStatusWindow('<span style="font-weight:bold;">'+elementName+'</span> <s:property value="getText('al.message.alternativesnamesdisplayed')"/>');
							//$("#editDiv").show();
							$("#showLanguagesDiv").hide();
							$("#showEditLanguagesDiv").show();
							$("#actionsButtons").show();
							$("#divGroupNodesContainer").show();
							$("#editTargetSubmit").remove();
							$("#editTargetCancel").remove();
							$("#editLanguagesDiv").append("<div id='editTargetSubmitDiv' class='divAction' ><s:property value="getText('al.message.editalternativenames')"/></div>");
							$("#editLanguagesDiv").append("<div id='editTargetCancelDiv' class='divAction' ><s:property value="getText('al.message.canceledittarget')"/></div>");
							$("#editTargetSubmitDiv").click(function(){
								$.post('editTarget.action',{'languageTarget':$("#languageTarget").val(),'target':$("#target").val(),'ALElement':$("select[name=ALElement]").val()},function(response){
									$("body").html(response);
									document.getElementById("ALElement").selectedIndex = selectedIndex;
									updateStatusWindow('<s:property value="getText('al.message.editedalternativesnamesdone')"/>');
									$("#showLanguagesDiv").hide();
									$("#showEditLanguagesDiv").show();
									$("#actionsButtons").show();
									$("#divGroupNodesContainer").show();
									$("#editDiv").show();
								});
							});
							$("#editTargetCancelDiv").click(function(){
								$("#editLanguagesDiv").remove();
								$("#editTarget").attr("disabled","disabled");
							});
						});
					}else{
						updateStatusWindow("<s:property value='getText("al.message.noElementSelected")' />");
					}
				});
				$("body").ready(function(){
					$("#jActions").show();
					$("#filterSelectContainer").append($("#filterSelect"));
					$("#editAL_editAL").remove();
					$("#editAL_modifyStructure").remove();
					$("#filterSelectContainer").append($("#addDiv"));
					$("#divGroupNodesContainer").append($("#divGroupNodes"));
					$("#submitFormGroupNodes").remove();
					$("#divGroupNodesContainer").append($("#changeNodeDiv"));
					$("#selectListActionsDiv").remove();
					$("#divSelectListActionsDiv").remove();
					$("#actionsSelectDivContainer").append($("#selectOnlyListElements"));
					$("#actionsSelectDivContainer").append($("#actionsButtons"));
					$("#showEditLanguagesDiv").append($("#showAlternativeNamesDiv"));
					$("#showEditLanguagesDiv").append($("#editLanguagesDiv"));
					$("#divForSubmitButtom").append($("#finalSubmitButton")); 
					$("#selectOnlyListElements").css("margin-bottom","0px");
					$("#editDiv").hide();
					if($("select[name=ALElement] option:selected").index()==-1){
						$("#actionsButtons").hide();
						$("#showEditLanguagesDiv").hide();
						$("#divGroupNodesContainer").hide();
						$("select[name='ALElement']").change(function(){
							$("#showLanguagesDiv").show();
							$("#actionsButtons").show();
							$("#showEditLanguagesDiv").show();
							$("#divGroupNodesContainer").show();
							$("#showAlternativeNamesDiv").hide();
							$("#editDiv").hide();
							$("#editLanguagesDiv").hide();
						});
						if($("#submitDeleteTarget").is('*')){
							$("#submitDeleteTarget").remove();
							$("#showAlternativeNamesDiv").append("<div id='submitDeleteTargetDiv' class='divAction' ><s:property value="getText('al.message.deletetarget')"/></div>");
							$("#submitDeleteTargetDiv").click(function(){
								var editTargetValue=$("#editTarget").val();
								var selectedIndex = $("select[name=ALElement] option:selected").index();
								var selectedIndex2 = $("#editTarget option:selected").index();
								if(selectedIndex2!=0){
									$.post('deleteTarget.action',{'editTarget':editTargetValue,'ALElement':$("select[name=ALElement]").val()},function(response){
										$("body").html(response);
										updateStatusWindow("<span style=\"font-weight:bold;\">"+editTargetValue+"</span> "+'<s:property value="getText('al.message.alternativenameremoved')" />');
										document.getElementById("ALElement").selectedIndex = selectedIndex;
										$("#showLanguagesDiv").hide();
										$("#actionsButtons").show();
										$("#divGroupNodesContainer").show();
									});
								}else{
									updateStatusWindow("<span style=\"font-weight:bold;\">"+editTargetValue+"</span> "+'<s:property value="getText('al.message.cannotremovefirstalternativename')" />');
									document.getElementById("editTarget").selectedIndex = -1;
								}
							});
						}
					}
				});
				function showcurtain(){
					$("#curtain").show();
					};
			</script>
			
			<div id="filterSelect">
				<div class="firstFilterDiv">
					<label for="textAL"><s:property value="getText('al.message.name')" />:</label>
					<input type="text" name="textAL" id="textAL" class="inputTextBar" />
				</div>
				<div>
					<div style="float:left;">
						<div class="secondFilterDiv">
							<label for="element" style="padding-right:4px;"><s:property value="getText('al.message.element')" />:</label>
							<select name="element" id="element">
								<option value="series" ><s:property value="getText('al.message.series')"/></option>
								<option selected="selected" value="file"><s:property value="getText('al.message.file')"/></option>
							</select>
						</div>
						<div class="secondFilterDiv">
							<label for="selectedLang"><s:property value="getText('al.message.selectlanguage')"/>:</label>
							<select name="selectedLang" id="selectedLang" >
								<s:iterator var="row" value="langList">
									<s:if test='#row.getIsoname().toLowerCase().equals("eng")'>
										<option selected="selected" value="<s:property value="#row.getIsoname().toLowerCase()"/>"><s:property value="#row.getLname()" /></option>
									</s:if>
									<s:else>
										<option value="<s:property value="#row.getIsoname()"/>"><s:property value="#row.getLname()" /></option>
									</s:else>
								</s:iterator>
							</select>
							<input type="submit" class="inputSubmitAL" id="editAL_editAL" name="action:editAL" value="<s:property value="getText('al.message.addtolist')" />"/>
						</div>
					</div>
				</div>
			</div>
			<div id="filteList" class="filterListAL">
				<s:if test="AL.size()==0">
					<s:property value="getText('al.message.noelements')" />
				</s:if>
				<s:elseif test="AL.size()<2">
						<div class="divFilterThree">
							<div class="secondFilterDiv">
								<label for="ALElement" class="ALElementLabel"><s:property value="getText('al.message.listal')" /></label>
								<select id="selectOnlyListElements" class="selectListElementsAL" name="ALElement" id="ALElement" size="2">
				</s:elseif>
				<s:else>
						<div id="selectOnlyListElements" class="divSelectOnlyListElements">
							<label for="ALElement" class="ALElementLabel"><s:property value="getText('al.message.listal')" /></label>
							<select class="divSelectOnlyListElements2" name="ALElement" id="ALElement" size="<s:property value="AL.size()" />">
				</s:else>								
							<s:iterator var="row" value="AL">
								<option style="text-align:justify;" value="<s:property value="#row.id" />" ><s:property value="#row.name" /></option>
							</s:iterator>
					<s:if test="AL.size()>0">
							</select>
							<s:if test="elementLanguages.size()>0">
								<div id="showAlternativeNamesDiv" style="float:left;width:100%;">
									<p><s:property value="getText('al.message.alternativenames')" /></p>
									<p class="ALP">
										<s:if test="%{edit==false || elementLanguages.size()<2}">
											<select id="editTarget" name="editTarget" size="<s:property value="elementLanguages.size()" />" disabled="disabled">
										</s:if>
										<s:else>
											<select id="editTarget" name="editTarget" size="<s:property value="elementLanguages.size()" />" >
										</s:else>
												<s:iterator status="stat" var="rowLanguage" value="elementLanguages">
													<s:if test="#stat.index==0">
														<s:set name="firstANCreated" value="#rowLanguage.substring(0,3)" /> 
													</s:if>
													<option value="<s:property value="#rowLanguage" />"><s:property value="#rowLanguage" /></option>
												</s:iterator>
											</select>
									</p>
								</div>
								<s:if test="%{edit==true}">
								<div id="editLanguagesDiv" style="float:left;width:100%;">
									<s:if test="elementLanguages.size()>1">
									<p><input type="submit" id="submitDeleteTarget" name="action:deleteTarget" value="<s:property value="getText('al.message.deletetarget')"/>"/></p>
									</s:if>
									<p class="ALP2"><label for="target" style="float:left;"><s:property value="getText('al.message.anwritelanguage')"/>: </label><input type="text" id="target" name="target" /></p>
									<label for="languageTarget"><s:property value="getText('al.message.anselectlanguage')"/>: </label>
									<select id="languageTarget" name="languageTarget">
										<s:iterator var="row" value="langList">
											<s:if test='#row.getIsoname().toLowerCase().equals("eng")'>
												<s:if test="%{!(#firstANCreated.equals(#row.getIsoname().toLowerCase()))}">
													<option selected="selected" value="<s:property value="#row.getIsoname().toLowerCase()"/>"><s:property value="#row.getLname()" /></option>
												</s:if>
											</s:if>
											<s:else>
												<s:if test="%{!(#firstANCreated.equals(#row.getIsoname().toLowerCase()))}">
													<option value="<s:property value="#row.getIsoname().toLowerCase()"/>"><s:property value="#row.getLname()" /></option>
												</s:if>
											</s:else>
										</s:iterator>
									</select>
									<p><input id="editTargetSubmit" type="submit" name="action:editTarget" value="<s:property value="getText('al.message.editalternativenames')"/>" /> <input id="editTargetCancel" type="submit" name="action:editAL" value="<s:property value="getText('al.message.canceledittarget')"/>" /></p>
								</div>
								</s:if>
							</s:if>
						</div>
						<div id="divSelectListActionsDiv">
							<p><input type="submit" id="moveUpAL" name="action:moveUpAL" value="<s:property value="getText('al.message.up')"/>" /></p>
							<p><input type="submit" id="moveDownAl" name="action:moveDownAL" value="<s:property value="getText('al.message.down')"/>" /></p>
							<p><input type="submit" id="seeLanguagesSubmit" name="action:showLanguages" value="<s:property value="getText('al.message.showalternativenames')"/>" /></p>
							<s:if test="elementLanguages.size()>0">
								<p>
									<input type="submit" id="seeLanguagesSubmit" name="action:editElement" value="<s:property value="getText('al.message.editalternativenames')"/>" />
								</p>
							</s:if>
						</div>
						<s:if test="groupList!=null && groupList.size()>0">
						<div id="divGroupNodes" style="float:left;margin-top:10px;">
							<p><label for="father" style="float:left;"><s:property value="getText('al.message.selectGroup')" />: </label>
							<s:if test="groupList.size()>0" >
								<select id="father" name="father" size="1" >
								<s:iterator var="row" value="groupList" >
									<option value="<s:property value="#row.id" />"><s:property value="#row.name" /></option>
								</s:iterator>
								</select>
							</s:if>
							</p>
							<%--<s:select id="father" name="father" list="groupList" size="1" theme="simple" />--%>
							<div class="ALP">
								<s:submit action="changeNode" id="submitFormGroupNodes" key="al.message.changeGroup" />
							</div>
						</div>
					</s:if>
					</div>
					
					<div>
						<input class="inputButtonSubmitAL2" type="submit" id="editAL_modifyStructure" name="action:modifyStructure" value="<s:property value="getText('al.message.modify')"  />"/>
						</s:if>
						<input id="finalSubmitButton" class="inputButtonSubmitAL" type="submit" id="editAL_feditAL" onclick="showcurtain();"  name="action:feditAL" value="<s:property value="getText('al.message.finish')" />"/>
					</div>
				</s:form>
				</div>
			</s:if>
	</div>
			
	<!-- BEGIN - Curtain div hidden -->
		<div id="curtain" class="curtainAL" align="center" style="display:none;"> 
			<div style="width:100%; height:50%; float:left;">&nbsp;</div>
			<div style="width:100%; float:left;">
				  <div style="font-weight: bold;">					
					<s:property value="getText('content.message.processwaiting')"/>
				  </div>
			</div>
		</div>
