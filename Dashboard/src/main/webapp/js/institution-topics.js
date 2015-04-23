function initInstitutionTopics() {
    $("#displayCreateEditTopicMapping_mergeButton").click( function(e) {
        e.preventDefault();
        mergeTopics();
        return false;
    });
}

function mergeTopics() {
    var dataCountryManager = $('#keywordscountrymanager').text();
    var dataInstitutionManager = $('#keywords').text();
    var arrayCountryManager = dataCountryManager.split('|');
    var arrayInstitutionManager = dataInstitutionManager.split('|');
    var union = createUnion(arrayCountryManager, arrayInstitutionManager);
    var stringUnion = union.join("|");
    alert(stringUnion);
    $("#keywords").html(stringUnion);
}

function createUnion(array1, array2) {
    var hash = {}, union = [];
    $.each(
        $.merge($.merge([], array1), array2), function (index, value) {
            hash[value] = value;
        }
    );
    $.each(hash, function (key, value) {
        union.push(key);
    });
    return union;
}