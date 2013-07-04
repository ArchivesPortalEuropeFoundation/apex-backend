/* JS for uploadcontent page */
/**
 * Write upload status bar into document
 */
function writeStatusBar(label,errorText,confirmText){
	document.write("<div id=\"bar\" style=\"width:400px;height:30px;border:2px solid;border-radius:5px 5px 5px 5px;display:none;\"><div id=\"progress\" style=\"float:left;width:1px;height:30px;\"> </div></div>");
	document.write("<input type=\"button\" id=\"uploadButton\" onclick=\"upload('"+errorText+"','"+confirmText+"')\" value=\""+label+"\" />");
}

/**
 * Stop the browser to avoid the form will be sent.
 * @param label
 * @param text
 */
function stopBrowser(label,text){
	if(label && xmlHttpRequest){
		alert(text);
		xmlHttpRequest.abort();
		window.location.href = "upload.action";
	}
}

/**
 * Is the alternative/normal method to send the form.
 */
function alternativeForIe(){
	//$("#uploadButton").remove();
    $("#progress").remove();
    //$("#stopButton").remove();
    $("#httpUpload").submit();
}
/**
 * This method updates the status bar into uploadcontent page
 * @param evt
 */
function uploadProgress(evt){
    if(evt.lengthComputable){
    	document.getElementById("bar").style.display="block";
		var percentComplete = Math.round(evt.loaded * 100 / evt.total);
		document.getElementById('progress').innerHTML = "<div style='font-weight:bold;margin-top:5px;'>"+percentComplete.toString() + "%</div>";
		var letter = Math.round(16*percentComplete/100);
		var letter2 = "";
		switch(letter){
			case 5:
				letter2 = "a";
				break;
			case 4:
				letter2 = "b";
				break;
			case 3:
				letter2 = "c";
				break;
			case 2:
				letter2 = "d";
				break;
			case 1:
				letter2 = "e";
				break;
			case 0:
				letter2 = "f";
				break;
			default:
				letter2 = Math.abs(letter-15.0).toString();
		}
		$('#progress').css("background-color","#"+letter2+"e"+letter2);
		$('#progress').width((percentComplete*4)+"px");
		if(percentComplete>90){
			$('#bar').html("<p style=\"font-weight:bold;\"><img alt=\"loading, please wait\" src=\"images/colorbox/loading.gif\">"+textWhileIngesting+"</p>");
			$('#bar').css("border","0px");
			$("body").append("<div style=\"background-color:#dddddd;opacity:0.4;position:fixed;top:0px;left:0px;width:100%;height:100%;z-index:99;\"> </div>");
			document.getElementById('stopButton').style.display='none';
		}
    }else{
    	document.getElementById('progress').innerHTML = "unable to show";
    }
}
function addConfirm(confirmText){
	$("a").click(function(event){
		event.preventDefault();
		var response = confirm(confirmText);
		if(response){
			window.location.href = $(this).attr("href");
		}
	});
}
/**
 * This option works in all compatible HTML5 browsers
 * @param xmlHttpRequest
 */
function uploadFirstOption(xmlHttpRequest,confirmText){
	try{/* HTML5 way, compatible with FIREFOX>3,SAFARI & CHROME*/
	    var fd = new FormData();
	    fd.append("httpFile", document.getElementById('httpFile').files[0]);
	    xmlHttpRequest.upload.addEventListener("progress", uploadProgress, false);
	    xmlHttpRequest.open("post","httpUpload.action",true);
	    addConfirm(confirmText);
		xmlHttpRequest.send(fd);
	}catch(ex){
		console.log("Error using FormData (HTML5) blob way :: "+ex.message);
		readWithBlob(document.getElementById('httpFile').files[0]);
	}
}
/**
 * This option is for old versions of firefox (like 3.6 or older)
 * @param file
 */
function readFileFirefox3(file){
	var data1 = null;
	try{
		data1 = file.getAsBinary(); //data = file.getAsText("UTF-8");
	}catch(e){
		console.log("Exception trying read a file :: "+e.message);
	}
	return data1;
}
/**
 * This method is for send the form content with Opera browser and ie10
 * @param file
 */
