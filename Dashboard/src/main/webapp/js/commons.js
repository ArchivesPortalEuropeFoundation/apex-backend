//based on Bastiaan Verhoef comment on issue Bug #1411
function HTMLDecode(s){
    return $('<div></div>').html(s).text();
}

function alertAndDecode(text){
	alert(HTMLDecode(text));
}

function confirmAndDecode(text){
	return confirm(HTMLDecode(text));
}