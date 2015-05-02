package com.brons.untamed2;


import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AppEventsLogger;
import com.facebook.Session;
import com.melnykov.fab.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.sql.Date;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.concurrent.TimeUnit;

public class CommunityView extends ActionBarActivity {

	SwipeRefreshLayout swipelayout;
	ListView list;							//ListView for list of posts
	Typeface typeFace;						//font for symbols (user icons)
	ArrayList<HashMap<String, String>> postlist = new ArrayList<HashMap<String, String>>();	//ArrayList of the posts
	Hashtable<String, String> font;			//Hashtable of the font, correlating the imported symbol and it's hex code
	int page;
	int current_item;
	String [] cidd;
	LocationManager locationManager;
	LocationListener locationListener;
	String lat;
	String lon;
	Toolbar actionBar;
	SharedPreferences prefs;
	SharedPreferences sharedPreferences;

		@Override
	    protected void onCreate(Bundle savedInstanceState) {
			prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());


			cidd = getResources().getStringArray(R.array.cid);
	        super.onCreate(savedInstanceState);
	        setContentView(R.layout.activity_community_view);
	        setupActionBar();			//sets up activity's action bar


			list = (ListView)findViewById(R.id.list);					//gets listview

			FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
			fab.attachToListView(list);
			fab.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					// Intent intent = new Intent(getApplicationContext(), NewMessage.class);
					// intent.putExtra("postid", postlist.size());
					//	 startActivityForResult(intent, 1);

					AlertDialog.Builder alert = new AlertDialog.Builder(CommunityView.this);

					alert.setTitle("New Confession");
					alert.setMessage("Enter a confession");

					// Set an EditText view to get user input
					final EditText input = new EditText(getApplicationContext());
					alert.setView(input);

