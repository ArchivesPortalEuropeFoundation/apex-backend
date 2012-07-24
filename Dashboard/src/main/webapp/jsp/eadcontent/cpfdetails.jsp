<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@taglib prefix="x" uri="http://java.sun.com/jsp/jstl/xml"%>
<%@taglib prefix="s" uri="/struts-tags"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <link href="${pageContext.request.contextPath}/css/eadcontent/eadcontent.css" type="text/css" rel="stylesheet" />
        <link href="${pageContext.request.contextPath}/css/eadcontent/expand.css" type="text/css" rel="stylesheet" />
        <script src="${pageContext.request.contextPath}/js/jquery/jquery_1.4.2.min.js" type="text/javascript"></script>
        <title>${cpf.cpfId}</title>
    </head>
    <body>
        <div id="body">
            <c:import var="xslt" url="../../xsl/eac2html.xsl" />
            <x:transform xml="${cpf.xml}" xslt="${xslt}"/>
        </div>
    </body>
</html>