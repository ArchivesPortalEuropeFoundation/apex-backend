<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles" %>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<div>
	<input type="hidden" id="contextPath" value="${pageContext.request.contextPath}">
	<s:if test="%{newInstitutionsFile.size() > 0 || oldRelatedInstitutions.size() > 0 || oldSameIdentifierInstitutionsFile.size() > 0 || oldEmptyIdentifierInstitutionsFile.size() > 0 || updatedInstitutions.size() > 0 || institutionsToBeDeleted.size() > 0}">
		<s:if test="%{oldRelatedInstitutions.size() > 0 || oldSameIdentifierInstitutionsFile.size() > 0 || oldEmptyIdentifierInstitutionsFile.size() > 0}">
			<div id="reportMessage">
				<div id="divSummary" class="alDivSummary">
					<h1><s:property value="getText('al.message.elementNeedsAttention')" /></h1>
				</div>

				<div>&nbsp;</div>

				<s:if test="%{oldRelatedInstitutions.size() > 0}">
					<div id="messageSameName">
						<div>&nbsp;</div>
						<s:property value="getText('al.message.firstLabelSummary')" />
						<s:iterator value="oldRelatedInstitutions" var="sameName" status="status">
							<b><s:property value="#sameName.ainame" /></b><s:if test="%{oldRelatedInstitutions.size() > (#status.index + 1)}">, </s:if>
						</s:iterator>
						<s:property value="getText('al.message.summarySameName')" />
						<div>&nbsp;</div>
					</div>
				</s:if>

				<s:if test="%{oldSameIdentifierInstitutionsFile.size() > 0}">
					<div id="messageSameIdentifier">
						<div>&nbsp;</div>
						<s:property value="getText('al.message.firstLabelSummary')" />
						<s:iterator value="oldSameIdentifierInstitutionsFile" var="sameIdentifier" status="status">
							<b><s:property value="#sameIdentifier.ainame" /></b><s:if test="%{oldSameIdentifierInstitutionsFile.size() > (#status.index + 1)}">, </s:if>
						</s:iterator>
						<s:property value="getText('al.message.summarySameIdentifiers')" />
						<div>&nbsp;</div>
					</div>
				</s:if>

				<s:if test="%{oldEmptyIdentifierInstitutionsFile.size() > 0}">
					<div id="messageEmptyIdentifier">
						<div>&nbsp;</div>
						<s:property value="getText('al.message.firstLabelSummary')" />
						<s:iterator value="oldEmptyIdentifierInstitutionsFile" var="emptyIdentifier" status="status">
							<b><s:property value="#emptyIdentifier.ainame" /></b><s:if test="%{oldEmptyIdentifierInstitutionsFile.size() > (#status.index + 1)}">, </s:if>
						</s:iterator>
						<s:property value="getText('al.message.summaryEmptyIdentifiers')" />
						<div>&nbsp;</div>
					</div>
				</s:if>

				<div>&nbsp;</div>
				<div>&nbsp;</div>
			</div>
		</s:if>

		<div id="report">
			<div id="divDescription" class="alDivShowHide">
				<h1><img id="imgExpandDetails" src="images/expand/mas.gif"/> <s:property value="getText('al.message.detail')" /></h1>
			</div>

			<div>&nbsp;</div>

			<div id="contentReport">
				<s:if test="%{newInstitutionsFile.size() > 0}">
					<div id="insertsPart">
						<div>
							<h2><s:property value="getText('al.message.institutionstoinsert')" /></h2>
						</div>
						<div>&nbsp;</div>
						<div>
							<s:iterator value="newInstitutionsFile" var="institution" status="status">
								<div class="keep_ai2">
									<div class="middleDiv"><label for="insertInstitution_<s:property value="#status.index" />"><s:property value="#institution.ainame" /></label></div>
									<div class="middleDiv"><input type="text" class="readOnlyInput" id="insertInstitution_<s:property value="#status.index" />" value="<s:property value="#institution.internalAlId" />" readonly onfocus="this.blur()" /></div>
									<div>&nbsp;</div>
								</div>
							</s:iterator>
						</div>
					</div>
				</s:if>
				<s:if test="%{oldEmptyIdentifierInstitutionsFile.size()>0 || oldRelatedInstitutions.size()>0 || oldSameIdentifierInstitutionsFile.size()>0 }">
					<form name="updatesPartForm" id="updatesPartForm" method="post">
						<div id="updatesPart">
							<s:if test="%{oldRelatedInstitutions.size() > 0}">
								<div id="institutionsWithSameNameDiv">
									<div>
										<h2><s:property value="getText('al.message.institutionsdetectedwithsamename')" /></h2>
									</div>
									<div>&nbsp;</div>
									<div id="institutionsWithSameNameOldDiv" class="middleDiv">
										<s:iterator value="oldRelatedInstitutions" var="oldInstitution" status="status">
											<div id="institutionChanged_<s:property value="#status.index"/>">
												<div><label for="oldSameNameInstitution[<s:property value="#status.index"/>]"><s:property value="#oldInstitution.ainame" /></label></div>
												<div><input type="text" class="readOnlyInput" id="oldSameNameInstitution[<s:property value="#status.index"/>]" name="oldSameNameInstitution[<s:property value="#status.index"/>]" value="<s:property value="#oldInstitution.internalAlId" />"  readonly onfocus="this.blur()" /></div>
												<div>&nbsp;</div>
											</div>
										</s:iterator>
									</div>
									<div id="institutionsWithSameNameNewDiv" class="middleDiv">
										<s:iterator value="newRelatedInstitutions" var="newInstitution" status="status">
											<div id="institutionChanged<s:property value="#status.index"/>">
												<div><label for="newSameNameInstitution[<s:property value="#status.index"/>]"><s:property value="#newInstitution.ainame" /></label></div>
												<div><input type="text" class="readOnlyInput" id="newSameNameInstitution[<s:property value="#status.index"/>]" name="newSameNameInstitution[<s:property value="#status.index"/>]" value="<s:property value="#newInstitution.internalAlId" />" readonly onfocus="this.blur()" /></div>
												<div>&nbsp;</div>
											</div>
										</s:iterator>
									</div>
								</div>
							</s:if>
	
							<s:if test="%{oldSameIdentifierInstitutionsFile.size() > 0}">
								<div id="institutionsWithSameIdDiv">
									<div>
										<h2><s:property value="getText('al.message.sameIdentifier')" /></h2>
									</div>
									<div>&nbsp;</div>
									<div id="institutionsWithSameIdOldDiv" class="middleDiv">
										<s:iterator value="oldSameIdentifierInstitutionsFile" var="oldSameIdentifier" status="status">
											<div id="sameIdInstitutionChanged_<s:property value="#status.index"/>">
												<div><label for="oldSameIdentifierInstitution[<s:property value="#status.index"/>]"><s:property value="#oldSameIdentifier.ainame" /></label></div>
												<div>
													<input type="text" class="readOnlyInput" id="oldSameIdentifierInstitution[<s:property value="#status.index"/>]" name="oldSameIdentifierInstitution[<s:property value="#status.index"/>]" value="<s:property value="#oldSameIdentifier.internalAlId" />" readonly onfocus="this.blur()" />
													<input type="hidden" id="sameIdentifiersNamed[<s:property value="#status.index"/>]" name="sameIdentifiersNamed[<s:property value="#status.index"/>]" value="<s:property value="#oldSameIdentifier.ainame" />" />
													</div>
												<div>&nbsp;</div>
											</div>
										</s:iterator>
									</div>
									<div id="institutionsWithSameIdNewDiv" class="middleDiv">
										<s:iterator value="newSameIdentifierInstitutionsFile" var="newSameIdentifier" status="status">
											<div id="sameIdInstitutionChanged<s:property value="#status.index"/>">
												<div><label for="newSameIdentifierInstitution[<s:property value="#status.index"/>]"><s:property value="#newSameIdentifier.ainame" /></label></div>
												<div><input type="text" class="readOnlyInput" id="newSameIdentifierInstitution[<s:property value="#status.index"/>]" name="newSameIdentifierInstitution[<s:property value="#status.index"/>]" value="<s:property value="#newSameIdentifier.internalAlId" />" readonly onfocus="this.blur()" /></div>
												<div>&nbsp;</div>
											</div>
										</s:iterator>
									</div>
								</div>
							</s:if>
	
							<s:if test="%{oldEmptyIdentifierInstitutionsFile.size() > 0}">
								<div id="institutionsWithEmptyIdDiv">
									<div>
										<h2><s:property value="getText('al.message.emptyIdentifier')" /></h2>
									</div>
									<div>&nbsp;</div>
									<div id="institutionsWithEmptyIdOldDiv" class="middleDiv">
										<s:iterator value="oldEmptyIdentifierInstitutionsFile" var="oldEmptyIdentifier" status="status">
											<div id="emptyIdInstitutionChanged_<s:property value="#status.index"/>">
												<div><label for="oldEmptyIdentifierInstitution[<s:property value="#status.index"/>]"><s:property value="#oldEmptyIdentifier.ainame" /></label></div>
												<div>
													<input type="text" class="readOnlyInput" id="oldEmptyIdentifierInstitution[<s:property value="#status.index"/>]" name="oldEmptyIdentifierInstitution[<s:property value="#status.index"/>]" value="<s:property value="#oldEmptyIdentifier.internalAlId" />" readonly onfocus="this.blur()" />
													<input type="hidden" id="emptyIdentifiersNamed[<s:property value="#status.index"/>]" name="emptyIdentifiersNamed[<s:property value="#status.index"/>]" value="<s:property value="#oldEmptyIdentifier.ainame" />" />
												</div>
												<div>&nbsp;</div>
											</div>
										</s:iterator>
									</div>
									<div id="institutionsWithEmptyIdNewDiv" class="middleDiv">
										<s:iterator value="newEmptyIdentifierInstitutionsFile" var="newEmptyIdentifier" status="status">
											<div id="emptyIdInstitutionChanged<s:property value="#status.index"/>">
												<div><label for="newEmptyIdentifierInstitution[<s:property value="#status.index"/>]"><s:property value="#newEmptyIdentifier.ainame" /></label></div>
												<div><input type="text" class="readOnlyInput" id="newEmptyIdentifierInstitution[<s:property value="#status.index"/>]" name="newEmptyIdentifierInstitution[<s:property value="#status.index"/>]" value="<s:property value="#newEmptyIdentifier.internalAlId" />" readonly onfocus="this.blur()" /></div>
												<div>&nbsp;</div>
											</div>
										</s:iterator>
									</div>
								</div>
							</s:if>
	
						</div>
					</form>
				</s:if>
				
				<s:if test="%{updatedInstitutions.size() > 0}">
					<div id="updatedInstitutionsDiv">
						<div id="alupdatesMessage">
							<h2><s:property value="getText('al.message.changedparent')" /></h2>
							<div>&nbsp;</div>
						</div>
						<s:iterator value="updatedInstitutions" var="updated" status="status">
							<div class="alupdates">
								<div class="middleDiv"><label for="updatedInstitution_<s:property value="#status.index"/>"><s:property value="#updated.ainame" /></label></div>
								<div class="middleDiv"><input type="text" class="readOnlyInput" id="updatedInstitution_<s:property value="#status.index"/>" value="<s:property value="#updated.internalAlId" />" readonly onfocus="this.blur()" /></div>
								<div>&nbsp;</div>
							</div>
						</s:iterator>
					</div>
				</s:if>

				<s:if test="%{institutionsToBeDeleted.size() > 0}">
					<div id="deletesPart">
						<div>
							<div>
								<h2><s:property value="getText('al.message.institutionstodelete')" /></h2>
							</div>
							<div>&nbsp;</div>
							<s:iterator value="institutionsToBeDeleted" var="identifier" status="status">
							<div class="aldeletes">
								<div class="middleDiv"><label for="deleteInstitution_<s:property value="#status.index"/>"><s:property value="#identifier.ainame" /></label></div>
								<div class="middleDiv"><input type="text" class="readOnlyInput" id="deleteInstitution_<s:property value="#status.index"/>" value="<s:property value="#identifier.internalAlId" />" readonly onfocus="this.blur()" /></div>
								<div>&nbsp;</div>
							</div>
							</s:iterator>
						</div>
					</div>
				</s:if>
			</div>
		</div>
	</s:if>

	<s:else>
		<div id="noChangesDiv">
			<div>&nbsp;</div>
			<s:property value="getText('al.message.noChanges')" />
			<div>&nbsp;</div>
			<s:property value="getText('a.message.overwritefile.question')" />
		</div>
	</s:else>

	<div>&nbsp;</div>
	<div id="actionsPart">
		<table>
			<tr>
			<s:if test="%{oldEmptyIdentifierInstitutionsFile.size()>0 || oldRelatedInstitutions.size()>0 || oldSameIdentifierInstitutionsFile.size()>0 }">
				<td>
					<div id="divChangeIdentifiers">
						<div id="buttonChangeIdentifiers" class="alDivAction" ><s:property value="getText('al.message.changeIdentifiers')" /></div>
					</div>
				</td>
			</s:if>
				<td>
					<div id="divRecheckIdentifiers" class="hidden">
						<div id="buttonRecheckIdentifiers" class="alDivAction" ><s:property value="getText('al.message.recheckIdentifiers')" /></div>
					</div>
				</td>

				<td>
					<div id="divContinueUpload">
						<form name="continueUploadForm" action="ALContinueUpload.action" id="continueUploadForm" method="post">
							<div id="buttonContinueUpload" class="alDivAction" ><s:property value="getText('al.message.continue')" /></div>
						</form>
					</div>
				</td>

				<td>
					<div id="divCancelOverwrite">
						<form name="cancelOverwriteForm" action="ALCancelOverwrite.action" id="cancelOverwriteForm" method="post">
							<div id="buttonCancelOverwrite" class="alDivAction" ><s:property value="getText('al.message.cancel')" /></div>
							<s:hidden name="Overwrite" value="false"></s:hidden>
						</form>
					</div>
				</td>

				<td>
					<div id="divCancelEdition" class="hidden">
						<form name="cancelEditionForm" action="ALReport.action" id="cancelEditionForm" method="post">
							<div id="buttonCancelEdition" class="alDivAction" ><s:property value="getText('al.message.cancel')" /></div>
						</form>
					</div>
				</td>
			</tr>
		</table>
	</div>
	<div>&nbsp;</div>

	<script type="text/javascript">
		$(document).ready(function(){
			initReport();
		});
	</script>
</div>
