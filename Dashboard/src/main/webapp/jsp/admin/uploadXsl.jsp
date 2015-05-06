<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="s" uri="/struts-tags"%>
<div>
    <s:actionmessage />
    <s:actionerror />
  <c:choose>
    <c:when test="${countries != null}">
      <table class="defaultlayout fullWidth">
        <thead>
          <tr>
            <th><s:text name="label.xsl.upload.country.id" /></th>
            <th><s:text name="label.xsl.upload.country.name" /></th>
            <th><s:text name="label.xsl.upload.action" /></th>
          </tr>
        </thead>
        <tbody>
        <c:forEach var="item" items="${countries}">

          <tr>
            <td><c:out value="${item.id}" /></td>
            <td><c:out value="${item.cname}" /></td>
            <td>
                <s:form action="xslUploadChooseInstitution.action" method="POST" theme="simple">
                  <input type="hidden" name="countryId" value="${item.id}" />
                  <s:submit key="content.message.go" />
                </s:form>
            </td>
          </tr>
        </c:forEach>
        </tbody>
      </table>
    </c:when>
    <c:when test="${countryId != null and institutions != null}">
      <table class="defaultlayout fullWidth">
        <thead>
        <tr>
          <th><s:text name="label.xsl.upload.institution.id" /></th>
          <th><s:text name="label.xsl.upload.institution.name" /></th>
          <%--<th><s:text name="label.xsl.upload.file" /></th>--%>
          <%--<th><s:text name="label.xsl.upload.file.name" /></th>--%>
          <th><s:text name="label.xsl.upload.action" /></th>
        </tr>
        </thead>
        <tbody>
        <c:forEach var="item" items="${institutions}">
            <tr>
              <td><c:out value="${item.archivalInstitution.aiId}" /></td>
              <td><c:out value="${item.archivalInstitution.ainame}" /></td>
              <td>
                <c:forEach var="xsl" items="${item.xslUploads}">
                  <s:form method="POST" theme="simple">
                    <input type="text" id="editXslFilename" name="editXslFilename" value="${xsl.readableName}" />
                    <input type="hidden" name="institutionId" value="${item.archivalInstitution.aiId}" />
                    <input type="hidden" name="xslUploadId" value="${xsl.id}" />
                    <s:submit key="content.message.editname" action="editXslFilename" />
                    <s:submit key="content.message.delete" action="deleteXsl" />
                  </s:form>
                  <br/>
                </c:forEach>
                <s:form action="xslUploadFile" enctype="multipart/form-data" method="POST" theme="simple">
                  <s:file id="xslFile" theme="simple" name="xslFile" key="label.xsl.upload.file"/>
                  <s:textfield id="xslFilename" theme="simple" name="xslFilename" key="label.xsl.upload.file.name"/>
                  <input type="hidden" name="institutionId" value="${item.archivalInstitution.aiId}" />
                  <s:submit key="content.message.go" />
                </s:form>
              </td>
            </tr>
        </c:forEach>
        </tbody>
      </table>
    </c:when>
  </c:choose>
</div>
