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



function loadGoogleAnalytics() {
	var hostname = window.location.hostname;
	  (function(i,s,o,g,r,a,m){i['GoogleAnalyticsObject']=r;i[r]=i[r]||function(){
	  (i[r].q=i[r].q||[]).push(arguments)},i[r].l=1*new Date();a=s.createElement(o),
	  m=s.getElementsByTagName(o)[0];a.async=1;a.src=g;m.parentNode.insertBefore(a,m)
	  })(window,document,'script','//www.google-analytics.com/analytics.js','ga');

	if (hostname == "www.archivesportaleurope.net" || hostname == "archivesportaleurope.net" || hostname == "dashboard.archivesportaleurope.net"){
		ga('create', 'UA-37870082-1', 'archivesportaleurope.net');
	}else if (hostname == "contentchecker.archivesportaleurope.net"){
		ga('create', 'UA-40082820-1', 'contentchecker.archivesportaleurope.net');
	}else {
		ga('create', 'UA-42624055-1', 'development.archivesportaleurope.net');
	}
	ga('send', 'pageview');
}