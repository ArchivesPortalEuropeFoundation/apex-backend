(function($) {
	$.fn.richSelect = function(params) {
		if(params==null || params instanceof Array){
			params = new Array();
		}
		var id = $(this).attr("id");
		if(id.indexOf("#")>=0){
			id = id.substring(id.indexOf("#")+1);
		}
		if(!("no_hide" in params && params["no_hide"]==true)){
			$(this).css("display","none");
		}
		var divRichSelect = '<div id="divRichSelect'+id;
		if("global_class" in params){
			divRichSelect += '" class=\"'+params["class"]+'\" ';
		}else{
			divRichSelect += '" class=\"default_global_class\" ';
		}
		divRichSelect += '>';
		divRichSelect += '<ul id="ul_RichSelect'+id+'"></ul></div>';
		
		$(this).after(divRichSelect);
		$("#"+id+" option").each(function(){
			
			var child = "<li id=\"li_"+$(this).val()+"\"";
			
			var style = " style=\"";
			if($(this).attr("style")!="undefined"){
				style += $(this).attr("style");
				if(style.lastIndexOf(";")!==(style.length-1)){
					style+=";";
				}
			}
			
			//update if the element is selected before the richSelect is loaded
			var selected = $(this).attr("selected");
			
			style+="\"";
			var classes = $(this).attr("class");
			style+=" class=\"";
			style+="pointer ";
			if(classes){
				style+=classes;
				if("individual_class" in params){
					style+=" "+params["individual_class"];
				}
			}
			if(selected || selected == "selected"){
				style+=" selected";
			}
			//disable browser text selection on component 
			if(!("text_selection" in params && params["text_selection"]==true)){
				style+=" text_unseleced";
			}
			style+="\" ";
			child +=style+" >"+$(this).text()+"</li>";
			$("#ul_RichSelect"+id).append(child);
			
		});
		
		//events definition
		$("#ul_RichSelect"+id+" li").bind("click",function(){
			var tempId = $(this).attr("id");
			tempId = tempId.substring("li_".length);
			
			var selectedValue = $("#"+id).val(tempId).attr("selected");
			if(!selectedValue){
				selectedValue = false;
			}
			$("#"+tempId).attr("selected",!selectedValue);
			if(("multiple" in params && params["multiple"]!="true") || !("multiple" in params)){ //unselect all elements 
				$(this).siblings().each(function(){ //remove all selected elements
					$(this).removeClass("selected");
				});
			}
			
			if($(this).attr("class") && $(this).attr("class").indexOf("selected")>=0){
				$(this).removeClass("selected");
			}else{
				$(this).addClass("selected");
			}
			//additional events
			if("onelement_click" in params){ 
				params["onelement_click"]();
			}
			if("onelement_unselected" in params){ 
				var classes = $(this).attr("class");
				if(classes.indexOf("selected")<0){
					params["onelement_unselected"]();
				}
			}
			if("onelement_selected" in params){
				var classes = $(this).attr("class");
				if(classes.indexOf("selected")>=0){
					params["onelement_selected"]();
				}
			}
		});
		//end events
	};
})(jQuery);