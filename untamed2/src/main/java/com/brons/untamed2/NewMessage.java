package com.brons.untamed2;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AppEventsLogger;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

public class NewMessage extends Activity implements  OnItemClickListener, OnItemSelectedListener {

	Button send;
	Button cancel;
	int postid;
	EditText mEdit;
	SharedPreferences sharedPreferences;	//shared preferences, for getting the universal access token
	String text = "";
	String [] cids;
	String [] actions;
	SharedPreferences prefs;
	
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.makepost);

		prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());	//gets the access token from the shared prefs
        
        mEdit = (EditText) findViewById(R.id.postdata);
                
        send = (Button) findViewById(R.id.send);
        cancel = (Button) findViewById(R.id.cancel);

		cids = getResources().getStringArray(R.array.cid);
		actions = getResources().getStringArray(R.array.actions);

		TextView loc = (TextView) findViewById(R.id.Location);
		TextView fair = (TextView) findViewById(R.id.befair);

		int cid = prefs.getInt("comid", -1);

		loc.setText(actions[cid]);
		fair.setText("BE FAIR: Messages that engage in bullying or harassment can lead to the permanent banning of your account on Untamed.");

		send.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				try {
					text = mEdit.getText().toString();
					sendMessage();
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}
        	
        });
        
        cancel.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
					cancel();	
			}
        	
        });

    }
	
	/** Called when the user clicks the Send button 
	 * @throws JSONException */
	public void sendMessage() throws JSONException{
		if(text.length()==0)
			Toast.makeText(getApplicationContext(), "Please type a confession", Toast.LENGTH_LONG).show();
		
		else
		new JSONParse().execute();
		setResult(RESULT_OK, null);
		finish();
	}
	
    private class JSONParse extends AsyncTask<String, String, JSONObject> {
        private ProgressDialog pDialog;				//creates a new progress dialog to show the loading of messages
       @Override
         protected void onPreExecute() {			//before executing
             super.onPreExecute();
             pDialog = new ProgressDialog(NewMessage.this);	//creates and displays the progress dialog
             pDialog.setMessage("Sending confession...");
             pDialog.setIndeterminate(false);
             pDialog.setCancelable(true);
             pDialog.show();
       }
       
       @Override
         protected JSONObject doInBackground(String... args) {	//do in background while the process is executing
         JSONParser jParser = new JSONParser();					//creates a new JSONParser object
         // Getting JSON from URL
         if(isNetworkAvailable()){								//as long as the network is available
         JSONObject json = null;				//creates a new JSONObject
		try {
			int cid = prefs.getInt("comid", -1);
			String token = prefs.getString("access_token", null);
			if(token!=null)
				//sets JSON object as downloaded with the access token (TODO: implement a better system than using 1 for community and school for type)
				json = jParser.getJSONFromUrl("https://beuntamed.com/app/api/2.0/posts/add_post", token, text, cids[cid], 0);
		} catch (IOException | JSONException e) {		//catches specific exceptions
				return null;
		}
        Log.i("option selected", json.toString());

         return json;								//returns the JSON object
        }
         
         else{
        	 JSONObject json;
			try {
				json = new JSONObject(getStringProperty("json"));
				return json;
			} catch (JSONException e) {
				runOnUiThread(new Runnable() {
	   				  public void run() {
	   					    Toast.makeText(getApplicationContext(), 
	   					            "Error Submitting Confession", 
	   					            Toast.LENGTH_SHORT).show();
	   				  }
	   				});
			}
			//TODO: implement proper offline handling. Currently does not handle disconnection from network
			
			return null;
         }
       }
       @Override
       protected void onPostExecute(JSONObject json) {
    	  // pDialog.dismiss();
    	   if(json ==null){
			   runOnUiThread(new Runnable() {				//Toast message claiming Error with loading the data
					  public void run() {
						    Toast.makeText(getApplicationContext(), 
						            "Cannot submit post. Please try again later or contact the admins", 
						            Toast.LENGTH_SHORT).show();
					  }
					});
		   }else{
			  // Log.i("option selected", json.toString());
		   }
         
        
     }}
	
	public void cancel(){
		finish();
	}
	
    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager 
              = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

	
	@Override
	protected void onResume() {
	  super.onResume();

	  // Logs 'install' and 'app activate' App Events.
	  AppEventsLogger.activateApp(this);
	}
	
	@Override
	protected void onPause() {
	  super.onPause();

	  // Logs 'app deactivate' App Event.
	  AppEventsLogger.deactivateApp(this);
	}

	@Override
	public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,
			long arg3) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onNothingSelected(AdapterView<?> arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		// TODO Auto-generated method stub
		
	}
	
	   public String getStringProperty(String key) {
	    	sharedPreferences = this.getSharedPreferences("preferences", Context.MODE_PRIVATE);
	    String res = null;
	    if (sharedPreferences != null) {
	        res = sharedPreferences.getString(key, null);
	    }
	    return res;
	}
}
