/**
 * Function to set the value for the ID of the profile selected.
 */
function initPage(){
	// Set the initial value of the profile.
	$("#selectedProfile").val($("#selectProfile").val());
	
	// Set the value when the select change.
	$('#selectProfile').change(function() {
		$("#selectedProfile").val($("#selectProfile").val());
	});
}