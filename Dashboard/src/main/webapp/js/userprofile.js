function initPage() {
    $('#profileCb').change(function() {
        var params = {profilelist: $("#profileCb").val()};
        $.get("userprofiles.action", params, function(data) {
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
    $("ul#userprofileTabsContainer li a[href^='#tab']").each(function() {
        $(this).removeClass("userprofileCurrenttab");
    });
    $("div[id='" + shown + "']").show();
    $("ul#userprofileTabsContainer li a[href='#" + shown + "']").addClass("userprofileCurrenttab");
}
