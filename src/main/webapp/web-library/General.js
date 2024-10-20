
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

function formatDateToSGTime(datetime) {
    // Create a Date object from the input datetime (assuming it's in UTC)
    let date = new Date(datetime);

    // Add 8 hours to convert UTC to Singapore time (UTC+8)
    date.setHours(date.getHours() + 8);

    // Manually format the date to "yyyy-MM-dd HH:mm:ss"
    const year = date.getFullYear();
    const month = String(date.getMonth() + 1).padStart(2, '0'); // Months are zero-indexed, so +1
    const day = String(date.getDate()).padStart(2, '0');
    const hours = String(date.getHours()).padStart(2, '0');
    const minutes = String(date.getMinutes()).padStart(2, '0');
    const seconds = String(date.getSeconds()).padStart(2, '0');

    // Combine to "yyyy-MM-dd HH:mm:ss"
    const formattedDate = `${year}-${month}-${day} ${hours}:${minutes}:${seconds}`;

    return formattedDate;
}













