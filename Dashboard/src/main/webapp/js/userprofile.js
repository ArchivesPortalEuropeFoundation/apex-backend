function initPage() {
    $('#profileCb').change(function() {
        var selected = $("#profileCb").val();
        var params = {profilelist: $("#profileCb").val()};
        $.get("edituserprofiles.action", params, function(data) {
            
        });
    });
}