//A HORRIBLY DISORGANIZED CLASS WITH NO FUNCTIONS JUST CONSTRUCTORS THAT PARSES JSON DATA FROM A SERVER
//GOD PLEASE FORGIVE ME FOR I HAVE MADE THE WORST FUCKING JAVA PROGRAM EVER

package com.brons.untamed2;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;


public class JSONParser {
	static InputStream is = null;
	  static JSONObject jObj = null;
	  static String json = "";
	  // constructor
	  public JSONParser() {
	  }
	  /***
	   * for getting message list
	   * @param url
	   * @param accesstok
	   * @return
	   * @throws IOException
	   * @throws JSONException
	   */
	  public JSONObject getJSONFromUrl(String url, String accesstok) throws IOException, JSONException {
		  
	    // Making HTTP request
		  String urlParameters = "session_token="+accesstok;
		  URL url2 = new URL(url);
		  URLConnection conn = url2.openConnection();

		  conn.setDoOutput(true);

		  OutputStreamWriter writer = new OutputStreamWriter(conn.getOutputStream());

		  writer.write(urlParameters);
		  writer.flush();
		  
		  BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
		  String json = reader.readLine();
		  JSONTokener tokener = new JSONTokener(json);
		  jObj = new JSONObject(tokener);

	    // return JSON String
	    return jObj;
	  }


	public JSONObject getJSONFromUrl(String url, String accesstok, String post_id, boolean n) throws IOException, JSONException {

		// Making HTTP request
		String urlParameters = "session_token="+accesstok+"&post_id="+post_id;
		URL url2 = new URL(url);
		URLConnection conn = url2.openConnection();

		conn.setDoOutput(true);

		OutputStreamWriter writer = new OutputStreamWriter(conn.getOutputStream());

		writer.write(urlParameters);
		writer.flush();

		BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
		String json = reader.readLine();
		JSONTokener tokener = new JSONTokener(json);
		jObj = new JSONObject(tokener);

		// return JSON String
		return jObj;
	}
	  
	  /***
	   *  for getting individual messages
	   * @param url
	   * @param accesstok
	   * @param eid
	   * @param pid
	   * @param pak
	   * @return
	   * @throws IOException
	   * @throws JSONException
	   */
	  public JSONObject getJSONFromUrl(String url, String accesstok, String eid, String pid, String pak) throws IOException, JSONException {

	    // Making HTTP request
		  String urlParameters = "session_token="+accesstok;
		  URL url2 = new URL(url);
		  URLConnection conn = url2.openConnection();

		  conn.setDoOutput(true);

		  OutputStreamWriter writer = new OutputStreamWriter(conn.getOutputStream());

		  writer.write(urlParameters);
		  writer.flush();
		  
		  BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
		  String json = reader.readLine();
		  JSONTokener tokener = new JSONTokener(json);
		  jObj = new JSONObject(tokener);

	    // return JSON String
	    return jObj;
	  }
	  
	  /***
	   * For logging in via email and password
	   * @param url
	   * @param userinput
	   * @param pass
	   * @return
	   * @throws IOException
	   * @throws JSONException
	   */
	  public JSONObject getJSONFromUrl(String url, String userinput, String pass) throws IOException, JSONException {
		  //Session session = Session.getActiveSession();
		  //String accessToken = session.getAccessToken();

		  
	    // Making HTTP request
		  String urlParameters = "userinput="+userinput+"&pass="+pass;
		  URL url2 = new URL(url);
		  URLConnection conn = url2.openConnection();

		  conn.setDoOutput(true);

		  OutputStreamWriter writer = new OutputStreamWriter(conn.getOutputStream());

		  writer.write(urlParameters);
		  writer.flush();
		  
		  BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
		  String json = reader.readLine();
		  JSONTokener tokener = new JSONTokener(json);
		  jObj = new JSONObject(tokener);

	    // return JSON String
	    return jObj;
	  }
	  
	  /***
	   * changing password
	   * @param url
	   * @param token
	   * @param pass
	   * @param cpass
	   * @return
	   * @throws IOException
	   * @throws JSONException
	   */
	  public JSONObject getJSONFromUrl(String url, String token, String pass, String cpass) throws IOException, JSONException {
		  //Session session = Session.getActiveSession();
		  //String accessToken = session.getAccessToken();

		  
	    // Making HTTP request
		  String urlParameters = "session_token="+token+"&pass="+pass+"&pass_confirm="+cpass;
		  URL url2 = new URL(url);
		  URLConnection conn = url2.openConnection();

		  conn.setDoOutput(true);

		  OutputStreamWriter writer = new OutputStreamWriter(conn.getOutputStream());

		  writer.write(urlParameters);
		  writer.flush();
		  
		  BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
		  String json = reader.readLine();
		  JSONTokener tokener = new JSONTokener(json);
		  jObj = new JSONObject(tokener);

	    // return JSON String
	    return jObj;
	  }
	  
	  /***
	   * n is a USELESS parameter but I am the laziest programmer ever LOL
	   * New confession
	   * @param url
	   * @param token
	   * @param text
	   * @param cid
	   * @param n
	   * @return
	   * @throws IOException
	   * @throws JSONException
	   */
	  public JSONObject getJSONFromUrl(String url, String token, String text, String cid, int n) throws IOException, JSONException {
		  //Session session = Session.getActiveSession();
		  //String accessToken = session.getAccessToken();

		  
	    // Making HTTP request
		  String urlParameters = "session_token="+token+"&text="+text+"&cid="+cid;
		  URL url2 = new URL(url);
		  URLConnection conn = url2.openConnection();

		  conn.setDoOutput(true);

		  OutputStreamWriter writer = new OutputStreamWriter(conn.getOutputStream());

		  writer.write(urlParameters);
		  writer.flush();
		  
		  BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
		  String json = reader.readLine();
		  JSONTokener tokener = new JSONTokener(json);
		  jObj = new JSONObject(tokener);

	    // return JSON String
	    return jObj;
	  }
	  
