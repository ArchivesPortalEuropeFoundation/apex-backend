<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
                 
                    <div >
                        <ul>
                            <li><p><br/></p></li>
                            <li><br/><span style="font : bold 100% sans-serif;color : #333;text-align : left;"><s:property value="getText('dashboard.menu.contentproviderinf')" /></span><p><br/></p></li>
                            <li><a href="createsimpleeagwithmenu.action"> - <s:property value="getText('dashboard.menu.editeag')" /></a></li>
                            <li><a href="uploadowneagwithmenu.action"> - <s:property value="getText('dashboard.menu.uploadeag')" /></a></li>
                            <li><a href="downloadeag.action?ai_id=<s:property value="%{ai_id}"/>" target="_blank"> - <s:property value="getText('dashboard.menu.downloadeag')" /></a></li>
                            <li><br/><a href="changeainame.action"><s:property value="getText('dashboard.menu.changeainame')" /></a><br/></li>
                            <li><br/><a href ="upload.action"><s:property value="getText('dashboard.menu.uploadcontent')" /></a><br/></li>
                            <li><br/><a href ="checkfilesuploaded.action"><s:property value="getText('dashboard.menu.contentmanager')" /></a><br/></li>                           
                        </ul>
                    </div>
