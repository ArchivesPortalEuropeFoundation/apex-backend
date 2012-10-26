<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
		<script type="text/javascript">
			$(document).ready(function() {
				initContentManager();
			});
		</script>
<div id="contentmanager">
	<div class="searchOptionsContent">
		<form id="newSearchForm">
				<table>
					<tr>
						<th>Conversion status:</th><td> <input type="checkbox" checked="checked" value="1" >Ok</input><input type="checkbox" checked="checked">No</input></td>
						<th>Validation status:</th><td> <input type="checkbox" checked="checked">Ok</input><input type="checkbox" checked="checked">No</input><input type="checkbox" checked="checked">Error</input></td>
						<th>Index status:</th><td> <input type="checkbox" checked="checked">Ok</input><input type="checkbox" checked="checked">No</input></td>
						<th>Type: </th><td> <input type="radio" name="hi" checked="checked" value="1" >Findingaid</input><input type="radio"  name="hi">Holdingsguide</input><input  name="hi" type="radio" >Sourceguide</input></td>
					</tr>
					<tr>
						<th>Conversion for Europeana status:</th><td> <input type="checkbox" checked="checked">Ok</input><input type="checkbox" checked="checked">No</input></td>
						<th>Delivered for Europeana status:</th><td> <input type="checkbox" checked="checked">Ok</input><input type="checkbox" checked="checked">No</input></td>
						<th>Holdings guide status: </th><td> <input type="checkbox" checked="checked">Ok</input><input type="checkbox" checked="checked">No</input></td>
						
					</tr>
					
					<tr><td colspan="3"> <input type="text" value="" name="searchTerms"><select class="small" name="search" style="float:left;">
							
							<option value="0">All</option>
							
							<option value="1">ID</option>
							
							<option value="2">Title</option>	
						</select><input type="submit" id="searchButton" value="Search" class="mainButton"><span>Order by:</span><select class="small" name="orderBy">
                            <option value="uploadDate">Date</option>
                            <option value="title">Title</option>
                            <option value="eadid">ID</option>
                        </select></td></tr>
				</table>
		</form>
	</div>
	<div id="eads">

	<div id="ead-results-container">

	</div>
	</div>
</div>
