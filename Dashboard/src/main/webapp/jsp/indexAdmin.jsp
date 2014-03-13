<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>

         <div>
                  <ul>				                 
					<li><s:a action="createCountry"> - <s:property value="getText('admin.menu.countrycreation')" /> </s:a></li>
					<%-- <li><s:a action="insertAlternativeCountryNames"> - <s:property value="getText('admin.menu.countrytranslations')" />  </s:a></li> --%>
					<li><s:a action="storeLanguage"> - <s:property value="getText('admin.menu.languagecreation')" /></s:a></li>
					<li><s:a action="viewDptVersions"> - <s:property value="getText('admin.menu.dptversions')" /></s:a></li>
					<li><s:a action="downloadCountriesStatistics"><s:text name="admin.menu.statistics.countries" /></s:a></li>
					<li><s:a action="downloadInstitutionsStatistics"><s:text name="al.menu.statistics.institutions" /></s:a></li>
					<li><s:a action="sessionManagement"><s:property value="getText('admin.menu.sessionmanagement')" /></s:a></li>
					<li><s:a action="userManagement"><s:property value="getText('admin.menu.usermanagement')" /></s:a></li>						
					<li><s:a action="manageQueue"><s:text name="admin.queuemanagement.title" /></s:a> </li>
					<li><s:a action="manageHarvest"><s:text name="admin.harvestmanagement.title" /></s:a> </li>
                 </ul>
         </div>

