<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="apenet" uri="http://commons.apenet.eu/tags"%>
<%@ taglib prefix="dashboard" uri="http://dashboard.archivesportaleurope.eu/tags"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<tiles:useAttribute id="cssInternal" name="cssfilesInternal" classname="java.util.List" />
<tiles:useAttribute id="jsInternal" name="jsfilesInternal" classname="java.util.List" />
<html xmlns="http://www.w3.org/1999/xhtml">
    <head>
      <meta content="text/html; charset=UTF-8;" http-equiv="content-type" />
      <meta name="Description" content="APENET" />
      <meta name="Keywords" content="archives portal europe, apenet, europeana" />
      <title><apenet:resource><tiles:getAsString name="title"/></apenet:resource></title>
      <link rel="stylesheet" href="${pageContext.request.contextPath}<tiles:getAsString name="maincss"/>" type="text/css"/>
      <c:forEach var="item" items="${jsInternal}"><script src="${pageContext.request.contextPath}${item}" type="text/javascript"></script></c:forEach>
      <c:forEach var="item" items="${cssInternal}"><link rel="stylesheet" href="${pageContext.request.contextPath}${item}" type="text/css" /></c:forEach>
		<script type="text/javascript">
		var gId = 'UA-42624055-1';
		var hostname = window.location.hostname;
		if (hostname == "www.archivesportaleurope.net" 
				|| hostname == "archivesportaleurope.net" 
				|| hostname == "dashboard.archivesportaleurope.net"){
			gId = 'UA-37870082-1';
		} else if (hostname == "contentchecker.archivesportaleurope.net") {
			gId = 'UA-40082820-1';
		}
		var _gaq = _gaq || [];
		_gaq.push([ '_setAccount', gId ]);
		_gaq.push([ '_trackPageview' ]);
		</script>
    </head>
    <body>
    <dashboard:securityContext var="securityContext" />
    <dashboard:changeLanguage varSelectedLanguage="selectedLanguage" varCurrentAction="currentAction" />	 
        <div id="principal">
            <div id="wrap">
                <div id="dashboard">
              			<div id="header"> <div id="left-header"><div id="titledashboard"><apenet:resource><tiles:getAsString name="title"/></apenet:resource></div> </div> </div>

				                   
                   <div id="menubar" >
                   		<div id="breadcrumb">
                   			<c:if test="${!empty securityContext}">
                   				<span class="linkBread" ><a href="index.action" ><s:text name="breadcrumb.section.home"/></a></span>

	                   			<s:iterator var="breadcrumb" value="breadcrumbRoute" status="breadcrumbStatus">&nbsp;-&nbsp;
	                   				<c:choose>
	                   					<c:when test="${breadcrumbStatus.last or empty breadcrumb.href}">
	                   						<span class="withoutLinkBread" >${breadcrumb.place}</span>
	                   					</c:when>
	                   					<c:otherwise>
	                   						<span class="linkBread" ><a href="${breadcrumb.href}" >${breadcrumb.place}</a></span>
	                   					</c:otherwise>
	                   				</c:choose>
	                   			</s:iterator>
	                   		</c:if>
                   		</div>
                
	                    <div id="userFeatures">
	                    <c:choose>
	                    	<c:when test="${empty securityContext}">
								  <s:a action="login" namespace="/"><s:property value="getText('label.login.login')"/></s:a>	                 			
	                    	</c:when>
	                    	<c:otherwise>
								<c:out value="${securityContext.name}"/>&nbsp;|&nbsp;<c:if test="${securityContext.child}"><a href="logout.action?parent=true"><s:property value="getText('label.login.logout.toparent')"/>&nbsp;<c:out value="${securityContext.parentName}"/></a>&nbsp;|&nbsp;</c:if>					
								<s:a action="logout"  namespace="/"><s:property value="getText('label.login.logout')"/></s:a>&nbsp;|&nbsp;<s:a action="view"  namespace="/"><s:property value="getText('label.edit.user')"/></s:a>	              
	                    	</c:otherwise>
	                    </c:choose>    
						</div>
                     	    <div id="language">
								<s:form name="languageSelection" id="languageSelection" action="changeLanguage" theme="simple" method="post">
									<select title="<s:property value="getText('header.chooselanguage')"/>" name="request_locale" id="selectLanguage" onchange="sendLanguageSelectionForm();" >
										<option title="Deutsch" value="de" ${selectedLanguage['de']}>Deutsch</option>				
										<option title="Ελληνικά" value="el" ${selectedLanguage['el']}>Ελληνικά</option>
										<option title="English" value="en" ${selectedLanguage['en']}>English</option>
								        <option title="Español" value="es"${selectedLanguage['es']}>Español</option>				
										<option title="Français" value="fr" ${selectedLanguage['fr']}>Français</option>
										<option title="Gaeilge" value="ga" ${selectedLanguage['ga']}>Gaeilge</option>
										<option title="Hrvatski" value="hr" ${selectedLanguage['hr']}>Hrvatski</option>
										<option title="íslenska" value="is" ${selectedLanguage['is']}>íslenska</option>
										<option title="Italiano" value="it" ${selectedLanguage['it']}>Italiano</option>
										<option title="Latviešu" value="lv" ${selectedLanguage['lv']}>Latviešu</option>
										<option title="Malti" value="mt" ${selectedLanguage['mt']}>Malti</option>				
										<option title="Nederlands" value="nl" ${selectedLanguage['nl']}>Nederlands</option>
										<option title="Polski" value="pl" ${selectedLanguage['pl']}>Polski</option>
										<option title="Português" value="pt" ${selectedLanguage['pt']}>Português</option>
										<option title="Slovenščina" value="sl" ${selectedLanguage['sl']}>Slovenščina</option>				
										<option title="Suomi" value="fi" ${selectedLanguage['fi']}>Suomi</option>
										<option title="Svenska" value="sv" ${selectedLanguage['sv']}>Svenska</option>   
						      		</select>
									<input type="hidden" id="currentAction" name="currentAction" value="${currentAction}"/>
									<script type="text/javascript">
										function sendLanguageSelectionForm()	
										{
											document.getElementById("languageSelection").submit();
										}
									</script>
									<noscript>
										<s:submit key="label.ok"></s:submit>
									</noscript>
								</s:form>
						    </div>  	                    
	                </div>
			    
			    </div>
			    <p>&nbsp;</p>			    
			    <div id="main" class="main">
			    	<tiles:useAttribute id="containsMenu" name="withmenu"/>
			    	<c:choose>
			    		<c:when test="${containsMenu == 'true'}">
					    	<div id="menuv" class="menuv"><tiles:insertAttribute name="menu"/></div>			    			    
					    	<div id="bodyt" class="bodyt"><tiles:insertAttribute name="body"/></div>			    		
			    		</c:when>
			    		<c:otherwise>
			    			<tiles:insertAttribute name="body"/>
			    		</c:otherwise>
			    	</c:choose>			    	

                </div>                  
                
            </div>
            
            <div id="footernavbar">
                    <div id="footerCatInner">
                        <ul>
							<li><a id="food1" href="#" title="Navigation guide"><s:property value="getText('label.footer.navigationguide')"/> | </a></li>
                            <li><a id="food2" href="termsOfUse.action" title="<s:property value="getText('terms.use.title')"/>"><s:property value="getText('label.footer.termsofuse')"/> | </a></li>
                            <li><a id="food3" href="#" title="Site map"><s:property value="getText('label.footer.sitemap')"/> | </a></li>
                          	<li><a id="food4" href="contact.action" title="Contact"><s:property value="getText('label.footer.contact')"/></a></li>  
                        </ul>
                    </div>
                    <div id="footerSupportText"><s:text name="footer.support"/></div>
                    <div id="footerSupportLogos"><img  src="images/ictpsp_logo.gif"/><img  src="images/eu_logo.gif"/></div>

                </div>
        </div>
	<script type="text/javascript">
	(function() {
		var ga = document.createElement('script');
		ga.type = 'text/javascript';
		ga.async = true;
		ga.src = ('https:' == document.location.protocol ? 'https://ssl'
				: 'http://www')
				+ '.google-analytics.com/ga.js';
		var s = document.getElementsByTagName('script')[0];
		s.parentNode.insertBefore(ga, s);
	})();
	</script>
    </body>
</html>                