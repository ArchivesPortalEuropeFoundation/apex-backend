<%@ taglib prefix="s" uri="/struts-tags" %>
<div id="aLandscape" class="aLandscape">
    <div id="divCountry">
        <p style="text-align: center;"><s:property value="country"/> <s:property value="getText('al.message.archivallandscape')" /></p>
        <hr/>
        <form id="editArchivalLandscape" name="form" method="post">
            <div id="divError" class="divError">
                <s:actionmessage />
            </div>
            <div id="jActions">
                <div id="filterSelectContainer" class="filterContainer">
                    <div id="filterSelect">
                        <div class="firstFilterDiv">
                            <label for="textAL"><s:property value="getText('al.message.name')" />:</label>
                            <input type="text" name="textAL" id="textAL" class="inputTextBar"/>
                        </div>
                        <div>
                            <div style="float:left;">
                                <div class="secondFilterDiv">
                                    <label for="element" style="padding-right:4px;"><s:property value="getText('al.message.element')" />:</label>
                                    <select name="element" id="element">
                                        <option value="series"><s:property value="getText('al.message.series')"/></option>
                                        <option selected="selected" value="file"><s:property value="getText('al.message.file')"/></option>
                                    </select>
                                </div>
                                <div class="secondFilterDiv">
                                    <label for="selectedLang"><s:property value="getText('al.message.selectlanguage')"/>:</label>
                                    <select name="selectedLang" id="selectedLang" >
                                        <s:iterator var="row" value="langList">
                                            <s:if test='#row.getIsoname().toLowerCase().equals("eng")'>
                                                <option selected="selected" value="<s:property value="#row.getIsoname().toLowerCase()"/>"><s:property value="#row.getLname()" /></option>
                                            </s:if>
                                            <s:else>
                                                <option value="<s:property value="#row.getIsoname()"/>"><s:property value="#row.getLname()" /></option>
                                            </s:else>
                                        </s:iterator>
                                    </select>
                                </div>
                            </div>
                        </div>
                    </div>
                    <div id="addDiv" class="divAction">
                        <s:property value="getText('al.message.addtolist')" />
                    </div>
                </div>
                <div id="actionsSelectDivContainer" class="filterContainer">
                    <s:if test="AL.size()==0">
                        <s:property value="getText('al.message.noelements')" />
                    </s:if>
                    <s:else>
                        <div id="selectOnlyListElements" class="divSelectOnlyListElements" style="margin-bottom: 0px; ">
                            <label for="archivalLandscapeSelect" class="ALElementLabel"><s:property value="getText('al.message.listal')" /></label>
                            <select class="divSelectOnlyListElements2" name="archivalLandscapeSelect" id="archivalLandscapeSelect" size="<s:property value="archivalInstitutions.size()" />">
                                <s:iterator var="row" value="archivalInstitutions">
                                    <option style="text-align:justify;" value="<s:property value="#row.id" />" ><s:property value="#row.name" /></option>
                                </s:iterator>
                            </select>
                        </div>
                    </s:else>
                    <div id="actionsButtons" style="display: none; ">
                        <div id="deleteDiv" class="divAction"><s:property value="getText('al.message.modify')" /></div>
                        <div id="moveUpDiv" class="divAction"><s:property value="getText('al.message.up')" /></div>
                        <div id="moveDownDiv" class="divAction"><s:property value="getText('al.message.down')" /></div>
                        <div id="unselectDiv" class="divAction">Unselect List Elements</div>
                    </div>
                </div>
                <div id="stateDiv" class="divStatus"></div>

                <s:if test="groupArchivalInstitutionList != null && groupArchivalInstitutionList.size() > 0">
                    <div id="divGroupNodes" style="float:left;margin-top:10px;">
                        <p><label for="father" style="float:left;"><s:property value="getText('al.message.selectGroup')" />: </label>
                            <select id="father" name="father" size="1" >
                                <s:iterator var="row" value="groupArchivalInstitutionList">
                                    <option value="<s:property value="#row.id" />"><s:property value="#row.name" /></option>
                                </s:iterator>
                            </select>
                        </p>
                        <div class="ALP">
                            <s:submit action="changeNode" id="submitFormGroupNodes" key="al.message.changeGroup" />
                        </div>
                    </div>
                </s:if>

                <div id="divForSubmitButtom" class="filterContainer" style="border:0px;margin-top:5px;margin-bottom:30px;">
                    <input id="finalSubmitButton" class="inputButtonSubmitAL" type="submit" onclick="showcurtain();" value="Save"/>
                </div>
            </div>

            <script type="text/javascript">
                $("#addDiv").click(function () {

                });
                $("#moveUpDiv").click(function () {

                });
                $("#moveDownDiv").click(function () {

                });
                $("#deleteDiv").click(function () {
                    if(confirm("Are you sure you want to delete that archival institution? If yes, all the files linked to the institution will be erase forever, and so will its child institutions.")) {
                        if(confirm("Really really sure?")) {
                            //todo: Erase the AI with AJAX
                            $.post('deleteArchivalInstitution.action', {'archivalInstitutionToErase':$("select[name=archivalLandscapeSelect] option:selected").val()}, function(response) {
                                if(true) { //If erasing worked
                                    //erase the AI in the list
                                    updateStatusWindow("<span style=\"font-weight:bold;\">" + $("select[name=archivalLandscapeSelect] option:selected").text() + "</span> " + '<s:property value="getText('al.message.wasdeleted')" />');
                                } else { //Erasing did not work
                                    updateLongStatusWindow("<span style=\"font-weight:bold;\">" + $("select[name=archivalLandscapeSelect] option:selected").text() + "</span> " + 'could not be deleted... Sorry, try again or contact an administrator.');
                                }
                                $("#archivalLandscapeSelect").selectedIndex = -1;
                                $("#actionsButtons").hide();
                            });
                        }
                    }
                });

                $("#unselectDiv").click(function () {
                    $("select[name=archivalLandscapeSelect]")[0].selectedIndex = -1;
                    $("#actionsButtons").hide();
                });
                $("#finalSubmitButton").click(function() {

                });
                $("body").ready(function () {

                });
                $("#archivalLandscapeSelect").change(function() {
//                    $("#archivalLandscapeSelect option:selected");
                    $("#actionsButtons").show();
                });
                function showcurtain() {
                    $("#curtain").show();
                }
                function updateStatusWindow(text) {
                    $("#stateDiv").html(text);
                    $("#stateDiv").fadeIn('slow');
                    setTimeout('$("#stateDiv").fadeOut("slow")', 2000);
                }
                function updateLongStatusWindow(text) {
                    $("#stateDiv").html(text);
                    $("#stateDiv").fadeIn('slow');
                    setTimeout('$("#stateDiv").fadeOut("slow")', 4000);
                }
            </script>
        </form>
    </div>
</div>