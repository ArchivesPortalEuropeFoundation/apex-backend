<%@page import="eu.apenet.dashboard.manual.EADCMUnit"%>
<%@page import="java.util.List"%>
<%@page import="eu.apenet.commons.types.XmlType"%>
<%@ page import="eu.apenet.dashboard.manual.contentmanager.ContentManagerAction" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
    
   	<!-- BEGIN - Curtain div hidden for controlling harvesting process -->
   	<s:if test="harvesting==1">
		<div id="curtain" class="curtain" align="center"> 
			<div style="width:100%; height:50%; float:left;">&nbsp;</div>
			<div style="width:100%; float:left;">
				<div id="curtain_message" class="curtain_message">					
					<s:property value="getText('content.message.harvesting')"/>
				</div> 
				<div>
				<input type="submit" onclick='$("#curtain").hide();' value="Close"></input>
				</div>
			</div>		
		</div>
	</s:if>
    <!-- END - Curtain div hidden for controlling harvesting process -->
    
    <div id="listFiles">
        <span id="clearAll" class="linkList leftSide">[<s:property value="getText('content.message.clearall')" />]</span>
        <span id="selectAllFAs" class="linkList leftSide">[<s:property value="getText('content.message.selectall')" />]</span>
        <div style="clear:both;"></div>
        <div id="sizeFiles"></div>
    </div>
	<div>
        <s:set name="xmlTypeIdEAD_FA"><%=XmlType.EAD_FA.getIdentifier()%></s:set>
        <s:set name="xmlTypeIdEAD_HG"><%=XmlType.EAD_HG.getIdentifier()%></s:set>
        <s:set name="xmlTypeIdEAD_SG"><%=XmlType.EAD_SG.getIdentifier()%></s:set>
        <s:set name="xmlTypeIdEAC_CPF"><%=XmlType.EAC_CPF.getIdentifier()%></s:set>
        <s:set name="orderByDate"><%=ContentManagerAction.ORDER_BY_DATE%></s:set>
        <s:set name="orderByEadid"><%=ContentManagerAction.ORDER_BY_EADID%></s:set>
        <s:set name="orderByTitle"><%=ContentManagerAction.ORDER_BY_TITLE%></s:set>
        <s:set name="orderByFilestateConversion"><%=ContentManagerAction.ORDER_BY_FILESTATE_CONVERSION%></s:set>
        <s:set name="orderByFilestateValidation"><%=ContentManagerAction.ORDER_BY_FILESTATE_VALIDATION%></s:set>
        <s:set name="orderByFilestateIndexation"><%=ContentManagerAction.ORDER_BY_FILESTATE_INDEXATION%></s:set>
        <s:set name="orderByFilestateEseConversion"><%=ContentManagerAction.ORDER_BY_FILESTATE_ESE_CONVERSION%></s:set>
        <s:set name="orderByFilestateEseDelivery"><%=ContentManagerAction.ORDER_BY_FILESTATE_ESE_DELIVERY%></s:set>

        <s:set name="SORT_BY_OK"><%=ContentManagerAction.SORT_BY_OK%></s:set>
        <s:set name="SORT_BY_NO"><%=ContentManagerAction.SORT_BY_NO%></s:set>
        <s:set name="SORT_BY_ERROR"><%=ContentManagerAction.SORT_BY_ERROR%></s:set>
        <s:set name="SORT_BY_LINKED"><%=ContentManagerAction.SORT_BY_LINKED%></s:set>
        <s:set name="SORT_BY_NOT_LINKED"><%=ContentManagerAction.SORT_BY_NOT_LINKED%></s:set>
        <s:set name="SORT_BY_SCHEDULED"><%=ContentManagerAction.SORT_BY_SCHEDULED%></s:set>
        <s:set name="SORT_BY_READY"><%=ContentManagerAction.SORT_BY_READY%></s:set>
        <s:set name="SORT_BY_DELIVERED"><%=ContentManagerAction.SORT_BY_DELIVERED%></s:set>

        <s:set name="DEFAULT_STATUSES_QUERY_STRING">convertStatus=<s:property value="convertStatus"/>&amp;validateStatus=<s:property value="validateStatus"/>&amp;indexStatus=<s:property value="indexStatus"/>&amp;holdingsGuideStatus=<s:property value="holdingsGuideStatus"/>&amp;convertEseStatus=<s:property value="convertEseStatus"/>&amp;deliverStatus=<s:property value="deliverStatus"/></s:set>

	<c:set var="deleteMessage"><s:property value="getText('content.message.areyousure')" /></c:set>
	<c:set var="deleteIndexMessage"><s:property value="getText('content.message.areyousureindex')" /></c:set>
	<script type="text/javascript">
		function confirmDelete(){
	 		return window.confirm('${deleteMessage}'); //File
	 	}
	 	function confirmDeleteFromIndex(){
	 		return window.confirm("${deleteIndexMessage}"); //File
	 	}
        $(document).ready(function() {
            $("#clearAll").bind("click", function(value){
                clearFAsFromSession("${pageContext.request.contextPath}");
            });
            $("#selectAllFAs").bind("click", function(value){
                addAllFAsInSession("${pageContext.request.contextPath}");
            });
            $(".checkboxSave").bind("click", function(){
                addOneFA("${pageContext.request.contextPath}", $(this).val());
            });
            $("#selectAll").bind("click", function(){
                var ids = new Array();
                $("input:checkbox[name=check]").each(function(){
                    if(!$(this).is(":checked")) {
                        $(this).attr('checked','checked');
                        ids.push(this.value);
                    }
                });
                addFewFAs("${pageContext.request.contextPath}", ids);
            });
            $("#selectNone").bind("click", function(){
                var ids = new Array();
                $("input:checkbox[name=check]").each(function(){
                    if($(this).is(":checked")) {
                        $(this).removeAttr('checked');
                        ids.push(this.value);
                    }
                });
                addFewFAs("${pageContext.request.contextPath}", ids);
            });
            count("${pageContext.request.contextPath}");

            //Here we set the correct statuses in the dropdown selects
            <s:if test="convertStatus != null">
                changeStatus("#convertStatus", <s:property value="convertStatus"/>);
            </s:if>
            <s:if test="validateStatus != null">
                changeStatus("#validateStatus", <s:property value="validateStatus"/>);
            </s:if>
            <s:if test="convertEseStatus != null">
                changeStatus("#convertEseStatus", <s:property value="convertEseStatus"/>);
            </s:if>
            <s:if test="indexStatus != null">
                changeStatus("#indexStatus", <s:property value="indexStatus"/>);
            </s:if>
            <s:if test="holdingsGuideStatus != null">
                changeStatus("#holdingsGuideStatus", <s:property value="holdingsGuideStatus"/>);
            </s:if>
            <s:if test="deliverStatus != null">
                changeStatus("#deliverStatus", <s:property value="deliverStatus"/>);
            </s:if>
        });
        function changeStatus(nameSelect, valueOption){
            $(nameSelect).val(valueOption);
        }
	</script>
	</div>
	<div style="margin-top:-20px;width: 100%;float:left;">
		<form name="frmR" method="post" action="upload.action">
			<input type="submit" name="rUploadAction" value="<s:property value="getText('dashboard.menu.uploadcontent')" />" />
		</form> 
	</div>
	<div id="global" style="float:left;" >
        <s:if test="xmlTypeId == #xmlTypeIdEAD_FA">
            <a title="to holdings" href="content.action?xmlTypeId=<s:property value="xmlTypeIdEAD_HG"/>"><s:property value="getText('content.message.toholdings')"/></a><br/>
            <a title="to source guides" href="content.action?xmlTypeId=<s:property value="xmlTypeIdEAD_SG"/>"><s:property value="getText('content.message.tosourceguides')"/></a><br/>
        </s:if>
        <s:elseif test="xmlTypeId == #xmlTypeIdEAD_HG">
            <a title="to findings" href="content.action?xmlTypeId=<s:property value="xmlTypeIdEAD_FA"/>"><s:property value="getText('content.message.tofinding')"/></a><br/>
            <a title="to source guides" href="content.action?xmlTypeId=<s:property value="xmlTypeIdEAD_SG"/>"><s:property value="getText('content.message.tosourceguides')"/></a><br/>
        </s:elseif>
        <s:elseif test="xmlTypeId == #xmlTypeIdEAC_CPF">
            <a title="to findings" href="content.action?xmlTypeId=<s:property value="xmlTypeIdEAD_FA"/>"><s:property value="getText('content.message.tofinding')"/></a><br/>
            <a title="to holdings" href="content.action?xmlTypeId=<s:property value="xmlTypeIdEAD_HG"/>"><s:property value="getText('content.message.toholdings')"/></a><br/>
            <a title="to source guides" href="content.action?xmlTypeId=<s:property value="xmlTypeIdEAD_SG"/>"><s:property value="getText('content.message.tosourceguides')"/></a>
        </s:elseif>
        <s:elseif test="xmlTypeId == #xmlTypeIdEAD_SG">
            <a title="to findings" href="content.action?xmlTypeId=<s:property value="xmlTypeIdEAD_FA"/>"><s:property value="getText('content.message.tofinding')"/></a><br/>
            <a title="to holdings" href="content.action?xmlTypeId=<s:property value="xmlTypeIdEAD_HG"/>"><s:property value="getText('content.message.toholdings')"/></a><br/>
        </s:elseif>
	</div>
	<% int i=0; %>
	<div id="contentM" style="float:left;">
		<form action="" style="padding: 1px; margin: 2px; border: 1px black dotted;">
			<div id="search">
				<p>
					<%--<label>--%>
						<input type="text" name="searchTerms" value="<s:property value="searchTerms"/>" style="float:left;" />
						<select style="float:left;" name="search" class="small">
							<s:if test="search==1"><option selected="selected" value='0' ><s:property value="getText('content.message.all')"/></option></s:if>
							<s:else><option value='0'><s:property value="getText('content.message.all')"/></option></s:else>
							<s:if test="search==1"><option value='1' selected="selected" ><s:property value="getText('content.message.id')"/></option></s:if>
							<s:else><option value='1' ><s:property value="getText('content.message.id')"/></option></s:else>
							<s:if test="search==2"><option value='2' selected="selected" ><s:property value="getText('content.message.title')"/></option></s:if>
							<s:else><option value='2' ><s:property value="getText('content.message.title')"/></option></s:else>	
						</select>
						<s:hidden id="aiId" value="%{aiId}" name="aiId" />
						<s:hidden id="xmlTypeId" value="%{xmlTypeId}" name="xmlTypeId" />
                        <span style="margin-left:10px; width:100px;"><s:property value="getText('content.message.orderby')"/>:</span>
                        <select name="orderBy" style="width:100px;">
                            <option value="<s:property value="#orderByDate" />"><s:property value="getText('content.message.date')"/></option>
                            <option value="<s:property value="#orderByTitle" />"><s:property value="getText('content.message.title')"/></option>
                            <option value="<s:property value="#orderByEadid" />"><s:property value="getText('content.message.id')"/></option>
                        </select>
					<%--</label>--%>
				</p>
            <s:if test="xmlTypeId == #xmlTypeIdEAD_FA">
                <div id="searchAdvanced" style="clear:both;">
                    <table>
                        <tr>
                            <td style="width: 30%">
                                <div class="status">
                                    <span style="width: 65%"><s:property value="getText('content.message.statuses.conversion')"/>:</span>
                                    <select name="convertStatus" id="convertStatus" style="width: 35%">
                                        <option><s:property value="getText('content.message.all')"/></option>
                                        <option value="<s:property value="#SORT_BY_OK" />"><s:property value="getText('content.message.ok')"/></option>
                                        <option value="<s:property value="#SORT_BY_NO" />"><s:property value="getText('content.message.no')"/></option>
                                    </select>
                                </div>
                            </td>
                            <td style="width: 30%">
                                <div class="status">
                                    <span style="width: 65%"><s:property value="getText('content.message.statuses.validation')"/>:</span>
                                    <select name="validateStatus" id="validateStatus" style="width: 35%">
                                        <option><s:property value="getText('content.message.all')"/></option>
                                        <option value="<s:property value="#SORT_BY_OK" />"><s:property value="getText('content.message.ok')"/></option>
                                        <option value="<s:property value="#SORT_BY_NO" />"><s:property value="getText('content.message.no')"/></option>
                                        <option value="<s:property value="#SORT_BY_ERROR" />"><s:property value="getText('content.message.errorsmall')"/></option>
                                    </select>
                                </div>
                            </td>
                            <td style="width: 30%">
                                <div class="status">
                                    <span style="width: 65%"><s:property value="getText('content.message.statuses.holdingsguide')"/>:</span>
                                    <select name="holdingsGuideStatus" id="holdingsGuideStatus" style="width: 35%">
                                        <option><s:property value="getText('content.message.all')"/></option>
                                        <option value="<s:property value="#SORT_BY_LINKED" />"><s:property value="getText('content.message.linkedshort')"/></option>
                                        <option value="<s:property value="#SORT_BY_NOT_LINKED" />"><s:property value="getText('content.message.notlinked')"/></option>
                                    </select>
                                </div>
                            </td>
                        </tr>
                        <tr>
                            <td style="width: 30%">
                                <div class="status">
                                    <span style="width: 65%"><s:property value="getText('content.message.statuses.indexed')"/>:</span>
                                    <select name="indexStatus" id="indexStatus" style="width: 35%">
                                        <option><s:property value="getText('content.message.all')"/></option>
                                        <option value="<s:property value="#SORT_BY_OK" />"><s:property value="getText('content.message.ok')"/></option>
                                        <option value="<s:property value="#SORT_BY_SCHEDULED" />"><s:property value="getText('content.message.scheduled')"/></option>
                                        <option value="<s:property value="#SORT_BY_NO" />"><s:property value="getText('content.message.no')"/></option>
                                    </select>
                                </div>
                            </td>
                            <td style="width: 30%">
                                <div class="status">
                                    <span style="width: 65%;"><s:property value="getText('content.message.statuses.convertese')"/>:</span>
                                    <select name="convertEseStatus" id="convertEseStatus" style="width: 35%">
                                        <option><s:property value="getText('content.message.all')"/></option>
                                        <option value="<s:property value="#SORT_BY_OK" />"><s:property value="getText('content.message.ok')"/></option>
                                        <option value="<s:property value="#SORT_BY_NO" />"><s:property value="getText('content.message.no')"/></option>
                                    </select>
                                </div>
                            </td>
                            <td style="width: 30%">
                                <div class="status">
                                    <span style="width: 65%"><s:property value="getText('content.message.statuses.deliverese')"/>:</span>
                                    <select name="deliverStatus" id="deliverStatus" style="width: 35%">
                                        <option><s:property value="getText('content.message.all')"/></option>
                                        <option value="<s:property value="#SORT_BY_NO" />"><s:property value="getText('content.message.no')"/></option>
                                        <option value="<s:property value="#SORT_BY_READY" />"><s:property value="getText('content.message.delivered')"/></option>
                                        <option value="<s:property value="#SORT_BY_DELIVERED" />"><s:property value="getText('content.message.harvested')"/></option>
                                    </select>
                                </div>
                            </td>
                        </tr>
                    </table>
                </div>
            </s:if>
                <div>
                    <input type="submit" value="<s:property value="getText('content.message.search')" />" name="action:search" id="content_message_search" />
                    <s:if test="searchTerms.length()>0 || convertStatus != null || validateStatus != null || indexStatus != null || holdingsGuideStatus != null || convertEseStatus != null || deliverStatus != null">
                        <a href="content.action?xmlTypeId=<s:property value="%{xmlTypeId}" />"><s:property value="getText('content.message.clearresults')" /></a>
                    </s:if>
                </div>
			</div>
		</form>
		<div id="content_cont">
			<div id="content_nav" style="width:100%;float:left;">
				<table style="float:left;width: 300px;">
					<thead><tr><th><s:property value="getText('content.message.total')"/>: <s:property value="size"/></th></tr></thead>
					<tfoot><tr><td></td></tr></tfoot>
					<tbody>
	        		<tr><td>
	        			<s:if test="size>4">
	        			<%  Integer pageNumber = (Integer)request.getAttribute("pageNumber");
							Long size = (Long)request.getAttribute("size");
							Integer limit = (Integer)request.getAttribute("limit");
							if(limit==null){
								limit=4;
							}
							Long lastPage = size/limit;
							if(size%limit>0){
								lastPage++;
							}
							short count = 0;
							if(size>5){//The results number
								count = 5; 
							}else{
								count = size.shortValue();
							}
							for(int x=0;x<=lastPage && count>=0;x++){
								if(x==pageNumber ){%>
									<%count--; %>
									&nbsp;<%=x+1%>&nbsp;
								<%}else if (x==0){ //Prev %>
				        			<s:if test="xmlTypeId == #xmlTypeIdEAD_FA || xmlTypeId == #xmlTypeIdEAD_HG || xmlTypeId == #xmlTypeIdEAD_SG">
				        				<s:if test="searchTerms.length()>0">
				        					<%if(pageNumber>2){ %>
												<a title="first" href="search.action?search=<s:property value="search" />&amp;limit=<s:property value="%{limit}" />&amp;xmlTypeId=<s:property value="xmlTypeId"/>&amp;searchTerms=<s:property value="searchTerms"/>&amp;orderBy=<s:property value="orderBy" />&amp;orderDecreasing=<s:property value="orderDecreasing" />&amp;pageNumber=0&amp;<s:property value="DEFAULT_STATUSES_QUERY_STRING"/>" ><s:property value="getText('content.message.start')" /></a>
											<%}%>
											<a title="preview" href="search.action?search=<s:property value="search" />&amp;limit=<s:property value="%{limit}" />&amp;xmlTypeId=<s:property value="xmlTypeId"/>&amp;searchTerms=<s:property value="searchTerms"/>&amp;orderBy=<s:property value="orderBy" />&amp;orderDecreasing=<s:property value="orderDecreasing" />&amp;pageNumber=<s:property value="%{pageNumber-1}" />&amp;<s:property value="DEFAULT_STATUSES_QUERY_STRING"/>" ><s:property value="getText('content.message.prev')"/></a>
										</s:if>
                                        <s:else>
											<%if(pageNumber>2){ %>
												<a title="first" href="content.action?limit=<s:property value="%{limit}" />&amp;xmlTypeId=<s:property value="xmlTypeId"/>&amp;orderBy=<s:property value="orderBy" />&amp;orderDecreasing=<s:property value="orderDecreasing" />&amp;<s:property value="DEFAULT_STATUSES_QUERY_STRING"/>&amp;pageNumber=0" ><s:property value="getText('content.message.start')" /></a>
											<%}%>
											<a title="preview" href="content.action?limit=<s:property value="%{limit}" />&amp;xmlTypeId=<s:property value="xmlTypeId"/>&amp;orderBy=<s:property value="orderBy" />&amp;orderDecreasing=<s:property value="orderDecreasing" />&amp;<s:property value="DEFAULT_STATUSES_QUERY_STRING"/>&amp;pageNumber=<s:property value="%{pageNumber-1}" />" ><s:property value="getText('content.message.prev')"/></a>
										</s:else>
									</s:if>
								<%}else if(x==(lastPage) && !(pageNumber==(lastPage-1))){ //Next %>
									<s:if test="xmlTypeId == #xmlTypeIdEAD_FA || xmlTypeId == #xmlTypeIdEAD_HG || xmlTypeId == #xmlTypeIdEAD_SG">
										<s:if test="searchTerms.length()>0">
											<a href="search.action?search=<s:property value="search" />&amp;limit=<s:property value="%{limit}" />&amp;pageNumber=<s:property value="%{pageNumber+1}" />&amp;xmlTypeId=<s:property value="xmlTypeId"/>&amp;searchTerms=<s:property value="searchTerms" />&amp;orderBy=<s:property value="orderBy" />&amp;orderDecreasing=<s:property value="orderDecreasing" />&amp;<s:property value="DEFAULT_STATUSES_QUERY_STRING"/>" ><s:property value="getText('content.message.next')"/></a>
											<%if(pageNumber<(lastPage)-3){%>
												<a href="search.action?search=<s:property value="search" />&amp;limit=<s:property value="%{limit}" />&amp;pageNumber=<%=(lastPage)-1%>&amp;xmlTypeId=<s:property value="xmlTypeId"/>&amp;searchTerms=<s:property value="searchTerms" />&amp;orderBy=<s:property value="orderBy" />&amp;orderDecreasing=<s:property value="orderDecreasing" />&amp;<s:property value="DEFAULT_STATUSES_QUERY_STRING"/>" ><s:property value="getText('content.message.next')"/> <s:property value="getText('content.message.final')" /> </a>
											<%}%>
										</s:if>
                                        <s:else>
											<a href="content.action?pageNumber=<s:property value="%{pageNumber+1}" />&amp;limit=<s:property value="%{limit}" />&amp;xmlTypeId=<s:property value="xmlTypeId"/>&amp;orderBy=<s:property value="orderBy" />&amp;orderDecreasing=<s:property value="orderDecreasing" />&amp;<s:property value="DEFAULT_STATUSES_QUERY_STRING"/>" ><s:property value="getText('content.message.next')"/></a>
											<%if(pageNumber<(lastPage)-3){%>
												<a href="content.action?pageNumber=<%=(lastPage)-1%>&amp;limit=<s:property value="%{limit}" />&amp;xmlTypeId=<s:property value="xmlTypeId"/>&amp;orderBy=<s:property value="orderBy" />&amp;orderDecreasing=<s:property value="orderDecreasing" />&amp;<s:property value="DEFAULT_STATUSES_QUERY_STRING"/>" > <s:property value="getText('content.message.final')" /> </a>
											<%}%>
										</s:else>
									</s:if>
								<%}if(( (x<pageNumber && ( (x>pageNumber-3) && (pageNumber-5<0 || x+5>pageNumber)) ) 
										|| (count>0 && x>pageNumber) || (pageNumber==1 && x==0 && pageNumber!=x+1) || (count>0 && ((x==pageNumber-4 && pageNumber+1==(lastPage)) 
										|| (x==pageNumber-3 && pageNumber==(lastPage)-1) || (x==pageNumber-3 && pageNumber==(lastPage)-2) ) ) ) && x<=size/limit /*last page 3 shouldn't be showned*/){%>
									<%count--; %>
									<s:if test="searchTerms.length()>0">
										<a title="<%=x+1%>" href="search.action?search=<s:property value="search" />&amp;limit=<s:property value="%{limit}" />&amp;pageNumber=<%=x%>&amp;xmlTypeId=<s:property value="xmlTypeId"/>&amp;searchTerms=<s:property value="searchTerms"/>&amp;orderBy=<s:property value="orderBy" />&amp;orderDecreasing=<s:property value="orderDecreasing" />&amp;<s:property value="DEFAULT_STATUSES_QUERY_STRING"/>" > <%=x+1%> </a>
									</s:if><s:else>
										<a title="<%=x+1%>" href="content.action?pageNumber=<%=x%>&amp;xmlTypeId=<s:property value="xmlTypeId"/>&amp;limit=<s:property value="%{limit}" />&amp;orderBy=<s:property value="orderBy" />&amp;orderDecreasing=<s:property value="orderDecreasing" />&amp;<s:property value="DEFAULT_STATUSES_QUERY_STRING"/>" > <%=x+1%> </a>
									</s:else>
								<%}
							}%>
						</s:if>
					</td></tr>
					</tbody>
	        	</table>
	        	<form name="resultsPerPage" id="resultsPerPage" action="changeLimit.action" method="post">
					<input type="hidden" name="xmlTypeId" value="<s:property value="xmlTypeId" />"/>
	                <input type="hidden" name="pageNumber" value="<s:property value="pageNumber"/>"/>
	                <input type="hidden" name="searchTerms" value="<s:property value="searchTerms" />" />
	                <input type="hidden" name="orderBy" value="<s:property value="orderBy" />" />
	                <input type="hidden" name="orderDecreasing" value="<s:property value="orderDecreasing" />" />
                    <s:hidden name="convertStatus"/>
                    <s:hidden name="validateStatus"/>
                    <s:hidden name="indexStatus"/>
                    <s:hidden name="holdingsGuideStatus"/>
                    <s:hidden name="convertEseStatus"/>
                    <s:hidden name="deliverStatus"/>
		        	
		        	<script type="text/javascript">
						function sendResultsPerPageForm()	
						{
							document.getElementById("resultsPerPage").submit();
						}
					</script>
					<noscript>
						<!-- <s:submit key="content.message.results"></s:submit> -->
						<input type="submit" name="action:changeLimit" style="float:right;" value="<s:property value="getText('content.message.results')" />" />
					</noscript>
		        	
		        	<select style="float:right;" name="limit" class="small" onchange="sendResultsPerPageForm();">
		        			<s:if test="limit==4">
                                <option selected="selected" value="4" >4</option>
                            </s:if>
			        		<s:else>
                                <option value="4" >4</option>
                            </s:else>
			        		<s:if test="size>4">
			        			<s:if test="limit==10">
                                    <option selected="selected" value="10" >10</option>
                                </s:if>
			        			<s:else>
                                    <option value="10" >10</option>
                                </s:else>
								<s:if test="size>10">				        			
					        		<s:if test="limit==20">
                                        <option selected="selected" value="20" >20</option>
                                    </s:if>
					        		<s:else>
                                        <option value="20" >20</option>
                                    </s:else>
					        		<s:if test="size>20">
						        		<s:if test="limit==40">
                                            <option selected="selected" value="40" >40</option>
                                        </s:if>
						        		<s:else>
                                            <option value="40" >40</option>
                                        </s:else>
						        		<s:if test="size>40">
							        		<s:if test="limit==100">
                                                <option selected="selected" value="100" >100</option>
                                            </s:if>
							        		<s:else>
                                                <option value="100" >100</option>
                                            </s:else>
							        	</s:if>
						        	</s:if>
				        		</s:if>
			        		</s:if>
		        	</select>
		        </form>
        	</div>
            <s:if test="xmlTypeId == #xmlTypeIdEAD_FA">
            	<c:set var="batchProcessingEnabled"><s:property value="%{batchProcessingEnabled}"/></c:set>
            	<c:choose>
            		<c:when test="${batchProcessingEnabled}">
                <div style="float: left;">
                <table>
                    <tr>
                        <td>
                            <form action="">
                                <input type="hidden" name="type" value="conversion"/>
                                <input type="hidden" name="xmlTypeId" value="<s:property value="%{xmlTypeId}"/>"/>
                                <input type="hidden" name="pageNumber" value="<s:property value="pageNumber"/>"/>
                                <input type="hidden" name="limit" value="<s:property value="limit"/>"/>
                                <input type="submit" value="<s:property value= "getText('content.message.batch.convert')"/>" id="batchConversion"/>
                            </form>
                        </td>
                        <td>
                            <form action="">
                                <input type="hidden" name="type" value="validation"/>
                                <input type="hidden" name="xmlTypeId" value="<s:property value="%{xmlTypeId}"/>"/>
                                <input type="hidden" name="pageNumber" value="<s:property value="pageNumber"/>"/>
                                <input type="hidden" name="limit" value="<s:property value="limit"/>"/>
                                <input type="hidden" name="searchTerms" value="<s:property value="searchTerms"/>"/>
                                <input type="submit" value="<s:property value= "getText('content.message.batch.validate')"/>" id="batchValidation"/>
                            </form>
                        </td>
                        <c:if test="${empty solr_not_available}">
                            <td>
                                <form action="">
                                    <input type="hidden" name="type" value="indexing"/>
                                    <input type="hidden" name="xmlTypeId" value="<s:property value="%{xmlTypeId}"/>"/>
                                    <input type="hidden" name="pageNumber" value="<s:property value="pageNumber"/>"/>
                                    <input type="hidden" name="limit" value="<s:property value="limit"/>"/>
                                    <input type="submit"  value="<s:property value="getText('content.message.batch.index')"/>" id="batchIndex"/>
                                </form>
                            </td>
                            <td>
                                <form action="">
                                    <input type="hidden" name="type" value="deleting"/>
                                    <input type="hidden" name="xmlTypeId" value="<s:property value="%{xmlTypeId}"/>"/>
                                    <input type="hidden" name="pageNumber" value="<s:property value="pageNumber"/>"/>
                                    <input type="hidden" name="limit" value="<s:property value="limit"/>"/>
                                    <input type="submit" value="<s:property value="getText('content.message.batch.delete')"/>" id="batchDeletion"/>
                                </form>
                            </td>
                            <td>
                                <form action="">
                                    <input type="hidden" name="type" value="deletingFromIndex"/>
                                    <input type="hidden" name="xmlTypeId" value="<s:property value="%{xmlTypeId}"/>"/>
                                    <input type="hidden" name="pageNumber" value="<s:property value="pageNumber"/>"/>
                                    <input type="hidden" name="limit" value="<s:property value="limit"/>"/>
                                    <input type="submit" value="<s:property value="getText('content.message.batch.deletefromindex')"/>" id="batchDeletionFromIndex"/>
                                </form>
                            </td>
                        </c:if>
                    </tr>
                </table>
                <table>
                    <tr>
                        <td>
                             <form action="">
                                <input type="hidden" name="type" value="convertingToEse"/>
                                <input type="hidden" name="xmlTypeId" value="<s:property value="%{xmlTypeId}"/>"/>
                                <input type="hidden" name="pageNumber" value="<s:property value="pageNumber"/>"/>
                                <input type="hidden" name="limit" value="<s:property value="limit"/>"/>
                                <input type="hidden" name="search" value="<s:property value="search"/>"/>
                                <input type="hidden" name="searchTerms" value="<s:property value="searchTerms"/>"/>
                                <input type="hidden" name="orderDate" value="<s:property value="orderDate"/>"/>
                                <input type="submit" value="<s:property value="getText('content.message.batch.converttoese')"/>" id="batchEseConversion"/>
                            </form>
                        </td>
                        <td>
                             <form action="">
                                <input type="hidden" name="type" value="deletingEse"/>
                                <input type="hidden" name="xmlTypeId" value="<s:property value="%{xmlTypeId}"/>"/>
                                <input type="hidden" name="pageNumber" value="<s:property value="pageNumber"/>"/>
                                <input type="hidden" name="limit" value="<s:property value="limit"/>"/>
                                <input type="hidden" name="search" value="<s:property value="search"/>"/>
                                <input type="hidden" name="searchTerms" value="<s:property value="searchTerms"/>"/>
                                <input type="hidden" name="orderDate" value="<s:property value="orderDate"/>"/>
                                <input type="submit" value="<s:property value="getText('content.message.batch.deleteese')"/>" id="batchEseDeleting"/>
                            </form>
                        </td>
                        <td>
                             <form action="">
                                <input type="hidden" name="type" value="deliveringToEuropeana"/>
                                <input type="hidden" name="xmlTypeId" value="<s:property value="%{xmlTypeId}"/>"/>
                                <input type="hidden" name="pageNumber" value="<s:property value="pageNumber"/>"/>
                                <input type="hidden" name="limit" value="<s:property value="limit"/>"/>
                                <input type="hidden" name="search" value="<s:property value="search"/>"/>
                                <input type="hidden" name="searchTerms" value="<s:property value="searchTerms"/>"/>
                                <input type="hidden" name="orderDate" value="<s:property value="orderDate"/>"/>
                                <!-- <input type="submit" value="Deliver current FindingAids to Europeana" id="batchEuropenaDelivering"/>-->
                                <s:submit theme="simple" key="content.message.batch.downloadese" action="downloadAllESE"/>
                            </form>
                        </td>
                        <td>
                             <form action="">
                                <input type="hidden" name="type" value="deliveringToEuropeana"/>
                                <input type="hidden" name="xmlTypeId" value="<s:property value="%{xmlTypeId}"/>"/>
                                <input type="hidden" name="pageNumber" value="<s:property value="pageNumber"/>"/>
                                <input type="hidden" name="limit" value="<s:property value="limit"/>"/>
                                <input type="hidden" name="search" value="<s:property value="search"/>"/>
                                <input type="hidden" name="searchTerms" value="<s:property value="searchTerms"/>"/>
                                <input type="hidden" name="orderDate" value="<s:property value="orderDate"/>"/>
                                <input type="submit" value="Deliver current FindingAids to Europeana" id="batchEuropeanaDelivering"/>
                            </form>
                        </td>
                        <td>
                             <form action="">
                                <input type="hidden" name="type" value="batchEuropeanaDelete"/>
                                <input type="hidden" name="xmlTypeId" value="<s:property value="%{xmlTypeId}"/>"/>
                                <input type="hidden" name="pageNumber" value="<s:property value="pageNumber"/>"/>
                                <input type="hidden" name="limit" value="<s:property value="limit"/>"/>
                                <input type="hidden" name="search" value="<s:property value="search"/>"/>
                                <input type="hidden" name="searchTerms" value="<s:property value="searchTerms"/>"/>
                                <input type="hidden" name="orderDate" value="<s:property value="orderDate"/>"/>
                                <input type="submit" value="Delete FindingAids from Europeana" id="batchEuropeanaDelete"/>
                            </form>
                        </td>
                    </tr>
                    <tr>
                        <td>
                            <form action="">
                                <input type="hidden" name="type" value="batchDoItAll"/>
                                <input type="hidden" name="xmlTypeId" value="<s:property value="%{xmlTypeId}"/>"/>
                                <input type="hidden" name="pageNumber" value="<s:property value="pageNumber"/>"/>
                                <input type="hidden" name="limit" value="<s:property value="limit"/>"/>
                                <input type="hidden" name="search" value="<s:property value="search"/>"/>
                                <input type="hidden" name="searchTerms" value="<s:property value="searchTerms"/>"/>
                                <input type="hidden" name="orderDate" value="<s:property value="orderDate"/>"/>
                                <input type="submit" value="Do it all" id="batchDoItAll"/>
                            </form>
                        </td>
                    </tr>
                </table>
                <c:if test="${empty solr_not_available}">
                    <script type="text/javascript">
                        $("#batchIndex").colorbox(
                            {
                                width:"80%",
                                height:"200px",
                                inline:false,
                                overlayClose:false,
                                href:"batchAction.action?type=indexing"
                            }
                        );
                        $("#batchDeletion").colorbox(
                                {
                                    width:"80%",
                                    height:"200px",
                                    inline:false,
                                    overlayClose:false,
                                    href:"batchAction.action?type=deleting"
                                }
                            );
                        $("#batchDeletionFromIndex").colorbox(
                                {
                                    width:"80%",
                                    height:"200px",
                                    inline:false,
                                    overlayClose:false,
                                    href:"batchAction.action?type=deletingFromIndex"
                                }
                            );
                    </script>
                </c:if>
                <script type="text/javascript">
                    $("#batchConversion").colorbox(
                        {
                            width:"80%",
                            height:"200px",
                            inline:false,
                            overlayClose:false,
                            href:"batchAction.action?type=conversion"
                        }
                    );
                    $("#batchValidation").colorbox(
                        {
                            width:"80%",
                            height:"200px",
                            inline:false,
                            overlayClose:false,
                            href:"batchAction.action?type=validation"
                        }
                    );

                    $("#batchEseConversion").colorbox(
                            {
                                width:"90%",
                                height:"90%",
                                inline:false,
                                iframe: true,
                                overlayClose:false,
                                onCleanup:function(){ location.reload(true); },
                                href:"displayConvertEseBatch.action?search=<s:property value="search"/>&searchTerms=<s:property value="searchTerms"/>"
                            }
                        );
                    $("#batchEseDeleting").colorbox(
                            {
                                width:"80%",
                                height:"200px",
                                inline:false,
                                overlayClose:false,
//                                    onCleanup:function(){ location.reload(true); },
                                href:"batchAction.action?type=deletingEse&search=<s:property value="search"/>&searchTerms=<s:property value="searchTerms"/>"
                            }
                        );
                    $("#batchEuropeanaDelivering").colorbox(
                            {
                                width:"80%",
                                height:"200px",
                                inline:false,
                                overlayClose:false,
                                onCleanup:function(){ location.reload(true); },
                                href:"batchAction.action?type=deliveringToEuropeana&search=<s:property value="search"/>&searchTerms=<s:property value="searchTerms"/>"
                            }
                        );
                    $("#batchEuropeanaDelete").colorbox(
                        {
                            width:"80%",
                            height:"200px",
                            inline:false,
                            overlayClose:false,
                            onCleanup:function(){ location.reload(true); },
                            href:"batchAction.action?type=batchEuropeanaDelete&search=<s:property value="search"/>&searchTerms=<s:property value="searchTerms"/>"
                        }
                    );
                    $("#batchDoItAll").colorbox(
                        {
                            width:"80%",
                            height:"200px",
                            inline:false,
                            overlayClose:false,
                            onCleanup:function(){ location.reload(true); },
                            href:"batchAction.action?type=batchDoItAll"
                        }
                    );
                </script>
                <s:if test="xmlTypeId != #xmlTypeIdEAC_CPF">
                    <s:if test="(searchTerms == null or searchTerms == '') and convertStatus == null and validateStatus == null and indexStatus == null and holdingsGuideStatus == null and convertEseStatus == null and deliverStatus == null">
                        <span class="bold"><s:property value="getText('content.statistics.title.all')"/></span><br/>
                    </s:if>
                    <s:else>
                        <span class="bold"><s:property value="getText('content.statistics.title.search')"/></span><br/>
                    </s:else>
                    <s:property value="getText('content.statistics.converted')"/> <s:property value="totalConvertedFiles"/> / <s:property value="size"/><br/>
                    <s:property value="getText('content.statistics.validated')"/> <s:property value="totalValidatedFiles"/> / <s:property value="size"/><br/>
                    <s:property value="getText('content.statistics.indexed')"/> <s:property value="totalIndexedFiles"/> / <s:property value="size"/><br/>
                    <s:if test="xmlTypeId == #xmlTypeIdEAD_FA">
                        <s:property value="getText('content.statistics.numberofunitsindexed')"/>: <s:property value="totalIndexedUnits"/> / <s:property value="totalIndexedFiles"/> <s:property value="getText('content.statistics.indexedfiles')"/><br />
                        <s:property value="getText('content.statistics.convertedeuropeana')"/> <s:property value="totalConvertedToESEFiles"/> / <s:property value="totalIndexedFiles"/><br/>
                        <s:property value="getText('content.statistics.recordsforeuropeana')"/>: <s:property value="totalRecordsConvertedToESEFiles"/> <s:property value="getText('content.statistics.recordsforeuropeana.created')"/> / <s:property value="totalConvertedToESEFiles"/> <s:property value="getText('content.statistics.recordsforeuropeana.filesprocessed')"/><br/>
                        <s:property value="getText('content.statistics.recordsdeliveredtoeuropeana')"/>: <s:property value="totalRecordsDeliveredToEuropeana"/> / <s:property value="totalRecordsConvertedToESEFiles"/> <s:property value="getText('content.statistics.recordsforeuropeana.filesprocessed')"/><br/>
                    </s:if>
                </s:if>
                <c:if test="${!empty solr_not_available}">
                    <span style="color:red;font-weight:bold;" ><s:property value="content.message.solrerror"/></span>
                </c:if>
            </div>
            </c:when>
            		<c:otherwise><div id="batchProcessMessage"><s:text name="content.batch.disabled"/></div></c:otherwise>
            	</c:choose>
        </s:if>
        <s:if test="xmlTypeId == #xmlTypeIdEAD_HG">
            <a href="hgTreeCreation.action"><s:property value="getText('content.message.label.createHG')"/></a>
        </s:if>
        	<div id="content_div_table" style="width:100%;float:left;margin-bottom:30px;">
				<table>
                    <thead>
                        <tr>
                            <th>
                                <s:property value="getText('content.message.select.label')"/><br/>
                                <span class="linkList" id="selectAll">[<s:property value="getText('content.message.select.all')"/>]</span> - <span class="linkList" id="selectNone">[<s:property value="getText('content.message.select.none')"/>]</span>
                            </th>
                            <th>
                                <s:if test="xmlTypeId == #xmlTypeIdEAC_CPF">
                                    <s:property value="getText('content.message.recordid')" />
                                </s:if>
                                <s:else>
                                    <s:property value="getText('content.message.id')" />
                                    <a class="noStyle" href="content.action?pageNumber=<s:property value="%{pageNumber}" />&amp;xmlTypeId=<s:property value="%{xmlTypeId}"/>&amp;limit=<s:property value="%{limit}" />&amp;orderBy=<s:property value="#orderByEadid" />&amp;orderDecreasing=true&amp;<s:property value="DEFAULT_STATUSES_QUERY_STRING"/>"><img class="noStyle" src="images/expand/arrow-down.gif" alt="down" /></a>
                                    <a class="noStyle" href="content.action?pageNumber=<s:property value="%{pageNumber}" />&amp;xmlTypeId=<s:property value="%{xmlTypeId}"/>&amp;limit=<s:property value="%{limit}" />&amp;orderBy=<s:property value="#orderByEadid" />&amp;orderDecreasing=false&amp;<s:property value="DEFAULT_STATUSES_QUERY_STRING"/>"><img class="noStyle" src="images/expand/arrow-up.gif" alt="up" /></a>
                                </s:else>
                            </th>
                            <th>
                                <s:property value="getText('content.message.title')" />
                                <a class="noStyle" href="content.action?pageNumber=<s:property value="%{pageNumber}" />&amp;xmlTypeId=<s:property value="%{xmlTypeId}"/>&amp;limit=<s:property value="%{limit}" />&amp;orderBy=<s:property value="#orderByTitle" />&amp;orderDecreasing=true&amp;<s:property value="DEFAULT_STATUSES_QUERY_STRING"/>"><img class="noStyle" src="images/expand/arrow-down.gif" alt="down" /></a>
                                <a class="noStyle" href="content.action?pageNumber=<s:property value="%{pageNumber}" />&amp;xmlTypeId=<s:property value="%{xmlTypeId}"/>&amp;limit=<s:property value="%{limit}" />&amp;orderBy=<s:property value="#orderByTitle" />&amp;orderDecreasing=false&amp;<s:property value="DEFAULT_STATUSES_QUERY_STRING"/>"><img class="noStyle" src="images/expand/arrow-up.gif" alt="up" /></a>
                            </th>
                            <th>
                                <s:property value="getText('content.message.date')"/>
                                <a class="noStyle" href="content.action?pageNumber=<s:property value="%{pageNumber}" />&amp;xmlTypeId=<s:property value="%{xmlTypeId}"/>&amp;limit=<s:property value="%{limit}" />&amp;orderBy=<s:property value="#orderByDate" />&amp;orderDecreasing=true&amp;<s:property value="DEFAULT_STATUSES_QUERY_STRING"/>"><img class="noStyle" src="images/expand/arrow-down.gif" alt="down" /></a>
                                <a class="noStyle" href="content.action?pageNumber=<s:property value="%{pageNumber}" />&amp;xmlTypeId=<s:property value="%{xmlTypeId}"/>&amp;limit=<s:property value="%{limit}" />&amp;orderBy=<s:property value="#orderByDate" />&amp;orderDecreasing=false&amp;<s:property value="DEFAULT_STATUSES_QUERY_STRING"/>"><img class="noStyle" src="images/expand/arrow-up.gif" alt="up" /></a>
                            </th>
                            <s:if test="xmlTypeId != #xmlTypeIdEAC_CPF">
                                <th>
                                    <s:property value="getText('content.message.conversion')"/><br/>(<s:property value="nbOfConvertedFiles"/> / <s:property value="listEADCMUnit.size()"/>)
                                    <a class="noStyle" href="content.action?pageNumber=<s:property value="%{pageNumber}" />&amp;xmlTypeId=<s:property value="%{xmlTypeId}"/>&amp;limit=<s:property value="%{limit}" />&amp;orderBy=<s:property value="#orderByFilestateConversion" />&amp;orderDecreasing=true&amp;<s:property value="DEFAULT_STATUSES_QUERY_STRING"/>"><img class="noStyle" src="images/expand/arrow-down.gif" alt="down" /></a>
                                    <a class="noStyle" href="content.action?pageNumber=<s:property value="%{pageNumber}" />&amp;xmlTypeId=<s:property value="%{xmlTypeId}"/>&amp;limit=<s:property value="%{limit}" />&amp;orderBy=<s:property value="#orderByFilestateConversion" />&amp;orderDecreasing=false&amp;<s:property value="DEFAULT_STATUSES_QUERY_STRING"/>"><img class="noStyle" src="images/expand/arrow-up.gif" alt="up" /></a><br/>
                                    <a href="#conversionOptsDiv" class="link_right" id="conversionOpts">Options</a>
                                </th>
                                <th >
                                   <s:property value="getText('content.message.validation')"/><br/>(<s:property value="nbOfValidatedFiles"/> / <s:property value="listEADCMUnit.size()"/>)
                                    <a class="noStyle" href="content.action?pageNumber=<s:property value="%{pageNumber}" />&amp;xmlTypeId=<s:property value="%{xmlTypeId}"/>&amp;limit=<s:property value="%{limit}" />&amp;orderBy=<s:property value="#orderByFilestateValidation" />&amp;orderDecreasing=true&amp;<s:property value="DEFAULT_STATUSES_QUERY_STRING"/>"><img class="noStyle" src="images/expand/arrow-down.gif" alt="down" /></a>
                                    <a class="noStyle" href="content.action?pageNumber=<s:property value="%{pageNumber}" />&amp;xmlTypeId=<s:property value="%{xmlTypeId}"/>&amp;limit=<s:property value="%{limit}" />&amp;orderBy=<s:property value="#orderByFilestateValidation" />&amp;orderDecreasing=false&amp;<s:property value="DEFAULT_STATUSES_QUERY_STRING"/>"><img class="noStyle" src="images/expand/arrow-up.gif" alt="up" /></a>
                                </th>
                                <th>
                                    <s:property value="getText('content.message.indexed')"/><br/>(<s:property value="nbOfIndexedFiles"/> / <s:property value="listEADCMUnit.size()"/>)
                                    <a class="noStyle" href="content.action?pageNumber=<s:property value="%{pageNumber}" />&amp;xmlTypeId=<s:property value="%{xmlTypeId}"/>&amp;limit=<s:property value="%{limit}" />&amp;orderBy=<s:property value="#orderByFilestateIndexation" />&amp;orderDecreasing=true&amp;<s:property value="DEFAULT_STATUSES_QUERY_STRING"/>"><img class="noStyle" src="images/expand/arrow-down.gif" alt="down" /></a>
                                    <a class="noStyle" href="content.action?pageNumber=<s:property value="%{pageNumber}" />&amp;xmlTypeId=<s:property value="%{xmlTypeId}"/>&amp;limit=<s:property value="%{limit}" />&amp;orderBy=<s:property value="#orderByFilestateIndexation" />&amp;orderDecreasing=false&amp;<s:property value="DEFAULT_STATUSES_QUERY_STRING"/>"><img class="noStyle" src="images/expand/arrow-up.gif" alt="up" /></a>
                                </th>
                                <s:if test="xmlTypeId == #xmlTypeIdEAD_FA">
                                    <th>
                                        <s:property value="getText('content.message.holdings')"/>
                                    </th>
                                    <th>
                                        <s:property value="getText('content.message.convert2')"/>
                                        <br/>(<s:property value="nbOfConvertedToEuropeanaFiles"/> / <s:property value="listEADCMUnit.size()"/>)
                                        <a class="noStyle" href="content.action?pageNumber=<s:property value="%{pageNumber}" />&amp;xmlTypeId=<s:property value="%{xmlTypeId}"/>&amp;limit=<s:property value="%{limit}" />&amp;orderBy=<s:property value="#orderByFilestateEseConversion" />&amp;orderDecreasing=true&amp;<s:property value="DEFAULT_STATUSES_QUERY_STRING"/>"><img class="noStyle" src="images/expand/arrow-down.gif" alt="down" /></a>
                                        <a class="noStyle" href="content.action?pageNumber=<s:property value="%{pageNumber}" />&amp;xmlTypeId=<s:property value="%{xmlTypeId}"/>&amp;limit=<s:property value="%{limit}" />&amp;orderBy=<s:property value="#orderByFilestateEseConversion" />&amp;orderDecreasing=false&amp;<s:property value="DEFAULT_STATUSES_QUERY_STRING"/>"><img class="noStyle" src="images/expand/arrow-up.gif" alt="up" /></a>
                                    </th>
                                    <th>
                                        <s:property value="getText('content.message.deliver')"/>
                                        <br/>(<s:property value="nbOfDeliveredToEuropeanaFiles"/> / <s:property value="listEADCMUnit.size()"/>)
                                        <a class="noStyle" href="content.action?pageNumber=<s:property value="%{pageNumber}" />&amp;xmlTypeId=<s:property value="%{xmlTypeId}"/>&amp;limit=<s:property value="%{limit}" />&amp;orderBy=<s:property value="#orderByFilestateEseDelivery" />&amp;orderDecreasing=true&amp;<s:property value="DEFAULT_STATUSES_QUERY_STRING"/>"><img class="noStyle" src="images/expand/arrow-down.gif" alt="down" /></a>
                                        <a class="noStyle" href="content.action?pageNumber=<s:property value="%{pageNumber}" />&amp;xmlTypeId=<s:property value="%{xmlTypeId}"/>&amp;limit=<s:property value="%{limit}" />&amp;orderBy=<s:property value="#orderByFilestateEseDelivery" />&amp;orderDecreasing=false&amp;<s:property value="DEFAULT_STATUSES_QUERY_STRING"/>"><img class="noStyle" src="images/expand/arrow-up.gif" alt="up" /></a>
                                    </th>
                                </s:if>
                                <%-- New column for HG (linked) --%>
                                <s:elseif test="xmlTypeId == #xmlTypeIdEAD_HG">
                                    <th>
                                        <s:property value="getText('content.message.linked')"/>
                                    </th>
                                </s:elseif>
                                <th>
                                    <s:property value="getText('content.message.actions')"/>
                                </th>
                            </s:if>
                        </tr>
                    </thead>
                    <tbody>
                    <%
                        Integer columns = 8;
                        Integer results = (Integer)request.getAttribute("results");
                        if(results==null){
                            results=4; //Results per page
                        }
                    %>
                        <s:iterator var="row" value="listEADCMUnit" status="statusEADCMUnit">
                            <tr>
                                <td>
                                    <input class="checkboxSave" type="checkbox" name="check" id="check_<s:property value="#row.eadCMUnitId"/>" value="<s:property value="#row.eadCMUnitId"/>" />
                                </td>
                                <td class="nocenter">
                                    <s:property value="#row.eadCMUnitEadId"/>
                                </td>
                                <td class="title">
                                       <s:property value="#row.eadCMUnitTitle" />
                                </td>
                                <td>
                                    <s:property value="#row.eadCMUnitUpDate"/>
                                </td>
                                <s:if test="xmlTypeId != #xmlTypeIdEAC_CPF">
                                    <!--Conversion-->
                                    <td>
                                        <s:if test="#row.eadCMUnitState==3 or #row.eadCMUnitState==4 or #row.eadCMUnitState>5">
                                            <span style="color:green;font-weight:bold;" ><s:property value="getText('content.message.ok')"/></span>
                                            <s:iterator var="tst" value="#row.eadCMUnitListWarnings">
                                                <s:if test="#tst.iswarning==true">
                                                    <script type="text/javascript">
                                                        document.write("<br/><input type=\"button\" value=\"<s:property value="getText('content.message.showwarnings')"/>\" id=\"content_showwarning_w_<s:property value="#statusEADCMUnit.index"/>\" ></input>");
                                                        $("#content_showwarning_w_<s:property value="#statusEADCMUnit.index"/>").colorbox({width:"80%", inline:false, href:"showwarnings.action?id=<s:property value="#row.eadCMUnitId"/>&iswarning=true&xmlTypeId=<s:property value="xmlTypeId"/>"});
                                                    </script>
                                                    <noscript>
                                                        <br/><a href="showwarnings.action?id=<s:property value="#row.eadCMUnitId"/>&amp;iswarning=false&amp;xmlTypeId=<s:property value="xmlTypeId"/>" ><s:property value="getText('content.message.showerror')"/></a>
                                                    </noscript>
                                                </s:if>
                                            </s:iterator>
                                        </s:if>
                                        <s:else>
                                            <span style="color:red;font-weight:bold;" ><s:property value="getText('content.message.no')"/></span>
                                            <s:if test="(#row.eadCMUnitState==1 or #row.eadCMUnitState==2)">
                                                <s:if test="xslFiles.size()>1">
                                                    <script type="text/javascript">
                                                        document.write("<select name=\"xslFilesChoice\" onchange=\"changeXslValue(this, 'content_convert_xsl');\">");
                                                        document.write("<s:iterator value="xslFiles" var="xsl">\t<option value=\"<s:property value="#xsl" />\"><s:property value="#xsl"/></option></s:iterator>");
                                                        document.write("</select>");
                                                    </script>
                                                    <noscript>
                                                        <label>
                                                            <select name="content_convert_xsl" >
                                                                <s:iterator value="xslFiles" var="xsl">
                                                                    <option value="<s:property value="#xsl.status" />" ><s:property value="#xsl" /></option>
                                                                </s:iterator>
                                                            </select>
                                                        </label>
                                                    </noscript>
                                                </s:if>
                                            </s:if>
                                        </s:else>
                                    </td>
                                    <!--Validation-->
                                    <td>
                                        <s:if test="#row.eadCMUnitState<4">
                                             <span style="color:red;font-weight:bold;" ><s:property value="getText('content.message.no')"/></span>
                                         </s:if>
                                         <s:elseif test="#row.eadCMUnitState==6">
                                             <span style="color:red;font-weight:bold;" ><s:property value="getText('content.message.fatalerror')"/></span>
                                             <%--<input type="button" value="<s:property value="getText('content.message.showerror')"/>" name="action:showwarnings" onkeypress="window.location='showwarnings.action?id=<s:property value="#row.eadCMUnitId"/>&amp;holding=<s:property value="holding"/>'" onclick="window.location='showwarnings.action?id=<s:property value="#row.eadCMUnitId"/>&amp;holding=<s:property value="holding"/>'" id="content_showwarning"></input>--%>
                                         </s:elseif>
                                         <s:else>
                                             <span style="color:green;font-weight:bold;" ><s:property value="getText('content.message.ok')"/></span><br />
                                                <c:set var="previewEadId"><s:property value="#row.eadCMUnitEadId"/></c:set>
                                                <form action="preview.action" method="get" target="preview${previewEadId}">
                                                    <input type="hidden" name="id" value="<s:property value="#row.eadCMUnitId"/>"/>
                                                    <input type="hidden" name="xmlTypeId" value="<s:property value="xmlTypeId"/>"/>
                                                     <input type="submit" value="<s:property value="getText('content.message.preview')"/>"  id="content_preview"/>
                                                     <!-- download button -->
                                                    <s:if test="#row.eadCMUnitState >= 4">
                                                        <s:submit theme="simple" key="content.message.download" action="download"/>
                                                    </s:if>
                                                 </form>
                                         </s:else>
                                         <s:if test="#row.eadCMUnitState<8">
                                            <s:iterator var="tst" value="#row.eadCMUnitListWarnings">
                                                <s:if test="!#tst.iswarning">
                                                    <script type="text/javascript">
                                                        document.write("<br/><input type=\"button\" value=\"<s:property value="getText('content.message.showerror')"/>\" id=\"content_showwarning_e_<s:property value="#statusEADCMUnit.index"/>\" ></input>");
                                                        $("#content_showwarning_e_<s:property value="#statusEADCMUnit.index"/>").colorbox({width:"80%", inline:false, href:"showwarnings.action?id=<s:property value="#row.eadCMUnitId"/>&iswarning=false&xmlTypeId=<s:property value="xmlTypeId"/>"});
                                                    </script>
                                                    <noscript>
                                                        <br/><a href="showwarnings.action?id=<s:property value="#row.eadCMUnitId"/>&amp;iswarning=false&amp;xmlTypeId=<s:property value="xmlTypeId"/>" ><s:property value="getText('content.message.showerror')"/></a>
                                                    </noscript>
                                                </s:if>
                                            </s:iterator>
                                         </s:if>
                                    </td>
                                     <!--index-->
                                    <td>
                                        <s:if test="#row.eadCMUnitState<=6" >
                                            <span style="color:red;font-weight:bold;" ><s:property value="getText('content.message.no')"/></span>
                                        </s:if>
                                        <s:elseif test="#row.eadCMUnitState==7">
                                            <span style="font-weight:bold;" ><s:property value="getText('content.message.indexing')"/></span>
                                        </s:elseif>
                                        <s:elseif test="#row.eadCMUnitState>=8&&#row.eadCMUnitState<15" >
                                            <span style="color:green;font-weight:bold;" ><s:property value="getText('content.message.ok')"/></span>
                                            <br />
                                            <s:if test="#row.countEadCMUnitDocs >= 0">
                                            <s:property value="getText('content.message.units')" /> <span style="color:black;font-weight:bold;" ><s:property value="#row.countEadCMUnitDocs" /></span>
                                            </s:if><s:else><s:property value="getText('content.message.units')" /> <span style="color:red;font-weight:bold;" ><s:property value="content.message.error" /></span></s:else>
                                        </s:elseif>
                                        <s:elseif test="#row.eadCMUnitState==15">
                                            <span style="color:green;font-weight:bold;" ><s:property value="getText('content.message.readytoindex')"/><p>&nbsp;</p>(<s:property value="getText('content.message.queuePosition')"/>: <s:property value="#row.queuePosition"/>)</span>
                                            <br />
                                            <s:if test="indexError!=null">
                                            	<br/>
                                            	<span style="color:red;font-weight:bold;"><s:property value="getText('content.message.indexerror')"/></span>
                                            </s:if>
                                        </s:elseif>
                                    </td>
                                    <s:if test="xmlTypeId == #xmlTypeIdEAD_FA">
                                        <!-- HG Title -->
                                        <td >
                                            <s:if test="{#row.eadCMUnitHolding!=null}">
                                                <s:property value="#row.eadCMUnitHolding"/>
                                            </s:if>
                                            <s:else>
                                                <s:property value="getText('content.message.noholdings')"/>
                                            </s:else>
                                        </td>
                                        <!-- Converted to Europeana -->
                                        <td>
                                            <s:if test="#row.eadCMUnitState<=8||#row.eadCMUnitState==12||#row.eadCMUnitState==15">
                                                <span style="color:red;font-weight:bold;" ><s:property value="getText('content.message.no')"/></span>
                                            </s:if>
                                            <s:else>
                                                <span style="color:green;font-weight:bold;" ><s:property value="getText('content.message.ok')"/></span>
                                                
                                                <text value="content.message.daos" />(<span style="color:black;font-weight:bold;" ><s:property value="#row.numberOfDAOs" /></span>)
                                                <br /><c:if test="${row.numberOfDAOs > 0}">
                                                <form action="htmlPreview" method="get" target="_blank">
                                                    <input type="hidden" name="id" value="<s:property value="#row.eadCMUnitId"/>"/>
                                                    <input type="submit" value="<s:property value="getText('content.message.preview.ese')"/>"  id="content_preview_ese"/>
                                                 </form>
                                                <s:form action="downloadEse" method="get">
                                                    <input type="hidden" name="id" value="<s:property value="#row.eadCMUnitId"/>"/>
                                                    <input type="submit" value="<s:property value="getText('content.message.download.ese')"/>"  id="content_download_ese"/>
                                                 </s:form>
                                                 </c:if>
                                            </s:else>
                                        </td>
                                        <!-- Deliver to Europeana -->
                                        <td>
                                            <!--<c:if test="${row.numberOfDAOs == 0}">
                                            <s:if test="harvesting==2 && #row.eadCMUnitState==9">
                                                    <span style="font-weight:bold;" ><s:property value="getText('content.message.nounits')"/></span>
                                            </s:if>
                                            </c:if>-->
                                            <s:if test="#row.eadCMUnitState<=9 || #row.eadCMUnitState==12 || #row.eadCMUnitState==15">
                                                <span style="color:red;font-weight:bold;" ><s:property value="getText('content.message.no')"/></span>
                                            </s:if>
                                            <s:elseif test="#row.eadCMUnitState==10">
                                                    <span style="font-weight:bold;" ><s:property value="getText('content.message.delivered')"/></span>
                                            </s:elseif>
                                            <s:else>
                                                <span style="font-weight:bold;" ><s:property value="getText('content.message.harvested')"/></span>
                                            </s:else>
                                        </td>
                                    </s:if>
                                    <!-- Linked -->
                                    <s:if test="xmlTypeId == #xmlTypeIdEAD_HG">
                                        <td>
                                            <s:if test="#row.eadCMUnitState<=13 || #row.eadCMUnitState==15">
                                                    <span style="color:red;font-weight:bold;" ><s:property value="getText('content.message.no')"/></span>
                                                </s:if><s:else>
                                                    <br /><span style="font-weight:bold;"><s:property value="#row.findingAidsLinked" />/<s:property value="#row.possibleFindingAidsLinked" /></span>
                                                    <s:if test="#row.findingAidsLinked!=#row.possibleFindingAidsLinked">
                                                        <%--<s:if test="findingAidsLinked && listNotLinkedId!=#row.eadCMUnitId" > --%>
                                                        <s:if test="listNotLinkedId!=#row.eadCMUnitId" >
                                                            <s:form theme="simple" method="post" action="getFindingAidsNotLinked">
                                                                <s:hidden name="xmlTypeId" />
                                                                <s:hidden name="pageNumber"/>
                                                                <s:hidden name="limit" />
                                                                <s:hidden name="id" value="%{#row.eadCMUnitId}"/>
                                                                <s:hidden name="convertStatus"/>
                                                                <s:hidden name="validateStatus"/>
                                                                <s:hidden name="indexStatus"/>
                                                                <s:hidden name="holdingsGuideStatus"/>
                                                                <s:hidden name="convertEseStatus"/>
                                                                <s:hidden name="deliverStatus"/>
                                                                <s:submit theme="simple" key="content.message.getFindingAidsNotLinked" />
                                                            </s:form>
                                                        </s:if><s:elseif test="listNotLinkedId==#row.eadCMUnitId">
                                                        	<img id="loadingPageGif" alt="loading, please wait" src="images/colorbox/loading.gif">
                                                            <p id="pDivIdentifiersPopUp">
                                                                    <div id="divIdentifiersPopUp" style="border:1px black solid;background-color:white;margin:10px;text-align:justify;display:none;">
                                                                        <div id="maxDivIdentifiersPopUp" style="background-color:#aaa;padding-top:2px;padding-bottom:2px;float:left;width:100%;text-align:right;"><span id="maxDivIdentifiersPopUpIcon2" style="border: 1px solid grey;float:right;cursor:pointer;padding:1px;margin-right:3px;background-color:#ccc;width:15px;height:15px;text-align:center;">X</span><span id="maxDivIdentifiersPopUpIcon" style="border: 1px solid grey;float:right;cursor:pointer;padding:1px;margin-right:3px;background-color:#ccc;width:15px;height:15px;text-align:center;">+</span></div>
                                                                        <div id="identifiersContainer">
                                                                            <%-- Finding aids not uploaded yet --%>
                                                                            <div id="findingAidsNotUploadedYetDiv" style="border:1px groove black;margin-top:20px;">
                                                                                <p id="findingFigure1" style="background-color:#c1c1c1;text-align:center;"><s:property value="getText('content.message.findingaidsout')"/>: <span style="font-weight:bold;"><s:property value="listOfFindingAidsOut.size()"/></span></p>
                                                                                <s:if test="listOfFindingAidsOut.size()>0">
                                                                                    <div id="listOfFindingAidsOut" style="overflow-x:hidden;overflow-y:auto;height:80px;">
                                                                                    <div style="float:left;width:29%;text-align:center;"><s:property value="getText('content.message.id')"/></div><div style="float:right;width:70%;text-align:center;"><s:property value="getText('content.message.title')"/></div>
                                                                                    <s:iterator var="clItem" value="listOfFindingAidsOut" >
                                                                                        <div style="float:right;border:1px solid;border-radius:2px 2px 2px 2px;width:99%;background-color:#c1c1c1;">
                                                                                    		<div style="float:left;width:29%;"><span style="float:left;text-align:justify;font-weight:10px;width:100%;"><span style="font-style:italic;"><s:property value="#clItem.hrefEadid" /></span></span></div>
                                                                                            <div style="float:right;width:70%;border-left: 2px;"><span style="float:left;text-align:justify;font-weight:10px;width:100%;"><span style="font-weight:bold;" ><s:property value="#clItem.unittitle" /></span></span></div>
                                                                                        </div>
                                                                                    </s:iterator>
                                                                                    </div>
                                                                                </s:if>
                                                                            </div>
                                                                            <%-- Finding aids not indexed yet --%>
                                                                            <div id="findingAidsNotIndexedYetDiv" style="border:1px groove black;margin-top:20px;">
                                                                                <p id="findingFigure2" style="background-color:#d1d1d1;text-align:center;"><s:property value="getText('content.message.findingaidsnotindexed')"/>: <span style="font-weight:bold;"><s:property value="listOfFindingAidsNotIndex.size()"/></span></p>
                                                                                <s:if test="listOfFindingAidsNotIndex.size()>0">
                                                                                	<div style="float:left;width:29%;text-align:center;"><s:property value="getText('content.message.id')"/></div><div style="float:right;width:70%;text-align:center;"><s:property value="getText('content.message.title')"/></div>
                                                                                    <div id="listOfFindingAidsNotIndex" style="overflow-x:hidden;overflow-y:auto;height:80px;">
                                                                                        <s:iterator var="faItem" value="listOfFindingAidsNotIndex" >
                                                                                            <div style="float:right;border:1px solid;border-radius:2px 2px 2px 2px;width:99%;background-color:#d1d1d1;">
                                                                                            	<div style="float:left;width:29%;"><span style="float:left;text-align:justify;font-weight:10px;width:100%;"><span style="font-style:italic;"><s:property value="#faItem.eadid" /></span></span></div>
                                                                                                <div style="float:right;width:70%;border-left: 2px;"><span style="float:left;text-align:justify;font-weight:10px;width:100%;"><a style="text-align:justify;" href="content.action?searchTerms=<s:property value="#faItem.eadid" />&xmlTypeId=0"><s:property value="#faItem.title" /></a></span></div>
                                                                                            </div>
                                                                                        </s:iterator>
                                                                                    </div>
                                                                                </s:if>
                                                                            </div>
                                                                            <%-- Finding aids indexed --%>
                                                                            <div id="findingAidsIndexedDiv" style="border:1px groove black;margin-top:20px;">
                                                                                <p id="findingFigure3" style="background-color:#d9d9d9;text-align:center;"><s:property value="getText('content.message.findingAidsLinked')"/>: <span style="font-weight:bold;"><s:property value="listOfFindingAidsIndexed.size()"/></span></p>
                                                                                <s:if test="listOfFindingAidsIndexed.size()>0">
                                                                                    <div id="listOfFindingAidsIndexed" style="overflow-x:hidden;overflow-y:auto;height:80px;">
                                                                                    <div style="float:left;width:29%;text-align:center;"><s:property value="getText('content.message.id')"/></div><div style="float:right;width:70%;text-align:center;"><s:property value="getText('content.message.title')"/></div>
	                                                                                <s:iterator var="faItem" value="listOfFindingAidsIndexed" >
	                                                                                    <div style="float:right;border:1px solid;border-radius:2px 2px 2px 2px;width:99%;background-color:#d9d9d9;">
	                                                                                      	<div style="float:left;width:29%;"><span style="float:left;text-align:justify;font-weight:10px;width:100%;"><span style="font-style:italic;"><s:property value="#faItem.eadid" /></span></span></div>
	                                                                                        <div style="float:right;width:70%;border-left: 2px;"><span style="float:left;text-align:justify;font-weight:10px;width:100%;"><a style="text-align:justify;" href="content.action?searchTerms=<s:property value="#faItem.eadid" />&xmlTypeId=0"><s:property value="#faItem.title" /></a></span></div>
	                                                                                    </div>
	                                                                                </s:iterator>
                                                                                    </div>
                                                                                </s:if>
                                                                            </div>
                                                                        </div>
                                                                    </div>
                                                                    <script type="text/javascript">
                                                                    	$(document).ready(function(){$("img#loadingPageGif").remove();});
	                                                                    function createPopup(){
	                                                                        var popupwindow = window.open(Math.random().toString(), "pw"+Math.random(), "location=1,status=1,scrollbars=1,width=640,height=480");
	                                                                        popupwindow.moveTo(0, 0);
	                                                                        var popupContent = "<html>\n<head>\n\t<title>"+new Date().toLocaleString()+"\n</title></head>\n<body>\n<div id=\"popup\" style=\"width:100%;height:100%;float:left;\">\n";
	                                                                        popupContent += "<script src=\"js/jquery/jquery_1.4.2.min.js\" type=\"text/javascript\"></script";
	                                                                        popupContent += ">\n<script type=\"text/javascript\">\n";
	                                                                        popupContent += "function hideDiv(show,hide1,hide2){\n";
	                                                                        popupContent += "	$(\"#\"+show).show();\n ";
	                                                                        popupContent += "	$(\"#\"+hide1).hide();\n ";
	                                                                        popupContent += "	$(\"#\"+hide2).hide();\n  }\n </script";
	                                                                        popupContent += "> <div style=\"float:left;font-size:12px;width:100%;\">\n";
	                                                                        popupContent += "<div id=\"tab1\" style=\"cursor:pointer;background-color:#c1c1c1;opacity:0.7;height:40px;float:left;width:33%;text-align:center;\" onclick=\"hideDiv('contentTab1','contentTab2','contentTab3')\">"+$("#findingFigure1").html()+"</div>";
	                                                                        popupContent += "<div id=\"tab2\" style=\"cursor:pointer;background-color:#d1d1d1;opacity:0.7;height:40px;float:left;width:33%;margin-left:2px;text-align:center;\" onclick=\"hideDiv('contentTab2','contentTab1','contentTab3')\">"+$("#findingFigure2").html()+"</div>";
	                                                                        popupContent += "<div id=\"tab3\" style=\"cursor:pointer;background-color:#d9d9d9;opacity:0.7;height:40px;float:left;width:33%;margin-left:2px;text-align:center;\" onclick=\"hideDiv('contentTab3','contentTab1','contentTab2')\">"+$("#findingFigure3").html()+"</div>\n</div>";
		                                                                        popupContent += "<div id=\"contentTab1\" style=\"cursor:default;margin-top:10px;font-size:12px;opacity:0.9;\">\n";
		                                                                        if($("#listOfFindingAidsOut").html()!=null){
		                                                                        	popupContent += $("#listOfFindingAidsOut").html();
		                                                                        }
		                                                                        popupContent += "</div>\n";
		                                                                        popupContent += "<div id=\"contentTab2\" style=\"display:none;font-size:12px;opacity:0.9;\">\n";
		                                                                        if($("#listOfFindingAidsNotIndex").html()!=null){
		                                                                        	popupContent += $("#listOfFindingAidsNotIndex").html();
		                                                                        }
		                                                                        popupContent += "</div>\n ";
		                                                                        popupContent += "<div id=\"contentTab3\" style=\"display:none;font-size:12px;opacity:0.9;\">\n";
		                                                                        if($("#listOfFindingAidsIndexed").html()!=null){
		                                                                        	popupContent += $("#listOfFindingAidsIndexed").html();
		                                                                        }
		                                                                        popupContent += "</div>\n";
	                                                                        popupContent += "</div>\n</div>\n";
	                                                                        popupContent +="</body>\n</html>";
	                                                                        popupwindow.document.write(popupContent);
	                                                                    }
	                                                                    function appendSFNLYButton(){
	                                                                        $("#divIdentifiersPopUp").hide();
	                                                                        $("#pDivIdentifiersPopUp").append('<input type="button" id="showIdentifiersPopUp" value="<s:property value="getText('content.message.getFindingAidsNotLinked')" />" onclick="showPopUpButton()" />');
	                                                                    }
	                                                                    $("#maxDivIdentifiersPopUpIcon").click(function(){
	                                                                        if($("#divIdentifiersPopUp").css("position")!="absolute"){
	                                                                            $("#divIdentifiersPopUp").css("position","absolute");
	                                                                            $("#divIdentifiersPopUp").css("top","0px");
	                                                                            $("#divIdentifiersPopUp").css("left","0px");
	                                                                            $("#divIdentifiersPopUp").css("height","100%");
	                                                                            $("#maxDivIdentifiersPopUpIcon").html("-");
	                                                                            $("#listOfFindingAidsNotIndex").css("height","200px");
	                                                                            $("#listOfFindingAidsIndexNotLinked").css("height","200px");
	                                                                            $("#listOfFindingAidsOut").css("height","200px");
	                                                                            $("#listOfFAEadIdLinked").css("height","200px");
	                                                                        }else{
	                                                                            $("#divIdentifiersPopUp").css("position","");
	                                                                            $("#divIdentifiersPopUp").css("top","");
	                                                                            $("#divIdentifiersPopUp").css("left","");
	                                                                            $("#divIdentifiersPopUp").css("height","");
	                                                                            $("#maxDivIdentifiersPopUpIcon").html("+");
	                                                                            $("#listOfFindingAidsNotIndex").css("height","80px");
	                                                                            $("#listOfFindingAidsIndexNotLinked").css("height","80px");
	                                                                            $("#listOfFindingAidsOut").css("height","80px");
	                                                                            $("#listOfFAEadIdLinked").css("height","80px");
	                                                                        }
	                                                                    });
	                                                                    $("#maxDivIdentifiersPopUpIcon2").click(appendSFNLYButton());
	                                                                    $("#maxDivIdentifiersPopUpIcon3").click(createPopup());
	                                                                    function showPopUpButton(){
	                                                                    	createPopup();
	                                                                        //$("#divIdentifiersPopUp").show();
	                                                                        //$("#showIdentifiersPopUp").remove();
	                                                                    }
                                                                    </script>
                                                            </p>
                                                        </s:elseif>
                                                    </s:if>
                                                </s:else>
                                        </td>
                                    </s:if>
                                    <!-- Actions -->
                                    <td  class="actions">
                                        <s:form theme="simple" method="post" id="formActions_%{#statusEADCMUnit.index}">
                                            <s:hidden name="id" id="id" value="%{#row.eadCMUnitId}"/>
                                            <s:hidden name="xmlTypeId" id="xmlTypeId" value="%{xmlTypeId}"/>
                                            <s:hidden name="pageNumber" id="pageNumber" value="%{pageNumber}"/>
                                            <s:hidden name="limit" id="limit" value="%{limit}"/>
                                            <s:hidden id="searchTerms" name="searchTerms" value="%{searchTerms}" />
                                            <s:hidden name="orderBy" id="orderBy" value="%{orderBy}"/>
                                            <s:hidden name="orderDecreasing" id="orderDecreasing" value="%{orderDecreasing}"/>
                                            <s:hidden name="convertStatus"/>
                                            <s:hidden name="validateStatus"/>
                                            <s:hidden name="indexStatus"/>
                                            <s:hidden name="holdingsGuideStatus"/>
                                            <s:hidden name="convertEseStatus"/>
                                            <s:hidden name="deliverStatus"/>
                                            <s:hidden name="harvesting" id="harvesting" value="%{harvesting}"/>

                                            <!-- validate button -->
                                            <s:if test="#row.eadCMUnitState==1 || #row.eadCMUnitState==3">
                                                <s:submit theme="simple" key="content.message.validate" action="validate" cssClass="content_%{#statusEADCMUnit.index}"/>
                                            </s:if>
                                            <!-- convert button -->
                                            <s:if test="#row.eadCMUnitState==1 || #row.eadCMUnitState==2">
                                                <s:if test="#row.eadCMUnitState==2">
                                                    <s:hidden name="xsl"  value="default.xsl"/>
                                                </s:if>
                                                <s:submit theme="simple" key="content.message.convert" cssClass="content_convert_%{#statusEADCMUnit.index}"/>
                                            </s:if>
                                            <!-- edition button -->
                                            <s:if test="#row.eadCMUnitState == 1 || #row.eadCMUnitState == 2 || #row.eadCMUnitState == 3 || #row.eadCMUnitState == 4 || #row.eadCMUnitState == 5">
                                                <s:submit theme="simple" key="label.edit" action="editEad" onclick="window.open('editEad.action?id=%{#row.eadCMUnitId}&xmlTypeId=%{xmlTypeId}'); return false;"/>
                                            </s:if>
                                            <!-- index button -->
                                            <c:if test="${(row.eadCMUnitState == 4 or row.eadCMUnitState == 5) and empty solr_not_available}">
                                                <s:submit id="indextotemp" theme="simple" key="content.message.index" action="indextotemp" cssClass="content_%{#statusEADCMUnit.index}">
                                                <input type="hidden" name="type" value="indextotemp"/>
                                                </s:submit>
                                            </c:if>
                                            <!--  convert to ese button -->
                                            <s:if test="#row.eadCMUnitState==8">
                                                <s:submit theme="simple" key="content.message.convert.ese" action="displayEseConvert" cssClass="content_%{#statusEADCMUnit.index}"/>
                                            </s:if>
                                            <!--  Deliver to Europeana button -->
                                            <s:if test="#row.eadCMUnitState==9 && #row.numberOfDAOs > 0">
                                                <s:submit theme="simple" key="content.message.deliver.europeana" action="deliverToEuropeana" cssClass="content_%{#statusEADCMUnit.index}"/>
                                            </s:if>
                                            <!--  Delete from Europeana button -->
                                            <s:if test="#row.eadCMUnitState==10 && #row.numberOfDAOs > 0">
                                                <s:submit theme="simple" key="content.message.delete.europeana" action="deleteFromEuropeana" cssClass="content_%{#statusEADCMUnit.index}"/>
                                            </s:if>
                                             <!-- delete ESE files -->
                                            <c:if test="${row.eadCMUnitState > 8 and row.numberOfDAOs > 0}">
                                                <s:submit theme="simple" key="content.message.delete.ese" action="deleteEse" cssClass="content_%{#statusEADCMUnit.index}" />
                                            </c:if>
                                            <!-- delete from index -->
                                            <c:if test="${(row.eadCMUnitState >= 8 and row.eadCMUnitState != 15) and empty solr_not_available}">
                                                <s:submit theme="simple" key="content.message.delete2" action="deleteFromIndex" cssClass="content_%{#statusEADCMUnit.index}" />
                                            </c:if>
                                            <!-- delete from being indexed button -->
                                            <c:if test="${row.eadCMUnitState==15}">
                                                <s:submit theme="simple" key="content.message.deletefromIndexqueue" action="deletefromindexqueue" cssClass="content_%{#statusEADCMUnit.index}" >
                                                <input type="hidden" name="type" value="deletefromindexqueue"/>
                                                </s:submit>
                                            </c:if>
                                            <!-- delete button -->
                                            <c:if test="${(row.eadCMUnitState != 7 and empty solr_not_available)}">
                                                <s:submit theme="simple" key="content.message.delete" action="delete" cssClass="content_%{#statusEADCMUnit.index}" />
                                            </c:if>

                                        </s:form>
                                
                                        <script type="text/javascript" language="javascript">
                                            $(".content_<s:property value="#statusEADCMUnit.index"/>").colorbox(
                                                {
                                                    width:"60%",
                                                    height:"100px",
                                                    overlayClose:true,
                                                    inline:true,
                                                    href:"#waitDivNormal",
                                                    onLoad:function(){
                                                        $("#cboxClose").unbind();
                                                        $("#cboxClose").css("display", "none");                                                        
                                                        var doAction = true;
                                                        var actionName = $(this).attr("name");
                                                        if(actionName == "action:deleteFromIndex"){
                                                            doAction = confirmDeleteFromIndex();
                                                        } else if(actionName == "action:delete"){
                                                            doAction = confirmDelete();
                                                        }
                                                        if(doAction){                                                        	 
                                                            $form = $("#formActions_<s:property value="#statusEADCMUnit.index"/>");
                                                            $form.attr("action", actionName.split(":")[1] + ".action");                                                            
                                                            if ((actionName.split(":")[1]=="indextotemp")||(actionName.split(":")[1]=="deletefromindexqueue")){                                                            	
																$.getJSON("indexingLaunched.action",function(response){																	
																	 $.each(response, function(key, val) {																		
																		if(val == "true"){																		
																			$("#cboxClose").css("display", "inline");
																			if (actionName.split(":")[1]=="indextotemp"){
																				$("#waitDivNormal").html("<s:property value="getText('content.message.queue.schedulingNotPossible')"/>");
																			}else{
																				$("#waitDivNormal").html("<s:property value="getText('content.message.queue.deletescheduledNotPossible')"/>");
																			}
																			$("#cboxClose").click(function(){																				
																				$.fn.colorbox.close();
																			});
																		}else{
																			$form.submit();
																		}
																	 });
																});														
															}else{
																$form.submit();
															}														                                                           
                                                        } else {
                                                            $.fn.colorbox.close();
                                                        }
                                                    }
                                                }
                                            );
                                            $(".content_convert_<s:property value="#statusEADCMUnit.index"/>").colorbox(
                                                {
                                                    width:"40%",
                                                    height:"70px",
                                                    overlayClose:false,
                                                    inline:true,
                                                    href:"#waitDiv",
                                                    onComplete:function(){
                                                        createLoop('${pageContext.request.contextPath}', '${aiId}', '${row.eadCMUnitId}', '${xmlTypeId}');
                                                    }
                                                }
                                            );
                                            $("#conversionOpts").colorbox(
                                                {
                                                    width:"80%",
                                                    height:"200px",
                                                    inline:true,
                                                    overlayClose:false,
                                                    onLoad:function(){ checkCurrentOpts("${pageContext.request.contextPath}"); },
                                                    href: "#conversionOptsDiv"
                                                }
                                            );
                                        </script>
                                    </td>
                                </s:if>
                            </tr>
                        </s:iterator>
                    </tbody>
		        </table>
		     	<s:if test="size>4">
		     		<div style="height: 30px;"></div>
		     	</s:if>
		    </div>
	    </div>
	    <div style="display:none;"><div id="waitDivNormal"><s:property value="getText('content.message.processwaiting')"/></div></div>
        <div style="display:none;"><div id="waitDiv"><s:property value="getText('content.message.processwaiting')"/><div id="progressbar"></div><div id="progresstext"></div></div></div>
        <div style="display:none;">
            <div id="conversionOptsDiv">
                <form action="">
                    <table>
                        <tr>
                            <td>Use existing role type if found? <input type="checkbox" name="useExistingRole" id="useExistingRole" value="useExistingRole" /></td>
                            <td>Default role type:</td>
                        </tr>
                        <tr>
                            <td></td>
                            <td><input type="radio" class="roleTypeRadio" name="roleType" value="IMAGE" />IMAGE</td>
                        </tr>
                        <tr>
                            <td></td>
                            <td><input type="radio" class="roleTypeRadio" name="roleType" value="VIDEO" />VIDEO</td>
                        </tr>
                        <tr>
                            <td></td>
                            <td><input type="radio" class="roleTypeRadio" name="roleType" value="SOUND" />SOUND</td>
                        </tr>
                        <tr>
                            <td></td>
                            <td><input type="radio" class="roleTypeRadio" name="roleType" value="TEXT" />TEXT</td>
                        </tr>
                        <tr>
                            <td></td>
                            <td><input type="radio" class="roleTypeRadio" name="roleType" value="UNSPECIFIED" />UNSPECIFIED</td>
                        </tr>
                    </table>
                    <input type="button" id="submitBtnRoleType" value="Submit"/>
                    <input type="button" id="cancelBtnRoleType" value="Cancel"/>
                </form>
            </div>
        </div>
    	    
		<div id="texto" class="indexMessage" style="display:none;"><s:property value ="getText('content.message.queue.schedulingNotPossible')"></s:property></div>
		     
        <script type="text/javascript">
            $(document).ready(function(){
                createProgressBar();
            });
        </script>
	</div>