	  /***
	   * gets list of posts for location-based stuff
	   * @param url
	   * @param token
	   * @param cid
	   * @param type
	   * @param geo_lat
	   * @param geo_lon
	   * @return
	   * @throws IOException
	   * @throws JSONException
	   */
	  public JSONObject getJSONFromUrl(String url, String token, String cid, String type, String geo_lat, String geo_lon, int p) throws IOException, JSONException {
		  //Session session = Session.getActiveSession();
		  //String accessToken = session.getAccessToken();

		  String urlParameters = "";
	    // Making HTTP request
		  if(type.equalsIgnoreCase("location")){
			  urlParameters = "session_token="+token+"&type="+type+"&p="+p+"&geo_lat="+geo_lat+"&geo_lon="+geo_lon;
		  }else{
			  urlParameters = "session_token="+token+"&cid="+cid+"&type="+type+"&p="+p;
		  }


		  URL url2 = new URL(url);
		  URLConnection conn = url2.openConnection();

		  conn.setDoOutput(true);

		  OutputStreamWriter writer = new OutputStreamWriter(conn.getOutputStream());

		  writer.write(urlParameters);
		  writer.flush();
		  
		  BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
		  String json = reader.readLine();
		  JSONTokener tokener = new JSONTokener(json);
		  jObj = new JSONObject(tokener);

	    // return JSON String
	    return jObj;
	  }
	  
	  /***
	   * gets individual post
	   * @param url
	   * @param token
	   * @param pid
	   * @param n
	   * @return
	   * @throws IOException
	   * @throws JSONException
	   */
	  public JSONObject getJSONFromUrl(String url, String token, String pid, int n) throws IOException, JSONException {
		  //Session session = Session.getActiveSession();
		  //String accessToken = session.getAccessToken();

		  
	    // Making HTTP request
		  String urlParameters = "session_token="+token+"&post_id="+pid;
		  URL url2 = new URL(url);
		  URLConnection conn = url2.openConnection();

		  conn.setDoOutput(true);

		  OutputStreamWriter writer = new OutputStreamWriter(conn.getOutputStream());

		  writer.write(urlParameters);
		  writer.flush();
		  
		  BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
		  String json = reader.readLine();
		  JSONTokener tokener = new JSONTokener(json);
		  jObj = new JSONObject(tokener);

	    // return JSON String
	    return jObj;
	  }
	  
	  /***
	   * just does logout
	   * @param url
	   * @return
	   * @throws IOException
	   * @throws JSONException
	   */
	  public JSONObject getJSONFromUrl(String url) throws IOException, JSONException {
		  //Session session = Session.getActiveSession();
		  //String accessToken = session.getAccessToken();

		  
	    // Making HTTP request
		//  String urlParameters = "session_token="+token+"&post_id="+pid;
		  URL url2 = new URL(url);
		  URLConnection conn = url2.openConnection();

		  conn.setDoOutput(true);

		  OutputStreamWriter writer = new OutputStreamWriter(conn.getOutputStream());

		//  writer.write(urlParameters);
		  writer.flush();
		  
		  BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
		  String json = reader.readLine();
		  JSONTokener tokener = new JSONTokener(json);
		  jObj = new JSONObject(tokener);

	    // return JSON String
	    return jObj;
	  }
	  
	  /***
	   * Facebook login!
	   * @param url
	   * @param token
	   * @param n
	   * @return
	   * @throws IOException
	   * @throws JSONException
	   */
	  public JSONObject getJSONFromUrl(String url, String token, int n) throws IOException, JSONException {
		  //Session session = Session.getActiveSession();
		  //String accessToken = session.getAccessToken();

		  
	    // Making HTTP request
		  String urlParameters = "authResponse[accessToken]="+token;
		  URL url2 = new URL(url);
		  URLConnection conn = url2.openConnection();

		  conn.setDoOutput(true);

		  OutputStreamWriter writer = new OutputStreamWriter(conn.getOutputStream());

		 writer.write(urlParameters);
		  writer.flush();
		  
		  BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
		  String json = reader.readLine();
		  JSONTokener tokener = new JSONTokener(json);
		  jObj = new JSONObject(tokener);

	    // return JSON String
	    return jObj;
	  }


	public JSONObject getJSONFromUrl(String url, String token, String parent_type, String parent_id, boolean n) throws IOException, JSONException {
		//Session session = Session.getActiveSession();
		//String accessToken = session.getAccessToken();


		// Making HTTP request
		String urlParameters = "session_token"+token+"&parent_type="+parent_type+"&parent_id"+parent_id;
		URL url2 = new URL(url);
		URLConnection conn = url2.openConnection();

		conn.setDoOutput(true);

		OutputStreamWriter writer = new OutputStreamWriter(conn.getOutputStream());

		writer.write(urlParameters);
		writer.flush();

		BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
		String json = reader.readLine();
		JSONTokener tokener = new JSONTokener(json);
		jObj = new JSONObject(tokener);

		// return JSON String
		return jObj;
	}

	}
