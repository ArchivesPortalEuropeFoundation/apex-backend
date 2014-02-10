<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib uri="/struts-tags" prefix="s" %>

<link rel="stylesheet" href="${pageContext.request.contextPath}/css/eaccpf/eaccpf.css" type="text/css"/>


<div id="eacCpfDiv">
    <s:form id="webformChooseMode" name="webformChooseMode" method="POST" enctype="multipart/form-data" action="edit.action" theme="simple">
        <div id="container" class="container">
            <div id="headerContainer">
            </div>

            <table id="choosetypeTable" class="tablePadding">
                <tr>
                    <td colspan="2" style="text-align: center;"><b>Note:</b> Loading files is currently discouraged, as some data (all dates; genealogies/biographies) will not be shown at the moment<br /><hr /></td>
                </tr>
                <tr>
                    <td>Do you want</td>
                    <td><s:radio id="useMode" name="useMode" list="useModeList" listKey="key" listValue="value" onchange="loadUseModeContext();"/></td>
                </tr>
                <tr id="newType" class="area">
                    <td>Please specify, if you want to describe</td>
                    <td><s:radio id="new" name="cpfType" list="cpfTypeList" listKey="key" listValue="value" value="defaultCpfType"/></td>
                </tr>
                <tr id="newLanguage" class="area">
                    <td>Please set the default language and script for this file (this can be individually changed for each item later)</td>
                    <td><s:select id="new" name="defaultLanguage" list="languages" listKey="key" listValue="value" />&nbsp;&nbsp;&nbsp;&nbsp;
                    <s:select id="new" name="defaultScript" list="scriptList" listKey="key" listValue="value" /></td>
                </tr>
                <tr id="load" class="area">
                    <td>Please select a file to load</td>
                    <td>
                        <s:file theme="simple" name="upload" label="File"/>
                    </td>
                </tr>
            </table>
        </div>
        <table id="goButtonPanel">
            <input type="button" class="rightButton" id="buttonGo" value="Go" onclick='clickGoAction()' />
        </table>
        <input type="hidden" id="currentTr" value="" />
    </s:form>
</div>