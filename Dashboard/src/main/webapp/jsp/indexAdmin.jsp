<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>

         <div>
                  <ul>				                 
					<li><s:a action="createCountry"> - <s:property value="getText('admin.menu.countrycreation')" /> </s:a></li>
					<li><s:a action="insertAlternativeCountryNames"> - <s:property value="getText('admin.menu.countrytranslations')" />  </s:a></li>
					<li><s:a action="storeLanguage"> - <s:property value="getText('admin.menu.languagecreation')" /></s:a></li>
					<li><s:a action="viewDptVersions"> - <s:property value="getText('admin.menu.dptversions')" /></s:a></li>
					<li><s:a action="downloadGeneralAL" target="_blank"> - <s:property value="getText('admin.menu.downloadal')" /> </s:a></li>
					<li><s:a action="countryState"><s:property value="getText('admin.menu.generalcountrystate')" /></s:a></li>
					<li><s:a action="sessionManagement"><s:property value="getText('admin.menu.sessionmanagement')" /></s:a></li>
					<li><s:a action="userManagement"><s:property value="getText('admin.menu.usermanagement')" /></s:a></li>						
					<li><s:a action="showFilesFailed"><s:property value="getText('admin.menu.filesfailed')" /></s:a> </li>
                 </ul>
         </div>

