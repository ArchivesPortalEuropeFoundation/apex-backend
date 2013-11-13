<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>	

<div id="eagwebform" align="center">
	<p><span style="font-weight: bold;font-size:large;"><s:property value="getText('label.ai.changeainame.title')" /></span><br/><br/></p>

	<s:if test="hasActionErrors()">
		<div class="errors">
			<s:actionerror/><br/>
			<s:property value="errormessage"/>
		</div>
	</s:if>
	<s:else>
		<s:if test="%{allok==true}">
			<p><br><br><br><s:property value="getText('label.ai.changeainame.success')" /></p>

			<br><br><br>
			<s:form method="get">
				<s:submit key="label.continue" action="dashboardHome"/>
			</s:form>
		</s:if>
		<s:else>
			<s:form method="POST">
				<s:hidden name="ai_id" value="%{ai_id}"></s:hidden>
				<s:textfield name="name" value="%{name}" key="label.ai.changeainame.currentname"></s:textfield>
				<s:textfield name="newname" value="" key="label.ai.changeainame.newname"></s:textfield>
				<p>&nbsp;<br/><br/></p>
				<!--<s:textfield name="repeatnewname" value="" key="Repeat new name:" cssStyle="width:190%"></s:textfield>-->
				<s:if test="currentAction == 'changeainame'">
					<s:submit key="label.validate" action="validatechangeainame"/>
				</s:if>
				<!--<s:elseif test="currentAction == 'createsimpleeagwithmenu'">
					<s:submit key="label.validate" action="uploadsimpleeagwithmenu"/>
				</s:elseif>-->
				<s:elseif test="currentAction == 'createeag2012withmenu'">
					<s:submit key="label.validate" action="uploadsimpleeagwithmenu"/>
				</s:elseif>
				<s:submit key="label.cancel" action="dashboardHome"/>
			</s:form>
		</s:else>
	</s:else>

</div>