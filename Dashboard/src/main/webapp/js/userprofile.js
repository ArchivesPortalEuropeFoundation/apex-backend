function initPage() {
    $('#profileCb').change(function() {
        var params = {profilelist: $("#profileCb").val()};
        $.get("userprofiles.action", params, function(data) {
            $("#wrap").html(data);
        });
    });
}

function hideAndShow(idPrefix, shown) {
    $("div[id^='" + idPrefix + "']").each(function() {
        $(this).hide();
    });
    $("ul#userprofileTabsContainer li a[href^='#tab']").each(function() {
        $(this).removeClass("userprofileCurrenttab");
    });
    $("div[id='" + shown + "']").show();
    $("ul#userprofileTabsContainer li a[href='#" + shown + "']").addClass("userprofileCurrenttab");
}
