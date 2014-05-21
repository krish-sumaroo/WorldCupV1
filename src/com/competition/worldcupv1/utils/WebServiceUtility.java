package com.competition.worldcupv1.utils;

import java.io.IOException;
import java.util.List;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class WebServiceUtility {
	//private static final String webServiceURL = "http://10.0.2.2/WorldCup_web/index.php/resource/"; 
	private static final String webServiceURL = "http://54.85.111.97/wc/master/index.php/";
	
	public JSONObject  extractList(String listUrl) throws ClientProtocolException, IOException, JSONException {
		JSONObject object = null;
		String url = webServiceURL+listUrl;
		HttpClient httpclient = new DefaultHttpClient();
		try{
			HttpPost httppost = new HttpPost(url);
	        ResponseHandler<String> responseHandler = new BasicResponseHandler();
	        String responseBody = httpclient.execute(httppost, responseHandler);
	        if(!responseBody.equalsIgnoreCase("")){	 
	        	object = new JSONObject(responseBody);
	        	// jArray = new JSONArray(responseBody);
	        }
		}
		finally{
        	httpclient.getConnectionManager().shutdown();
        }
		return object;		
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public JSONObject postData(List nameValuePairsList, String urlMethod) throws ClientProtocolException, IOException, JSONException{
		HttpClient httpclient = new DefaultHttpClient(); 
		String url = webServiceURL+urlMethod;
		JSONObject jObject = null;
		HttpPost httppost  = null;
		ResponseHandler<String> responseHandler = null;
		String responseBody ="";
		try{
			httppost = new HttpPost(url);
	        httppost.setEntity(new UrlEncodedFormEntity(nameValuePairsList));        	
	        responseHandler = new BasicResponseHandler();
	        responseBody = httpclient.execute(httppost, responseHandler);  
	        
	        if(!responseBody.equalsIgnoreCase("")){	       
	        	jObject = new JSONObject(responseBody);
	        }
		}
		finally{
        	httpclient.getConnectionManager().shutdown();
        }
		return jObject;		
	}
}
