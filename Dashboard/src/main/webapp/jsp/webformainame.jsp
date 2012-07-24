<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>	
<script type="text/javascript">
function showError(){
	document.getElementById("diverror").style.display='inline';
	document.getElementById("diverror").style.backgroundColor= '#CCC';
	document.getElementById("diverror").style.color='red';
	document.getElementById("showerror").style.display='none';
	
}
function hideError(){
	document.getElementById("diverror").style.display='none';
	document.getElementById("showerror").style.display='inline';
}
</script>
	<div id="eagwebform" align="center">
	<p><span style="font-weight: bold;font-size:large;"><s:property value="getText('label.ai.changeainame.title')" /></span><br></p>
	
	
	<s:if test="%{allok==true}">		
		<p><br><br><br><s:property value="getText('label.ai.changeainame.success')" /></p>
		
		<br><br><br>
		<s:form method="post" enctype= "multipart/form-data">
			<s:submit key="label.continue" action="dashboardHome"/>
		</s:form>
	</s:if>
	<s:elseif test="%{allok==false}">
		<br><br>
		<s:property value="getText('label.ai.changeainame.error')"/>
		<br><br>
		<input type="button" id="showerror" value="<s:property value="getText('label.ai.changeainame.showerrorbutton')"/>" onclick="showError();"/>
		<br><br><br><br>
		<div id="diverror" style="display:none; width: 500px; height: 300px;"><s:property value="errormessage"/>
		<br><br>
		<input type="button" id="hideerror" value="<s:property value="getText('label.ai.changeainame.hideerrorbutton')"/>" onclick="hideError();"/>
		</div>
	</s:elseif>
	<s:else>
	<p><br><br><s:property value="getText('label.ai.changeainame.advice1')" />	
	</p>
        
    	<br>
    	<br>
    	<br>
		<s:form method="POST">		
			<s:hidden name="ai_id" value="%{ai_id}"></s:hidden>
    		<s:textfield name="name" value="%{name}" key="label.ai.changeainame.currentname" cssStyle="width:190%"></s:textfield>
    		<s:textfield name="newname" value="" key="label.ai.changeainame.newname" cssStyle="width:190%"></s:textfield>
    		<p>&nbsp;<br/><br/></p>
    		<!--<s:textfield name="repeatnewname" value="" key="Repeat new name:" cssStyle="width:190%"></s:textfield>-->    		
    		<s:if test="currentAction == 'changeainame'">
    			<s:submit key="label.validate" action="validatechangeainame"/>
            </s:if>
            <!--<s:elseif test="currentAction == 'createsimpleeagwithmenu'">
    			<s:submit key="label.validate" action="uploadsimpleeagwithmenu"/>
            </s:elseif>-->
    		<s:submit key="label.cancel" action="dashboardHome"/>
		</s:form>
		
		</s:else>        
	</div>
