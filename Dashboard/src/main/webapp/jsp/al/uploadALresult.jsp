<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles" %>
<div style="width:100%">
   
   		<s:actionmessage/>
        <s:if test="changeidentifierchance">
        		<s:property value="getText('al.message.changeidentifierchance')"/> <s:a label="here" action="changeAlIdentifiers">here</s:a> <s:property value="getText('al.message.changeidentifierchance2')"/>
        </s:if>
   
        <form id = "httpOverwriteAL"  enctype="multipart/form-data" >
           
           <br></br>
           
           <s:if test="cancel==false">           
		        <s:if test="archivalInstitutionsToDelete.size()>0"> 
		        <s:property value="getText('al.message.institutionstodelete')"/>
		        <br></br>
			        <s:iterator id="archivalInstitutionsToDelete" status="stat" value="archivalInstitutionsToDelete" var="row">
						<option value="<s:property value="#row.aiId" />" ><s:property value="#row.ainame" /></option>
						<p><s:textfield id="archivalinstitutionid%{#stat.index}" name="identifier" value="%{#row.internalAlId}" required="true" key="label.identier" ></s:textfield></p>					
						<p><s:textfield id="aiid" name="aiid" value="%{#row.aiId}" cssStyle="display:none;" ></s:textfield></p>
					</s:iterator>
				</s:if>        
		        <br></br>
	       
		        <s:if test="archivalInstitutionsToInsert.size()>0"> 
		        <s:property value="getText('al.message.institutionstoinsert')"/>
		        <br></br>
			        <s:iterator value="archivalInstitutionsToInsert" var="row">
						<option value="<s:property value="#row.aiId" />" ><s:property value="#row.ainame" />
						</option>
						<p><s:textfield value="%{#row.internalAlId}" cssStyle="background-color:#ECE9EA;" theme="simple"></s:textfield></p>						
					</s:iterator> 
					<br></br>        
				</s:if>
				
				<s:if test="archivalInstitutionsNameChanged.size()>0">
		        	<s:property value="getText('al.message.changedprimaryname')" />
		        	<br></br>            
		        	<s:iterator value="archivalInstitutionsNameChanged" var="row">
		        		<option value="<s:property value="#row.aiAnId" />"> <s:property value="#row.AiAName" /></option><br></br>
		        	</s:iterator>
		        	<br></br>
		        </s:if>
		        
		        <s:if test="archivalInstitutionsParentChanged.size()>0">
		        	<s:property value="getText('al.message.changedparent')" />
		        	<br></br>        
		        	<s:iterator value="archivalInstitutionsParentChanged" var="row">
		        		<option value="<s:property value="#row.aiId" />"> <s:property value="#row.ainame" /></option><br></br>
		        	</s:iterator>
		        	<br></br>
		        </s:if>   
			</s:if>      
	        
	        <s:if test="checking==false">
	        	<s:hidden name="httpFileFileName" value="%{httpFileFileName}"></s:hidden>		
   				<s:submit id="1" key="label.storeidentifier" action="changeAlIdFromUpload" onclick = "addCheckButton();" theme="simple" cssClass="mainButton"></s:submit>
   			</s:if>
        <br></br>
        </form>
          
        <s:if test="archivalInstitutionsNameNotChanged.size()>0">
        	<s:property value="getText('al.message.notchangedprimaryname')" />
        	<br></br>            
        	<s:iterator value="archivalInstitutionsNameNotChanged" var="row">
        		<option value="<s:property value="#row.aiAnId" />"> <s:property value="#row.AiAName" /></option><br></br>
        	</s:iterator>
        	<br></br>
        </s:if>
        
        <s:if test="archivalInstitutionsParentNotChanged.size()>0">
        	<s:property value="getText('al.message.notchangedparent')" />
        	<br></br>
        	<s:iterator value="archivalInstitutionsParentNotChanged" var="row">
        		<option value="<s:property value="#row.aiId" />"> <s:property value="#row.ainame" /></option><br></br>
        	</s:iterator>
        	<br></br>
        </s:if>
        
		<s:if test="changeidentifierchance">
       			<s:submit id="2" key="label.changeidentifier" onclick ="editIdentifier();" theme="simple"  cssClass="mainButton"><s:hidden name="Overwrite" value="false"></s:hidden></s:submit>
       	</s:if>
       <br></br>
        <table width="20%">
	    <tr>
	        <s:if test="checking">
	        	<td >
	        		<s:form action="httpOverwriteAL" >
	        			<s:hidden name="httpFileFileName" value="%{httpFileFileName}"></s:hidden>
	        			<s:submit  key="Checking again"  cssClass="mainButton"><s:hidden name="Overwrite" value="true" theme="simple" ></s:hidden></s:submit>
	        		</s:form>       			
	        	</td>
	        </s:if>
	        <s:elseif test="cancel">
	        
		        <td>
		        <s:form id ="nooverwrite" action="httpOverwriteAL">
		        		<s:submit key="label.cancel" action="httpOverwriteAL" theme="simple"><s:hidden name="Overwrite" value="false"></s:hidden></s:submit>
		        </s:form>       
		        </td>	        
	        </s:elseif>
	        <s:else>
	        	<td>
		    	<s:form id ="over" action="httpOverwriteAL">        		
		        		<s:submit key="label.upload" action="httpOverwriteAL" theme="simple"><s:hidden name="httpFileFileName" value="%{httpFileFileName}"></s:hidden><s:hidden name="resultAL" value="true"></s:hidden><s:hidden name="Overwrite" value="true"></s:hidden>
		        		</s:submit>       			
		        </s:form>	        
		        </td>	        
		        <td>
		        <s:form id ="nooverwrite" action="httpOverwriteAL">
		        		<s:submit key="label.cancel" action="httpOverwriteAL" theme="simple"><s:hidden name="Overwrite" value="false"></s:hidden></s:submit>
		        </s:form>       
		        </td>	    
	        
	        </s:else>	         
	     </tr>
		</table>
		
       <script type="text/javascript">
       		
       		document.getElementById(1).style.display='none';       		
       		var cont = 0;
       		var identifier = "archivalinstitutionid";
       		var httpOverwriteAL = document.getElementById("httpOverwriteAL");
			for (var i = 0; i< httpOverwriteAL.elements.length; i ++)					
			if (httpOverwriteAL.elements[i].id.indexOf('archivalinstitutionid') != -1) 	
				{						
				document.getElementById(identifier+cont).disabled = "disabled";
				cont ++;
				}
			
			document.getElementById(2).style.display='inline';
			
			function editIdentifier()
			{	
				cont = 0;				
				for (var i = 0; i< httpOverwriteAL.elements.length; i ++)					
					if (httpOverwriteAL.elements[i].id.indexOf('archivalinstitutionid') != -1) 	
						{						
						document.getElementById(identifier+cont).disabled = "";
						cont ++;
						}					
				document.getElementById(1).style.display='inline';
				document.getElementById(2).style.display='none';
			}
			
			function addCheckButton()
			{
				document.getElementById(1).style.display='none';
				var nooverwrite = "nooverwrite";				
				document.getElementById(nooverwrite).style.display='none';
				document.getElementById(2).style.display='none';
				
				var over = "over";
				document.getElementById(over).style.display='none';
			}			
		</script>
</div>