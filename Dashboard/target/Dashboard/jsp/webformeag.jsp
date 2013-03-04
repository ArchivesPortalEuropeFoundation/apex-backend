<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
	
	<!-- BEGIN - Curtain div hidden -->
	<div id="curtain" class="curtain" style="display:none;"> 
		<div style="width:100%; height:50%; float:left;">&nbsp;</div>
		<div style="width:100%; float:left;">
			<div id="curtain_message" class="curtain_message">
				<s:property value="getText('label.eag.retrieving.hg')" />
			</div> 
		</div>		
	</div>
	<!-- END - Curtain div hidden -->

	<div id="eagwebform" align="center">
		<s:property value="getText('label.eag.webcreation')" />
		<br/>
     	<s:property value="getText('label.eag.webcreationwarn')" />   
    	<br>
    	<br>
    	<br>
		<form id="eagForm" name="eagForm" action="createsimpleeagwithmenu.action" method="POST">
			<input type="hidden" name="ai_id" value="<s:property value='ai_id'/>" id="eagForm_ai_id"/>
			<table class="wwFormTable" id="eagFormTable">
				
				<s:hidden name="currentAction" value="%{currentAction}" />
				<s:textfield name="name" value="%{name}" key="label.ai.ainame" required="true" cssStyle="width:190%"></s:textfield>
				<s:textfield name="englishName" value="%{englishName}" key="label.ai.english.name" cssStyle="width:190%"></s:textfield>
    			<s:textfield name="id" value="%{id}" key="label.ai.identifier" required="true" cssStyle="width:190%"></s:textfield>
				<s:textfield name="country" value="%{country}" key="label.ai.country" required="true" cssStyle="width:190%"></s:textfield>
				<s:textfield name="cityTown" value="%{cityTown}" key="label.ai.city" required="true" cssStyle="width:190%"></s:textfield>
				<s:textfield name="postalCode" value="%{postalCode}" key="label.ai.postal.code" required="true" cssStyle="width:190%"></s:textfield>
				<s:textfield name="street" value="%{street}" key="label.ai.street" required="true" cssStyle="width:190%"></s:textfield>
				<s:textfield name="telephone" value="%{telephone}" key="label.ai.telephone" required="true" cssStyle="width:190%"></s:textfield>
				<s:textfield name="emailAddress" value="%{emailAddress}" key="label.ai.email" required="true" cssStyle="width:190%"></s:textfield>
				<s:textfield name="webPage" value="%{webPage}" key="label.ai.web" required="true" cssStyle="width:190%"></s:textfield>
				<s:textfield name="workingPlaces" value="%{workingPlaces}" key="label.ai.working.places" required="true" cssStyle="width:190%"></s:textfield>
				<s:textfield name="archivalHoldings" value="%{archivalHoldings}" key="label.ai.extent.archival.holdings" required="true" cssStyle="width:190%"></s:textfield>
				<s:select list="#{'1':'yes', '2':'no'}" key="label.ai.access" name="access" value="access" required="true"></s:select>
		    	<s:select list="#{'1':'yes', '2':'no'}" key="label.ai.handicapped" name="handicapped" value="handicapped" required="true"></s:select>
		    	<s:select list="#{'1':'yes', '2':'no'}" key="label.ai.library" name="library" value="library" required="true"></s:select>
		    	<s:select list="#{'1':'yes', '2':'no'}" key="label.ai.laboratory" name="laboratory" value="laboratory" required="true"></s:select>
		    	<s:select list="#{'1':'yes', '2':'no'}" key="label.ai.reproduction" name="reproduction" value="reproduction" required="true"></s:select>
			    <s:select list="#{'1':'yes', '2':'no'}" key="label.ai.automation" name="automation" value="automation" required="true"></s:select>
	   			
	   			<s:if test="repositorguideResource.isEmpty()">
	   				<s:set name="lastIteration" value="0"/>
	   			</s:if>	    		
	    		<s:else>
		   			<s:iterator value="repositorguideResource" status="stat" var="row">
		   			
						<s:set name="lastIteration" value="%{#stat.index}"/>
						
						<tr id="repositorguideInformationTr<s:property value='%{#stat.index}'/>">
	    					<td class="tdLabel">
	    						<br>
	    						<label for="eagForm_repositorguideInformation<s:property value='%{#stat.index}'/>" class="label"><s:property value="getText('label.ai.hg.information')" />:</label>
	    					</td>
	    					<td>
	    						<br>
	    						<input type="text" name="repositorguideInformation" id="repositorguideInformation<s:property value='%{#stat.index}'/>" value="<s:property value='repositorguideInformation[#stat.index]'/>" style="width:190%"/>
	    					</td>
						</tr>
						
						<s:if test="repositorguidePossibleHGTitle.size() == 1 && repositorguidePossibleHGTitle.get(0).equals('')">
							<tr id="repositorguidePossibleHGTitleTr<s:property value='%{#stat.index}'/>">
		    					<td class="tdLabel">
		    						<label for="eagForm_repositorguideResource<s:property value='%{#stat.index}'/>" class="label"><s:property value="getText('label.ai.hg.resource')" />:</label>
		    					</td>
		    					<td>
									<s:select list="#{'1':'External'}" name="repositorguideResource" id="repositorguideResource%{#stat.index}" value="repositorguideResource[#stat.index]" onchange="var currentId = '%{#stat.index}'; var valueSelected = document.getElementById('repositorguideResource' + currentId).value; changerepositorguideURLAvailability('repositorguideURL' + currentId, 'repositorguidePossibleHGTitle' + currentId, valueSelected);" theme="simple"></s:select>
		    					</td>
							</tr>							
						</s:if>
						<s:else>
							<tr id="repositorguideResourceTr<s:property value='%{#stat.index}'/>">
		    					<td class="tdLabel">
		    						<label for="eagForm_repositorguideResource<s:property value='%{#stat.index}'/>" class="label"><s:property value="getText('label.ai.hg.resource')" />:</label>
		    					</td>
		    					<td>
									<s:select list="#{'1':'External', '2':'Local'}" name="repositorguideResource" id="repositorguideResource%{#stat.index}" value="repositorguideResource[#stat.index]" onchange="changerepositorguideURLAvailability(this);" theme="simple"></s:select>
		    					</td>
							</tr>							
						</s:else>
						 
						<c:if test="${row == '1'}">
							<tr id="repositorguideURLTr<s:property value='%{#stat.index}'/>">
		    					<td class="tdLabel" id="repositorguideURL<s:property value='%{#stat.index}'/>Label">
		    						<label for="eagForm_repositorguideURL" class="label"><s:property value="getText('label.ai.hg.external.url')" />:</label>
		    					</td>
		    					<td>
		    						<input type="text" name="repositorguideURL" id="repositorguideURL<s:property value='%{#stat.index}'/>" value="<s:property value='repositorguideURL[#stat.index]'/>" style="width:190%"/>
		    					</td>
							</tr>
						</c:if>
						<c:if test="${row == '2'}">
							<tr id="repositorguideURLTr<s:property value='%{#stat.index}'/>">
		    					<td class="tdLabel" id="repositorguideURL<s:property value='%{#stat.index}'/>Label" style="display:none">
		    						<label for="eagForm_repositorguideURL" class="label"><s:property value="getText('label.ai.hg.external.url')" />:</label>
		    					</td>
		    					<td>
		    						<input type="text" name="repositorguideURL" id="repositorguideURL<s:property value='%{#stat.index}'/>" value="<s:property value='repositorguideURL[#stat.index]'/>" style="width:190%; display:none;"/>
		    					</td>
							</tr>
						</c:if>
		
						<c:if test="${row == '1'}">
							<tr id="repositorguidePossibleHGTitleTr<s:property value='%{#stat.index}'/>">
		    					<td class="tdLabel">
		    					</td>
		    					<td>
									<s:select list="repositorguidePossibleHGTitle" name="repositorguidePossibleHGTitle" value="%{localHGSelected[#stat.index]}" id="repositorguidePossibleHGTitle%{#stat.index}" cssStyle="display:none;" theme="simple"></s:select>		    					
								</td>
							</tr>
						</c:if>
						<c:if test="${row == '2'}">
							<tr id="repositorguidePossibleHGTitleTr<s:property value='%{#stat.index}'/>">
		    					<td class="tdLabel">
		    					</td>
		    					<td>
									<s:select list="repositorguidePossibleHGTitle" name="repositorguidePossibleHGTitle" value="%{localHGSelected[#stat.index]}" id="repositorguidePossibleHGTitle%{#stat.index}" theme="simple"></s:select>				
								</td>
							</tr>
						</c:if>
	    				<tr id="deleteRepositorguideButtonTr<s:property value='%{#stat.index}'/>">
	    					<td class="tdLabel">
	    					</td>
	    					<td>
			    				<input type="button" value="<s:property value="getText('label.ai.deleterepositorguide.button')" />" name="deleteRepositorguideButton<s:property value='%{#stat.index}'/>" id="deleteRepositorguideButton<s:property value='%{#stat.index}'/>" onClick="deleterepositorguide(this)" />
							</td>
						</tr>
						
		   			</s:iterator>
	    		</s:else>
	    		
   				<tr>
   					<td class="tdLabel">
   						<br>
   					</td>
   					<td>
   						<br>
	    				<input type="button" value="<s:property value="getText('label.ai.addrepositorguide.button')" />" name="addRepositorguideButton" />
	    				<script type="text/javascript">

	    				</script>
					</td>
				</tr>
				<tr>
   					<td>
   						<br>
   						<br>
			    		<s:if test="currentAction == 'createsimpleeag'">
			    			<s:submit key="label.validate" action="uploadsimpleeag" theme="simple"/>
			            </s:if>
			            <s:elseif test="currentAction == 'createsimpleeagwithmenu'">
			    			<s:submit key="label.validate" action="uploadsimpleeagwithmenu" theme="simple"/>
			            </s:elseif>
			    		<s:submit key="label.cancel" action="dashboardHome" theme="simple"/>
   					</td>
				</tr>
			</table>
			<br></br>
			<br></br>
		</form>

	</div>
	
	<script type="text/javascript">
	
		var lastIterationGlobal = -1;
	    
		//Onclick event within addRepositorguideButton
		$("input[name='addRepositorguideButton']").click(function(){
			var lastIteration = <s:property value='#lastIteration' />; 
			var repositorguideInformationText = '<s:property value="getText('label.ai.hg.information')" />'; 
			var repositorguideResourceText = '<s:property value="getText('label.ai.hg.resource')" />'; 
			var repositorguideURLText = '<s:property value="getText('label.ai.hg.external.url')" />'; 
			var deleteRepositorguideButtonText = '<s:property value="getText('label.ai.deleterepositorguide.button')" />'; 
			
			//First, it is necessary to show the curtain for waiting until the data is retrieved from the server
			$("#curtain").show();
			addNewRepositorGuide(lastIteration, repositorguideInformationText, repositorguideResourceText, repositorguideURLText, deleteRepositorguideButtonText);
		});
		
		function changerepositorguideURLAvailability(select){

	    	var value = select.value;
			var nameSplitted = select.id.split('repositorguideResource');
			var currentId = nameSplitted[1];
			var idrepositorguideURL = 'repositorguideURL' + currentId;  
			var idrepositorguidePossibleHGTitle = 'repositorguidePossibleHGTitle' + currentId;
	
			if (value == "1") {
	    		// The user has selected an external resource
	    		// It is necessary to hide the HG Titles dropdown box, display the URL textfield and remove the content 
	    		document.getElementById(idrepositorguidePossibleHGTitle).style.display = "none";	
		    	document.getElementById(idrepositorguideURL).style.display = "block";
		    	document.getElementById(idrepositorguideURL + "Label").style.display = "block";
		    	document.getElementById(idrepositorguideURL).value = "";
	    	}
	    	else {
	    		// The user has selected a local resource
	    		// It is necessary to display the HG Titles dropdown box, hide the URL textfield and add a dummy content 
	    		document.getElementById(idrepositorguidePossibleHGTitle).style.display = "block";	
		    	document.getElementById(idrepositorguideURL).style.display = "none";
		    	document.getElementById(idrepositorguideURL + "Label").style.display = "none";
		    	document.getElementById(idrepositorguideURL).value = "Local Holdings Guide";
	    	}
	    }

	    function deleterepositorguide(button){

	    	var nameSplitted = button.id.split('deleteRepositorguideButton');
			var currentId = nameSplitted[1];
			var idrepositorguideInformationTr = 'repositorguideInformationTr' + currentId;  
			var idrepositorguideURLTr = 'repositorguideURLTr' + currentId;  
			var idrepositorguidePossibleHGTitleTr = 'repositorguidePossibleHGTitleTr' + currentId;
			var idrepositorguideResourceTr = 'repositorguideResourceTr' + currentId;
			var iddeleteRepositorguideButtonTr = 'deleteRepositorguideButtonTr' + currentId;

			$('#' + idrepositorguideInformationTr).remove();
			$('#' + idrepositorguideURLTr).remove();
			$('#' + idrepositorguidePossibleHGTitleTr).remove();
			$('#' + idrepositorguideResourceTr).remove();
			$('#' + iddeleteRepositorguideButtonTr).remove();			
	    }
	    
	    function addNewRepositorGuide(lastIteration, repositorguideInformationText, repositorguideResourceText, repositorguideURLText, deleteRepositorguideButtonText){
	    	
	    	if (lastIterationGlobal == -1) {
	    		//First time to enter
	    		lastIterationGlobal = lastIteration;
	    	}
	    	else {
	    		lastIterationGlobal = lastIterationGlobal + 1;
	    	}
	    	
			lastIteration = lastIterationGlobal;
	    	
	    	var currentIteration = lastIteration + 1;
	    	var table = document.getElementById("eagFormTable");
	    	var rowsNumber = table.rows.length;
	    	var row = table.insertRow(rowsNumber - 2);
	    	row.id = "repositorguideInformationTr" + currentIteration;
	    	var cell1 = row.insertCell(0);
	    	var cell2 = row.insertCell(1);

	    	//repositorguideInformation, repositorguideResource, repositorguideURL, repositorguidePossibleHGTitle, deleteRepositorguideButton
	    	var repositorguidePossibleHGTitleSelect = document.createElement('select');
	    	var option = null;

	    	$.getJSON("${pageContext.request.contextPath}/generaterepositorguidePossibleHGTitlePartJSON.action", 
					
					//No parameters are deliver to the server 
					function(dataResponse) 
					{
	    				for (var i = 0 ; i < dataResponse.length; i ++){
							option = document.createElement('option');
							option.text = dataResponse[i].hgtitle;
							option.value = dataResponse[i].hgtitle;
							try {
								// for IE earlier than version 8
							  	repositorguidePossibleHGTitleSelect.add(option,repositorguidePossibleHGTitleSelect.options[null]);
							}
							catch (e) {
								repositorguidePossibleHGTitleSelect.add(option,null);
							}
						}
	    				
						//repositorguideInformation
				    	var repositorguideInformationInput = document.createElement('input');
			
				    	repositorguideInformationInput.type = 'text';
				    	repositorguideInformationInput.name = 'repositorguideInformation';
				    	repositorguideInformationInput.id = 'repositorguideInformation' + currentIteration;
				    	repositorguideInformationInput.style.width = '190%';
				    	cell1.innerHTML = repositorguideInformationText + ':';
				    	cell2.appendChild(repositorguideInformationInput);
				    	
				    	
				    	$.getJSON("${pageContext.request.contextPath}/generateInformationMessageJSON.action", 
								
								//No parameters are deliver to the server 
								function(dataResponse) 
								{
					    			//The value of the text field is filled with a message from the server by default
				    				repositorguideInformationInput.value = dataResponse[0].informationMessage;
								}
						);
	    				
	    				//repositorguideResource
	    		    	//var repositorguideResourceSelect = document.getElementById("repositorguideResource" + lastIteration).cloneNode(true); 

	    		    	//rowsNumber = table.rows.length;
	    		    	//row = table.insertRow(rowsNumber - 2);
	    		    	//row.id = "repositorguideResourceTr" + currentIteration;
	    		    	//cell1 = row.insertCell(0);
	    		    	//cell2 = row.insertCell(1);
	    		    	//repositorguideResourceSelect.id = 'repositorguideResource' + currentIteration;
	    		    	//repositorguideResourceSelect.selectedIndex = 0;
	    		    	//cell1.innerHTML = repositorguideResourceText + ':';
	    		    	//cell2.appendChild(repositorguideResourceSelect);

	    		    	rowsNumber = table.rows.length;
	    		    	row = table.insertRow(rowsNumber - 2);
	    		    	row.id = "repositorguideResourceTr" + currentIteration;
	    		    	cell1 = row.insertCell(0);
	    		    	cell2 = row.insertCell(1);
	    		    	cell1.innerHTML = repositorguideResourceText + ':';
	    		    	if (repositorguidePossibleHGTitleSelect.length > 0) {
	    		    		//There are possible HGs for adding to the EAG, so it is necessary to include Local resource
	    			    	cell2.innerHTML = "<select name='repositorguideResource' id='repositorguideResource" + currentIteration + "' onchange='changerepositorguideURLAvailability(this);'><option value='1' selected='selected'>External</option><option value='2'>Local</option></select>";
	    		    	}
	    		    	else {
	    		    		//There are not HGs, so it is not necessary to include Local resource
	    			    	cell2.innerHTML = "<select name='repositorguideResource' id='repositorguideResource" + currentIteration + "' onchange='changerepositorguideURLAvailability(this);'><option value='1' selected='selected'>External</option></select>";	    		
	    		    	}

	    		    	//repositorguideURL
	    		    	var repositorguideURLInput = document.createElement('input');

	    		    	rowsNumber = table.rows.length;
	    		    	row = table.insertRow(rowsNumber - 2);
	    		    	row.id = "repositorguideURLTr" + currentIteration;
	    		    	cell1 = row.insertCell(0);
	    		    	cell2 = row.insertCell(1);
	    		    	repositorguideURLInput.type = 'text';
	    		    	repositorguideURLInput.name = 'repositorguideURL';
	    		    	repositorguideURLInput.id = 'repositorguideURL' + currentIteration;
	    		    	repositorguideURLInput.style.width = '190%';
	    		    	cell1.id = 'repositorguideURL' + currentIteration + 'Label'
	    		    	cell1.innerHTML = repositorguideURLText + ':';
	    		    	cell2.appendChild(repositorguideURLInput);

	    		    	//repositorguidePossibleHGTitle
	    		    	if (repositorguidePossibleHGTitleSelect.length > 0) {
	    		    		//There are possible HGs for adding to the EAG
	    			    	rowsNumber = table.rows.length;
	    			    	row = table.insertRow(rowsNumber - 2);
	    			    	row.id = "repositorguidePossibleHGTitleTr" + currentIteration;
	    			    	cell1 = row.insertCell(0);
	    			    	cell2 = row.insertCell(1);
	    			    	repositorguidePossibleHGTitleSelect.name = 'repositorguidePossibleHGTitle';
	    			    	repositorguidePossibleHGTitleSelect.id = 'repositorguidePossibleHGTitle' + currentIteration;
	    			    	repositorguidePossibleHGTitleSelect.selectedIndex = 0;
	    			    	repositorguidePossibleHGTitleSelect.style.display = 'none';
	    			    	cell2.appendChild(repositorguidePossibleHGTitleSelect);	    		
	    		    	}
												
	    		    	//deleteRepositorguideButton
	    		    	//var deleteRepositorguideButton = document.getElementById("deleteRepositorguideButton" + lastIteration).cloneNode(true); 

	    		    	//rowsNumber = table.rows.length;
	    		    	//row = table.insertRow(rowsNumber - 2);
	    		    	//row.id = "deleteRepositorguideButtonTr" + currentIteration;
	    		    	//cell1 = row.insertCell(0);
	    		    	//cell2 = row.insertCell(1);
	    		    	//deleteRepositorguideButton.id = 'deleteRepositorguideButton' + currentIteration;
	    		    	//cell2.appendChild(deleteRepositorguideButton);
	    		    	
	    		    	rowsNumber = table.rows.length;
	    		    	row = table.insertRow(rowsNumber - 2);
	    		    	row.id = "deleteRepositorguideButtonTr" + currentIteration;
	    		    	cell1 = row.insertCell(0);
	    		    	cell2 = row.insertCell(1);
	    		    	cell2.innerHTML = "<input type='button' value='" + deleteRepositorguideButtonText +  "' name='deleteRepositorguideButton" + currentIteration + "' id='deleteRepositorguideButton" + currentIteration + "' onClick='deleterepositorguide(this);' />";
						
	    		    	//The curtain is hidden when the data is retrieved from the server
	    		    	$("#curtain").hide();
					}
			);
	    	
	    }

	</script>
