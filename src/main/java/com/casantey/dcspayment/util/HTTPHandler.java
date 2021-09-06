package com.casantey.dcspayment.util;

import okhttp3.*;

import java.io.IOException;



public class HTTPHandler {
	
	private static final OkHttpClient client = new OkHttpClient();
	
	 public static String postObject(String url, String json, String authorization) {

		 try {
	         MediaType mediaType = MediaType.parse("application/json");
	         RequestBody body = RequestBody.create(mediaType, json);
	         Response response;
	         Request request;
	         
	         if(authorization.length() > 0) {
	         request = new Request.Builder()
	                 .url(url)
	                 .post(body)
	                 .addHeader("Content-Type", "application/json")
	                 .addHeader("Authorization", authorization)
	                 .build();
	         }else {
	        	 request = new Request.Builder()
		                 .url(url)
		                 .post(body)
		                 .addHeader("Content-Type", "application/json")
		                 .build();
	         }

	         response = client.newCall(request).execute();

	         if (response.body() != null) {

	            String jsonData = response.body().string();

	            return jsonData;

	         }

	         return response.message();
	      } catch (IOException ex) {
	         return ex.toString();
	      }
	   }
	 
	 public static String postPaymentConfirmation(String url, String json) {

			try {

				OkHttpClient client = new OkHttpClient();

				MediaType mediaType = MediaType.parse("application/json");
				RequestBody body = RequestBody.create(mediaType, json);

				Request request = new Request.Builder().url(url).post(body).addHeader("Content-Type", "application/json")
						.build();

				Response response = client.newCall(request).execute();

				if (response.body() != null) {

					String jsonData = response.body().string();

					return jsonData;

				}

				return response.message();
			} catch (IOException ex) {
				return ex.toString();
			}
		}
	 
	 public static String getObject(String url) {
	      String result = null;
	      try {

	         Request request = new Request.Builder()
	                 .url(url)
	                 .build();

	         Response response = client.newCall(request).execute();

	         result = response.body().string();

	      } catch (IOException ex) {
	         System.out.println("Get Obj Err: "+ex.getLocalizedMessage());
	      }

	      return result;
	   }
	 
	 public static String fetchEGCR(String url, String json, String key) {

	      OkHttpClient client = new OkHttpClient();
	      try {
	         MediaType mediaType = MediaType.parse("application/json");
	         RequestBody body = RequestBody.create(mediaType, json);
	         
//	         url = "https://uat-cagdmdaapi.paqtechnologies.com/v1/receipts/createreceiptonly";
//	         key = "K0rDNV5zn7UdQsYjMJpRziRBsc3R80KM7MT8Q/O2Ddg=";
	         Request request = new Request.Builder()
	                 .url(url)
	                 .post(body)
	                 .addHeader("Content-Type", "application/json")
	                 .addHeader("X-Api-Key", key)
	                 .build();
	        
	         Response response = client.newCall(request).execute();
	         

	         if (response.body() != null) {

	            String jsonData = response.body().string();
	            System.out.println("Response: "+jsonData);

	            return  jsonData;

	         }

	         return null;
	      } catch (IOException ex) {
	    	  
	         return null;
	      }
	   }
	 
//	 public static void main(String[] args) {
//		 InvoiceModel model = new InvoiceModel();
//		 model.setCallbackUrl("https://google.com");
//		 model.setCancellationUrl("https://google.com");
//		 model.setReturnUrl("https://google.com");
//		 model.setDescription("Desc");
//		 model.setClientReference("45459898uiyuy");
//		 model.setTotalAmount(0.5);
//		 model.setMerchantAccountNumber(Constants.MERCHANT_ID);
//		 
//		 
//		 System.out.println("Post Result: "+HTTPHandler.postObject(Constants.HUBTEL_WEB_CHECK_OUT_URL, new Gson().toJson(model), Constants.HUBTEL_CHECK_OUT_AUTHORIZATION));
//		 
//	 }
	

}