function readWithBlob(file){
	try{
	    var reader = new FileReader();
	    reader.addEventListener("loadend", function(evt){
	    	/*Opera and Internet Explorer(10 with FileReader) haven't "progress" event listener*/
			//xmlHttpRequest.upload.addEventListener("progress",uploadProgress,false); 
			//xmlHttpRequest.upload.onprogress = uploadProgress;
			xmlHttpRequest.open("post","httpUpload.action",true);
			var boundary = "BOUNDARY------"+(new Date).getMilliseconds();
			var request = buildPetition(evt.target.result,boundary);
			xmlHttpRequest.setRequestHeader("Content-Type","multipart/form-data; boundary="+boundary);
			xmlHttpRequest.setRequestHeader("Content-Length",request.length);
			//xmlHttpRequest.sendAsBinary(request); //Opera hasn't sendAsBinary
			xmlHttpRequest.send(request);
	    }, false);
	    try{
	    	reader.readAsBinaryString(file);
	    }catch(ex){
	    	console.log("Exception trying read a file with readAsBinaryString(file), trying with blob :: "+ex.message);
	    	var blob = file.slice(0,file.size-1);
	    	reader.readAsBinaryString(blob);
	    }
	}catch(e){
		console.log("Exception trying read a file with blob method, trying normal way :: "+e.message);
		alternativeForIe();
	}
}
/**
 * Get all the content of a file and return it, it works with IE versions that permit this action.
 * @param path
 */
function readFileIE(path){
	try{
		return new ActiveXObject("Scripting.FileSystemObject").OpenTextFile(path,1).ReadAll();
	}catch(ex){
		console.log("Your browser NOT be able to upload script (too security?), TRYING the normal upload process! :: "+ex.description);
		alternativeForIe();
	}
}

/**
 * Function which uses readFileIE, to use in versions <= ie9
 * @param xmlHttpRequest
 */
function internetExplorer9Way(xmlHttpRequest){
	var completePath = "";
	completePath = $("#httpFile").val();
	if(xmlHttpRequest.addEventListener || xmlHttpRequest.upload){
		//adding event 'progress'
		if(xmlHttpRequest.upload!=undefined){ //TODO
			xmlHttpRequest.upload.addEventListener("progress",uploadProgress,false);
		}else{
			xmlHttpRequest.addEventListener("progress",uploadProgress,false);
		}
		xmlHttpRequest.open("post","httpUpload.action",true);
		data = readFileIE(completePath); //It's needed a way to take the complete path of the file with IE8
		var boundary = "BOUNDARY------"+(new Date).getMilliseconds();
		var request = buildPetition(data,boundary);
		xmlHttpRequest.setRequestHeader("Content-Type","multipart/form-data; boundary="+boundary);
		xmlHttpRequest.setRequestHeader("Content-Length",request.length);
		xmlHttpRequest.send(request);
		alert(request);
	}else{//not suppported '.upload'
		alternativeForIe();
	}
}

/**
 * this is the other way to upload in ie9 without progress bar
 */
function internetExplorer9Way2(){
	alternativeForIe();
}

/**
 * It's the second method to upload content (alternative of HTML5 standar browsers)
 * @param xmlHttpRequest
 */
function uploadSecondOption(xmlHttpRequest){
	var data;
	if($.browser.msie){
		try{
			if(window.FileReader){
				readWithBlob(document.getElementById("httpFile").files[0]);
			}else{
				//internetExplorer9Way(xmlHttpRequest);
				internetExplorer9Way2();
			}
		}catch(ex){
			console.log("Your browser is NOT compatible :: "+ex.description);
			alternativeForIe();
		}
	}else if($.browser.mozilla){//firefox3 way
		try{
			data = readFileFirefox3(document.getElementById('httpFile').files[0]);
			var boundary = "BOUNDARY------"+(new Date).getMilliseconds();
			var request = buildPetition(data,boundary);
			xmlHttpRequest.upload.addEventListener("progress",uploadProgress,false);
			xmlHttpRequest.open("post","httpUpload.action",true);
			xmlHttpRequest.setRequestHeader("Content-Type","multipart/form-data; boundary="+boundary);
			xmlHttpRequest.setRequestHeader("Content-Length",request.length);
			xmlHttpRequest.sendAsBinary(request);
		}catch(ex){
			console.log("trying alternative·"+ex.message);
			alternativeForIe();
		}
	}else{ //Normal submit 
		alternativeForIe();
	}
}
/**
 * This function builds the form content to be sent with ajax in plain text form. Returns it.
 * @param data
 * @param boundary
 * @returns {String}
 */
