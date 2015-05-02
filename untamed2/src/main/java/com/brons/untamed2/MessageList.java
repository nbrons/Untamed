package com.brons.untamed2;

import android.app.ActionBar;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBarActivity;
import android.text.Html;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AppEventsLogger;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.sql.Date;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.concurrent.TimeUnit;

public class MessageList extends ActionBarActivity {
	ListView list;
	TextView text;
	TextView comments;
	TextView likes;
	Typeface typeFace;
	SharedPreferences sharedPreferences;
	ArrayList<HashMap<String, String>> postlist = new ArrayList<HashMap<String, String>>();
	static Button notifCount;
	static int mNotifCount = 0;    
	String hexColor;
	Hashtable<String, String> font;
	
	//private string access_token = PreferenceManager.getDefaultSharedPreferences(context).getString("MYLABEL", "defaultStringIfNothingFound"); 
	
	  
	  JSONArray array = null;
	    @Override
	    protected void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        setContentView(R.layout.activity_main);
	        setupActionBar();
	        font = new Hashtable<String,String>();
	        font.put("mask-alien6", "\ue000");
	        font.put("mask-android11", "\ue001");
	        font.put("mask-apple53", "\ue002");
	        font.put("mask-bear10", "\ue003");	        
	        font.put("mask-bear14", "\ue004");
	        font.put("mask-brief1", "\ue005");
	        font.put("mask-carrot5", "\ue006");
	        font.put("mask-cheese6", "\ue007");
	        font.put("mask-chicken2", "\ue008");
	        font.put("mask-chimp", "\ue009");
	        font.put("mask-clown", "\ue00a");
	        font.put("mask-coffee17", "\ue00b");
	        font.put("mask-cow11", "\ue00c");
	        font.put("mask-creepy", "\ue00d");
	        font.put("mask-dark20", "\ue00e");
	        font.put("mask-donut4", "\ue00f");
	        font.put("mask-drink24", "\ue010");
	        font.put("mask-duck5", "\ue011");
	        font.put("mask-emotion", "\ue012");
	        font.put("mask-emotion117", "\ue013");
	        font.put("mask-fish14", "\ue014");
	        font.put("mask-fox2", "\ue015");
	        font.put("mask-fried5", "\ue016");
	        font.put("mask-head1", "\ue017");
	        font.put("mask-icecream6", "\ue018");
	        font.put("mask-joystick10", "\ue019");
	        font.put("mask-lion", "\ue01a");
	        font.put("mask-lion5", "\ue01b");
	        font.put("mask-man82", "\ue01c");
	        font.put("mask-pacman2", "\ue01d");
	        font.put("mask-pirate", "\ue01e");
	        font.put("mask-plump", "\ue01f");
	        font.put("mask-rabbit6", "\ue020");
	        font.put("mask-rocket48", "\ue021");
	        font.put("mask-rocket61", "\ue022");
	        font.put("mask-sail1", "\ue023");
	        font.put("mask-sandwich", "\ue024");
	        font.put("mask-saturn8", "\ue025");
	        font.put("mask-sheep1", "\ue026");
	        font.put("mask-skunk1", "\ue027");
	        font.put("mask-sneaker", "\ue028");
	        font.put("mask-snowflake3", "\ue029");
	        font.put("mask-straight9", "\ue02a");
	        font.put("mask-stripy", "\ue02b");
	        font.put("mask-sun79", "\ue02c");
	        font.put("mask-syringe", "\ue02d");
	        font.put("mask-toad", "\ue02e");
	        font.put("mask-tux", "\ue02f");
	        font.put("mask-two28", "\ue030");
	        font.put("mask-umbrella30", "\ue031");
	        font.put("mask-vehicle3", "\ue032");
	        font.put("mask-water34", "\ue033");
	        
	        
	        
