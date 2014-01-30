function initPage() {
    $('#profileCb').change(function() {
        var params = {profilelist: $("#profileCb").val()};
        $.get("ingestionprofiles.action", params, function(data) {
            $("#principal").replaceWith(data);
            $("body meta").remove();
            $("body title").remove();
//            $("body link[type='text/css']").remove();
        });
    });
}

function hideAndShow(idPrefix, shown) {
    $("div[id^='" + idPrefix + "']").each(function() {
        $(this).hide();
    });
    $("ul#ingestionprofileTabsContainer li a[href^='#tab']").each(function() {
        $(this).removeClass("ingestionprofileCurrenttab");
    });
    $("div[id='" + shown + "']").show();
    $("ul#ingestionprofileTabsContainer li a[href='#" + shown + "']").addClass("ingestionprofileCurrenttab");
}

function validateAndSave(profileNameError, edmDaoError, languageError) {
    var profilename = $("#profilename").attr("value");
    if (profilename == null || profilename == "") {
        alert(profileNameError);
        return;
    }
    var upFileAction = $("#uploadedFileAction").attr("value");
    if (upFileAction == "2"){
        var edmDaoType = $("#edmDaoType").attr("value");
        if (edmDaoType == null || edmDaoType == "") {
            alert(edmDaoError);
            return;
        }
        var languageSelection = $("#languageselection").attr("value");
        if (languageSelection == null || languageSelection == "") {
            alert(languageError);
            return;
        }
    }
    $('#webformIngestionprofile').submit();
}