<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<script type='text/javascript'>
	$(function() {
  
		$('#selectPartner').change(function() {
		      if($(this).val() == "")
		      {
		    	  $("#newPartner").removeClass("hidden");
		      }else {
		    	  $("#newPartner").removeClass("hidden").addClass("hidden");
		      }
		});

	});
</script>
<div>
	<s:form action="createInstitutionManager" method="post" theme="simple">
	<table>
		<tr>
			<td class="inputLabel"><s:label key="usermanagement.associate.institutionmanager" for="selectPartner" /><span class="required">*</span>:</td>
			<td><s:select id="selectPartner" name="existingPartnerId" key="usermanagement.associate.institutionmanager"  listKey="value" headerKey="" headerValue="%{getText('usermanagement.associate.institutionmanager.select')}" listValue="content" list="partners"/></td>
		</tr>
	</table>
	<table id="newPartner">
		<tr>
			<td colspan="2" class="subheader"><s:text name="usermanagement.create.institutionmanager.new"/></td>
		</tr>
		<tr>
			<td class="inputLabel"><s:label key="label.firstname" for="firstName" /><span class="required">*</span>:</td>
			<td><s:textfield id="firstName" name="firstName"/><s:fielderror fieldName="firstName"/></td>
		</tr>	
		<tr>
			<td class="inputLabel"><s:label key="label.lastname" for="lastName" /><span class="required">*</span>:</td>
			<td><s:textfield id="lastName" name="lastName"/><s:fielderror fieldName="lastName"/></td>
		</tr>
			
		<tr>
			<td class="inputLabel"><s:label key="label.email" for="email" /><span class="required">*</span>:</td>
			<td><s:textfield id="email" name="email"/><s:fielderror fieldName="email"/></td>
		</tr>
	</table>
	<table>
		<tr>
			<td colspan="2"><s:submit key="label.ok" cssClass="mainButton" name="okButton" /><s:submit method="cancel" key="label.cancel" name="cancelButton" onclick="form.onsubmit=null"/>
			</td>
		</tr>
	</table>			
		<s:actionerror />
		
		<s:hidden name="countryId"/>
		<s:hidden name="aiId"/>		

		
		
	</s:form>
</div>
