<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles" %>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<div>
	<input type="hidden" id="contextPath" value="${pageContext.request.contextPath}">

	<!-- Checks to display the changes report or non changes message. -->
	<s:if test="%{newInstitutionsFile.size() > 0 || oldRelatedInstitutions.size() > 0 || nameSet.size() > 0 || oldEmptyIdentifierInstitutionsFile.size() > 0 || updatedInstitutions.size() > 0 || institutionsToBeDeleted.size() > 0 || oldDuplicateNameInstitutionsMap.size() > 0 || newDuplicateNameInstitutionsMap > 0}">
		<!-- Checks to display the report. -->
		<s:if test="%{nameSet.size() > 0}">
			<div id="reportMessage">
				<!-- Need decision title. -->
				<div id="divSummary" class="alDivSummary">
					<h1><s:property value="getText('al.message.elementNeedsAttention')" /></h1>
				</div>

				<!-- Institutions with same name but different internal identifiers. -->
				<s:if test="%{nameSet.size() > 0}">
					<div id="messageSameName">
						<div>&nbsp;</div>
						<div>&nbsp;</div>
						<s:property value="getText('al.message.firstLabelSummary')" />
						<s:iterator value="nameSet" var="sameName" status="status">
							<b><s:property value="#sameName" /></b><s:if test="%{nameSet.size() > (#status.index + 1)}">, </s:if>
						</s:iterator>
						<s:property value="getText('al.message.summarySameName')" />
					</div>
				</s:if>
			</div>
		</s:if>
		<!-- Display message for no decision is needed. -->
		<s:if test="%{newInstitutionsFile.size() > 0 || oldEmptyIdentifierInstitutionsFile.size() > 0 || updatedInstitutions.size() > 0 || institutionsToBeDeleted.size() > 0}">
			<div id="informationMessage">
				<div>&nbsp;</div>
				<div>&nbsp;</div>
				<!-- Other changes title. -->
				<div id="divInformation" class="alDivSummary">
					<h1><s:property value="getText('al.message.elementInformation')" /></h1>
				</div>

				<!-- Institutions without internal identifiers. -->
				<s:if test="%{oldEmptyIdentifierInstitutionsFile.size() > 0}">
					<div id="messageEmptyIdentifier">
						<div>&nbsp;</div>
						<div>&nbsp;</div>
						<s:property value="getText('al.message.firstLabelSummary')" />
						<s:iterator value="oldEmptyIdentifierInstitutionsFile" var="emptyIdentifier" status="status">
							<b><s:property value="#emptyIdentifier.ainame" /></b><s:if test="%{oldEmptyIdentifierInstitutionsFile.size() > (#status.index + 1)}">, </s:if>
						</s:iterator>
						<s:property value="getText('al.message.summaryEmptyIdentifiers')" />
					</div>
				</s:if>

				<!-- Institutions that will be added to the system. -->
				<s:if test="%{newInstitutionsFile.size() > 0}">
					<div id="messageNewInstitutions">
						<div>&nbsp;</div>
						<div>&nbsp;</div>
						<s:property value="getText('al.message.firstLabelSummary')" />
						<s:iterator value="newInstitutionsFile" var="newInstitution" status="status">
							<b><s:property value="#newInstitution.ainame" /></b><s:if test="%{newInstitutionsFile.size() > (#status.index + 1)}">, </s:if>
						</s:iterator>
						<s:property value="getText('al.message.summaryNewInstituions')" />
					</div>
				</s:if>

				<!-- Institutions that will be deleted from the system. -->
				<s:if test="%{updatedInstitutions.size() > 0}">
					<div id="messageUpdateInstitutions">
						<div>&nbsp;</div>
						<div>&nbsp;</div>
						<s:property value="getText('al.message.firstLabelSummary')" />
						<s:iterator value="updatedInstitutions" var="updateInstitution" status="status">
							<b><s:property value="#updateInstitution.ainame" /></b><s:if test="%{updatedInstitutions.size() > (#status.index + 1)}">, </s:if>
						</s:iterator>
						<s:property value="getText('al.message.summaryUpdatedInstituions')" />
					</div>
				</s:if>

				<!-- Institutions that will be deleted from the system. -->
				<s:if test="%{institutionsToBeDeleted.size() > 0}">
					<div id="messageDeleteInstitution">
						<div>&nbsp;</div>
						<div>&nbsp;</div>
						<s:property value="getText('al.message.firstLabelSummary')" />
						<s:iterator value="institutionsToBeDeleted" var="deleteInstitution" status="status">
							<b><s:property value="#deleteInstitution.ainame" /></b><s:if test="%{institutionsToBeDeleted.size() > (#status.index + 1)}">, </s:if>
						</s:iterator>
						<s:property value="getText('al.message.summaryDeletedInstituions')" />
					</div>
				</s:if>
			</div>
		</s:if>
	</s:if>

	<!-- Display the no changes report. -->
	<s:else>
		<div id="noChangesDiv">
			<div>&nbsp;</div>
			<s:property value="getText('al.message.noChanges')" />
			<div>&nbsp;</div>
			<s:property value="getText('a.message.overwritefile.question')" />
		</div>
	</s:else>

	<!-- Div for show the differences between identifiers in DB and identifiers in file. -->
	<div id="detailsDiv">
		<div id="divDescription">
			<h1><s:property value="getText('al.message.detail')" /></h1>
		</div>

		<div>&nbsp;</div>

		<div id="contentReport">
			<form name="updatesPartForm" id="updatesPartForm" action="ALContinueUpload.action" method="post">
				<s:hidden id="overwriteIDs" name="overwriteIDs" value="continue"></s:hidden>

				<s:if test="%{oldRelatedInstitutions.size() > 0}">
					<div id="institutionsWithSameNameDiv">
						<div>
							<h2>
								<s:property value="getText('al.message.institutionsdetectedwithsamename')" />
							</h2>
						</div>

						<div>&nbsp;</div>

						<!-- Div with the information of the identifiers in DB. -->
						<div id="institutionsWithSameNameOldDiv" class="middleDiv">
							<div id="currentlyIdsDiv">
								<div>
									<label><b><s:property value="getText('al.message.currentlyIdentifiers')" /></b></label>
								</div>
							</div>

							<div>&nbsp;</div>

							<s:iterator value="oldRelatedInstitutions" var="oldInstitution" status="status">
								<div id="institutionChanged_<s:property value="#status.index"/>">
									<div>
										<label for="oldSameNameInstitution[<s:property value="#status.index"/>]"><s:property value="#oldInstitution.ainame" /></label>
									</div>
									<div>
										<input type="text" class="readOnlyInput" id="oldSameNameInstitution[<s:property value="#status.index"/>]" name="oldSameNameInstitution[<s:property value="#status.index"/>]" value="<s:property value="#oldInstitution.internalAlId" />"  readonly onfocus="this.blur()" />
									</div>

									<div>&nbsp;</div>
								</div>
							</s:iterator>
						</div>

						<!-- Div with the information of the identifiers in file. -->
						<div id="institutionsWithSameNameNewDiv" class="middleDiv">
							<div id="newIdsDiv">
								<div>
									<label><b><s:property value="getText('al.message.identifiersInFile')" /></b></label>
								</div>
							</div>

							<div>&nbsp;</div>

							<s:iterator value="newRelatedInstitutions" var="newInstitution" status="status">
								<div id="institutionChanged<s:property value="#status.index"/>">
									<div>
										<label for="newSameNameInstitution[<s:property value="#status.index"/>]"><s:property value="#newInstitution.ainame" /></label>
									</div>
									<div>
										<input type="text" class="readOnlyInput" id="newSameNameInstitution[<s:property value="#status.index"/>]" name="newSameNameInstitution[<s:property value="#status.index"/>]" value="<s:property value="#newInstitution.internalAlId" />" readonly onfocus="this.blur()" />
									</div>

									<div>&nbsp;</div>
								</div>
							</s:iterator>
						</div>
					</div>

					<div>&nbsp;</div>
				</s:if>

				<s:if test="%{oldDuplicateNameInstitutionsMap.size() > 0}">
					<div id="institutionsWithDuplicateNameDiv">
						<div>
							<h2>
								<s:property value="getText('al.message.institutionsdetectedwithduplicatednames')" />
							</h2>
						</div>

						<div>&nbsp;</div>

						<!-- Div with the information of the identifiers in DB. -->
						<div id="institutionsWithDuplicateNameOldDiv" class="middleDiv">
							<div id="currentlyIdsDiv">
								<div>
									<label><b><s:property value="getText('al.message.currentlyIdentifiers')" /></b></label>
								</div>
							</div>

							<div>&nbsp;</div>

							<s:iterator value="oldDuplicateNameInstitutionsMap" var="oldDuplicateInstitutionMap" status="status">
								<s:iterator value="#oldDuplicateInstitutionMap.value" var="oldDuplicateInstitutionList" status="statusList">
									<div id="institutionDuplicate_<s:property value="#status.index"/>_<s:property value="#statusList.index"/>">
										<s:if test="#oldDuplicateInstitutionList.ainame != 'delete' && #oldDuplicateInstitutionList.ainame != 'add'">
											<label for="oldDuplicateNameInstitution_<s:property value="#status.index"/>_<s:property value="#statusList.index"/>"><s:property value="#oldDuplicateInstitutionMap.key" /></label>
										</s:if>
										<s:else>
											<label for="oldDuplicateNameInstitutionAdd_<s:property value="#status.index"/>_<s:property value="#statusList.index"/>"><s:property value="#oldDuplicateInstitutionMap.key" /></label>
										</s:else>
									</div>
									<div>
										<s:if test="#oldDuplicateInstitutionList.ainame != 'delete' && #oldDuplicateInstitutionList.ainame != 'add'">
											<input type="text" class="readOnlyInput" id="oldDuplicateNameInstitution_<s:property value="#status.index"/>_<s:property value="#statusList.index"/>" name="oldDuplicateNameInstitution_<s:property value="#status.index"/>_<s:property value="#statusList.index"/>" value="<s:property value="#oldDuplicateInstitutionList.internalAlId" />" readonly onfocus="this.blur()" />
										</s:if>
										<s:else>
											<input type="text" class="whiteInput" style="border: 1px solid #FFFFFF;" id="oldDuplicateNameInstitutionAdd_<s:property value="#status.index"/>_<s:property value="#statusList.index"/>" name="oldDuplicateNameInstitutionAdd_<s:property value="#status.index"/>_<s:property value="#statusList.index"/>" value="<s:property value="getText('al.message.addInstitution')"/>" readonly onfocus="this.blur()" />
										</s:else>
									</div>

									<div>&nbsp;</div>
								</s:iterator>
							</s:iterator>
						</div>

						<!-- Div with the information of the identifiers in file. -->
						<div id="institutionsWithDuplicateNameNewDiv" class="middleDiv">
							<div id="newIdsDiv">
								<div>
									<label><b><s:property value="getText('al.message.identifiersInFile')" /></b></label>
								</div>
							</div>

							<div>&nbsp;</div>

							<s:iterator value="newDuplicateNameInstitutionsMap" var="newDuplicateInstitutionMap" status="status">
								<s:iterator value="#newDuplicateInstitutionMap.value" var="newDuplicateInstitutionList" status="statusList">
									<div id="institutionDuplicate<s:property value="#status.index"/>_<s:property value="#statusList.index"/>">
										<label for="selectNew_<s:property value="#status.index"/>_<s:property value="#statusList.index"/>"><s:property value="#newDuplicateInstitutionMap.key" /></label>
									</div>
										<div>
											<select class="alSelect" id="selectNew_<s:property value="#status.index"/>_<s:property value="#statusList.index"/>" name="selectNew[<s:property value="#status.index"/>][<s:property value="#statusList.index"/>]" onchange="selectionChanged($(this));">
												<option value="---">---</option>
												<s:iterator value="#newDuplicateInstitutionMap.value" var="newDuplicateInstitutionOption" status="statusOption">
													<s:if test="#newDuplicateInstitutionOption.ainame != 'delete' && #newDuplicateInstitutionOption.ainame != 'add'">
														<option value="<s:property value="#newDuplicateInstitutionOption.internalAlId"/>"><s:property value="#newDuplicateInstitutionOption.internalAlId" /></option>
													</s:if>
													<s:else>
														<option value="delete"><s:property value="getText('al.message.deleteitem')"/></option>
													</s:else>
												</s:iterator>
											</select>
										</div>

									<div>&nbsp;</div>
								</s:iterator>
							</s:iterator>
						</div>
					</div>
				</s:if>
			</form>
		</div>
	</div>

	<div>&nbsp;</div>

	<!-- Div for the buttons. -->
	<div id="actionsPart">
		<table>
			<tr>
				<!-- Display details button. -->
				<td>
					<div id="divDisplayDetails">
						<div id="buttonDisplayDetails"  class="alDivAction" >
							<s:property value="getText('al.message.displayDetails')" />
						</div>
					</div>
				</td>

				<!-- Overwrite identifiers button. -->
				<td>
					<div id="divOverwriteIds">
						<div id="buttonOverwriteIds" class="alDivAction">
							<s:property value="getText('al.message.overwriteIds')" />
						</div>
					</div>
				</td>

				<!-- Keep identifiers button. -->
				<td>
					<div id="divKeepIds">
						<div id="buttonKeepIds" class="alDivAction">
							<s:property value="getText('al.message.keepIds')" />
						</div>
					</div>
				</td>

				<!-- Continue the upload button. -->
				<td>
					<div id="divContinueUpload">
						<form name="continueUploadForm" action="ALContinueUpload.action" id="continueUploadForm" method="post">
							<div id="buttonContinueUpload" class="alDivAction" >
								<s:property value="getText('al.message.continue')" />
								<s:hidden id="overwriteIDs" name="overwriteIDs" value="continue"></s:hidden>
							</div>
						</form>
					</div>
				</td>

				<!-- Cancel the upload button. -->
				<td>
					<div id="divCancelOverwrite">
						<form name="cancelOverwriteForm" action="ALCancelOverwrite.action" id="cancelOverwriteForm" method="post">
							<div id="buttonCancelOverwrite" class="alDivAction" ><s:property value="getText('al.message.cancel')" /></div>
							<s:hidden name="Overwrite" value="false"></s:hidden>
						</form>
					</div>
				</td>
			</tr>
		</table>
	</div>

	<div>&nbsp;</div>

	<script type="text/javascript">
		$(document).ready(function(){
			initReport('<s:property value="getText(\'al.message.fillSelects\')" />');
		});
	</script>
</div>