					alert.setPositiveButton("Send", new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int whichButton) {
							String text = input.getText().toString();
							if (text.length() < 10)
								Toast.makeText(getApplicationContext(), "Your confession is too short!", Toast.LENGTH_LONG).show();
							else {
								page = 1;
								current_item = 0;
								new SendConfJSONParse().execute(text);
							}
						}
					});

					alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int whichButton) {
							// Canceled.
						}
					});

					alert.show();


				}
			});
			fab.show();




			swipelayout = (SwipeRefreshLayout) findViewById(R.id.swipe_container);
			swipelayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {

				@Override
				public void onRefresh() {
					postlist.clear();
					page = 1;
					current_item = 0;
					new JSONParse().execute();

				}

			});



			//if(isLocationEnabled(this)) {
				locationManager =
						(LocationManager) getSystemService(Context.LOCATION_SERVICE);
				// Define a listener that responds to location updates
				locationListener = new LocationListener() {
					public void onLocationChanged(Location location) {
						// Called when a new location is found by the network location provider.
						lat = Double.toString(location.getLatitude());
						lon = Double.toString(location.getLongitude());
					}

					public void onStatusChanged(String provider, int status, Bundle extras) {
						locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);
					}

					public void onProviderEnabled(String provider) {
						locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);
					}

					public void onProviderDisabled(String provider) {
					}
				};
			if(isLocationEnabled(this))
				// Register the listener with the Location Manager to receive location updates
				locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);
			//}
			else{
				lat ="";
				lon="";
			}


	        page =1;
	        current_item=0;
	        
	        font = new Hashtable<String,String>();	//implements hash table of all the characters in the font
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
	        font.put("mask-emoticon", "\ue012");
	        font.put("mask-emoticon117", "\ue013");
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
	                           
	        
	        typeFace = Typeface.createFromAsset(this.getAssets(), "fonts/font.ttf");	//initializes typeface
	       // new JSONParse().execute();				//upon loading, executes JSONParse automatically
	    
	    }

		private void setupActionBar() {
			 // Adapter
			 SpinnerAdapter adapter =
					 ArrayAdapter.createFromResource(this, R.array.actions,
							 android.R.layout.simple_spinner_dropdown_item);



			 int cid = prefs.getInt("comid", -1);

			 actionBar = (Toolbar) findViewById(R.id.action_bar_toolbar);

			   setSupportActionBar(actionBar);

			Drawable dr = getResources().getDrawable(R.drawable.untamed);
			Bitmap bitmap = ((BitmapDrawable) dr).getBitmap();
			Drawable d = new BitmapDrawable(getResources(), Bitmap.createScaledBitmap(bitmap, 100, 100, true));


			   actionBar.setLogo(d);
			   getSupportActionBar().setDisplayShowTitleEnabled(false);
			   getSupportActionBar().show();



			   Spinner spinner = (Spinner) findViewById(R.id.spinner_nav);
			   spinner.setAdapter(adapter);
			   spinner.setSelection(cid);
			   spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
				   @Override
				   public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

					   String[] items = getResources().getStringArray(R.array.actions); // List items from res

					   if(position==0&&!isLocationEnabled(getApplicationContext())){
						   runOnUiThread(new Runnable() {
							   public void run() {
								   Toast.makeText(getApplicationContext(),
										   "Location services not available. Please enable to view nearby posts.",
										   Toast.LENGTH_LONG).show();
							   }
						   });
					   }
					   else {
						   SharedPreferences.Editor editor = prefs.edit();
						   editor.putInt("comid", position);
						   editor.commit();

						   if (swipelayout != null)
							   swipelayout.setRefreshing(true);
						   page = 1;
						   current_item = 0;
						   postlist.clear();
						   new JSONParse().execute();
					   }
				   }

				   @Override
				   public void onNothingSelected(AdapterView<?> parent) {

				   }
			   }) ;







	           //ActionBar ab = getActionBar();			//gets the action bar
	           //ab.setDisplayShowCustomEnabled(true);	//custom action bar
	           //ab.setDisplayShowTitleEnabled(false);	//no wording on the action bar
			 //  actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);
			  // ab.setListNavigationCallbacks(adapter, callback);
			  // ab.setSelectedNavigationItem(cid);
	         //  LayoutInflater inflator = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	         // View v = inflator.inflate(R.layout.action_bar_title, null);

	         //  ab.setCustomView(v);
	       }


	
    private class JSONParse extends AsyncTask<String, String, JSONObject> {
       @Override
         protected void onPreExecute() {			//before executing
             super.onPreExecute();
       }
       
       @Override
         protected JSONObject doInBackground(String... args) {	//do in background while the process is executing
         JSONParser jParser = new JSONParser();					//creates a new JSONParser object
         // Getting JSON from URL
         if(isNetworkAvailable()){								//as long as the network is available
         JSONObject json = null;				//creates a new JSONObject
		try {
			int pcid = prefs.getInt("comid", -1);
			String token = prefs.getString("access_token", null);
			if(token!=null&&pcid!=-1) {

				if(cidd[pcid].equalsIgnoreCase("0")){
					Location location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

					if(location!=null)
					json = jParser.getJSONFromUrl("https://beuntamed.com/app/api/2.0/posts/get_posts", token, cidd[pcid], "location", Double.toString(location.getLatitude()), Double.toString(location.getLongitude()), page);

					else
						json = jParser.getJSONFromUrl("https://beuntamed.com/app/api/2.0/posts/get_posts", token, cidd[pcid], "community", "", "", page);

				}
				else {
					json = jParser.getJSONFromUrl("https://beuntamed.com/app/api/2.0/posts/get_posts", token, cidd[pcid], "community", "", "", page);
				}
				}
		} catch (IOException | JSONException e) {		//catches specific exceptions
				return null;
		}
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
	   					            "Error Loading Data", 
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
       try {

     	  if (json ==null){
  			runOnUiThread(new Runnable() {                //Toast message claiming Error with loading the data
				public void run() {
					Toast.makeText(getApplicationContext(),
							"Error retrieving posts. Please try again later. Error Code: 0x0001",
							Toast.LENGTH_SHORT).show();
				}
			});
   	   }else if(json.getString("status").equalsIgnoreCase("error")&&json.getString("cause").equalsIgnoreCase("no_posts")){
			  runOnUiThread(new Runnable() {
				  public void run() {
					  Toast.makeText(getApplicationContext(),
							  "No more posts to show for now!",            //toast message if there is an error
							  Toast.LENGTH_SHORT).show();
				  }
			  });

			  swipelayout.setRefreshing(false);
		  }
		  else if(json.getString("status").equalsIgnoreCase("error")){
			  runOnUiThread(new Runnable() {
				  public void run() {
					  Toast.makeText(getApplicationContext(),
							  "Please log in to continue.",            //toast message if there is an error
							  Toast.LENGTH_SHORT).show();
				  }
			  });
			  swipelayout.setRefreshing(false);
		  }

		  else{
   		  JSONArray array = null;						//new JSONArray
   		  JSONObject data = new JSONObject();			//new JSONObject
   		  data = json.getJSONObject("data");			//gets the data from the raw JSON
     	  array = data.getJSONArray("posts");			//gets list of posts from the raw JSON data section
     	       	  
     	  setStringProperty("json", array.toString());

			  page++;

        	  for(int i=0; i<array.length(); i++) {            //iterates array and populates listview
				  JSONObject c = array.getJSONObject(i);    //gets object i from array
				  JSONObject mask = c.getJSONObject("mask");//gets the mask information from the array
				  Post post = new Post(c.getInt("id"), c.getString("pid"), mask.getString("color"), mask.getString("class"), c.getInt("is_auth"),
						  0, c.getString("text"), c.getInt("like_count"), c.getInt("comment_count"),
						  c.getLong("time_posted"), 0);            //creates a new post object


				  Timestamp updateTime = new Timestamp(post.getTime_posted());    //new timestamp based on provided timestamp
				  Date date = new Date(updateTime.getTime() * 1000);                //date based on timestamp
				  String updatedTime = "";                                        //empty string
				  java.util.Date curr = new java.util.Date();                    //gets current date
				  int diffInDays = (int) ((curr.getTime() - date.getTime()) / (1000 * 60 * 60 * 24));    //difference between now and then
				  if (diffInDays < 7) {                                                //selected date format depending on recency
					  DateFormat df = new SimpleDateFormat("EEE hh:mm a");
					  updatedTime = df.format(date);
				  } else {
					  DateFormat df = new SimpleDateFormat("EEE MMM d");
					  updatedTime = df.format(date);                                //sets updated time in proper format
				  }

				  String tmp = post.getText().replaceAll("<br>", "\n").replaceAll("&hellip;", "... ").replaceAll("&amp;", "&").replaceAll("&ndash;", "-").replaceAll("&quot;", "\"")
						  .replaceAll("&ldquo;", "\"").replaceAll("&rdquo;", "\"").replaceAll("&lsquo;", "\'").replaceAll("&rdquo;", "\'")
						  .replaceAll("&gt;", ">").replaceAll("&lt;", "<");
				  post.setText(tmp);


				  if (post.getText().length() > 200) {
					  String tmp2 = (post.getText().substring(0, 200) + "...");
					  post.setText(tmp2);
				  }

				  HashMap<String, String> map = new HashMap<String, String>();    //creates hashmap for the post
				  map.put("has_voted", c.getString("has_voted"));
				  map.put("dark_color", mask.getString("dark_color"));
				  map.put("full_text", tmp);
				  map.put("id", String.valueOf(post.getId()));                    //adds all data to the post
				  map.put("pid", String.valueOf(post.getPid()));
				  map.put("mask_class", String.valueOf(font.get(post.getMask_class())));
				  map.put("mask_hex", String.valueOf(post.getMask_hex()));
				  map.put("text", String.valueOf(post.getText()));
				  map.put("isAuth", String.valueOf(post.getIs_auth()));
				  map.put("like_count", String.valueOf(post.getLike_count()) + " Votes " + String.valueOf(post.getComment_count()) + " Comments");
				  map.put("time_posted", updatedTime);

				  postlist.add(map);                                        //adds hashmap to list of posts
			  }
                  //creates and sets up adapter for the listview
                  ListAdapter adapter = new SimpleAdapter(CommunityView.this, postlist, R.layout.post_list_item, (new String[]{"text",
                		  "like_count", "mask_class", "mask_hex", "time_posted", "pid", "isAuth"}),
                		  (new int[] {R.id.text, R.id.likes, R.id.avatar, R.id.hexname, R.id.expires, R.id.pid, R.id.isauth})){

                	  @Override
                	  public View getView(int position, View convertView, ViewGroup parent) {

                	      View view = super.getView(position, convertView, parent);
                	      TextView text = (TextView) view.findViewById(R.id.avatar);		//gets textview for avatar
                	      TextView hex = (TextView) view.findViewById(R.id.hexname);		//hidden textview
                	      String s = hex.getText().toString();								//converts hex value to string
                	      	text.setTextColor(Color.parseColor(s));							//sets the color to the given color
                	        text.setTypeface(typeFace);										//sets the typeface to the typeface
                	        text.setTextSize(40);											//sets size
                	        text.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL);	//sets location
                	        text.setBackgroundResource(R.drawable.iconcircle);				//gives icon a circle border
                	        text.getBackground().setColorFilter(Color.parseColor(s), PorterDuff.Mode.MULTIPLY);	//sets background

                	      TextView summText = (TextView) view.findViewById(R.id.text);		//sets textview content size
                	      summText.setTextSize(16);


						  RelativeLayout relative = (RelativeLayout) view.findViewById(R.id.bottom_wrapper);
						  ImageView img = (ImageView) view.findViewById(R.id.imageView1);


						  if (1==1){		//TODO: FIX THIS SHIT NIGGA YOU ARE A MAN NOT A PUSSY
							  img.setImageResource(R.drawable.ic_action_warning);
							  relative.setBackgroundColor(Color.parseColor("#FF8000"));

						  }

						  relative.setOnClickListener(new View.OnClickListener() {
							  @Override
							  public void onClick(View v) {
								  TextView pid = (TextView) findViewById(R.id.pid);
								  String n = pid.getText().toString();

								  TextView auth = (TextView) findViewById(R.id.isauth);
								  String l = auth.getText().toString();
								  int i = Integer.valueOf(l);

								  if(i==0){
									  new ReportJSONParse().execute(n);
								  }
								  else{
									  new DeleteJSONParse().execute(n);
								  }

							  }
						  });


                	      return view;
                	    }

                  };

                 if(page<=2){


					 list.setAdapter(adapter);													//applies adapter
                 list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
					 @Override
					 public void onItemClick(AdapterView<?> parent, View view,                //click listener for items in the list
											 int position, long id) {

						 Intent intent = new Intent(CommunityView.this, SinglePost.class);
						  intent.putExtra("post", postlist.get(position));					//opens new single post
						  startActivity(intent);												//starts intent
					 }
				 });


                 list.setOnScrollListener(new OnScrollListener(){
                 	@Override
                 	public void onScroll(AbsListView view, int firstVisibleItem,
                 	int visibleItemCount, int totalItemCount) {


             		 int topRowVerticalPosition = (list == null || list.getChildCount() == 0) ? 0 : list.getChildAt(0).getTop();
             	     swipelayout.setEnabled(firstVisibleItem == 0 && topRowVerticalPosition >= 0);

					try {
						if (list.getLastVisiblePosition() == list.getAdapter().getCount() - 1
								&& list.getChildAt(list.getChildCount() - 1).getBottom() <= list.getHeight()) {
							current_item = list.getFirstVisiblePosition();
							swipelayout.setRefreshing(true);
							new JSONParse().execute();
							//scroll end reached
							//Write your code here
						}
					}catch(NullPointerException e){
					}


                 	}

     				@Override
     				public void onScrollStateChanged(AbsListView view,
     						int scrollState) {

     				}

                 });
                 }

                 list.setSelection(current_item);
                 swipelayout.setRefreshing(false);


        	   } 
         } catch (JSONException e) {
				runOnUiThread(new Runnable() {
					public void run() {
						Toast.makeText(getApplicationContext(),
								"No more posts to show for now!",            //toast message if there is an error
								Toast.LENGTH_SHORT).show();
					}
				});

	   }
        
     }}

    public static long[] calculateTime(long seconds) {				//function to calculate time as an array of longs for seconds to day, hour, mins, seconds
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
	        	new AlertDialog.Builder(this)
	            .setTitle("Log out?")
	            .setMessage("Are you sure you want to log out?")
	            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
	                public void onClick(DialogInterface dialog, int which) { 
	                    new Logout().execute();
	                }
	             })
	            .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
	                public void onClick(DialogInterface dialog, int which) { 
	                    // do nothing
	                }
	             })
	            .setIcon(android.R.drawable.ic_dialog_alert)
	             .show();
	            return true;	        	
	        default:
	            return super.onOptionsItemSelected(item);
	    }
	}
	
	
    public String getStringProperty(String key) {
    	sharedPreferences = this.getSharedPreferences("preferences", Context.MODE_PRIVATE);
    String res = null;
    if (sharedPreferences != null) {
        res = sharedPreferences.getString(key, null);
    }
    return res;
}

    public void setStringProperty(String key, String value) {
    sharedPreferences = this.getSharedPreferences("preferences", Context.MODE_PRIVATE);
    if (sharedPreferences != null) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key, value);
        editor.commit();
    }
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


    private class DeleteJSONParse extends AsyncTask<String, String, JSONObject> {
        private ProgressDialog pDialog;				//creates a new progress dialog to show the loading of messages
       @Override
         protected void onPreExecute() {			//before executing
             super.onPreExecute();
             pDialog = new ProgressDialog(CommunityView.this);	//creates and displays the progress dialog
             pDialog.setMessage("Deleting message...");
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
			String token = prefs.getString("access_token", null);
			if(token!=null)
				json = jParser.getJSONFromUrl("https://beuntamed.com/app/api/2.0/posts/delete_post", token, args[0], 0);
		} catch (IOException | JSONException e) {		//catches specific exceptions
				return null;
		}
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
	   					            "Error Deleting Confession", 
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
    	   pDialog.dismiss();
    	   if(json ==null){
			   runOnUiThread(new Runnable() {				//Toast message claiming Error with loading the data
					  public void run() {
						    Toast.makeText(getApplicationContext(), 
						            "Cannot delete post. Please try again later or contact the admins", 
						            Toast.LENGTH_SHORT).show();
					  }
					});
		   }else{
		   }

		   postlist.clear();
		   page=1;
		   current_item=0;
    	   new JSONParse().execute();
         
        
     }}


	private class ReportJSONParse extends AsyncTask<String, String, JSONObject> {
		private ProgressDialog pDialog;				//creates a new progress dialog to show the loading of messages
		@Override
		protected void onPreExecute() {			//before executing
			super.onPreExecute();
			pDialog = new ProgressDialog(CommunityView.this);	//creates and displays the progress dialog
			pDialog.setMessage("Reporting message...");
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
					String token = prefs.getString("access_token", null);
					if(token!=null)
						json = jParser.getJSONFromUrl("https://beuntamed.com/app/api/2.0/posts/report_post", token, args[0], 0);
				} catch (IOException | JSONException e) {		//catches specific exceptions
					return null;
				}

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
									"Error Reporting Confession",
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
			pDialog.dismiss();
			if(json ==null){
				runOnUiThread(new Runnable() {				//Toast message claiming Error with loading the data
					public void run() {
						Toast.makeText(getApplicationContext(),
								"Cannot report post. Please try again later or contact the admins",
								Toast.LENGTH_SHORT).show();
					}
				});
			}else{
			}

			postlist.clear();
			page=1;
			current_item=0;
			new JSONParse().execute();


		}}


    private class Logout extends AsyncTask<String, String, JSONObject> {
        private ProgressDialog pDialog;				//creates a new progress dialog to show the loading of messages
       @Override
         protected void onPreExecute() {			//before executing
             super.onPreExecute();
             pDialog = new ProgressDialog(CommunityView.this);	//creates and displays the progress dialog
             pDialog.setMessage("Logging out...");
             pDialog.setIndeterminate(false);
             pDialog.setCancelable(true);
             pDialog.show();
       }
       
       @Override
         protected JSONObject doInBackground(String... args) {	//do in background while the process is executing
         if(isNetworkAvailable()){								//as long as the network is available
         try {
        	 Session session = Session.getActiveSession();
        	    if (session != null) {

        	        if (!session.isClosed()) {
        	            session.closeAndClearTokenInformation();
        	            //clear your preferences if saved
        	        }
        	    } else {

        	        session = new Session(CommunityView.this);
        	        Session.setActiveSession(session);

        	        session.closeAndClearTokenInformation();
        	            //clear your preferences if saved

        	    }        	 
        	 
				URL url = new URL("https://beuntamed.com/app/api/2.0/logout");
				URLConnection conn = url.openConnection();
				conn.setDoOutput(true);
				OutputStreamWriter writer = new OutputStreamWriter(conn.getOutputStream());			
				writer.flush();	
			    PreferenceManager.getDefaultSharedPreferences(CommunityView.this).edit().putString("access_token", null).commit();
				Intent i = new Intent(CommunityView.this, LoginActivity.class);
				startActivity(i);
				finish();
			} catch (IOException e) {
				 Toast.makeText(getApplicationContext(), 
				            "Could not log out. Sorry!", 			//toast message if there is an error
				            Toast.LENGTH_SHORT).show();
			  
			}
         }
         
         else{
			
			return null;
         }
		return null;
       }
       @Override
       protected void onPostExecute(JSONObject json) {
    	   pDialog.dismiss();
    	           
       }}



	private class SendConfJSONParse extends AsyncTask<String, String, JSONObject> {
		private ProgressDialog pDialog;				//creates a new progress dialog to show the loading of messages
		@Override
		protected void onPreExecute() {			//before executing
			super.onPreExecute();
			pDialog = new ProgressDialog(CommunityView.this);	//creates and displays the progress dialog
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
						json = jParser.getJSONFromUrl("https://beuntamed.com/app/api/2.0/posts/add_post", token, args[0], cidd[cid], 0);
				} catch (IOException | JSONException e) {		//catches specific exceptions
					return null;
				}

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
			 pDialog.dismiss();
			if(json ==null){
				runOnUiThread(new Runnable() {				//Toast message claiming Error with loading the data
					public void run() {
						Toast.makeText(getApplicationContext(),
								"Cannot submit post. Please try again later or contact the admins",
								Toast.LENGTH_SHORT).show();
					}
				});
			}else{
			}


		}}


	public static boolean isLocationEnabled(Context context) {
		int locationMode = 0;
		String locationProviders;

		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT){

			try {
				locationMode = Settings.Secure.getInt(context.getContentResolver(), Settings.Secure.LOCATION_MODE);
			} catch (Settings.SettingNotFoundException e) {
				e.printStackTrace();
			}


			return locationMode != Settings.Secure.LOCATION_MODE_OFF;

		}else{
			locationProviders = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.LOCATION_PROVIDERS_ALLOWED);
			return !TextUtils.isEmpty(locationProviders);
		}


	}


}