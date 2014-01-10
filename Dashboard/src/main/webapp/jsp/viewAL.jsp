<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
        <%@ taglib prefix="s" uri="/struts-tags"%>   
<div id="directoryPortlet">
       
             
   	<!-- Begind - Div where the whole directory is displayed (left and right part) -->
   	<div id="wholeDirectory">
   		   	<%-- Div for the directory tree with countries and institutions --%>
		<p>&nbsp;</p>	   	

    	<!-- Begind - Div for the right part in which the EAG is displayed -->
		<div id="archivalInstitutionContactInformation" class="archivalInstitutionContactInformation">
			<div id="displayEAG" class="scrollbar">
			<div id="directory-column-right-content" class="portlet-column-content portlet-column-content-last">
				<div class="arrow_box">
					<p>
						<s:text name="directory.message.noInstitutionSelected" />
					</p>
				</div>
				&nbsp;
			</div>
				<div class="Aboutus">
			    		<div class="directoryEAGSection">
							
						</div>
				</div>
			</div>
		</div>
	   	<!-- End - Div for the right part in which the EAG is displayed -->
	   	
	   	
    	<!-- Begind - Div where the directory tree is displayed -->
		<div id="directoryTree" class = "directoryTree">
				<script type='text/javascript'>								
									
					$(function(){
								
						$("#directoryTree").dynatree({
							//Navigated Search Tree for Countries, Archival Institution Groups and Archival Institutions configuration
							title: "<s:property value="getText('viewAL.tree')" />",
							rootVisible: false,
							fx: { height: "toggle", duration: 200 },
							selectMode: 1,
											
							//Handleing events
											
							//Tree initialization
							initAjax: {
	              				url: "${pageContext.request.contextPath}/generatePreviewALTreeJSON.action",
	              				data: {couId: "${couId}"}
	              			},
											
	              			//Function to load only the part of the tree that the user wants to expand
	              			onLazyRead: function(node){
	              				            	
	              				node.appendAjax({
	              					url: "${pageContext.request.contextPath}/generateArchivalInstitutionPartPreviewALJSON.action",
	              					data: {nodeId: node.data.key}
	              				});
	              			},
	              							
	        				//Function to load the EAG information in the right part of the page using AJAX
	              			onActivate: function(node) {
	        					$("#directory-column-right-content").empty();
        						$("#directory-column-right-content").append("<div id='waitingImage'><img src='images/waiting.gif'/></div>");
	        					if( node.data.url ) {
	        						$("#directory-column-right-content").load(node.data.url, function() {
	        							initEagDetails();
	        						});
	        					}else{   //An empty group is selected
	        						$("#waitingImage").fadeOut("fast", function(){
	        						  if(node.data.isFolder){
	        							  // It's an empty group
	        							  $("#directory-column-right-content").append("<div class='arrow_box'><p><s:text name="directory.message.noInstitutionSelected" /></p>"); 
	        						  }else{
	        							  //The institution hasn't an EAG
	        							  $("#directory-column-right-content").append("<div class='arrow_box'><p><s:text name="directory.message.InstitutionSelectedWithoutEag" /></p>");
	        						  }	  
	        						});	
	        					}
	        				}
	              							
						});
								
					});	
				</script>
			
		</div>
		<!-- End - Div where the directory tree is displayed -->	
	</div>   	
</div>
	