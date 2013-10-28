function initPage() {
    $('#profileCb').change(function() {
        var params = {profilelist: $("#profileCb").val()};
        $.get("userprofiles.action", params, function(data) {
            $("#wrap").html(data);
        });
    });
}