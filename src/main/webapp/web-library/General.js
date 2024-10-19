

function getBaseURL() {
    const url = window.location.origin; // Returns http://localhost:8080
    const path = window.location.pathname.split('/'); // Split the pathname into parts
    return url + '/' + path[1] + '/'; // Combine the origin and the first part of the path (which is the project name)
}
function AjaxCall(url, method, data, successCallback, errorCallback) {
	$("#loading").show(); // Show loading overlay
	const baseURL = getBaseURL();
	$.ajax({
		url: baseURL+url,
		type: method,
		data: data,
		dataType: 'json', // Expecting JSON response
		success: function(response) {
			if (response.success) {
				successCallback(response); // Call the success callback
				$("#loading").hide();
			} else {
				errorCallback({ responseText: response.message }); // Call the error callback with message
				$("#loading").hide();
			}
		},
		error: function(jqXHR) {
			errorCallback(jqXHR); // Handle error response
			$("#loading").hide();
		},
	});
}




