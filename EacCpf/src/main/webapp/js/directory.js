

function seeLess(clazz,identifier){
	if (identifier){
		prefix = "#repository_" + identifier + " ." +clazz + " ";
	} else {
		prefix = "." +clazz + " ";
	}
	$(prefix + ".displayLinkSeeMore").removeClass("hidden");
	$(prefix + ".displayLinkSeeLess").addClass("hidden");
	$(prefix + ".longDisplay").hide();
}
function seeMore(clazz,identifier){
	if (identifier){
		prefix = "#repository_" + identifier + " ." +clazz + " ";
	} else {
		prefix = "." +clazz + " ";
	}
	$(prefix + ".displayLinkSeeLess").removeClass("hidden");
	$(prefix + ".displayLinkSeeMore").addClass("hidden");
	$(prefix + ".longDisplay").show();
}
function initEagDetails(){
	$(".displayLinkSeeLess").addClass("hidden");
	$(".longDisplay").hide();
	
	
}



