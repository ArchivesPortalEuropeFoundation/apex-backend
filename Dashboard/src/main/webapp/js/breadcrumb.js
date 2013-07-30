/**
 * breadcrumb.js. Used to footer part.
 */

function bBreadcrumb(bcString){
	var breadcrumb = "";
	$("#breadcrumb span").each(function(i){
		if(i!=0){
			breadcrumb+=" - ";
		}
		if(i<($("#breadcrumb").children().size()-1)){
			breadcrumb += escape("<span class=\"linkBread\">"+$(this).html()+"</span>");
		}else{
			var string = "<span class=\"linkBread\"><a href=\""+$(location).attr('href')+"\">"+$(this).html()+"</a></span>";
			breadcrumb += escape(string);
		}
	});
	var breadcrumbLinks = unescape(bcString);
	while(breadcrumbLinks.indexOf("&lt;") != -1){
		breadcrumbLinks = breadcrumbLinks.replace("&lt;","<");
	}
	while(breadcrumbLinks.indexOf("&gt;") != -1){
		breadcrumbLinks = breadcrumbLinks.replace("&gt;",">");
	}
	while(breadcrumbLinks.indexOf("&quot;") != -1){
		breadcrumbLinks = breadcrumbLinks.replace("&quot;","\"");
	}
	var text = "";
	if(breadcrumbLinks.length>2){
		breadcrumbLinks = breadcrumbLinks.substring(1, breadcrumbLinks.length-1);
		text = $("#titledashboard p").text();
		text = text.substring(text.indexOf("-"),text.length);
		$("#breadcrumb").html(breadcrumbLinks+" <span class=\"withoutLinkBread\">"+text+"</span>");
		breadcrumb = breadcrumbLinks;
	}
	if(breadcrumb.length>0){
		var link1 = $('#food1').attr('href')+'?breadcrumbLinks=\''+breadcrumb+'\'';
    	var link2 = $('#food2').attr('href')+'?breadcrumbLinks=\''+breadcrumb+'\'';
    	var link3 = $('#food3').attr('href')+'?breadcrumbLinks=\''+breadcrumb+'\'';
    	var link4 = $('#food4').attr('href')+'?breadcrumbLinks=\''+breadcrumb+'\'';
    	$('#food1').attr('href',link1);
    	$('#food2').attr('href',link2);
    	$('#food3').attr('href',link3);
    	$('#food4').attr('href',link4);
	}
}


