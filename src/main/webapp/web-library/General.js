function AjaxCall(url, method, data, successCallback, errorCallback) {
    $("#loading").show(); // Show loading overlay

	$.ajax({
	      url: url,
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
