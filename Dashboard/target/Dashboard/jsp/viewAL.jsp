<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
        <%@ taglib prefix="s" uri="/struts-tags"%>   
        
<link href='${pageContext.request.contextPath}/js/dynatree/skin/ui.dynatree.css' rel='stylesheet' type='text/css'>

<script src='${pageContext.request.contextPath}/js/jquery/jquery_1.4.2.min.js' type='text/javascript'></script>
<script src='${pageContext.request.contextPath}/js/jquery/jquery-ui.custom.min.js' type='text/javascript'></script>
<script src='${pageContext.request.contextPath}/js/jquery/jquery.cookie.js' type='text/javascript'></script>
<script src='${pageContext.request.contextPath}/js/dynatree/jquery.dynatree.js' type='text/javascript'></script>
             
   	<!-- Begind - Div where the whole directory is displayed (left and right part) -->
   	<div id="wholeDirectory" style="height:450px; width:100%;">
   		   	<%-- Div for the directory tree with countries and institutions --%>
		<p>&nbsp;</p>	   	

    	<!-- Begind - Div for the right part in which the EAG is displayed -->
		<div id="archivalInstitutionContactInformation" class="archivalInstitutionContactInformation">
			<div id="displayEAG" class="scrollbar">
				<div class="Aboutus">
			    		<div class="directoryEAGSection">
							<s:property value="getText('previewal.message.noInstitutionSelected')" />
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
							title: "Navigated Search Tree for Archival Landscape - Countries, Archival Insitution Groups and Archival Institutions",
							rootVisible: false,
							fx: { height: "toggle", duration: 200 },
							selectMode: 1,
											
							//Handleing events
											
							//Tree initialization
							initAjax: {
	              				url: "${pageContext.request.contextPath}/generatePreviewALTreeJSON.action",
	              				data: {couId: "${couId}", navTreeLang: "${navTreeLang}"}
	              			},
											
	              			//Function to load only the part of the tree that the user wants to expand
	              			onLazyRead: function(node){
	              				            	
	              				node.appendAjax({
	              					url: "${pageContext.request.contextPath}/generateArchivalInstitutionPartPreviewALJSON.action",
	              					data: {navTreeLang: "${navTreeLang}", nodeId: node.data.key}
	              				});
	              			},
	              							
	        				//Function to load the EAG information in the right part of the page using AJAX
	              			onActivate: function(node) {
	        					if( node.data.url ) {
	        						$("#displayEAG").empty();
	        						$("#displayEAG").append("<div id='waitingImage'><img src='images/waiting.gif'/></div>");
	        						$("#displayEAG").load(node.data.url);
	        					}
	        				}
	              							
						});
								
					});	
				</script>
			
		</div>
		<!-- End - Div where the directory tree is displayed -->	
	</div>   	
   	<!-- End - Div where the whole directory is displayed (left and right part) -->
	