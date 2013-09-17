<%@page import="java.util.Iterator"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.List"%>
<%@page import="com.opensymphony.xwork2.ActionContext"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
    <div id="hasElementChanged" style="visibility:hidden;"><s:property value="hasElementChanged"/></div>
    <div id="aLandscape" class="aLandscape">
		<div id="divCountry">
			<p id="titleCountry"><s:property value="country"/> <s:property value="getText('al.message.archivallandscape')" /></p><hr/>
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
					<div id="actionWarning"></div>
						<div id="moveUpDiv" class="divAction"><s:property value="getText('al.message.up')" /></div>
						<div id="moveDownDiv" class="divAction"><s:property value="getText('al.message.down')" /></div>
						<div id="unselectDiv" class="divAction"><s:property value="getText('al.message.unselect')" /></div>
						<div id="deleteDiv" class="divAction"><s:property value="getText('al.message.modify')" /></div>
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
				<div id="divForSubmitButtom" class="filterContainer"> </div>
			</div>
			<script type="text/javascript">
				function updateStatusWindow(text){
					$("#stateDiv").html(text);
					//$("body").append('<div id="divBlock" style="background-color:white;top:0px;left:0px;position:fixed;width:100%;height:100%;opacity:0.7;z-index:2;"> </div>');
					$("#stateDiv").fadeIn('slow');
					setTimeout('$("#stateDiv").fadeOut("slow")',2000);
					//setTimeout('$("#divBlock").remove()',1500);
				}
				var changenodedivfunction = function(){
					var selectedIndex = $("select[name=ALElement] option:selected").index();
					var elementName = $("select[name=ALElement] option:selected").text();
					var elementValue = $("select[name=ALElement] option:selected").val();
					var father = $("#father option:selected").text();
					var fatherId = $("#father option:selected").val();
					if(selectedIndex!=-1){
						$.post('changeNode.action',{"textAL":"","element":"","father" :fatherId,"ALElement":$("select[name=ALElement] option:selected").val()},function(response){
							$("body").html(response);
							$("select[name=ALElement] option[value*='"+elementValue+"']").attr('selected','selected'); //select
							$("li[id='li_"+elementValue+"']").addClass("selected"); //for safari and chrome browsers
							$("#editDiv").show();
							$("#showEditLanguagesDiv").show();
							$("#actionsButtons").show();
							$("#divGroupNodesContainer").show();
							alelementclick();
							if($("#changeNode_").length>0){
								updateStatusWindow('<span style="font-weight:bold;">'+elementName+'</span> <s:property value="getText('al.message.institutionhascontentindexed')"/>!');
							}else{
								updateStatusWindow('<span style="font-weight:bold;">'+elementName+'</span> <s:property value="getText('al.message.nodechangedto')"/> '+father+'!');
							}
						});
					}else{
						updateStatusWindow("<s:property value='getText("al.message.noElementSelected")' />");
					}
				};
				$("#changeNodeDiv").click(changenodedivfunction);
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
                        var doesAlreadyExist = false;
                        $("select[name=ALElement] > option").each(function() {
                            if(textAL == translateName(this.text)) {
                                doesAlreadyExist = true;
                                return false;
                            }
                        });
                        if(doesAlreadyExist) {
                            updateStatusWindow('<span style="font-weight:bold;">'+textAL+"</span> <s:property value="getText('al.message.existsAlready')" />");
                        } else {
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
					}
				});
                function translateName(name) {
                    return name.replace(/^[0-9|\||\.| |\*]*/, "");
                }
                function checkSpecialNodes(){
                	if ($('select[name=ALElement] option:selected').hasClass("nodelete")) {
						$('#actionWarning').html("<s:text name='al.message.contains.eads'/>");
						$('#deleteDiv').addClass("hidden");
					} else {
						$('#actionWarning').html("");
						$('#deleteDiv').removeClass("hidden");
					}
                }
				$("#moveUpDiv").click(function(){
					var selectedIndex = $("select[name=ALElement] option:selected").index();
					var elementName = $("select[name=ALElement] option:selected").text();
					var elementValue = $("select[name=ALElement] option:selected").val();
					if(selectedIndex!=-1){
						$.post('moveUpAL.action',{"textAL":"","element":"","ALElement": $("select[name=ALElement] option:selected").val()},function(response){
							$("body").html(response);
							//$("select[name=ALElement] option[value="+elementValue+"]").attr('selected','selected');
							updateStatusWindow('<span style="font-weight:bold;">'+elementName+'</span> <s:property value="getText('al.message.movedup')"/>');
							$("#showLanguagesDiv").show();
							$("#actionsButtons").show();
							$("#showEditLanguagesDiv").show();
							$("#divGroupNodesContainer").show();
							//$("option[value="+elementValue+"]").attr("selected",true); //re-select
							$("li[id='li_"+elementValue+"']").addClass("selected"); //for safari and chrome browsers
							$("select[name=ALElement] option").each(function(){
								if($(this).val()==elementValue){
									$(this).attr("selected",true);
									checkSpecialNodes();
								}
							});
							checkForNextPrevButtons($("select[name=ALElement] option:selected"));
							checkForDisableWrongPathers($('select[name=ALElement] option:selected'));
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
							//$("select[name=ALElement] option[value="+elementValue+"]").attr('selected','selected');
							updateStatusWindow('<span style="font-weight:bold;">'+elementName+'</span> <s:property value="getText('al.message.moveddown')"/>');
							$("#showLanguagesDiv").show();
							$("#actionsButtons").show();
							$("#showEditLanguagesDiv").show();
							$("#divGroupNodesContainer").show();
							$("li[id='li_"+elementValue+"']").addClass("selected"); //for safari and chrome browsers
							//$("option[value="+elementValue+"]").attr("selected",true); //re-select
							$("select[name=ALElement] option").each(function(){
								if($(this).val()==elementValue){
									$(this).attr("selected",true);
									checkSpecialNodes();
								}
							});
							checkForNextPrevButtons($("select[name=ALElement] option:selected"));
							checkForDisableWrongPathers($('select[name=ALElement] option:selected'));
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
				var unselectclickfunction = function(){
					var elementName = $("select[name=ALElement] option:selected").text();
					$("select[name=ALElement]")[0].selectedIndex = -1;
					updateStatusWindow('<span style="font-weight:bold;">'+elementName+'</span> <s:property value="getText('al.message.unselected')"/>');
					$("#actionsButtons").hide();
					$("#showEditLanguagesDiv").hide();
					$("#divGroupNodesContainer").hide();
					//unselect
					$("ul[id*='ALElement'] li.selected").css("background-color","");
					$("ul[id*='ALElement'] li.selected").removeClass("selected");
				};
				var alelementclick = function(){
					
					if ($('select[name=ALElement] option:selected').hasClass("nodelete")) {
						$('#actionWarning').html("<s:text name='al.message.contains.eads'/>");
						$('#deleteDiv').addClass("hidden");
					} else {
						$('#actionWarning').html("");
						$('#deleteDiv').removeClass("hidden");
					}
					$("#showLanguagesDiv").show();
					$("#actionsButtons").show();
					$("#showEditLanguagesDiv").show();
					if($("#father option").size()>1){
						$("#divGroupNodesContainer").show();
					}
					$("#showAlternativeNamesDiv").hide();
					$("#editDiv").hide();
					$("#editLanguagesDiv").hide();
					checkForNextPrevButtons($('select[name=ALElement] option:selected'));
					checkForDisableWrongPathers($('select[name=ALElement] option:selected'));
				};
				$("#unselectDiv").click(unselectclickfunction);
				
				$("#showLanguagesDiv").click(function(){
					var selectedIndex = $("select[name=ALElement] option:selected").index();
					var elementValue = $("select[name=ALElement] option:selected").val();
					if(selectedIndex!=-1){
						$.post('showLanguages.action','ALElement='+$("select[name=ALElement] option:selected").val(),function(response){
							$("body").html(response);
							$("select[name=ALElement]")[0].selectedIndex = selectedIndex;
							$("li[id='li_"+elementValue+"']").addClass("selected"); //for safari and chrome browsers
							updateStatusWindow('Show alternatives names done!');
							$("#showLanguagesDiv").hide();
							$("#editDiv").show();
							$("#showEditLanguagesDiv").show();
							$("#actionsButtons").show();
							$("#divGroupNodesContainer").show();
							if($('select[name=ALElement] option:selected').hasClass("nodelete")){
								$('#actionWarning').html("<s:text name='al.message.contains.eads'/>");
								$('#deleteDiv').addClass("hidden");
							}else{
								$('#actionWarning').html("");
								$('#deleteDiv').removeClass("hidden");
							}
						});
					}else{
						updateStatusWindow("<s:property value='getText("al.message.noElementSelected")' />");
					}
				});
				$("#editDiv").click(function(){
					var elementName = $("select[name=ALElement] option:selected").text();
					var selectedIndex = $("select[name=ALElement] option:selected").index();
					var elementValue = $("select[name=ALElement] option:selected").val();
					$("#editDiv").hide();
					if($("select[name=ALElement] option:selected").index()!=-1){
						$.post('editElement.action','ALElement='+elementValue,function(response){
							$("body").html(response);
							if($("#actionWarning").length>0 && $.trim($("#actionWarning").html()).length==0){
								$("#actionWarning").remove();
							}
							$("select[name=ALElement]")[0].selectedIndex = selectedIndex;
							$("li[id='li_"+elementValue+"']").addClass("selected"); //for safari and chrome browsers
							updateStatusWindow('<span style="font-weight:bold;">'+elementName+'</span> <s:property value="getText('al.message.alternativesnamesdisplayed')"/>');
							$("#editDiv").hide();
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
									$("li[id='li_"+elementValue+"']").addClass("selected"); //for safari and chrome browsers
									updateStatusWindow('<s:property value="getText('al.message.editedalternativesnamesdone')"/>');
									$("#showLanguagesDiv").hide();
									$("#showEditLanguagesDiv").show();
									$("#actionsButtons").show();
									$("#divGroupNodesContainer").show();
									//$("#editDiv").show();
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
					$("#actionsSelectDivContainer").append($("#filteList"));
					$("#actionsSelectDivContainer").append($("#selectOnlyListElements"));
					$("#actionsSelectDivContainer").append($("#actionsButtons"));
					$("#showEditLanguagesDiv").append($("#showAlternativeNamesDiv"));
					$("#showEditLanguagesDiv").append($("#editLanguagesDiv"));
					$("#divForSubmitButtom").append($("#finalSubmitButton")); 
					$("#selectOnlyListElements").css("margin-bottom","0px");
					if($("select[name=ALElement] option:selected").index()==-1){
						$("#actionsButtons").hide();
						$("#editDiv").hide();
						$("#showEditLanguagesDiv").hide();
						$("#divGroupNodesContainer").hide();
						$("select[name='ALElement']").click(alelementclick);
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
					}else if(!($("#editLanguagesDiv").is(":visible"))){
						$("#editDiv").show();
					}
					$("#ALElement").richSelect({
						"global_class":"divSelectOnlyListElements2",
						"individual_class":"",
						"onelement_click":/*$._data($("#ALElement")[0],"events")["click"][0].handler*/alelementclick,
						"onelement_unselected": /*$._data($("#unselectDiv")[0],"events")["click"][0].handler*/unselectclickfunction
					});
				});
				function showcurtain(){
					$("#curtain").show();
				};
				function checkForDisableWrongPathers(jqueryNode){
					$("select#father option").each(function(){
						if($(this).attr("value")!=jqueryNode.attr("value")){
							$(this).removeAttr("disabled");
						}else{
							$(this).attr("disabled","true");
						}
					});
					var paddingLeft = null;
					paddingLeft = jqueryNode.css("padding-left"); //used by previews developmens to enable a virtual tabulation
					if(paddingLeft && paddingLeft!=null && paddingLeft!=undefined){
						var numPaddingLeft = parseInt(paddingLeft.substring(0,paddingLeft.indexOf("px")));
						var stop = false;
						while(!stop && (nextJqueryNode = jqueryNode.next()) && nextJqueryNode.length>0){
							var currentNumPaddingLeft = parseInt(nextJqueryNode.css("padding-left").substring(0,paddingLeft.indexOf("px")));
							if(currentNumPaddingLeft>numPaddingLeft){ //add to blacklist
								$("select#father option").each(function(){
									if($(this).attr("value")==nextJqueryNode.attr("value")){
										$(this).attr("disabled","true");
									}
								});
							}else{
								stop = true;
							}
							jqueryNode = nextJqueryNode;
						}
					}
				}
				function checkForNextPrevButtons(jqueryNode){
					targetPaddingLeft = jqueryNode.css("padding-left");
					if(targetPaddingLeft.length>2){
						targetPaddingLeft = targetPaddingLeft.substring(0,2);
					}
					$("#moveUpDiv").show();
					$("#moveDownDiv").show();
					var paddingLeft = null;
					//check for downDivButton
					if(($("select[name=ALElement] option").size()-1)>$("select[name=ALElement] option").index(jqueryNode)){
						var next = jqueryNode.next();
						var exit = false;
						do{
							paddingLeft = next.css("padding-left");
							if(paddingLeft.length>2){
								paddingLeft = paddingLeft.substring(0,2);
							}
							if(parseInt(targetPaddingLeft) < parseInt(paddingLeft)){
								$("#moveDownDiv").hide();
							}else if(parseInt(paddingLeft) == parseInt(targetPaddingLeft)){
								exit = true;
								$("#moveDownDiv").show();
							}else{
								$("#moveDownDiv").hide();
								exit = true;
							}
						}while(!exit && (next = next.next()) && next.length>0);
					}else{
						$("#moveDownDiv").hide();
					}
					//check for upDivButton
					if($("select[name=ALElement] option").index(jqueryNode)>0){
						var prev = jqueryNode.prev();
						exit = false;
						do{
							paddingLeft = prev.css("padding-left");
							if(paddingLeft.length>2){
								paddingLeft = paddingLeft.substring(0,2);
							}
							if( parseInt(targetPaddingLeft) < parseInt(paddingLeft) ){
								$("#moveUpDiv").hide();
							}else if( parseInt(paddingLeft) == parseInt(targetPaddingLeft) ){
								exit = true;
								$("#moveUpDiv").show();
							}else{
								$("#moveUpDiv").hide();
								exit = true;
							}
							lastValue = parseInt(paddingLeft);
						}while(!exit && (prev = prev.prev()) && prev.length>0);
					}else{
						$("#moveUpDiv").hide();
					}
				}
				
			</script>
			<div id="filterSelect">
						<div class="firstFilterDiv">
							<label for="textAL"><s:property value="getText('al.message.name')" />:</label>
							<input type="text" name="textAL" id="textAL" class="inputTextBar" />
						</div> <!--end firstFilterDiv-->
						<div><!--div1-->
							<div style="float:left;">
								<div class="secondFilterDiv">
									<label for="element" style="padding-right:4px;"><s:property value="getText('al.message.element')" />:</label>
									<select name="element" id="element">
										<option value="series" ><s:property value="getText('al.message.series')"/></option>
										<option selected="selected" value="file"><s:property value="getText('al.message.file')"/></option>
									</select> <!--end select element-->
								</div> <!--end secondFilterDiv-->
								<div class="secondFilterDiv" id="secondFilterDivLang">
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
								</div> <!--end secondFilterDiv-->
							</div> <!--end style-->
						</div><!--end div1-->
				     </div><!--end filterSelect-->
				     <div id="filteList" class="filterListAL">
					<s:if test="AL.size()==0">
						<s:property value="getText('al.message.noelements')" />
					</s:if>
					<s:elseif test="AL.size()<2">
							<div class="divFilterThree">
								<div class="secondFilterDiv">
									<div style="float:left;width:100%;"> <!--style 1-->
										<label for="ALElement" class="ALElementLabel"><s:property value="getText('al.message.listal')" /></label>
									</div><!--end style 1-->
									<div style="float:left;width:100%;"> <!--style 2-->
										<select id="selectOnlyListElements" class="selectListElementsALNoMargin" name="ALElement" id="ALElement" size="2">
					</s:elseif>
					<s:else>
							<div id="selectOnlyListElements" class="divSelectOnlyListElements">
								<div style="float:left;width:100%;"> <!--style 3-->
									<label for="ALElement" class="ALElementLabel"><s:property value="getText('al.message.listal')" /></label>
								</div> <!--end style 3-->
								<div style="float:left;width:100%;"><!--style 4-->
									<select class="divSelectOnlyListElements2" name="ALElement" id="ALElement" size="<s:property value="AL.size()" />">
					</s:else>								
								<s:iterator var="row" value="AL">
										<option class="${row.cssClass}" style="padding-left:${15*row.depth}px;" value="${row.id}" ><s:property value="#row.name" /></option>
								</s:iterator>
						<s:if test="AL.size()>0"> <!--if 1-->
									</select> <!--end select divSelectOnlyListElements2-->
								</div><!--end style 4-->
								<s:if test="elementLanguages.size()>0"><!--if 2-->
									<div id="showAlternativeNamesDiv">
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
												</select> <!--end select editTarget-->
										</p> <!--end ALP-->
									</div><!--end showAlternativeNamesDiv-->
									<s:if test="%{edit==true}"> <!--if 3-->
										<div id="editLanguagesDiv">
											<s:if test="elementLanguages.size()>1">
												<p><input type="submit" id="submitDeleteTarget" name="action:deleteTarget" value="<s:property value="getText('al.message.deletetarget')"/>"/></p>
											</s:if>
											<p class="ALP2"><label for="target" style="float:left;"><s:property value="getText('al.message.anwritelanguage')"/>: </label><input type="text" id="target" name="target" /></p>
											<label  class="leftSpace" for="languageTarget"><s:property value="getText('al.message.anselectlanguage')"/>: </label>
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
											</select><!--end select languageTarget-->
											<p><input id="editTargetSubmit" type="submit" name="action:editTarget" value="<s:property value="getText('al.message.editalternativenames')"/>" /> <input id="editTargetCancel" type="submit" name="action:editAL" value="<s:property value="getText('al.message.canceledittarget')"/>" /></p>
										</div><!--end editLanguagesDiv-->
									</s:if><!--end if 3-->
								</s:if><!--end if 2-->
							
							</div><!--end selectOnlyListElements-->
                           </select><!--end select selectOnlyListElements-->
                           </div><!--end style 2--> 
							<div id="divSelectListActionsDiv">
								<p><input type="submit" id="moveUpAL" name="action:moveUpAL" value="<s:property value="getText('al.message.up')"/>" /></p>
								<p><input type="submit" id="moveDownAl" name="action:moveDownAL" value="<s:property value="getText('al.message.down')"/>" /></p>
								<p><input type="submit" id="seeLanguagesSubmit" name="action:showLanguages" value="<s:property value="getText('al.message.showalternativenames')"/>" /></p>
								<s:if test="elementLanguages.size()>0">
									<p>
										<input type="submit" id="seeLanguagesSubmit" name="action:editElement" value="<s:property value="getText('al.message.editalternativenames')"/>" />
									</p>
								</s:if>
							</div><!--end divSelectListActionsDiv-->
							<s:if test="groupList!=null && groupList.size()>0"><!-- if 4-->
								<div id="divGroupNodes" style="float:left;margin-top:10px;">
									<p><label for="father" style="float:left; padding-left:12px;"><s:property value="getText('al.message.selectGroup')" />: </label>
									<s:if test="groupList.size()>0" > <!--if 5-->
										<select id="father" name="father" size="1" >
										<s:iterator var="row" value="groupList" >
											<option value="<s:property value="#row.id" />"><s:property value="#row.name" /></option>
										</s:iterator>
										</select>
									</s:if><!--end if 5-->
									</p>
									<%--<s:select id="father" name="father" list="groupList" size="1" theme="simple" />--%>
									<div class="ALP">
										<s:submit action="changeNode" id="submitFormGroupNodes" key="al.message.changeGroup" />
									</div>
								</div><!--end divGroupNodes-->
						       </s:if><!--end if 4-->
                                                
						</div> <!--end divFilterThree-->
					
					<div>
						<input class="inputButtonSubmitAL2" type="submit" id="editAL_modifyStructure" name="action:modifyStructure" value="<s:property value="getText('al.message.modify')"  />"/>
						</s:if><!--end if 1-->
						<input id="finalSubmitButton" class="inputButtonSubmitAL" type="submit" id="editAL_feditAL" onclick="showcurtain();"  name="action:feditAL" value="<s:property value="getText('al.message.finish')" />"/>
					</div>
                  </div><!--end filteList-->
                                   
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