function buildPetition(data,boundary){
	var request = "--"+boundary+"\r\n" ;
	request += 'Content-Disposition: form-data; name="httpFile"; filename="'+document.getElementById("httpFile").value+'"\r\n';
	request += "Content-Type: application/octet-stream\r\n";
	request += "Content-Length: " + data.length+"\r\n\r\n";
	request += data + "\r\n" ;
	request+= "--" + boundary + "--" + "\r\n";
	return request;
}
/**
 * This is the main logic to send the form, it instances an AJAX connection and choose what method has to try.
 */
function upload(warningText,confirmText){
	if($("#httpFile").val().substr($("#httpFile").val().lastIndexOf("."),4).toLowerCase()==".xml" ||
			$("#httpFile").val().substr($("#httpFile").val().lastIndexOf("."),4).toLowerCase()==".zip" /*|| 
			$("#httpFile").val().substr($("#httpFile").val().lastIndexOf("."),4).toLowerCase()==".xsl" || 
			$("#httpFile").val().substr($("#httpFile").val().lastIndexOf("."),4).toLowerCase()==".xslt"*/
			){
		$("#uploadButton").remove();
		document.getElementById('stopButton').style.display='block';
		//var xmlHttpRequest;
		if (window.XMLHttpRequest){ //New browsers
	    	xmlHttpRequest = new XMLHttpRequest();
	    }
	    else if(window.ActiveXObject){//ActiveXObjects (Microsoft not compatible versions)
	        try{
	         	xmlHttpRequest = new ActiveXObject("MSXML2.XMLHTTP");
	        }catch(e){
	          	try{
	              	xmlHttpRequest = new ActiveXObject("Microsoft.XMLHTTP");
	          	}catch (e) {console.log("error :: "+e.message);}
	        }
	    }
	    if(!xmlHttpRequest){//The application will use the normal upload because the browser is not be able to use ajax
	    	alternativeForIe();
	    }else{
	    	xmlHttpRequest.onreadystatechange = function(){
				if(xmlHttpRequest.readyState == 4){
					$("body").html(xmlHttpRequest.responseText);
				}
			};
			var version = null;
			try{
				if($.browser.version.toString().indexOf(".")!=-1){
					var value = $.browser.version.toString().substring(0,$.browser.version.toString().lastIndexOf("."));
					if(value.lastIndexOf(".")!=-1 && value.indexOf(".")!=value.lastIndexOf(".")){
						var temp = value.split(".");
						value = temp[0];
						value += "."+temp[1];
					}
					version = new Number(value);
				}else{
					version = new Number($.browser.version.toString());
				}
			}catch(ex){
				version = $.browser.version;
				console.log("EX:: parsing version number::"+ex.message);
			}
		    if(!$.browser.msie && (($.browser.mozilla && version>=4) || ($.browser.webkit) || ($.browser.safari) )){
		    	uploadFirstOption(xmlHttpRequest,confirmText); /*(Firefox,Safari,Chrome...)*/
		    }else{ /*(Internet Explorers and olders browsers option)*/
		    	uploadSecondOption(xmlHttpRequest);
		    }
	    }
	}else{
		alert(warningText);
	}
}

function prepareDateDivTrigger() {
    $("#oaiType").change(function() {
        if($(this).val() == "Finland - France EAD") {
            $("#dates").removeClass("hidden");
        } else if(!$("#dates").hasClass("hidden")){
            $("#dates").addClass("hidden");
        }
    });
}