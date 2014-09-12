function checkBrowser(msg) {
	var alertMsg="";

	/* jQuery version */
	var version = parseInt($.browser.version, 10);

	var isMsie =/MSIE/.test(navigator.userAgent) && /Microsoft Internet Explorer/.test(navigator.appName);
	if (!isMsie)
		isMsie =/Trident/.test(navigator.userAgent) && /Netscape/.test(navigator.appName);
	var isMozilla = /Firefox/.test(navigator.userAgent);
	var isChrome = /Chrome/.test(navigator.userAgent) && /Google Inc/.test(navigator.vendor);
	var isSafari = /Safari/.test(navigator.userAgent) && /Apple Computer/.test(navigator.vendor);

	/* Browsers we can test */
	if (isChrome) {
		/* To detect Chrome version in second display */
		window.navigator.appVersion.match(/Chrome\/(.*?) /)[1];
		var test = parseInt(window.navigator.appVersion.match(/Chrome\/(\d+)\./)[1], 10);
		if (test<35)
			alertMsg = msg;
	}
	else if (isSafari && version<5)
		alertMsg = msg;
	else if (isMsie && version<7)
		alertMsg = msg;
	else if (isMozilla && version<17)
		alertMsg = msg;

	/* Show result */
	if (alertMsg!=""){
		$("#browser").text(alertMsg);
	}
}