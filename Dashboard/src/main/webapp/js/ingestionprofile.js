function initPage() {
    $(document).on("change", "#profileCb", function() {
        var params = {profilelist: $("#profileCb").val()};
        $.get("ingestionprofiles.action", params, function(data) {
            $("#principal").replaceWith(data);
            $("body meta").remove();
            $("body title").remove();
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

function validateAndSave(profileNameError, dataProviderError, edmDaoError, languageError, europeanaLicenseError) {
    var profilename = $("#profilename").attr("value");
    if (profilename == null || profilename == "") {
        alert(profileNameError);
        return;
    }
    var upFileAction = $("#uploadedFileAction").attr("value");
    if (upFileAction == "2") {
        var dataProvider = $("#textDataProvider").attr("value");
        if (dataProvider == null || dataProvider == "") {
            alert(dataProviderError);
            return;
        }
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
        var license = $("#licenseeuropeana").attr("checked");
        if (license == "checked") {
            var europeanaLicense = $("#europeanaLicense").attr("value");
            if (europeanaLicense == null || europeanaLicense == "") {
                alert(europeanaLicenseError);
                return;
            }
        }
    }
    $('#webformIngestionprofile').submit();
}

function changeDefaultOptionSet() {
    var assocType = $("#associatedFiletypeCb").val();
    var upFileAct = $("#uploadedFileAction").val();
    if (assocType != "0" && upFileAct == "2") {
        $("#uploadedFileAction").val("1");
    }
    if (assocType == "0") {
        var optionText = "Publish to Archives Portal Europe and Europeana";
        $('#uploadedFileAction option[value="1"]').after('<option value="2">' + optionText + '</option>');
    } else {
        $("#uploadedFileAction option[value='2']").remove();
    }
}