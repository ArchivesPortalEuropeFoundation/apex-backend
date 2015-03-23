<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="dashboard" uri="http://dashboard.archivesportaleurope.eu/tags"%>
<dashboard:securityContext var="securityContext" />
         <div>
                  <ul>
				  	<c:if test="${securityContext.admin}">
						<li><s:a action="createCountry"> - <s:property value="getText('admin.menu.countrycreation')" /> </s:a></li>
						<li><s:a action="storeLanguage"> - <s:property value="getText('admin.menu.languagecreation')" /></s:a></li>
						<li><s:a action="viewDptVersions"> - <s:property value="getText('admin.menu.dptversions')" /></s:a></li>
				  	</c:if>
					<li><s:a action="downloadCountriesStatistics"><s:text name="admin.menu.statistics.countries" /></s:a></li>
					<li><s:a action="downloadInstitutionsStatistics"><s:text name="al.menu.statistics.institutions" /></s:a></li>
					<li><s:a action="sessionManagement"><s:property value="getText('admin.menu.sessionmanagement')" /></s:a></li>
					<li><s:a action="userManagement"><s:property value="getText('admin.menu.usermanagement')" /></s:a></li>
				  	<li><s:a action="manageQueue"><s:text name="admin.queuemanagement.title" /></s:a> </li>
				  	<li><s:a action="manageHarvest"><s:text name="admin.harvestmanagement.title" /></s:a> </li>
					<c:if test="${securityContext.admin}">
				  		<li><s:a action="xslUploadChooseCountry"><s:property value="getText('admin.menu.xslupload')" /></s:a></li>
						<li><s:a action="showLogFiles"><s:text name="admin.showlogfiles.title" /></s:a> </li>
					</c:if>
					<li><s:a action="adminTopic"><s:text name="admin.topic.management" /></s:a> </li>
                 </ul>
         </div>

