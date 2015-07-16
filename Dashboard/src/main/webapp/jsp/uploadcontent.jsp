<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<div id="divForm">
    <div style="align: center;" >
        <s:actionmessage />
        <s:actionerror />

        <table>
            <tr>
                <td>
                    <s:radio list="uploadType" key="label.uploadtype" name="uploadTypeChoice" onclick="changeOptions(this)"/>
                </td>
            </tr>
            <%--            <tr>
                            <td>
                                <s:select id="profileCb" list="ingestionprofiles" key="ingestionprofiles.chooseprofile" name="ingestionprofile" listKey="value" listValue="content" />
                            </td>
                        </tr>--%>
        </table>
        <s:form id="httpUpload" method="POST" enctype="multipart/form-data" action="httpUpload">
            <table>
                <tr>
                    <td>
                        <s:select id="profileCb" list="ingestionprofiles" key="ingestionprofiles.chooseprofile" name="ingestionprofile" listKey="value" listValue="content" />
                    </td>
                </tr>
            </table>
            <p style="margin-top:10px;">
                <s:file id="httpFile" theme="simple" name="httpFile" key="label.filetoupload"/>
            </p>
            <p id="uploadControls" style="margin-top:10px;">
                <script type="text/javascript">
                    textWhileIngesting = '<s:property value="getText('label.upload.filewasingested')"/>';
                    writeStatusBar('<s:property value="getText('label.upload')"/>', '<s:property value="getText('label.upload.fileextensionerror')"/>', '<s:property value="getText('label.upload.confirmExit')"/>');
                </script>
                <noscript>
                <s:submit onclick="document.getElementById('stopButton').style.display='block';" key="label.upload" action="httpUpload" />
                </noscript>
            </p>
        </s:form>
        <s:form action="connectFTP" method="post">
            <s:textfield name="ftpUrl" key="labe.url" />
            <s:textfield name="ftpPort" key="label.port" />
            <s:textfield name="ftpUser" key="label.username" />
            <s:password name="ftpPwd" key="label.password" />
            <s:submit onclick="document.getElementById('stopButton').style.display='block';" method="connectFTP" key="label.ftpconexion" />
            <s:checkbox name="ftpRememberData" key="label.rememberLoginData" />
        </s:form>
        <div style="width:100%;float:left;">
            <div style="width:50%;float:left;margin-left: -1em;">&nbsp;</div>
            <input id="stopButton" style="display:none;float:left;" type="button" value="<s:property value="getText('label.stop')"/>" onclick="stopBrowser('stopButton', '<s:property value="getText('al.message.canceled')" />')" />
        </div>
    </div>
</div>
<script type="text/javascript">
    function changeOptions(option) {
        if (option.value == "FTP") {
            document.getElementById("httpUpload").style.display = "none";
            document.getElementById("connectFTP").style.display = "block";
        } else {
            document.getElementById("httpUpload").style.display = "block";
            document.getElementById("connectFTP").style.display = "none";
        }
    }
    function changeOptionsLoad() {
        if (document.getElementById("uploadTypeChoiceFTP").checked) {
            changeOptions(document.getElementById("uploadTypeChoiceFTP"));
        } else {
            changeOptions(document.getElementById("uploadTypeChoiceHTTP"));
        }
    }
    changeOptionsLoad();
</script>