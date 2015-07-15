<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>

<script type='text/javascript'>
    $(function () {
        $("#tree").dynatree({
            checkbox: true,
            fx: {height: "toggle", duration: 200},
            initAjax: {
                url: "${pageContext.request.contextPath}/updateFtp.action",
                data: {parentName: ''}
            },
            onSelect: function (select, dtnode) {
                if (select) {
                    $("#contentlist").append($('<li id="node_' + dtnode.data.name + '">' + dtnode.data.title + '</li>').append($("<input type='hidden' name='filesToUpload'/>").attr("value", dtnode.data.name)));
                    $("#saveFiles").removeAttr('disabled');
                } else {
                    var d = document.getElementById('contentlist');
                    d.removeChild(document.getElementById('node_' + dtnode.data.name));
                    if ($("#contentlist").has('li').length === 0) {
                        var button = document.getElementById('saveFiles');
                        button.setAttribute("disabled", "disabled");
                    }
                }
            },
            onActivate: function (dtnode) {
            },
            onLazyRead: function (dtnode) {
                dtnode.appendAjax({
                    url: "${pageContext.request.contextPath}/updateFtp.action",
                    data: {parentName: dtnode.data.name}
                });
            },
            minExpandLevel: 2
        });
    });

    function clickSubmitAction() {
        $('#saveFtpFiles').submit();
    }
</script>

<div id="treeContainer" class="dojo" align="left" style="float:left;">
    <div id="tree"></div>
</div>
<div id="searchContainer" class="actions dojo" style="float:right;">
    <div id="contents">
        <form id="saveFtpFiles" name="saveFtpFiles" method="POST" action="/Dashboard/saveFtpFiles.action" onsubmit="uploadBlackout('Downloading files...');">
            <h3><s:property value="getText('dashboard.uploadcontent.ftp.selectedFiles')" /></h3>
            <ul id="contentlist">
                <s:hidden name="ai_id" value="%{ai_id}" />
            </ul>
            <p></p>
            <table class="wwFormTable">
                <tr>
                    <td><s:select id="profileCb" list="ingestionprofiles" key="ingestionprofiles.chooseprofile" name="ingestionprofile" listKey="value" listValue="content" /></td>
                </tr>
            </table>
            <input type="button" id='saveFiles' name="saveFiles" value="<s:property value="getText('dashboard.uploadcontent.ftp.downloadSelectedFiles')" />" disabled="disabled" onclick="clickSubmitAction();" />
        </form>
    </div>
</div>
