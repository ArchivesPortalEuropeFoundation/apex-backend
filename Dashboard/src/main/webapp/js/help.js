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

function count() {
    try{
    	var loadUrl = "getFilesFromSession.action";
	    $.post(loadUrl, null, function(databack) {
	        if(databack != null){
	            printCount(databack.listId);
	        }
	    }, 'json');
    }
    catch (e) {
	}
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
function addOneFile(id){
    var loadUrl = "addOneFile.action";
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
function addFewFiles(ids){
    var loadUrl = "addOneFile.action";
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
function clearFilesFromSession(){
    $("#files").html("");
    var loadUrl = "clearFilesFromSession.action";
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
function addAllFAsInSession(){
    $("#files").html("");
    var loadUrl = "addAllFAsInSession.action";
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
function addAllEacCpfsInSession(){
    $("#files").html("");
    var loadUrl = "addAllEacCpfsInSession.action";
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

function checkCurrentOpts() {
    var loadUrl = "checkCurrentConversionOptions.action";
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
function prepareSubmitAndCancelBtns() {
    $("#submitBtnRoleType").unbind();
    $("#cancelBtnRoleType").unbind();
    $("#submitBtnRoleType").bind("click", function(){
        var loadUrl = "saveConversionOptions.action";
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