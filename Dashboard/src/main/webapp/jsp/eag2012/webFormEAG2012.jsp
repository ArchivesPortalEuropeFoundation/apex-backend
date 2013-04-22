<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>

<link rel="stylesheet" href="${pageContext.request.contextPath}/css/eag2012/eag2012.css" type="text/css"/>

<div id="eag2012Div">
	<form id="webformeag2012" name="webformeag2012" method="POST" action="createeag2012withmenu.action">
		<div id="eag2012Tabs">
			<ul id="eag2012TabsContainer">
				<li>
					<a href="#tab-yourInstitution"><s:property value="getText('label.ai.tab.yourinstitution.title')" /></a> 
				</li>
				<li>
					<a href="#tab-identity"><s:property value="getText('label.ai.tab.identity.title')" /></a>
				</li>
				<li>
					<a href="#tab-contact"><s:property value="getText('label.ai.tab.contact.title')" /></a>
				</li>
				<li>
					<a href="#tab-accessAndServices"><s:property value="getText('label.ai.tab.accessAndServices.title')" /></a>
				</li>
				<li>
					<a href="#tab-description"><s:property value="getText('label.ai.tab.description.title')" /></a>
				</li>
				<li>
					<a href="#tab-control"><s:property value="getText('label.ai.tab.control.title')" /></a>
				</li>
				<li>
					<a href="#tab-relations"><s:property value="getText('label.ai.tab.relations.title')" /></a>
				</li>
			</ul>
			<script type="text/javascript">
				$(document).ready(function(){
					hideAndShow("tab-","tab-yourInstitution");
					$("a[href^='#tab-']").click(function(){
						hideAndShow("tab-",$(this).attr("href").substring(1));
					});
				});
			</script>
			<div id="tab-yourInstitution">
					<jsp:include page="yourInstitutionTab.jsp" />
			</div>

			<div id="tab-identity">
					<jsp:include page="identityTab.jsp" />
			</div>

			<div id="tab-contact">
					<jsp:include page="contactTab.jsp" />
			</div>

			<div id="tab-accessAndServices">
					<jsp:include page="accessAndServicesTab.jsp" />
			</div>

			<div id="tab-description">
					<jsp:include page="descriptionTab.jsp" />
			</div>

			<div id="tab-control">
					<jsp:include page="controlTab.jsp" />
			</div>

			<div id="tab-relations">
					<jsp:include page="relationsTab.jsp" />
			</div>
		</div>
	</form>
</div>