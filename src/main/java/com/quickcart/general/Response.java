package com.quickcart.general;

import java.io.IOException;
import java.io.PrintWriter;

import jakarta.servlet.http.HttpServletResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.quickcart.data.models.ResponseData;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

public class Response {
	
	 private static final ObjectMapper objectMapper = new ObjectMapper();

	
	    public static void ResponseSuccess(HttpServletResponse response) throws IOException {
	    	objectMapper.registerModule(new JavaTimeModule()); // Register the JavaTimeModule
	        response.setContentType("application/json");
	        response.setStatus(HttpServletResponse.SC_OK); // 200 OK
	        String jsonResponse = objectMapper.writeValueAsString(new ResponseData(true, "Success", null));
	        PrintWriter out = response.getWriter();
	        out.write(jsonResponse);
	        out.flush();
	    }

	    public static void ResponseSuccess(HttpServletResponse response, Object data) throws IOException {
	    	objectMapper.registerModule(new JavaTimeModule()); // Register the JavaTimeModule
	        response.setContentType("application/json");
	        response.setStatus(HttpServletResponse.SC_OK); // 200 OK
	        String jsonResponse = objectMapper.writeValueAsString(new ResponseData(true, "Success", data));
	        PrintWriter out = response.getWriter();
	        out.write(jsonResponse);
	        out.flush();
	    }

	    public static void ResponseError(HttpServletResponse response, String errorMessage) throws IOException {
	    	objectMapper.registerModule(new JavaTimeModule()); // Register the JavaTimeModule
	        response.setContentType("application/json");
	        response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR); // 500 Internal Server Error	    
	        PrintWriter out = response.getWriter();
	        out.write(errorMessage);
	        out.flush();
	    }
	
	
}