	        typeFace = Typeface.createFromAsset(this.getAssets(), "fonts/font.ttf");
	        //new JSONComm().execute();
	        new JSONParse().execute();
	    
	    }
	    
	     private void setupActionBar() {
	           ActionBar ab = getActionBar();
	           ab.setDisplayShowCustomEnabled(true);
	           ab.setDisplayShowTitleEnabled(false);
	           ab.setIcon(R.drawable.untamed);
	           LayoutInflater inflator = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	           View v = inflator.inflate(R.layout.action_bar_title, null);

	           ab.setCustomView(v);

	       }
	
    private class JSONParse extends AsyncTask<String, String, JSONObject> {
        private ProgressDialog pDialog;
       @Override
         protected void onPreExecute() {
             super.onPreExecute();
              text = (TextView)findViewById(R.id.text);
        comments = (TextView)findViewById(R.id.comments);
        likes = (TextView)findViewById(R.id.likes);
             pDialog = new ProgressDialog(MessageList.this);
             pDialog.setMessage("Getting Data ...");
             pDialog.setIndeterminate(false);
             pDialog.setCancelable(true);
             pDialog.show();
       }
       @Override
         protected JSONObject doInBackground(String... args) {
         JSONParser jParser = new JSONParser();
         // Getting JSON from URL
         if(isNetworkAvailable()){
         JSONObject json = null;
		try {
			SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
			String token = prefs.getString("access_token", null);
			if(token!=null)
				json = jParser.getJSONFromUrl("https://beuntamed.com/app/api/2.0/get_messages", token);
		} catch (IOException | JSONException e) {
			runOnUiThread(new Runnable() {
 				  public void run() {
 					    Toast.makeText(getApplicationContext(), 
 					            "Error Loading Data", 
 					            Toast.LENGTH_SHORT).show();
 				  }
 				});
		}
         return json;
         }else{
        	 JSONObject json;
			try {
				json = new JSONObject(getStringProperty("json"));
				return json;
			} catch (JSONException e) {
				runOnUiThread(new Runnable() {
	   				  public void run() {
	   					    Toast.makeText(getApplicationContext(), 
	   					            "Error Loading Data", 
	   					            Toast.LENGTH_SHORT).show();
	   				  }
	   				});
			}
			
			return null;
         }
       }
       @Override
       protected void onPostExecute(JSONObject json) {
       pDialog.dismiss();
       try {
          // Getting JSON Array
          //user = json.getJSONArray(TAG_USER);
          //JSONObject c = user.getJSONObject(0);
     	  if (json ==null){
   			runOnUiThread(new Runnable() {
   				  public void run() {
   					    Toast.makeText(getApplicationContext(), 
   					            "Error Loading Data", 
   					            Toast.LENGTH_SHORT).show();
   				  }
   				});
   	   }  else{ 
   		  JSONObject data = new JSONObject();
   		  data = json.getJSONObject("data");
     	  array = data.getJSONArray("messages");   
     	  
     	  setStringProperty("json", array.toString());
     	         	  
     		  
          postlist.clear();

        	  for(int i=0; i<array.length(); i++){
        		  JSONObject c = array.getJSONObject(i);
        		  //postid, int communityid, String text, String time, String location, String distance, String comments, String likes
        		 // Message post = new Message(c.getString("eid"),c.getString("pid"),c.getString("pak"),c.getInt("is_unseen"),
        			//	  c.getInt("is_author"),c.getString("mask_class"),c.getString("mask_hex"),
        			//	  c.getString("summary_text"),c.getString("message_state"),c.getInt("update_timestamp"),c.getInt("expire_timestamp"));


        		  Message post = new Message(c.getString("eid"),c.getString("pid"),c.getString("pak"),c.getInt("is_unseen"),
        				  c.getInt("is_author"),c.getString("mask_class"),c.getString("mask_hex"),
        				  c.getString("summary_text"),c.getString("message_state"),c.getLong("update_timestamp"),0);
   
        		  
        		  if(post.getIs_unseen()==false){
        			  post.setExpire_timestamp(c.getLong("expire_timestamp"));
        		 }
 
        		  Timestamp updateTime = new Timestamp(post.getUpdate_timestamp());
        		  Date date = new Date(updateTime.getTime()*1000);
        		  String updatedTime = "";
        		  //604800
        		  java.util.Date curr = new java.util.Date();
        		  int diffInDays = (int) ((curr.getTime() - date.getTime()) / (1000 * 60 * 60 * 24));
        		  if(diffInDays<7){
            		  DateFormat df = new SimpleDateFormat("EEE hh:mm a");
            		  updatedTime = df.format(date);	  
        		  }else{
        		  DateFormat df = new SimpleDateFormat("EEE MMM d");
        		  updatedTime = df.format(date);
        		  }
        		  String unseen = "";
        		  if(post.getIs_unseen()&&post.getIs_author())		{//if the message is sent but not seen
        			  unseen="Delivered";
        			  updatedTime = " Delivered "+updatedTime;
        	  }
        		  else if(post.getIs_unseen()&&(!post.getIs_author()))	{					//if the message is received and unseen
        			  unseen="Unread Message";
        		  }
        		  else if(post.getIs_author()&&(!post.getIs_unseen()))	{					//if the message is sent and seen
        			  unseen="Replied";
        			  updatedTime = " Replied "+updatedTime;
        		  }
        		  else{
        			  unseen="Opened";							//if the message is sent and seen
        			  updatedTime = " Opened "+updatedTime;
        		  }
        		  String expires ="";
        		  long [] exptime = calculateTime(post.getExpire_timestamp());
        		  if(post.getIs_unseen()&&post.getIs_author()){
        			  expires = "Unread";
        		  }
        		  else if(post.getIs_unseen()){
        			  expires = "Unopened";
        		  }
        		  else if(exptime[0]>0){
        			  expires = "Expires in "+exptime[0]+"d";
        		  }
        		  else if(exptime[1]>0){
        			  expires = "Expires in "+exptime[1]+"h";
        		  }
        		  else if(exptime[2]>0){
        			  expires = "Expires in "+exptime[2]+"m";
        		  }
        		  else{
        			  expires = "null";
        		  }
        		  
        		  HashMap<String, String> map = new HashMap<String, String>();
        		  map.put("eid", String.valueOf(post.getEid()));
        		  map.put("pid", String.valueOf(post.getPid()));
        		  map.put("pak", String.valueOf(post.getPak()));
        		  map.put("is_unseen", unseen);
        		  map.put("is_author", String.valueOf(post.getIs_author()));
        		  map.put("mask_class", String.valueOf(font.get(post.getMask_class())));
        		  map.put("mask_hex", String.valueOf(post.getMask_hex()));
        		  map.put("summary_text", String.valueOf(post.getSummary_text()));
        		  map.put("message_state", String.valueOf(post.getMessage_state()));
        		  map.put("update_timestamp", updatedTime);
        		  map.put("expire_timestamp", expires);
        		  
                  list = (ListView)findViewById(R.id.list);
                  postlist.add(map);
                  ListAdapter adapter = new SimpleAdapter(MessageList.this, postlist, R.layout.list_v, (new String[]{"summary_text", "is_unseen",
                		  "update_timestamp", "mask_class", "mask_hex", "expire_timestamp"}), 
                		  (new int[] {R.id.text, R.id.comments, R.id.likes, R.id.avatar, R.id.hexname, R.id.expires})){
                	  
                	  @Override
                	  public View getView(int position, View convertView, ViewGroup parent) {
                	      View view = super.getView(position, convertView, parent);
                	      TextView text = (TextView) view.findViewById(R.id.avatar);
                	      TextView hex = (TextView) view.findViewById(R.id.hexname);
                	      String s = hex.getText().toString();
                	      	text.setTextColor(Color.parseColor(s));
                	        text.setTypeface(typeFace);
                	        text.setTextSize(40);
                	        text.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL);
                	        text.setBackgroundResource(R.drawable.iconcircle);
                	        text.getBackground().setColorFilter(Color.parseColor(s), PorterDuff.Mode.MULTIPLY);
       
                	      TextView summText = (TextView) view.findViewById(R.id.text);
                	      summText.setTextSize(20);
                	        
                	        TextView isUnseen = (TextView) view.findViewById(R.id.comments);
                	        String is = isUnseen.getText().toString();
                	        isUnseen.setVisibility(View.GONE);
                	        
                	        if(is.equalsIgnoreCase("Unread Message")){
                	        	summText.setTypeface(null, Typeface.BOLD);
                	        	TextView ts = (TextView) view.findViewById(R.id.likes);
                	        	String sk = ts.getText().toString();
                	        	ts.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
                	        	String n = "<font color=\'"+s+"\'>new!</font>"+" "+sk;
                	        	ts.setText(Html.fromHtml(n));
                	        }else if(is.equalsIgnoreCase("Delivered")){
                	        	TextView ts = (TextView) view.findViewById(R.id.likes);
                	        	Drawable d = getResources().getDrawable( R.drawable.box );
                	        	Bitmap bitmap = ((BitmapDrawable) d).getBitmap();
                	        	Drawable img = new BitmapDrawable(getResources(), Bitmap.createScaledBitmap(bitmap, 40, 40, true));
                	        	ts.setCompoundDrawablesWithIntrinsicBounds( img, null, null, null );
                	        }
                	        else if(is.equalsIgnoreCase("Replied")){
                	        	TextView ts = (TextView) view.findViewById(R.id.likes);
                	        	Drawable d = getResources().getDrawable( R.drawable.replied );
                	        	Bitmap bitmap = ((BitmapDrawable) d).getBitmap();
                	        	Drawable img = new BitmapDrawable(getResources(), Bitmap.createScaledBitmap(bitmap, 40, 40, true));
                	        	ts.setCompoundDrawablesWithIntrinsicBounds( img, null, null, null );
                	        }
                	        else if(is.equalsIgnoreCase("Opened")){
                	        	TextView ts = (TextView) view.findViewById(R.id.likes);
                	        	Drawable d = getResources().getDrawable( R.drawable.open );
                	        	Bitmap bitmap = ((BitmapDrawable) d).getBitmap();
                	        	Drawable img = new BitmapDrawable(getResources(), Bitmap.createScaledBitmap(bitmap, 40, 40, true));
                	        	ts.setCompoundDrawablesWithIntrinsicBounds( img, null, null, null );
                	        }
                	                       	        
                	        
                	      return view;
                	    }

                  };

                 list.setAdapter(adapter);
                 list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                     @Override
                     public void onItemClick(AdapterView<?> parent, View view,
                                             int position, long id) {
                    	 //Intent intent = new Intent(MessageList.this, SingleMessage.class);
                    	// intent.putExtra("post", postlist.get(position));
                        // startActivity(intent);
                     }
                 });  
        	  } 
        	   } 
         } catch (Exception e) {
				runOnUiThread(new Runnable() {
	   				  public void run() {
	   					    Toast.makeText(getApplicationContext(), 
	   					            "Error Converting Data", 
	   					            Toast.LENGTH_SHORT).show();
	   				  }
	   				});
         }
        
     }}
    
    
    public static long[] calculateTime(long seconds) {
        long day = TimeUnit.SECONDS.toDays(seconds);        
        long hours = TimeUnit.SECONDS.toHours(seconds) - TimeUnit.SECONDS.toHours(TimeUnit.SECONDS.toDays(seconds));
        long minute = TimeUnit.SECONDS.toMinutes(seconds) - TimeUnit.SECONDS.toMinutes(TimeUnit.SECONDS.toHours(seconds));
        long second = TimeUnit.SECONDS.toSeconds(seconds) - TimeUnit.SECONDS.toSeconds(TimeUnit.SECONDS.toMinutes(seconds));

        return new long[] {day, hours, minute, second};
        
    }

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		
	  //  RelativeLayout badgeLayout = (RelativeLayout) menu.findItem(R.id.action_refresh).getActionView();
	  //  TextView tv = (TextView) badgeLayout.findViewById(R.id.);
	 //   tv.setText("12");
		
		return super.onCreateOptionsMenu(menu);
	}
	

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
	    // Handle presses on the action bar items
	    switch (item.getItemId()) {
	        case R.id.action_logout:
	            Intent settingsintent = new Intent(this, SettingsActivity.class);
	            startActivity(settingsintent);
	            return true;       	
	        default:
	            return super.onOptionsItemSelected(item);
	    }
	}
	
	
    public String getStringProperty(String key) {
    	sharedPreferences = this.getSharedPreferences("preferences", this.MODE_PRIVATE);
    String res = null;
    if (sharedPreferences != null) {
        res = sharedPreferences.getString(key, null);
    }
    return res;
}

    public void setStringProperty(String key, String value) {
    sharedPreferences = this.getSharedPreferences("preferences", this.MODE_PRIVATE);
    if (sharedPreferences != null) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key, value);
        editor.commit();
    }
}
    
    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager 
              = (ConnectivityManager) getSystemService(this.CONNECTIVITY_SERVICE);
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
}