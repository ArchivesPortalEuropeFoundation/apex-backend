<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<div id="notLinkedFindingAids">
	<div class="left">
		<h1>
			<s:text name="content.message.findingaidsout" />
			(${countNotUploadedFindingAids})
		</h1>
		<div id="content_div_table">
			<table>
				<thead>
					<tr>
						<th><s:text name="content.message.id" /></th>
						<th><s:text name="content.message.title" /></th>
					</tr>
				</thead>
				<c:forEach var="notUploadedFindingAid" items="${notUploadedFindingAids}">
					<tr>
						<td>${notUploadedFindingAid.hrefEadid }</td>
						<td><span class="title">${notUploadedFindingAid.unittitle }</span></td>
					</tr>
				</c:forEach>
			</table>
		</div>
	</div>
	<div class="right">
		<h1>
			<s:text name="content.message.findingaidsnotindexed" />
			(${countNotIndexedFindingAids})
		</h1>
		<div id="content_div_table">
			<table>
				<thead>
					<tr>
						<th><s:text name="content.message.id" /></th>
						<th><s:text name="content.message.title" /></th>
					</tr>
				</thead>
				<c:forEach var="notIndexedFindingAid" items="${notIndexedFindingAids}">
					<tr>
						<td>${notIndexedFindingAid.eadid }</td>
						<td><span class="title">${notIndexedFindingAid.title }</span></td>
					</tr>
				</c:forEach>
			</table>
		</div>
	</div>