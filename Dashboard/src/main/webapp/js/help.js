function uploadBlackout(text_value) {
    var curtain = document.body.appendChild( document.createElement('div') );
    curtain.id = "curtain";
    curtain.onkeypress = curtain.onclick = function() {
        return false;
    };
    var loading = document.getElementById("curtain").appendChild(document.createElement('div'));
    loading.innerHTML = "<b>" + text_value + "</b>";
    loading.id = "loading";
    loading.onkeypress = loading.onclick = function() {
        return false;
    };
    return curtain;
}
function removeBlackout() {
    var d = document.body;
    var olddiv = document.getElementById("curtain");
    if(d == undefined || olddiv == undefined){return;}
    d.removeChild(olddiv);
}
function changeXslValue(selectTag, id){
    document.getElementById(id).value = selectTag.options[selectTag.selectedIndex].text;
}
function createProgressBar(){
    $("#progressbar").progressbar({ value: 0 });
    $("#progresstext").html("<p>0%</p>");
}
function createLoop(contextPath, aiId, id, xmlTypeId){
    var loadUrl = contextPath + "/AjaxControllerAction.action";
    var data = {id: id, aiId: aiId, xmlTypeId: xmlTypeId};
    $.post(loadUrl, data, function(databack){
        doLoop(contextPath);
    }, 'json');
}
function doLoop(contextPath){
    var loadUrl = contextPath + "/AjaxConversionControllerAction.action";
    $.post(loadUrl, null, function(databack){
        if(databack != null){
            $("#progressbar").progressbar({ value : databack.counter });
            $("#progresstext").html("<p>" + databack.counter + "%</p>");
            doLoop(contextPath);
        } else {
            $("#progressbar").progressbar({ value : 100 });
            $("#progresstext").html("<p>100%</p>");
            location.reload(true);
        }
    }, 'json');
}
function count(contextPath) {
    var loadUrl = contextPath + "/getFAsFromSession.action";
    $.post(loadUrl, null, function(databack) {
        if(databack != null){
            printCount(databack.listId);
        }
    }, 'json');
}
function printCount(listId){
    var nb = 0;
    $.each(listId, function(index, value) {
        nb++;
        if($("#check_" + value).length > 0){
            if(!$("#check_" + value).is(":checked")){
                $("#check_" + value).attr('checked','checked');
            }
        }
    });
    if(nb > 1) {
        $("#sizeFiles").html(nb + " selected files");
    } else {
        $("#sizeFiles").html(nb + " selected file");
    }
}
function addOneFA(contextPath, id){
    var loadUrl = contextPath + "/addOneFA.action";
    var data = {id: id};
    $.post(loadUrl, data, function(databack) {
        if(databack != null){
            if(databack.listId != null) {
                printCount(databack.listId);
            } else {
                count(contextPath);
            }
        }
    },'json');
}
function addFewFAs(contextPath, ids){
    var loadUrl = contextPath + "/addOneFA.action";
    var somedata_assoc = $.param({'ids': ids}, true);
    $.post(loadUrl, somedata_assoc, function(databack) {
        if(databack != null){
            if(databack.listId != null) {
                printCount(databack.listId);
            } else {
                count(contextPath);
            }
        }
    },'json');
}
function clearFAsFromSession(contextPath){
    $("#files").html("");
    var loadUrl = contextPath + "/clearFAsFromSession.action";
    $.post(loadUrl, null, function(databack) {
        if(databack != null){
            uncheckAllCheckboxes();
            if(databack.listId != null) {
                printCount(databack.listId);
            } else {
                count(contextPath);
            }
        }
    },'json');
}
function addAllFAsInSession(contextPath){
    $("#files").html("");
    var loadUrl = contextPath + "/addAllFAsInSession.action";
    $.post(loadUrl, null, function(databack) {
        if(databack != null){
            $("input:checkbox[name=check]").each(function(){
                if(!$(this).is(":checked")) {
                    $(this).attr('checked','checked');
                }
            });
            if(databack.listId != null) {
                printCount(databack.listId);
            } else {
                count(contextPath);
            }
        }
    },'json');
}
function uncheckAllCheckboxes(){
    $("input:checkbox[name=check]").each(function(){
        if($(this).is(":checked"))
            $(this).removeAttr('checked');
    });
}
function checkCurrentOpts(contextPath) {
    var loadUrl = contextPath + "/checkCurrentConversionOptions.action";
    $.post(loadUrl, null, function(databack){
        if(databack){
            if(databack.error){
                console.log("ERROR");
                $("input:radio[name=roleType]").val(["UNSPECIFIED"]);
                $("input:checkbox[name=useExistingRole]").val(["useExistingRole"]);
            } else {
                $("input:radio[name=roleType]").val([databack.optsDefault]);
                if(databack.optsUseExisting == 'true') {
                    $("input:checkbox[name=useExistingRole]").val(["useExistingRole"]);
                } else {
                    $("input:checkbox[name=useExistingRole]").removeAttr("checked");
                }
            }
        }
    }, 'json');
    prepareSubmitAndCancelBtns(contextPath);
}
function prepareSubmitAndCancelBtns(contextPath) {
    $("#submitBtnRoleType").unbind();
    $("#cancelBtnRoleType").unbind();
    $("#submitBtnRoleType").bind("click", function(){
        var loadUrl = contextPath + "/saveConversionOptions.action";
        var data = {optsUseExisting: $("#useExistingRole").is(":checked"), optsDefault: $("input:radio[name=roleType]:checked").val()};
        $.post(loadUrl, data, function(databack){
            if(databack){
                if(databack.error){
                    console.log("ERROR");
                } else {
                    $.fn.colorbox.close();
                }
            }
        }, 'json');
    });
    $("#cancelBtnRoleType").bind("click", function(){
        $.fn.colorbox.close();
    });
}

function checkEagLink(){
    $('.eagLink').each(function(index) {
        $(this).attr("title", "Check if the namespace of the file is correct...");
        $(this).bind("click", function(){
            var identifier = $(this).attr("id");
            $.post("checkEagNamespace.action", {fileId:$(this).attr("id")}, function(databack){
                if(databack.old == "true"){
                    $("#" + identifier).addClass("wrong");
                    $("#" + identifier).attr("title", "The namespace is not correct...");
                } else if(databack.old == 'false') {
                    $("#" + identifier).addClass("correct");
                    $("#" + identifier).attr("title", "The namespace is correct...");
                }
                $("#" + identifier).unbind("click");
                $("#" + identifier).removeClass("eagLink");
            });
        });
    });
}