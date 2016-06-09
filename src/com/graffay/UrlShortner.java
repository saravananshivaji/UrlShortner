package com.graffay;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.codehaus.jackson.map.ObjectMapper;
 
/**
 * 
 * @author Saran
 *
 */
public class UrlShortner {

	
	/**
	 * Method to post the json to Google API.
	 * 	  
	 * @param url
	 * @param message
	 * @param httpClient
	 * @return
	 * @throws IOException
	 * @throws IllegalStateException
	 * @throws UnsupportedEncodingException
	 * @throws RuntimeException
	 */
	private static String postToURL(String url, String message, DefaultHttpClient httpClient) throws IOException, IllegalStateException, UnsupportedEncodingException, RuntimeException {

		HttpPost postRequest = new HttpPost(url);
 
        StringEntity input = new StringEntity(message);
        input.setContentType("application/json");
        postRequest.setEntity(input);
 
        HttpResponse response = httpClient.execute(postRequest);
 
        if (response.getStatusLine().getStatusCode() != 200) {
            throw new RuntimeException("Failed : HTTP error code : "
                    + response.getStatusLine().getStatusCode());
        }
 
        BufferedReader br = new BufferedReader(
                new InputStreamReader((response.getEntity().getContent())));
 
        String output;
        StringBuffer totalOutput = new StringBuffer();

        while ((output = br.readLine()) != null) {
            totalOutput.append(output);
        }
        
        return totalOutput.toString();
    }
	
	public static void main(String [] a) {
		
		String apiKey = "Enter your API Key";
		String longUrl = "Enter your long url here";

		String url = "https://www.googleapis.com/urlshortener/v1/url?key=" + apiKey;
		
		String message = "{ \"longUrl\": \" "+ longUrl +"\"}";		
		DefaultHttpClient httpClient = new DefaultHttpClient();
		
		 String response = "No Res";
		try {
			response = postToURL(url, message, httpClient);
			
			ObjectMapper objectMapper = new ObjectMapper();


		    Result result = (Result) objectMapper.readValue(response, Result.class);

		    System.out.println("shortUrl = " + result.getId());
		    System.out.println("Kind     = " + result.getKind());
		    System.out.println("longUrl  = " + result.getLongUrl());
			
		} catch (IllegalStateException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (RuntimeException e) {
			e.printStackTrace();
		}
		 
	}
}